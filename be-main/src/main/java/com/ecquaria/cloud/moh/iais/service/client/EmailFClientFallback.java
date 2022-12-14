package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

/**
 * @author Wenkang
 * @date 2020/1/19 15:57
 */
public class EmailFClientFallback implements EmailClient {
    @Override
    public FeignResponseEntity<String> sendNotification(EmailDto email) {
        return IaisEGPHelper.getFeignResponseEntity("sendNotification",email);
    }

}
