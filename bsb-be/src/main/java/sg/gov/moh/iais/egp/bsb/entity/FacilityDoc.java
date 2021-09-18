package sg.gov.moh.iais.egp.bsb.entity;


import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class FacilityDoc extends BaseEntity {
    private String id;

    private Facility facility;

    private String name;

    private long size;

    private String fileRepoId;

    private Date submitAt;

    private String submitBy;

    private String docType;

    private String submitByName;
    private AuditTrailDto auditTrailDto;
    private Integer fileSn;
    private String submitAtStr;
    private Boolean isUpload;
    private String url;
    private String maskId;
}
