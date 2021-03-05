package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.GiroAccountService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.GiroAccountBeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GiroAccountServiceImpl
 *
 * @author junyu
 * @date 2021/3/3
 */
@Slf4j
@Service
public class GiroAccountServiceImpl implements GiroAccountService {

    @Autowired
    private LicEicClient licEicClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Value("${iais.sharedfolder.requestForInfo.in}")
    private String inSharedPath;
    @Value("${iais.syncFileTracking.shared.path}")
    private     String sharedPath;
    @Autowired
    GiroAccountBeClient giroAccountBeClient;

    @Override
    public SearchResult<GiroAccountInfoQueryDto> searchGiroInfoByParam(SearchParam searchParam) {
        return giroAccountBeClient.searchGiroInfoByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<OrganizationPremisesViewQueryDto> searchOrgPremByParam(SearchParam searchParam) {
        return giroAccountBeClient.searchOrgPremByParam(searchParam).getEntity();
    }

    @Override
    public List<GiroAccountInfoDto> createGiroAccountInfo(List<GiroAccountInfoDto> giroAccountInfoDto) {
        return giroAccountBeClient.createGiroAccountInfo(giroAccountInfoDto).getEntity();
    }

    @Override
    public void updateGiroAccountInfo(List<GiroAccountInfoDto> giroAccountInfoDto) {
        giroAccountBeClient.updateGiroAccountInfo(giroAccountInfoDto);
    }

    @Override
    public List<GiroAccountFormDocDto> findGiroAccountFormDocDtoListByAcctId(String acctId) {
        return giroAccountBeClient.findGiroAccountFormDocDtoListByAcctId(acctId).getEntity();
    }

    @Override
    public GiroAccountInfoDto findGiroAccountInfoDtoByAcctId(String acctId) {
        return giroAccountBeClient.findGiroAccountInfoDtoByAcctId(acctId).getEntity();
    }


    @Override
    public void syncFeGiroAcctDto(List<GiroAccountInfoDto> giroAccountInfoDtoList) {
        EicRequestTrackingDto trackDto = getLicEicRequestTrackingDtoByRefNo(giroAccountInfoDtoList.get(0).getEventRefNo());
        eicCallFeGiroLic(giroAccountInfoDtoList);
        trackDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        updateGiroAccountInfoTrackingDto(trackDto);

    }

    public void eicCallFeGiroLic(List<GiroAccountInfoDto> giroAccountInfoDtoList) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        log.info(StringUtil.changeForLog("=======>>>>>"+" Lic Giro Account Information Id :"+giroAccountInfoDtoList.get(0).getId()));

        gatewayClient.updateGiroAccountInfo(giroAccountInfoDtoList,
                signature.date(), signature.authorization(), signature2.date(), signature2.authorization());
    }

    @Override
    public void updateGiroAccountInfoTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto) {
        licEicClient.saveEicTrack(licEicRequestTrackingDto);
    }


    public EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo) {
        return licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
    }
}
