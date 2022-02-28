package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"applicationId"})
public class ApplicationMiscDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${application.id}")
    private String applicationId;

    private RoutingHistoryDto routingHistory;

    private String relateRecId;

    private LocalDate effectiveDate;

    private String reason;

    private String reasonContent;

    private String remarks;

    private String finalRemarks;

    private String riskLevel;

    private String riskLevelComments;

    private LocalDate erpReportDt;

    private LocalDate redTeamingReportDt;

    private LocalDate lentivirusReportDt;

    private LocalDate internalInspectionReportDt;

    private LocalDate validityStartDt;

    private LocalDate validityEndDt;

    private String selectedAfc;

    private String processDecision;

    private String suspensionType;

    private LocalDate suspensionStartDt;

    private LocalDate suspensionEndDt;

    private String additionalComments;
}
