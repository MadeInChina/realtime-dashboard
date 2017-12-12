package org.hanrw.app.springboot.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;

/**
 * New user data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("new_user_counts")
public class TotalNewUserData implements Serializable {
    @PrimaryKeyColumn(name = "created_time", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String createdTime;
    @Column(value = "total_count")
    private long totalCount;
}
