package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class ApprovalDto extends BaseEntityDto {
    private String id;

    private String approveNo;

    private String processType;

    private String approveType;

    private String status;

    private LocalDate approvalStartDate;

    private LocalDate approvalExpiryDate;

    private String renewable;
}
