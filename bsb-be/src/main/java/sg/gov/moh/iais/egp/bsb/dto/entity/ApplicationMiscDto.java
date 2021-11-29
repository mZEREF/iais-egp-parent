package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;


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

    private Date effectiveDate;

    private String reason;

    private String reasonContent;

    private String remarks;

    private String finalRemarks;

    private String riskLevel;

    private String riskLevelComments;

    private Date erpReportDt;

    private Date redTeamingReportDt;

    private Date lentivirusReportDt;

    private Date internalInspectionReportDt;

    private Date validityStartDt;

    private Date validityEndDt;

    private String selectedAfc;

    private String processDecision;
}
