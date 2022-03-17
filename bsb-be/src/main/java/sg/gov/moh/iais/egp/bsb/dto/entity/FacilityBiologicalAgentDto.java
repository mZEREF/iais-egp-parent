package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"facilityId", "facilityActivityId"})
public class FacilityBiologicalAgentDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${facility.id}")
    private String facilityId;

    private ApplicationDto application;

    @JMap(value = "${facilityActivity.id}")
    private String facilityActivityId;

    private String approveType;

    private String biologicalId;

    private List<FacilityAgentSampleDto> facilityAgentSamples;

    private FacilityBiologicalDto facilityBiological;

    private ApprovalDto approval;

    private String cloned;

    private String useStatus;

    private String status;
}
