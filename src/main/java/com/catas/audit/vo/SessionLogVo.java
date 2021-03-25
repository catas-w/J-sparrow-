package com.catas.audit.vo;

import com.catas.audit.entity.Sessionlog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SessionLogVo extends Sessionlog {

    private Integer page = 1;
    private Integer limit = 10;

    private String userName;

    private String hostName;

    private String startDate;

    @DateTimeFormat(pattern = "YYYY-mm-dd")
    private Date endDate;
}
