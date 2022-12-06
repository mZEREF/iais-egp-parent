package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * ValidationAjaxController
 *
 * @author Jinhua
 * @date 2019/11/11 10:30
 */
@Controller
@Slf4j
public class ValidationAjaxController {
    @RequestMapping(value = "/validation.do", method = RequestMethod.POST)
    public @ResponseBody String doValidation(HttpServletRequest request, HttpServletResponse response) {
        String[] contorllerPara = ParamUtil.getStrings(request, "paramController");
        if (contorllerPara == null) {
            return null;
        }
        String[] entityPara = ParamUtil.getStrings(request, "valEntity");
        String[] profiles = ParamUtil.getStrings(request,"valProfiles");

        Map<String, String> errorMsg = new HashMap<String, String>();
        try {
            for (int i = 0; i < contorllerPara.length; i++) {
                if (StringUtil.isEmpty(contorllerPara[i])) {
                    continue;
                }
                String controllerName = contorllerPara[i];

                Class<?> clazz = MiscUtil.getClassFromName(controllerName);
                Object obj = clazz.newInstance();
                Method method = clazz.getMethod("getValueFromPage", new Class[] {HttpServletRequest.class});

                // validation start
                log.info(StringUtil.changeForLog("<== The AJAX Validation DTO Class ==>" + entityPara[i].replaceAll("[\r\n]",
                        "")));
                if (!StringUtil.isEmpty(profiles[i])) {
                    ValidationResult constraintViolations =
                            WebValidationHelper.validateProperty(MiscUtil.getClassFromName(entityPara[i]).cast(
                                    method.invoke(obj, new Object[] {request})), profiles[i]);
                    if (constraintViolations.isHasErrors()) {
                        errorMsg.putAll(constraintViolations.retrieveAll());
                    }
                } else {
                    ValidationResult constraintViolations =
                            WebValidationHelper.validateEntity(MiscUtil.getClassFromName(entityPara[i]).cast(
                                    method.invoke(obj, new Object[] {request})));
                    if (constraintViolations.isHasErrors()) {
                        errorMsg.putAll(constraintViolations.retrieveAll());
                    }
                }
                // validation end.
            }

            return WebValidationHelper.generateJsonStr(errorMsg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException("",e);
        }
    }


}
