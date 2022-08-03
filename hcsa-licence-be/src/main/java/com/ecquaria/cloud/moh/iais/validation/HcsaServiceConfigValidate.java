package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpeRoutingSchemeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.ServiceConfigConstant;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * HcsaServiceConfigValidate
 *
 * @author suocheng
 * @date 7/11/2022
 */
@Slf4j
public class HcsaServiceConfigValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(Object obj,HttpServletRequest request) {
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate start ..."));
        ConfigService configService = SpringContextHelper.getContext().getBean(ConfigService.class);
        Map<String, String> result = IaisCommonUtils.genNewHashMap();
        HcsaServiceConfigDto hcsaServiceConfigDto = (HcsaServiceConfigDto)obj;

        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        String serviceType = hcsaServiceDto.getSvcType();
        if(StringUtil.isEmpty(serviceType)){
            serviceType = HcsaServiceDto.VALIDATE_COMM;
        }
        log.info(StringUtil.changeForLog("The serviceType is -->;"+serviceType));

        //validate the hcsaServiceConfigDto
        ValidationResult validationResultHcsaServiceConfigDto = WebValidationHelper.validateProperty(hcsaServiceConfigDto,serviceType);
        if(validationResultHcsaServiceConfigDto.isHasErrors()){
            result.putAll(validationResultHcsaServiceConfigDto.retrieveAll());
        }
        //validate the hcsaServiceDto
        ValidationResult validationResultHcsaServiceDto = WebValidationHelper.validateProperty(hcsaServiceDto,serviceType);
        if(validationResultHcsaServiceDto.isHasErrors()){
            result.putAll(validationResultHcsaServiceDto.retrieveAll());
        }
        //validate the svcCode and svcName repetition
        validateSvcCodeAndName(configService,hcsaServiceDto,result);

       // if(HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(serviceType)){
            //validate the hcsaSvcPersonnelDto
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
            for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
                ValidationResult validationResultHcsaSvcPersonnelDto = WebValidationHelper.validateProperty(hcsaSvcPersonnelDto,serviceType);
                if(validationResultHcsaSvcPersonnelDto.isHasErrors()){
                    result.putAll(transferErrorMapForPersonnel(validationResultHcsaSvcPersonnelDto.retrieveAll(),hcsaSvcPersonnelDto));
                }
            }
            //validate the HcsaSvcDocConfigDto
            List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = hcsaServiceConfigDto.getHcsaSvcDocConfigDtos();
            if(hcsaSvcDocConfigDtos!=null){
                for(int i = 0; i < hcsaSvcDocConfigDtos.size(); i++){
                    String docTitle = hcsaSvcDocConfigDtos.get(i).getDocTitle();
                    ValidationResult validationResultHcsaSvcDocConfigDto = WebValidationHelper.validateProperty(hcsaSvcDocConfigDtos.get(i),serviceType);
                    if(validationResultHcsaSvcDocConfigDto.isHasErrors()){
                        String docTitleError  = validationResultHcsaSvcDocConfigDto.retrieveAll().get("docTitle");
                        result.put("serviceDoc"+i,docTitleError);
                    }else{
                        int countForTitle = getCountForTitle(docTitle,hcsaSvcDocConfigDtos);
                        if(countForTitle >1){
                            result.put("serviceDoc"+i,"SC_ERR011");
                        }
                    }
                }
            }
       // }
        //for routingStages
        if(HcsaConsts.SERVICE_TYPE_BASE.equals(hcsaServiceDto.getSvcType())){
            log.info(StringUtil.changeForLog("validate the HcsaConfigPageDto"));
            Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtoMap =  hcsaServiceConfigDto.getHcsaConfigPageDtoMap();
            for(String key : hcsaConfigPageDtoMap.keySet()){
                List<HcsaConfigPageDto> hcsaConfigPageDtos = hcsaConfigPageDtoMap.get(key);
                Map<String,String> HcsaConfigPageDtoError = validateHcsaConfigPageDto(hcsaConfigPageDtos,serviceType);
                if(HcsaConfigPageDtoError.size() > 0){
                    result.put(key,"Error");
                    result.putAll(HcsaConfigPageDtoError);
                }
            }

        }
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate end ..."));
        return result;
    }

    private Map<String,String> validateHcsaConfigPageDto(List<HcsaConfigPageDto> hcsaConfigPageDtos,String serviceType ){
        log.info(StringUtil.changeForLog("The validateHcsaConfigPageDto start ..."));
        Map<String,String> result = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isNotEmpty(hcsaConfigPageDtos)){
            for(HcsaConfigPageDto hcsaConfigPageDto : hcsaConfigPageDtos ){
                // for Ins hcsaConfigPageDto
                ValidationResult validationResultHcsaConfigPageDto = WebValidationHelper.validateProperty(hcsaConfigPageDto,serviceType);
                if(validationResultHcsaConfigPageDto.isHasErrors()){
                    Map<String,String>  validationResultHcsaConfigPageDtoMap = validationResultHcsaConfigPageDto.retrieveAll();
                    for(String key : validationResultHcsaConfigPageDtoMap.keySet()){
                        result.put(key+hcsaConfigPageDto.getStageCode()+hcsaConfigPageDto.getAppType(),
                                validationResultHcsaConfigPageDtoMap.get(key));
                    }
                }
                // for Ins HcsaSvcSpeRoutingSchemeDto
                List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaConfigPageDto.getHcsaSvcSpeRoutingSchemeDtos();
                if(IaisCommonUtils.isNotEmpty(hcsaSvcSpeRoutingSchemeDtos)){
                    for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto : hcsaSvcSpeRoutingSchemeDtos ) {
                        ValidationResult validationResultHcsaSvcSpeRoutingSchemeDto = WebValidationHelper.validateProperty(hcsaSvcSpeRoutingSchemeDto, serviceType);
                        if (validationResultHcsaSvcSpeRoutingSchemeDto.isHasErrors()) {
                            Map<String, String> validationResultHcsaSvcSpeRoutingSchemeDtoMap = validationResultHcsaSvcSpeRoutingSchemeDto.retrieveAll();
                            for (String key : validationResultHcsaSvcSpeRoutingSchemeDtoMap.keySet()) {
                                result.put(key + hcsaConfigPageDto.getStageCode() + hcsaConfigPageDto.getAppType()+hcsaSvcSpeRoutingSchemeDto.getInsOder(),
                                        validationResultHcsaSvcSpeRoutingSchemeDtoMap.get(key));
                            }
                        }
                    }
                }
            }

        }
        log.info(StringUtil.changeForLog("The validateHcsaConfigPageDto end ..."));
        return result;
    }


    private int getCountForTitle(String docTitle,List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos){
        int result = 0;
        for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocConfigDtos){
            if(docTitle.equals(hcsaSvcDocConfigDto.getDocTitle())){
                result++;
            }
        }
        return result;
    }

    private Map<String,String> transferErrorMapForPersonnel(Map<String,String> map,HcsaSvcPersonnelDto hcsaSvcPersonnelDto){
        Map<String,String> result = IaisCommonUtils.genNewHashMap();
        if(map != null){
            String pageMandatoryCount =  map.get("pageMandatoryCount");
            String pageMaximumCount =  map.get("pageMaximumCount");
            String psnType =  hcsaSvcPersonnelDto.getPsnType();
            String pageFeildName = ServiceConfigConstant.NAME_MAP.get(psnType);
            if(StringUtil.isNotEmpty(pageMandatoryCount)){
                result.put("man-"+pageFeildName,pageMandatoryCount);
            }
            if(StringUtil.isNotEmpty(pageMaximumCount)){
                result.put("mix-"+pageFeildName,pageMaximumCount);
            }
        }
        return result;
    }

    private void validateSvcCodeAndName(ConfigService configService,HcsaServiceDto hcsaServiceDto,Map<String, String> result){
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateSvcCodeAndName start ..."));
        Map<String, Boolean> entity = configService.isExistHcsaService(hcsaServiceDto);
        Boolean svcCode = entity.get("svcCode");
        if(svcCode!=null && svcCode){
            result.put("svcCode","SC_ERR002");
        }
        Boolean svcName = entity.get("svcName");
        if(svcName!=null && svcName){
            result.put("svcName","SC_ERR001");
        }
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateSvcCodeAndName end ..."));
    }
}
