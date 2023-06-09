package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.action.LoginAccessCheck;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PreTerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import com.ecquaria.cloud.moh.iais.validation.dataSubmission.PreTerminationValidator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description Ajax
 * @Auther zhixing on 2/14/2022.
 */

@RequestMapping(value = "/top")
@RestController
@Slf4j
public class TopAjaxController implements LoginAccessCheck {

    protected final static String  CONSULTING_CENTER = "Health Promotion Board Counselling Centre";
    @Autowired
    private TopDataSubmissionService topDataSubmissionService;

    @Autowired
    private AppSubmissionService appSubmissionService;

    @PostMapping(value = "/retrieve-identification")
    public @ResponseBody
    Map<String, Object> retrieveIdentification(HttpServletRequest request) {
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        PatientInformationDto patientInformation = new PatientInformationDto();
        patientInformation.setIdType(idType);
        patientInformation.setIdNumber(idNo.toUpperCase());
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(2);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult vr = WebValidationHelper.validateProperty(patientInformation, "ART");
        if (vr != null && vr.isHasErrors()) {
            Map<String, String> params = IaisCommonUtils.genNewHashMap();
            params.put("idType", "idType");
            params.put("idNumber", "idNumber");
            errorMap.putAll(vr.retrieveAll(params));
        }
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);

        if (!errorMap.isEmpty()) {
            result.put(IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            topSuperDataSubmissionDto.setDraftPatientValid(false);
            DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto,request);

        } else {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
            PatientInformationDto top = topDataSubmissionService.getTopPatientSelect(idType, idNo, orgId);
            /*if(StringUtil.isEmpty(top.getPatientAge())){
                top.setPatientAge(0);
            }*/
            if (top != null) {
                patientInformation = top;
                topSuperDataSubmissionDto.setDraftPatientValid(true);
                DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto,request);

            }



            result.put("selection", patientInformation);
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/counselling-age")
    public Map<String, Object> checkCounsellingAge(HttpServletRequest request) throws Exception {
        String birthDate = (String) ParamUtil.getSessionAttr(request, "birthDate");
        String counsellingGiven = ParamUtil.getString(request, "counsellingGiven");
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(2);
        if (StringUtil.isEmpty(birthDate) || !CommonValidator.isDate(birthDate) || Formatter.compareDateByDay(birthDate) > 0 || StringUtil.isEmpty(counsellingGiven)) {
            result.put("birthDate", Boolean.TRUE);
            return result;
        }
        boolean b = PreTerminationValidator.validateDate(birthDate);
        boolean b1 = PreTerminationValidator.validateDate(counsellingGiven);
        if (b && b1) {
            int ageNew=Formatter.getAge(birthDate,counsellingGiven);

            PreTerminationDto preTerminationDto = new PreTerminationDto();
            preTerminationDto.setCounsellingAge(ageNew);
            result.put("selection", preTerminationDto);
        }
        return result;
    }

  /* @PostMapping(value = "/check-date")
    public @ResponseBody
   AjaxResDto checkDate(HttpServletRequest request){
        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_SUCCESS);
        Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
        chargesTypeAttr.put("name", "counsellingPlace");
        chargesTypeAttr.put("id", "counsellingPlaces");
        List<String> checkedVals = IaisCommonUtils.genNewArrayList();
        String counsellingPlace = (String) ParamUtil.getSessionAttr(request, "counsellingPlace");
        if(!StringUtil.isEmpty(counsellingPlace)){
           checkedVals.add(counsellingPlace);
        }
        String chargeTypeSelHtml = NewApplicationHelper.genMutilSelectOpHtml(chargesTypeAttr, getSelect(request), null, checkedVals, false,true);
        String counsellingPlaceError = (String) ParamUtil.getSessionAttr(request, "counsellingPlaceError");
        chargeTypeSelHtml = chargeTypeSelHtml + "<span  class=\"error-msg\" name=\"iaisErrorMsg\" id=\"error_counsellingPlaceError\">";
        if(!StringUtil.isEmpty(counsellingPlaceError)){
            counsellingPlaceError = MessageUtil.getMessageDesc(counsellingPlaceError);
            chargeTypeSelHtml = chargeTypeSelHtml +counsellingPlaceError;
        }
        ParamUtil.setSessionAttr(request,"counsellingPlaceError",null);
        chargeTypeSelHtml = chargeTypeSelHtml +" </span>";
        ajaxResDto.setResultJson(chargeTypeSelHtml);
        return ajaxResDto;
    }*/

    private List<SelectOption> getSelect(HttpServletRequest request){
        Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap();
        DataSubmissionHelper.setTopPremisesMap(request).values().stream().forEach(v->stringStringMap.put(v.getHciCode(),v.getPremiseLabel()));
        List<SelectOption> result = DataSubmissionHelper.genOptions(stringStringMap);
        String birthDate = (String) ParamUtil.getSessionAttr(request, "birthDate");
        String counsellingGivenDate = ParamUtil.getString(request, "counsellingGivenDate");
        if(StringUtil.isNotEmpty(counsellingGivenDate)){
            try {
                int ageNew=Formatter.getAge(birthDate,counsellingGivenDate);
                if(!StringUtil.isEmpty(ageNew)){
                    if(ageNew<16){
                        result.add(new SelectOption(DataSubmissionConsts.AR_SOURCE_OTHER,CONSULTING_CENTER));
                    }
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
        return result;
    }

}
