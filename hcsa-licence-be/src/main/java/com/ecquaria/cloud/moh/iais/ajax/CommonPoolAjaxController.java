package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2019-11-30 13:39
 **/
@Slf4j
@Controller
@RequestMapping("/common-pool")
public class CommonPoolAjaxController {

    @RequestMapping(value = "common.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> appGroup(HttpServletRequest request) {
        String groupNo = request.getParameter("groupNo");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(true){
            map.put("result", "Success");
        } else {
            map.put("result", "Fail");
        }
        return map;
    }
}