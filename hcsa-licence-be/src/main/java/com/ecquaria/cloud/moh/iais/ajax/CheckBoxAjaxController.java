package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yichen
 * @Date:2021/3/2
 */
@Slf4j
@Controller
@RequestMapping("/checkbox-ajax/")
public class CheckBoxAjaxController {
    @GetMapping(value = "/record-status")
    public @ResponseBody void changeCheckbox(HttpServletRequest request){
        log.info("==========>changeCheckbox>>>>>>>>>>>>>>>>>>");
        IaisEGPHelper.onChangeCheckbox(request);
    }
}
