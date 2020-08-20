package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yichen
 * @Date:2020/8/17
 */

@Getter
@Setter
public class EmailParam {
    private String templateId;
    private Map<String, Object> templateContent;
    private String queryCode;
    private String reqRefNum;
    private String refIdType;
    private String refId;
    private JobRemindMsgTrackingDto jobRemindMsgTrackingDto;
    private String subject;
    private String moduleType;
    private boolean smsOnlyOfficerHour = true;
    private HashMap<String, String> maskParams;
    private HashMap<String, String> subjectParams;
}
