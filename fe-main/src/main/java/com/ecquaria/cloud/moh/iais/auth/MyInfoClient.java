package com.ecquaria.cloud.moh.iais.auth;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "myinfo", configuration = FeignConfiguration.class)
public interface MyInfoClient {

    @GetMapping(value = "/testInfo/myinfo/person-basic/{uinfin}/",consumes = "application/jose",produces="application/jose")
    ResponseEntity<String> searchDataByIdNumber(@RequestHeader("Authorization") String authorization, @PathVariable(name = "uinfin") String idNumber,
                                                @RequestParam(name = "attributes") String[] attrs, @RequestParam(name = "clientId") String clientId,
                                                @RequestParam(name = "singpassEserviceId") String singPassEServiceId, @RequestParam(name = "txnNo", required = false) String txnNo);
}
