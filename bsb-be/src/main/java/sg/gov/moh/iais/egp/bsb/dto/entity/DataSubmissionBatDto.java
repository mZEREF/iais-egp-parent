package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"dataSubmissionId"})
public class DataSubmissionBatDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${dataSubmission.id}")
    private String dataSubmissionId;

    private BigDecimal expectedQty;

    private BigDecimal actualQty;

    private BigDecimal transferredQty;

    private String measurementUnit;

    private String destructionMethod;

    private String destructionProceduresDetails;

    private String discrepantReason;

    private String handleType;

    private String biologicalId;

    private String transferredUnit;

    private String reason;
}
