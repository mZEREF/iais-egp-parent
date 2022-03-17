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
@JGlobalMap(excluded = {"facilityId"})
public class FacilityActivityDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${facility.id}")
    private String facilityId;

    private ApplicationDto application;

    private String activityType;

    private List<FacilityBiologicalAgentDto> biologicalAgents;

    private ApprovalDto approval;

    private String cloned;

    private String useStatus;

    private String status;
}
