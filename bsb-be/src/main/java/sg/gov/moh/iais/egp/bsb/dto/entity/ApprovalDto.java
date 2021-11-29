package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class ApprovalDto extends BaseEntityDto {
    private String id;

    private String processType;

    private String approveNo;

    private String status;

    private Date approvalDate;

    private Date approvalStartDate;

    private Date approvalExpiryDate;

    private String suspendedStatus;
}
