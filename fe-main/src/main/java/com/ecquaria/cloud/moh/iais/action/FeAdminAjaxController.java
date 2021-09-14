package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2019-1-19 13:39
 **/
@Slf4j
@Controller
@RequestMapping("/feAdmin")
public class FeAdminAjaxController {

    @Autowired
    private OrgUserManageService orgUserManageService;

    @RequestMapping(value = "active.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> active(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String targetStatus = request.getParameter("targetStatus");
        String res = orgUserManageService.ChangeActiveStatus(userId,targetStatus);
        Map<String, Object> map = new HashMap();
        if (res != null) {
            map.put("active", res);
            map.put("result", "Success");
            return map;
        }else{
            map.put("result", "Fail");
            return map;
        }

    }


}