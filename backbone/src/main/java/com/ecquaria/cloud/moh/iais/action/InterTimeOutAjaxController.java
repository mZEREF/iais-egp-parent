package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/4/13 15:57
 **/
@Slf4j
@Controller
@RequestMapping("/time-out")
public class InterTimeOutAjaxController {

    @RequestMapping(value = "internet.extend", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> interTimeOutExtend(HttpServletRequest request) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        log.info("internet.extend ok!!!!!!!");
        return map;
    }

    @RequestMapping(value = "intranet.extend", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> intraTimeOutExtend(HttpServletRequest request) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        log.info("intranet.extend ok!!!!!!!");
        return map;
    }
}
