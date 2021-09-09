package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.approval.ApprovalApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.BiologicalQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.FacilityQueryDto;
import sg.gov.moh.iais.egp.bsb.entity.FacilityBiologicalAgent;
import sg.gov.moh.iais.egp.bsb.entity.FacilityDoc;
import sg.gov.moh.iais.egp.bsb.entity.FacilitySchedule;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@FeignClient(name = "bsb-fe-api", configuration = FeignConfiguration.class)
public interface ApprovalApplicationClient {

    @PostMapping(path = "/bsb-approval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApprovalApplicationDto> saveApproval(@RequestBody ApprovalApplicationDto approvalApplicationDto);

    @GetMapping(path = "/bsb-approval/{approvalType}")
    FeignResponseEntity<List<FacilityQueryDto>> getFacilityByApprovalType(@PathVariable("approvalType") String approvalType);

    @GetMapping(path = "/bsb-approval/schedule/{schedule}")
    FeignResponseEntity<List<BiologicalQueryDto>> getBiologicalBySchedule(@PathVariable("schedule") String schedule);

    @GetMapping(path = "/bsb-approval/biologicalName/{id}")
    FeignResponseEntity<BiologicalQueryDto> getBiologicalById(@PathVariable("id") String id);

    @GetMapping(path = "/bsb-approval/facilitySchedule/{facilityId}")
    FeignResponseEntity<List<FacilitySchedule>> getFacilityScheduleByFacilityId(@PathVariable("facilityId") String facilityId);

    @GetMapping(path = "/bsb-approval/facilityBiologicalAgent/{facilityScheduleId}")
    FeignResponseEntity<List<FacilityBiologicalAgent>> getFacilityBiologicalAgentByFacilityScheduleId(@PathVariable("facilityScheduleId") String facilityScheduleId);

    @GetMapping(path = "/bsb-approval/facilityDoc/{facilityId}")
    FeignResponseEntity<List<FacilityDoc>> getFacilityDocByFacilityId(@PathVariable("facilityId") String facilityId);
}
