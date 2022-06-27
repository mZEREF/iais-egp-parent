package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DocInfoService;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description Ajax
 * @Auther fanghao on 26/5/2022.
 */
@RequestMapping(value = "/doc")
@RestController
@Slf4j
public class DocInfoAjaxController {

    @Autowired
    private AppCommService appSubmissionService;

    @Autowired
    private DocInfoService docInfoService;

    @GetMapping(value = "/prg-input-info")
    public @ResponseBody
    Map<String, Object> getPrgNoInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the prgNo start ...."));
        String professionRegoNo = ParamUtil.getString(request, "prgNo");
        /*String doctorSource = ParamUtil.getString(request,"docSource");*/
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(1);
        ProfessionalResponseDto professionalResponseDto=appSubmissionService.retrievePrsInfo(professionRegoNo);
        DoctorInformationDto doctorInformationDto=docInfoService.getDoctorInformationDtoByConds(professionRegoNo,"ELIS");
        if(professionalResponseDto!=null){
            if("-1".equals(professionalResponseDto.getStatusCode()) || "-2".equals(professionalResponseDto.getStatusCode())){
                if(doctorInformationDto!=null){
                    professionalResponseDto.setName(doctorInformationDto.getName());
                    professionalResponseDto.setSpecialty(Collections.singletonList((doctorInformationDto.getSpeciality())));
                    professionalResponseDto.setQualification(Collections.singletonList(doctorInformationDto.getQualification()));
                    professionalResponseDto.setSubspecialty(Collections.singletonList(doctorInformationDto.getSubSpeciality()));
                }
            }
        }
        result.put("selections", doctorInformationDto);
        result.put("selection", professionalResponseDto);
        return result;
    }
}
