package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.approvalApplication.ApprovalApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.approvalApplication.BiologicalQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.approvalApplication.BsbFacilityQueryDto;

import java.util.List;

/**
 * @Author Li Ran
 * @Date 2021/7/27 16:01
 **/
@FeignClient(name = "bsb-fe-api", configuration = FeignConfiguration.class)
public interface ApprovalApplicationClient {

    @PostMapping(path = "/bsb-approval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApprovalApplicationDto> saveApproval(@RequestBody ApprovalApplicationDto approvalApplicationDto);

    @PostMapping(path = "/bsb-approval/{processType}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BsbFacilityQueryDto>> getFacilityByApprovalStatus(@PathVariable("processType") String processType);

    @PostMapping(path = "/bsb-approval/schedule/{schedule}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BiologicalQueryDto>> getBiologicalNameBySchedule(@PathVariable("schedule") String schedule);
}
