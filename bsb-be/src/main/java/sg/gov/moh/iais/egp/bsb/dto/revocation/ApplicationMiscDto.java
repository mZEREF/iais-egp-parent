package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Zhu Tangtang
 * @date 2021/8/17 16:59
 */
@Data
public class ApplicationMiscDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String applicationId;
    private String relateRecId;
    private Date effectiveDate;
    private String reason;
    private String reasonContent;
    private String remarks;
    private String createdBy;
    private Date createdAt;
    private String modifiedBy;
    private Date modifiedAt;
}
