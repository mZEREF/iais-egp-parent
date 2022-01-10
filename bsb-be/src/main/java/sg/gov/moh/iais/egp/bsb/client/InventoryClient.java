package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inventory.FacilityDto;
import sg.gov.moh.iais.egp.bsb.dto.inventory.InventoryAgentResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inventory.InventoryDtResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inventory.InventoryDto;
import sg.gov.moh.iais.egp.bsb.entity.Biological;

import java.util.List;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface InventoryClient {
    @GetMapping(path = "/fac_info/info")
    List<FacilityDto> queryAllFacilityInfo();

    @GetMapping(path = "/bio_info/bioName")
    FeignResponseEntity<List<String>> queryDistinctFA();

    @GetMapping(path ="/bio_info/name/{bioName}")
    ResponseDto<Biological> getBiologicalByName(@PathVariable(name = "bioName") String bioName);

    @GetMapping(value = "/iny_info/agent", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InventoryAgentResultDto> findInventoryByAgentInfo(@RequestBody InventoryDto dto);

    @GetMapping(value = "/iny_info/date", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InventoryDtResultDto> findInventoryByDt(@RequestBody InventoryDto dto);

    @GetMapping(path = "/bio_info/info/{biologicalId}")
    ResponseDto<Biological> getBiologicalById(@PathVariable(name = "biologicalId") String biologicalId);
}
