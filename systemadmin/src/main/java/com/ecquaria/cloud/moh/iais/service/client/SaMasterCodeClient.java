package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeToExcelDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Hc
 * @date 2019/12/10
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class, fallback = SaMasterCodeClientFallback.class)
public interface SaMasterCodeClient {

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

    @DeleteMapping(path = "/iais-mastercode/{did}/{uCount}")
    FeignResponseEntity<Void> delMasterCode(@PathVariable("did") String did, @PathVariable("uCount") int updateCount);

    @PutMapping(path = "/iais-mastercode",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MasterCodeDto> updateMasterCode(@RequestBody MasterCodeDto dto);

    @GetMapping(path = "/iais-mastercode/masterCode/{id}")
    FeignResponseEntity<MasterCodeDto> getMasterCodeById(@PathVariable("id") String id);

    @GetMapping(path = "/iais-mastercode/codeCategory/{description}")
    FeignResponseEntity<String> getCodeCategoryByDescription(@PathVariable("description") String description);

    @GetMapping(path = "/iais-mastercode/codeKey/{description}")
    FeignResponseEntity<String> getCodeKeyByDescription(@PathVariable("description") String description);

    @GetMapping(path = "/iais-mastercode/mastercode-catergory", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<MasterCodeCategoryDto>> getAllMasterCodeCategory();

    @GetMapping(path = "/iais-mastercode/mastercode-catergory/is-edit", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<MasterCodeCategoryDto>> getMasterCodeCategoryIsEdit();

    @GetMapping(path = "/iais-mastercode/mastercode-catergory-entity")
    FeignResponseEntity<MasterCodeCategoryDto> getMasterCodeCategory(@RequestParam(value = "categoryId")String categoryId);

    @GetMapping(path = "/iais-mastercode/master-code/master-code-key")
    FeignResponseEntity<Boolean> masterCodeKeyIsExist(@RequestParam(value = "masterCodeKey") String masterCodeKey);

    @GetMapping(path = "/iais-mastercode/master-code/suggest-code-description")
    FeignResponseEntity<List<String>> suggestCodeDescription(@RequestParam(value = "codeDescription") String codeDescription);

    @PostMapping(value = "/iais-mastercode/master-code/master-code-excel",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<MasterCodeDto>> saveMasterCodeExcel(@RequestBody List<MasterCodeToExcelDto> masterCodeToExcelDtoList);

    @PutMapping(value = "/iais-mastercode/master-code/expired-or-not-effect",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> inactiveMasterCode(@RequestBody AuditTrailDto auditTrailDto);

    @PutMapping(value = "/iais-mastercode/master-code/active-master-code",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> activeMasterCode(@RequestBody AuditTrailDto auditTrailDto);

    @GetMapping(path = "/iais-mastercode/max-version")
    FeignResponseEntity<MasterCodeDto> getMaxVersionMsDto(@RequestParam(value = "masterCodeKey") String masterCodeKey);

    @PutMapping(path = "/iais-mastercode/inactive-master-code")
    FeignResponseEntity<List<MasterCodeDto>> inactiveMasterCodeByKey(@RequestParam(value = "masterCodeKey") String masterCodeKey);
}
