package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.api.services.GatewayAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayNetsAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayPayNowAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayStripeAPI;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.dto.PmtReturnUrlDto;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ApplicationHelper
 *
 * @author suocheng
 * @date 2/24/2020
 */

@Slf4j
public class NewApplicationHelper {

   public static String genBankUrl(HttpServletRequest request, String payMethod, Map<String, String> fieldMap,
            PmtReturnUrlDto pmtReturnUrlDto) throws Exception {
        String url = "";
        switch (payMethod) {
            case ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT:
                url = GatewayStripeAPI.create_partner_trade_by_buyer_url(fieldMap, request, pmtReturnUrlDto.getCreditRetUrl());
                break;
            case ApplicationConsts.PAYMENT_METHOD_NAME_NETS:
                url = GatewayNetsAPI.create_partner_trade_by_buyer_url(fieldMap, request, pmtReturnUrlDto.getNetsRetUrl());
                break;
            case ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW:
                url = GatewayPayNowAPI.create_partner_trade_by_buyer_url(fieldMap, request, pmtReturnUrlDto.getPayNowRetUrl());
                break;
            default:
                url = GatewayAPI.create_partner_trade_by_buyer_url(fieldMap, request, pmtReturnUrlDto.getOtherRetUrl());
        }
        return url;
    }

}
