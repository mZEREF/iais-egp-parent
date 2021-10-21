package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description Ajax
 * @Auther chenlei on 10/21/2021.
 */
@Controller
@Slf4j
public class ArAjaxController {

    @GetMapping(value = "/ar-cycle-stage")
    public @ResponseBody String addPremisesHtml(HttpServletRequest request) {
        String currStage = ParamUtil.getString(request, "currStage");
        StringBuilder opts = new StringBuilder();
        if (StringUtil.isEmpty(currStage)) {

        }
        return null;
    }
}
