package sg.gov.moh.iais.egp.bsb.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author YiMing
 * DATE:2021/9/10 9:12
 **/
@Data
public class Notification {
    private String applicationNo;

    private String applicationName;

    private String applicationType;

    private String status;

    private Map<String,Object> contentParams;

    private Map<String,Object> subjectParams;
}
