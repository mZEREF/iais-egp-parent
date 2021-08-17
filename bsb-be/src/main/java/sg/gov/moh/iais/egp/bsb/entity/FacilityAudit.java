package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class FacilityAudit extends BaseEntity {
    private String id;

    private Facility facility;

    private String auditType;

    private String remarks;

    private String status;

    private Date auditDate;
}
