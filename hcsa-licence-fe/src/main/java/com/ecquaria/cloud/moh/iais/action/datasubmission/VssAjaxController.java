package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
/**
 * @Description Ajax
 * @Auther fanghao on 26/5/2022.
 */
@RequestMapping(value = "/vss")
@RestController
@Slf4j
public class VssAjaxController {

    @Autowired
    private AppSubmissionService appSubmissionService;

    @GetMapping(value = "/prg-input-info")
    public @ResponseBody
    ProfessionalResponseDto getPrgNoInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the prgNo start ...."));
        String professionRegoNo = ParamUtil.getString(request, "prgNo");
        return appSubmissionService.retrievePrsInfo(professionRegoNo);
    }
}
