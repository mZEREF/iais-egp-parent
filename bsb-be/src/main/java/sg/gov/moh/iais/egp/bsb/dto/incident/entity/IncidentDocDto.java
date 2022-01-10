package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author YiMing
 * @version 2021/12/28 9:04
 **/
@Data
public class IncidentDocDto implements Serializable {
    private String  document;

    private String docName;

    private Integer docSize;

    private String docType;

    private String fileRepoId;

    private Date submitDt;

    private String submitBy;

    private String status;
}
