package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.entity.BatContactPerson;
import sg.gov.moh.iais.egp.bsb.entity.BatFacilityDetail;
import sg.gov.moh.iais.egp.bsb.entity.BatLspInfo;
import sg.gov.moh.iais.egp.bsb.entity.BatSample;
import sg.gov.moh.iais.egp.bsb.entity.BatSpWorkActivity;
import sg.gov.moh.iais.egp.bsb.entity.BatSpecialHandleInfo;
import sg.gov.moh.iais.egp.bsb.entity.BatWorkActivity;

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

    private String status;

    private ApprovalDto approval;

    private String procurementMode;

    private BatLspInfo lspInfo;

    private BatSpecialHandleInfo specialHandleInfo;

    private List<BatSample> facilityAgentSamples;

    private List<BatWorkActivity> agentWorkActivities;

    private List<BatSpWorkActivity> agentSpecialHandleWorkActivities;

    private BatFacilityDetail transferringFacility;

    private BatContactPerson transferringFacilityContactPerson;

    private String useStatus;

    private Boolean blockFlag;
}
