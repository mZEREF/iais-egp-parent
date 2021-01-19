package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.AcraUenBeClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * AcraDeregistrationJobHandler
 *
 * @author Guyin
 * @date 12/04/2020
 */
@JobHandler(value="AcraDeregistrationJobHandler")
@Component
@Slf4j
public class AcraDeregistrationJobHandler extends IJobHandler {

    @Autowired
    AcraUenBeClient acraUenBeClient;

    @Autowired
    LicenceService licenceService;

    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    final static int OUTDATEMONTH = 6;
    @Override
    public ReturnT<String> execute(String s) throws IOException, TemplateException{
        log.info(StringUtil.changeForLog("AcraDeregistrationJobHandler start..." ));
        //get all out expire date + outdatemonth licence
        //org => licensee
        List<String> licenseeList = licenceService.getLicenceOutDate(OUTDATEMONTH);
        if(licenseeList != null && licenseeList.size() > 0){
            //disabel all licensee and org be and acra api
            acraUenBeClient.acraDeregister(licenseeList);
            //disabel all licensee and org fe
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            beEicGatewayClient.deRegisterAcra(licenseeList, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        }



        //org => licensees
//        if(licenceDtoList != null && licenceDtoList.size() > 0){
//            //get all organization and licensee in out licence
//            List<String> alllicensee = IaisCommonUtils.genNewArrayList();
//            for (LicenceDto item:licenceDtoList) {
//                alllicensee.add(item.getLicenseeId());
//            }
//            List<LicenseeDto> licenseeDtos = IaisCommonUtils.genNewArrayList();
//            licenseeDtos = licenceService.getLicenseeInIds(alllicensee);
//            Map<String,List<String>> organizationMap = IaisCommonUtils.genNewHashMap();
//            for (LicenseeDto item:licenseeDtos
//                 ) {
//                if(StringUtil.isEmpty(organizationMap.get(item.getOrganizationId()))){
//                    List<String> list = IaisCommonUtils.genNewArrayList();
//                    list.add(item.getId());
//                    organizationMap.put(item.getOrganizationId(),list);
//                }else{
//                    organizationMap.get(item.getOrganizationId()).add(item.getId());
//                }
//            }
//            //judge do all org in map need disable
//            List<String> orgList = IaisCommonUtils.genNewArrayList();
//            organizationMap.forEach((key,value) -> {
//                orgList.add(key);
//            });
//            for (String item:orgList
//                 ) {
//                //get all licensee in org
//                List<LicenseeDto> licenseeDtosInOrg = organizationClient.getLicenseeInOrg(item).getEntity();
//                //judge do need disable
//            }
//
//        }

        log.info(StringUtil.changeForLog("AcraDeregistrationJobHandler end..." ));
        return ReturnT.SUCCESS;

    }

}
