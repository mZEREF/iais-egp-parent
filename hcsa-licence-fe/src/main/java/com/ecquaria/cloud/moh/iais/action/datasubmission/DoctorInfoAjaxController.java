package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DocInfoService;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sop.webflow.process5.ProcessCacheHelper;

/**
 * @Description Ajax
 * @Auther fanghao on 26/5/2022.
 */
@RequestMapping(value = "/doc")
@RestController
@Slf4j
public class DoctorInfoAjaxController {

    @Autowired
    private AppCommService appSubmissionService;

    @Autowired
    private DocInfoService docInfoService;

    @GetMapping(value = "/prg-input-info")
    public @ResponseBody
    Map<String, Object> getPrgNoInfo(HttpServletRequest request) {
        String sessionId = UserSessionUtil.getLoginSessionID(request.getSession());
        UserSession userSession = ProcessCacheHelper.getUserSessionFromCache(sessionId);
        if (userSession == null || !"Active".equals(userSession.getStatus())) {
            throw new IaisRuntimeException("User session invalid");
        }
        log.debug(StringUtil.changeForLog("the prgNo start ...."));
        String professionRegoNo = ParamUtil.getString(request, "prgNo");
        String doctorSource = convertSource(ParamUtil.getString(request, "docSource"));
        String hciCode = ParamUtil.getString(request, "hciCode");
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(1);
        ProfessionalResponseDto professionalResponseDto=appSubmissionService.retrievePrsInfo(professionRegoNo);
        DoctorInformationDto doctorInformationDto=docInfoService.getDoctorInformationDtoByConds(professionRegoNo, doctorSource, hciCode);
        // PRS down
//        professionalResponseDto = new ProfessionalResponseDto();
//        professionalResponseDto.setHasException(true);
        //
        if(professionalResponseDto!=null){
            if("-1".equals(professionalResponseDto.getStatusCode()) || "-2".equals(professionalResponseDto.getStatusCode()) || professionalResponseDto.isHasException()){
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

    private String convertSource(String stage) {
        if (DataSubmissionConsts.DS_TOP.equals(stage)) {
            return DataSubmissionConsts.DOCTOR_SOURCE_ELIS_TOP;
        } else if (DataSubmissionConsts.DS_DRP.equals(stage)) {
            return DataSubmissionConsts.DOCTOR_SOURCE_ELIS_DRP;
        } else {
            return DataSubmissionConsts.DOCTOR_SOURCE_ELIS_VSS;
        }
    }
}
