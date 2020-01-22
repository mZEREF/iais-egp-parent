package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeToExcelDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Hc
 * @date 2019/12/10
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class, fallback = MasterCodeClientFallback.class)
public interface MasterCodeClient {

    @PostMapping(path = "/iais-mastercode/masterCode-param", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<MasterCodeQueryDto>> doQuery(@RequestBody SearchParam searchParam);

    @GetMapping(path = "/iais-mastercode/{masterCodeKey}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MasterCodeDto> getMasterCodeByMskey(@PathVariable("masterCodeKey") String masterCodeKey);

    @PostMapping(path = "/iais-mastercode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MasterCodeDto>  saveMasterCode(@RequestBody MasterCodeDto dto);

    @PostMapping(path = "/iais-mastercode/category", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MasterCodeCategoryDto>  saveMasterCodeCategory(@RequestBody MasterCodeCategoryDto dto);

    @PostMapping(value = "/iais-mastercode/masterCodes",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<MasterCodeToExcelDto>>  findAllMasterCode();

    @DeleteMapping(path = "/iais-mastercode/{did}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> delMasterCode(@PathVariable("did")String did);

    @PutMapping(path = "/iais-mastercode",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MasterCodeDto> updateMasterCode(@RequestBody MasterCodeDto dto);

    @GetMapping(path = "/iais-mastercode/masterCode/{id}")
    FeignResponseEntity<MasterCodeDto> getMasterCodeById(@PathVariable("id") String id);

    @GetMapping(path = "/iais-mastercode/codeCategory/{description}")
    FeignResponseEntity<String> getCodeCategoryByDescription(@PathVariable("description") String description);

    @GetMapping(path = "/iais-mastercode/codeKey/{description}")
    FeignResponseEntity<String> getCodeKeyByDescription(@PathVariable("description") String description);
}
