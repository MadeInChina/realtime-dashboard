package org.hanrw.app.spark.processor;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.hanrw.app.spark.entity.TotalNewUserData;
import org.hanrw.app.spark.vo.NewUser;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.javaFunctions;

/**
 * Class to process IoT data stream and to produce traffic data details.
 *
 * @author abaghel
 */
@Service
@Slf4j
public class NewUserDataProcessor {
    /**
     * Method to get total traffic counts of different type of vehicles for each route.
     *
     * @param filteredIotDataStream IoT data stream
     */
    public void processTotalNewUserData(JavaDStream<NewUser> filteredIotDataStream, String keyspace, String table) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        // We need to get count of new user group by create time
        JavaPairDStream<String, Long> countDStreamPair = filteredIotDataStream
                .mapToPair(newUser -> new Tuple2<>(sf.format(newUser.getCreatedTime()), 1L))
                .reduceByKey((a, b) -> a + b);

        // Transform to dstream of TrafficData
        JavaDStream<Tuple2<String, Long>> countDStream = countDStreamPair.map(tuple2 -> tuple2);

        JavaDStream<TotalNewUserData> trafficDStream = countDStream.map(totalTrafficDataFunc);

        // Map Cassandra table column
        Map<String, String> columnNameMappings = new HashMap<String, String>();
        columnNameMappings.put("totalCount", "total_count");
        columnNameMappings.put("createdTime", "created_time");

        // call CassandraStreamingJavaUtil function to save in DB
        javaFunctions(trafficDStream).writerBuilder(keyspace, table,
                CassandraJavaUtil.mapToRow(TotalNewUserData.class, columnNameMappings)).saveToCassandra();
    }


    //Function to get running sum by maintaining the state
    private static final Function3<String, Optional<Long>, State<Long>, Tuple2<String, Long>> totalSumFunc = (key, currentSum, state) -> {
        long totalSum = currentSum.or(0L) + (state.exists() ? state.get() : 0);
        Tuple2<String, Long> total = new Tuple2<>(key, totalSum);
        state.update(totalSum);
        return total;
    };

    //Function to create TotalUserData object from user data
    private static final Function<Tuple2<String, Long>, TotalNewUserData> totalTrafficDataFunc = (tuple -> {
        log.debug("Total Count : " + "key " + tuple._1() + " value " + tuple._2());
        TotalNewUserData totalNewUserData = new TotalNewUserData();
        totalNewUserData.setCreatedTime(tuple._1());
        totalNewUserData.setTotalCount(tuple._2());
        totalNewUserData.setTotalCount(tuple._2());
        log.debug("sssssssssssssssss-count>{}", totalNewUserData);
        return totalNewUserData;
    });


}
