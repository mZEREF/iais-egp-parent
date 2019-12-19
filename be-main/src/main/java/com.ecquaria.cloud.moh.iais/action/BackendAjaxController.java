package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
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
 * @Create: 2019-11-30 13:39
 **/
@Slf4j
@Controller
@RequestMapping("/backend")
public class BackendAjaxController {
    @Autowired
    InspectionService inspectionService;

    @RequestMapping(value="appGroup.do", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> appGroup(HttpServletRequest request, HttpServletResponse response)  {
        String groupNo = request.getParameter("groupno");
        SearchParam searchParamAjax = (SearchParam)ParamUtil.getSessionAttr(request,"searchParamAjax");
        Map<String, String> appNoUrl = (Map<String, String>)ParamUtil.getSessionAttr(request,"appNoUrl");

        Map<String,Object> map = new HashMap();
        if (groupNo != null){
            searchParamAjax.addFilter("groupNo", groupNo,true);

            QueryHelp.setMainSql("inspectionQuery", "AppByGroupAjax",searchParamAjax);
            SearchResult<InspectionAppInGroupQueryDto> ajaxResult = inspectionService.searchInspectionBeAppGroupAjax(searchParamAjax);

            map.put("appNoUrl", appNoUrl);
            map.put("ajaxResult", ajaxResult);
            map.put("result", "Success");
        }else {
            map.put("result", "Fail");
        }
        return map;
    }
}