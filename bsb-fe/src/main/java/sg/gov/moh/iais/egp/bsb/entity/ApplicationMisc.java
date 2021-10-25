package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ApplicationMisc extends BaseEntity {
    private String id;

    private Application application;

    private String relateRecId;

    private Date effectiveDate;

    private String reason;

    private String reasonContent;

    private String remarks;
}
