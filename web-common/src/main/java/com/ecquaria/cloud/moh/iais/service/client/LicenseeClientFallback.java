package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

/**
 * LicenseeClientFallback
 *
 * @author suocheng
 * @date 3/12/2020
 */

public class LicenseeClientFallback {
    public FeignResponseEntity<LicenseeDto> getLicenseeDtoById (String id){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public ResponseEntity<List<String>> getLicenseeEmails(String licenseeId) {
        return null;
    }

    public ResponseEntity<List<String>> getLicenseeMobiles(String licenseeId) {
        return null;
    }

    public FeignResponseEntity<List<String>> getEmailAddressListByLicenseeId(List<String> licenseeIdList) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getPersonByid(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
