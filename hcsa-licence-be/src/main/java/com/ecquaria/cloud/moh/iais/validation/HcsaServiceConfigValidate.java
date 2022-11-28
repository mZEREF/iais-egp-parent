package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ServiceConfigConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.CategoryDisciplineErrorsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCategoryDisciplineDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubServiceErrorsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubServicePageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
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
        if(hcsaServiceConfigDto.isCreate()){
            validateSvcCodeAndName(configService,hcsaServiceDto,result);
        }

        //validate the hcsaSvcPersonnelDto
        validateHcsaSvcPersonnelDto(hcsaServiceConfigDto,result,serviceType);

        //validate the HcsaSvcDocConfigDto
        validateHcsaSvcDocConfigDto(hcsaServiceConfigDto,result,serviceType);


        if(HcsaConsts.SERVICE_TYPE_BASE.equals(hcsaServiceDto.getSvcType())){
            //for routingStages
            validateRoutingStages(hcsaServiceConfigDto,result,serviceType);
            //for Discipline hcsaServiceCategoryDisciplineDtoMap
            validateCategoryDiscipline(hcsaServiceConfigDto,result,serviceType);
            //for Discipline Map<String,HcsaServiceSubServicePageDto> specHcsaServiceSubServicePageDtoMap
            validateSpecHcsaServiceSubServicePageDtoMap(hcsaServiceConfigDto,result,serviceType);
            //for Discipline Map<String,HcsaServiceSubServicePageDto> otherHcsaServiceSubServicePageDtoMap
            validateOtherHcsaServiceSubServicePageDtoMap(hcsaServiceConfigDto,result,serviceType);
        }
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate end ..."));
        return result;
    }

    private void validteSubService(Map<String,HcsaServiceSubServicePageDto> hcsaServiceSubServicePageDtoMap,Map<String, String> result,String serviceType){
        if(hcsaServiceSubServicePageDtoMap != null && hcsaServiceSubServicePageDtoMap.size() > 0){
            for(String premisesType : hcsaServiceSubServicePageDtoMap.keySet()){
                HcsaServiceSubServicePageDto hcsaServiceSubServicePageDto = hcsaServiceSubServicePageDtoMap.get(premisesType);
                //for sub service
                String[] subServiceCodes = hcsaServiceSubServicePageDto.getSubServiceCodes();
                String[] levels = hcsaServiceSubServicePageDto.getLevels();
                if(subServiceCodes != null && subServiceCodes.length>0){
                    List<HcsaServiceSubServiceErrorsDto> hcsaServiceSubServiceErrorsDtos =  IaisCommonUtils.genNewArrayList();
                    for (int i=0; i<subServiceCodes.length;i++){
                        HcsaServiceSubServiceErrorsDto  hcsaServiceSubServiceErrorsDto = new HcsaServiceSubServiceErrorsDto();
                        hcsaServiceSubServiceErrorsDto.setSubServiceCode(subServiceCodes[i]);
                        hcsaServiceSubServiceErrorsDto.setLevel(levels[i]);
                        ValidationResult validationResultCategoryDisciplineErrorsDto = WebValidationHelper.validateProperty(hcsaServiceSubServiceErrorsDto,serviceType);
                        if(validationResultCategoryDisciplineErrorsDto.isHasErrors()){
                            String subServiceCodeError = validationResultCategoryDisciplineErrorsDto.retrieveAll().get("subServiceCode");
                            subServiceCodeError = MessageUtil.getMessageDesc(subServiceCodeError);
                            hcsaServiceSubServiceErrorsDto.setErrorMsg(subServiceCodeError);
                            result.put("subServiceCode",subServiceCodeError);
                        }else{
                            if(isRepeatCategoryDiscipline(subServiceCodes,subServiceCodes[i])){
                                hcsaServiceSubServiceErrorsDto.setErrorMsg("Please don't repeat it");
                                result.put("subServiceCode","Please don't repeat it");
                            }
                        }
                        hcsaServiceSubServiceErrorsDtos.add(hcsaServiceSubServiceErrorsDto);
                    }
                    hcsaServiceSubServicePageDto.setHcsaServiceSubServiceErrorsDtos(hcsaServiceSubServiceErrorsDtos);
                }
                hcsaServiceSubServicePageDtoMap.put(premisesType,hcsaServiceSubServicePageDto);
            }
        }
    }

    private void validateOtherHcsaServiceSubServicePageDtoMap(HcsaServiceConfigDto hcsaServiceConfigDto,Map<String, String> result,String serviceType){
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateOtherHcsaServiceSubServicePageDtoMap start ..."));
        Map<String,HcsaServiceSubServicePageDto> otherHcsaServiceSubServicePageDtoMap = hcsaServiceConfigDto.getOtherHcsaServiceSubServicePageDtoMap();
        validteSubService(otherHcsaServiceSubServicePageDtoMap,result,serviceType);
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateOtherHcsaServiceSubServicePageDtoMap end ..."));
    }

    private void validateSpecHcsaServiceSubServicePageDtoMap(HcsaServiceConfigDto hcsaServiceConfigDto,Map<String, String> result,String serviceType){
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateSpecHcsaServiceSubServicePageDtoMap start ..."));
        Map<String,HcsaServiceSubServicePageDto> specHcsaServiceSubServicePageDtoMap = hcsaServiceConfigDto.getSpecHcsaServiceSubServicePageDtoMap();
        if(specHcsaServiceSubServicePageDtoMap != null && specHcsaServiceSubServicePageDtoMap.size() > 0){
            //for sectionHeader
            /*for(String premisesType : specHcsaServiceSubServicePageDtoMap.keySet()){
                HcsaServiceSubServicePageDto hcsaServiceSubServicePageDto = specHcsaServiceSubServicePageDtoMap.get(premisesType);
                ValidationResult validationResultHcsaServiceCategoryDisciplineDto = WebValidationHelper.validateProperty(hcsaServiceSubServicePageDto,serviceType);
                if(validationResultHcsaServiceCategoryDisciplineDto.isHasErrors()){
                    String sectionHeaderErrorMsg = validationResultHcsaServiceCategoryDisciplineDto.retrieveAll().get("sectionHeader");
                    result.put(premisesType+"-SVTP003-sectionHeader",sectionHeaderErrorMsg);
                }
            }*/
            //for subService
            validteSubService(specHcsaServiceSubServicePageDtoMap,result,serviceType);
        }
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateSpecHcsaServiceSubServicePageDtoMap end ..."));
    }

    private void validateCategoryDiscipline(HcsaServiceConfigDto hcsaServiceConfigDto,Map<String, String> result,String serviceType){
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateCategoryDiscipline start ..."));
        Map<String,HcsaServiceCategoryDisciplineDto> hcsaServiceCategoryDisciplineDtoMap = hcsaServiceConfigDto.getHcsaServiceCategoryDisciplineDtoMap();
        if(hcsaServiceCategoryDisciplineDtoMap != null && hcsaServiceCategoryDisciplineDtoMap.size() > 0){
            for(String premisesType : hcsaServiceCategoryDisciplineDtoMap.keySet()){
                //for sectionHeader
                HcsaServiceCategoryDisciplineDto hcsaServiceCategoryDisciplineDto = hcsaServiceCategoryDisciplineDtoMap.get(premisesType);
                /*ValidationResult validationResultPermanentHscdDto = WebValidationHelper.validateProperty(hcsaServiceCategoryDisciplineDto,serviceType);
                if(validationResultPermanentHscdDto.isHasErrors()){
                    String sectionHeaderErrorMsg = validationResultPermanentHscdDto.retrieveAll().get("sectionHeader");
                    result.put(premisesType+"-sectionHeader",sectionHeaderErrorMsg);
                }*/
                //for categoryDiscipline
                String[] categoryDisciplines = hcsaServiceCategoryDisciplineDto.getCategoryDisciplines();
                if(categoryDisciplines != null && categoryDisciplines.length>0){
                    List<CategoryDisciplineErrorsDto> categoryDisciplineDtos =  IaisCommonUtils.genNewArrayList();
                    for(String categoryDiscipline : categoryDisciplines){
                        CategoryDisciplineErrorsDto  categoryDisciplineErrorsDto = new CategoryDisciplineErrorsDto();
                        categoryDisciplineErrorsDto.setCategoryDiscipline(categoryDiscipline);
                        ValidationResult validationResultCategoryDisciplineErrorsDto = WebValidationHelper.validateProperty(categoryDisciplineErrorsDto,serviceType);
                        if(validationResultCategoryDisciplineErrorsDto.isHasErrors()){
                            String categoryDisciplineErro = validationResultCategoryDisciplineErrorsDto.retrieveAll().get("categoryDiscipline");
                            categoryDisciplineErrorsDto.setErrorMsg(categoryDisciplineErro);
                            result.put("categoryDiscipline",categoryDisciplineErro);
                        }else{
                            if(isRepeatCategoryDiscipline(categoryDisciplines,categoryDiscipline)){
                                categoryDisciplineErrorsDto.setErrorMsg("Please don't repeat it");
                                result.put("categoryDiscipline","Please don't repeat it");
                            }
                        }
                        categoryDisciplineDtos.add(categoryDisciplineErrorsDto);
                    }
                    hcsaServiceCategoryDisciplineDto.setCategoryDisciplineDtos(categoryDisciplineDtos);
                }
                hcsaServiceCategoryDisciplineDtoMap.put(premisesType,hcsaServiceCategoryDisciplineDto);
            }
        }
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateCategoryDiscipline end ..."));
    }

    private boolean isRepeatCategoryDiscipline(String[] categoryDisciplines,String categoryDiscipline){
        boolean result = false;
        if(StringUtil.isEmpty(categoryDiscipline)){
            result = true;
        }else{
            int count = 0;
           for(String cd : categoryDisciplines){
              if(categoryDiscipline.equals(cd)){
                 count ++;
              }
           }
           if(count > 1){
               result = true;
           }
        }
        return result;
    }

    private void validateRoutingStages(HcsaServiceConfigDto hcsaServiceConfigDto,Map<String, String> result,String serviceType){
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateRoutingStages start ..."));
        Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtoMap =  hcsaServiceConfigDto.getHcsaConfigPageDtoMap();
        for(String key : hcsaConfigPageDtoMap.keySet()){
            if(!ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(key) && !ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(key)){
                List<HcsaConfigPageDto> hcsaConfigPageDtos = hcsaConfigPageDtoMap.get(key);
                for(HcsaConfigPageDto hcsaConfigPageDto : hcsaConfigPageDtos){
                    validateHcsaConfigPageDto(hcsaConfigPageDto,key,result,serviceType);
                    if(!ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(key) && !ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(key)){
                        validateHcsaConfigPageDto(hcsaConfigPageDto,key,result,hcsaConfigPageDto.getStageId());
                        validateHcsaConfigPageDto(hcsaConfigPageDto,key,result,hcsaConfigPageDto.getIsMandatory());
                    }else {
                        validateHcsaConfigPageDto(hcsaConfigPageDto,key,result,hcsaConfigPageDto.getIsMandatory());
                    }
                }

            }
        }
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateRoutingStages end ..."));
    }

  private void validateHcsaConfigPageDto(HcsaConfigPageDto hcsaConfigPageDto,String key,Map<String, String> result,String validateString){
      Map<String,String> hcsaConfigPageDtoError = validateHcsaConfigPageDto(hcsaConfigPageDto,validateString);
      if(hcsaConfigPageDtoError.size() > 0){
          result.put(key,"Error");
          result.putAll(hcsaConfigPageDtoError);
      }
  }

    private void validateHcsaSvcDocConfigDto(HcsaServiceConfigDto hcsaServiceConfigDto,Map<String, String> result,String serviceType){
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateHcsaSvcDocConfigDto start ..."));
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
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateHcsaSvcDocConfigDto end ..."));
    }

    private void validateHcsaSvcPersonnelDto(HcsaServiceConfigDto hcsaServiceConfigDto,Map<String, String> result,String serviceType){
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateHcsaSvcPersonnelDto start ..."));
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
        if(IaisCommonUtils.isNotEmpty(hcsaSvcPersonnelDtos)){
            for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
                ValidationResult validationResultHcsaSvcPersonnelDto = WebValidationHelper.validateProperty(hcsaSvcPersonnelDto,serviceType);
                if(validationResultHcsaSvcPersonnelDto.isHasErrors()){
                    result.putAll(transferErrorMapForPersonnel(validationResultHcsaSvcPersonnelDto.retrieveAll(),hcsaSvcPersonnelDto));
                }
            }
        }
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate validateHcsaSvcPersonnelDto end ..."));
    }

    private Map<String,String> validateHcsaConfigPageDto(HcsaConfigPageDto hcsaConfigPageDto,String validateString ){
        log.info(StringUtil.changeForLog("The validateHcsaConfigPageDto start ..."));
        Map<String,String> result = IaisCommonUtils.genNewHashMap();

                // for Ins hcsaConfigPageDto
                ValidationResult validationResultHcsaConfigPageDto = WebValidationHelper.validateProperty(hcsaConfigPageDto,validateString);
                if(validationResultHcsaConfigPageDto.isHasErrors()){
                    Map<String,String>  validationResultHcsaConfigPageDtoMap = validationResultHcsaConfigPageDto.retrieveAll();
                    for(String key : validationResultHcsaConfigPageDtoMap.keySet()){
                        result.put(key+hcsaConfigPageDto.getStageCode()+hcsaConfigPageDto.getAppType(),
                                validationResultHcsaConfigPageDtoMap.get(key));
                    }
                }
                // for Ins HcsaSvcSpeRoutingSchemeDto
               /* List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaConfigPageDto.getHcsaSvcSpeRoutingSchemeDtos();
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
                }*/

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
