package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.entity.FacilityDoc;

import java.util.List;

/**
 * @author Zhu Tangtang
 */

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface DocClient {
    @PostMapping(value = "/bsb-doc/saveDoc", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveFacilityDoc(@RequestBody FacilityDoc facilityDoc);

    @GetMapping(value = "/bsb-doc/getFacilityDocByFacId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FacilityDoc>> getFacilityDocByFacId(@RequestParam("facId") String facId);

    @GetMapping(value = "/bsb-doc/deleteDocByFileRepoId")
    ResponseEntity<String> deleteByFileRepoId(@RequestParam(name = "fileRepoId") String id);
}
