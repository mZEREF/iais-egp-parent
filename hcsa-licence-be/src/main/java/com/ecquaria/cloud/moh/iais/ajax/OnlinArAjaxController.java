package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAppDetailsQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * OnlinArAjaxController
 *
 * @author junyu
 * @date 2021/11/30
 */
@Slf4j
@Controller
@RequestMapping("/hcsa/intranet/ar")
public class OnlinArAjaxController {


    @RequestMapping(value = "patientDetail.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> patientDetailAjax(HttpServletRequest request, HttpServletResponse response) {
        String dashFilterAppNo = (String) ParamUtil.getSessionAttr(request, "dashFilterAppNo");
        List<String> serviceList = (List<String>)ParamUtil.getSessionAttr(request, "dashSvcCheckList");
        List<String> appTypeList = (List<String>)ParamUtil.getSessionAttr(request, "dashAppTypeCheckList");
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, "dashSearchParam");
        String patientId = request.getParameter("patientId");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(patientId)){
            SearchParam searchParam = new SearchParam(DashAppDetailsQueryDto.class.getName());
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
            //set filter
            //searchParam = dashSysDetailDropFilter(searchParam, groupId, serviceList, appTypeList, searchParamGroup, dashFilterAppNo);
            //search
            //QueryHelp.setMainSql("intraDashboardQuery", "dashSystemDetailAjax", searchParam);
            SearchResult<DashAppDetailsQueryDto> searchResult = null;
            //set other data
            //searchResult = beDashboardAjaxService.setDashSysDetailsDropOtherData(searchResult);

            map.put("result", "Success");
            map.put("ajaxResult", searchResult);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

}
