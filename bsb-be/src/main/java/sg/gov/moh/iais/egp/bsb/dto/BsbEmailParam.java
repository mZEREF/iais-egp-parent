package sg.gov.moh.iais.egp.bsb.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author : YiMing
 * @date: 2021/9/16 17:15
 **/

@Data
public class BsbEmailParam {
    private String msgTemplateId;

    private String refId;

    private String refIdType;

    private String queryCode;

    private String reqRefNum;

    private String recipientType;

    private Map<String,byte[]> attachments;

    private Map<String,Object> msgContent;

    private Map<String,Object> msgSubject;
}
