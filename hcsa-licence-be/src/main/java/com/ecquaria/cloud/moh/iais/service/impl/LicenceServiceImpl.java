package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FEEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * LicenceServiceImpl
 *
 * @author suocheng
 * @date 11/29/2019
 */
@Service
public class LicenceServiceImpl implements LicenceService {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private SystemBeLicClient systemClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private FEEicGatewayClient feEicGatewayClient;

    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private SubmissionClient client;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Override
    public List<ApplicationLicenceDto> getCanGenerateApplications(int day) {
        Map<String,Object> param = new HashMap<>();
        param.put("day",day);

        return   applicationClient.getGroup(day).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getHcsaServiceById(List<String> serviceIds) {

        return  hcsaConfigClient.getHcsaService(serviceIds).getEntity();
    }

    @Override
    public String getHciCode(String serviceCode) {
        return     systemClient.hclCodeByCode(serviceCode).getEntity();
    }

    @Override
    public String getLicenceNo(String hciCode, String serviceCode, int yearLength) {

        Integer licenceSeq =  hcsaLicenceClient.licenceNumber(hciCode).getEntity();
        Map<String,Object> param = new HashMap();
        param.put("hciCode",hciCode);
        param.put("serviceCode",serviceCode);
        param.put("yearLength",yearLength);
        param.put("licenceSeq",licenceSeq);

        return    systemClient.licence(hciCode,serviceCode,yearLength,licenceSeq).getEntity();
    }

    @Override
    public String getGroupLicenceNo(String hscaCode, int yearLength) {
        String entity = hcsaLicenceClient.groupLicenceNumber(hscaCode).getEntity();

        return   systemClient.groupLicence(hscaCode,yearLength+"",entity).getEntity();
    }

    @Override
    public AppPremisesRecommendationDto getTcu(String appPremCorrecId) {
        return fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrecId,
                InspectionConstants.RECOM_TYPE_TCU).getEntity();
    }
    @Override
    public PremisesDto getLatestVersionPremisesByHciCode(String hciCode) {
        return hcsaLicenceClient.getLatestVersionPremisesByHciCode(hciCode).getEntity();
    }

    @Override
    public KeyPersonnelDto getLatestVersionKeyPersonnelByIdNoAndOrgId(String idNo, String orgId) {
        return hcsaLicenceClient.getLatestVersionKeyPersonnelByidNoAndOrgId(idNo,orgId).getEntity();
    }

    @Override
    public List<LicenceGroupDto> createSuperLicDto(EventBusLicenceGroupDtos eventBusLicenceGroupDtos) {
        String   callBackUrl = "egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/LicenceEventBusCallBack";

        SubmitReq req =EventBusHelper.getSubmitReq(eventBusLicenceGroupDtos,systemAdminClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_LICENCESAVE,
                EventBusConsts.SERVICE_NAME_LICENCESAVE,
                "https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/ApplicationView",
                callBackUrl, "sop",true,null);
        //
        SubmitResp submitResp = client.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);

        return   null;
    }

    @Override
    public List<LicenceGroupDto> createFESuperLicDto(List<LicenceGroupDto> licenceGroupDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        return feEicGatewayClient.createLicence(licenceGroupDtos, signature.date(), signature.authorization()).getEntity();
    }
}
