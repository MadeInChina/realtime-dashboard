package org.hanrw.app.springboot.vo;

import lombok.Data;
import org.hanrw.app.springboot.dao.entity.TotalNewUserData;

import java.io.Serializable;
import java.util.List;

@Data
public class Response implements Serializable {
    private List<TotalNewUserData> totalNewUserDataList;
}
