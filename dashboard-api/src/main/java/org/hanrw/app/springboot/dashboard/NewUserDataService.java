package org.hanrw.app.springboot.dashboard;

import org.apache.log4j.Logger;
import org.hanrw.app.springboot.dao.NewUserDataRepository;
import org.hanrw.app.springboot.dao.entity.TotalNewUserData;
import org.hanrw.app.springboot.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service class to send traffic data messages to dashboard ui at fixed interval using web-socket.
 *
 * @author abaghel
 */
@Service
public class NewUserDataService {
    private static final Logger logger = Logger.getLogger(NewUserDataService.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private NewUserDataRepository totalRepository;

    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //Method sends traffic data message in every 5 seconds.
    @Scheduled(fixedRate = 5000)
    public void trigger() {
        List<TotalNewUserData> totalNewUserDataList = new ArrayList<TotalNewUserData>();
        //Call dao methods
        totalRepository.findTotalNewUserDataByDate(sdf.format(new Date())).forEach(e -> totalNewUserDataList.add(e));
        //prepare response
        Response response = new Response();
        response.setTotalNewUserDataList(totalNewUserDataList);
        logger.info("Sending to UI " + response);
        //send to ui
        this.template.convertAndSend("/topic/newusers", response);
    }

}
