package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.service.HalpUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Description manage licnesee and user
 * @Auther chenlei on 5/17/2021.
 */
@Slf4j
@RestController
@RequestMapping(value = "halp-user")
public class HalpUserDelegate {

    @Autowired
    private HalpUserService halpUserService;

    @PostMapping(value = "/licensee-user-sync", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeUserDto> syncLicenseeUserFromBe(@RequestBody FeUserDto feUserDto) {
        log.info("Synchronize the licensee user");
        return ResponseEntity.ok(halpUserService.syncFeUserFromBe(feUserDto));
    }

}
