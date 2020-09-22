package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/1/19 15:57
 */
public class CommonEmailClientFallback implements CommonEmailClient {

    @Override
    public FeignResponseEntity<String> sendNotification(EmailDto email) {
        return null;
    }

    @Override
    public FeignResponseEntity<Map<String, String>> sendSMS(List<String> recipts, @Valid SmsDto sms, String reqRefNum) {
        return null;
    }
}
