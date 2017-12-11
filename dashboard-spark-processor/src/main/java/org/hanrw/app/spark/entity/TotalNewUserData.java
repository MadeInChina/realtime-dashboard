package org.hanrw.app.spark.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * New user data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalNewUserData implements Serializable {
    private long totalCount;
    private String createdTime;
    private String recordTime;

}
