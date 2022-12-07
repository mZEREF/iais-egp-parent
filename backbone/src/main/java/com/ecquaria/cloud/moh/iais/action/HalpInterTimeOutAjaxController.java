package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/4/13 15:57
 **/
@Slf4j
@Controller
@RequestMapping("/halp-time-out")
public class HalpInterTimeOutAjaxController {

    @GetMapping(value = "internet.extend")
    public @ResponseBody
    Map<String, Object> interTimeOutExtend(HttpServletRequest request) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        AccessUtil.getLoginUser(request);
        log.info("internet.extend ok!!!!!!!");
        return map;
    }

    @GetMapping(value = "intranet.extend")
    public @ResponseBody
    Map<String, Object> intraTimeOutExtend(HttpServletRequest request) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        AccessUtil.getLoginUser(request);
        log.info("intranet.extend ok!!!!!!!");
        return map;
    }
}
