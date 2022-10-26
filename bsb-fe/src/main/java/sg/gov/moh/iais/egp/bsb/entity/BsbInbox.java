package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class BsbInbox implements Serializable {
    private String id;

    private String userId;

    private String subject;

    private String msgType;

    private String refNo;

    private String appType;

    private String msgContent;

    private String status;

    private Date createdAt;

    private String createdBy;

    private Date modifiedAt;

    private String modifiedBy;
}
