package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2019-11-30 13:39
 **/
@Slf4j
@Controller
@RequestMapping("/emailAjax")
public class emailAjaxController {

    @Autowired
    DistributionListService distributionListService;
    @RequestMapping(value = "recipientsRoles.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> appGroup(HttpServletRequest request, HttpServletResponse response) {
        String serviceId = request.getParameter("serviceId");

        String status = AppConsts.COMMON_STATUS_ACTIVE;
        Map<String, Object> map = new HashMap();
        Map<String ,String> selection = new HashMap<>();
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtoList = distributionListService.roleByServiceId(serviceId,status);
        map.put("roles", hcsaSvcPersonnelDtoList);
        return map;
    }

}