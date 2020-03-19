package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @RequestMapping(value = "common.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> appGroup(HttpServletRequest request) {
        String groupNo = request.getParameter("groupNo");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(groupNo)){
            SearchParam searchParam = new SearchParam(ComPoolAjaxQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
            searchParam.addFilter("groupNo", groupNo, true);
            QueryHelp.setMainSql("inspectionQuery", "commonPoolAjax", searchParam);
            SearchResult<ComPoolAjaxQueryDto> ajaxResult = inspectionAssignTaskService.getAjaxResultByParam(searchParam);
            List<ComPoolAjaxQueryDto> comPoolAjaxQueryDtos = ajaxResult.getRows();
            if(!IaisCommonUtils.isEmpty(comPoolAjaxQueryDtos)){
                for(ComPoolAjaxQueryDto comPoolAjaxQueryDto : comPoolAjaxQueryDtos){
                    AppGrpPremisesDto appGrpPremisesDto = inspectionAssignTaskService.getAppGrpPremisesDtoByAppGroId(comPoolAjaxQueryDto.getAppPremCorrId());
                    String address = inspectionAssignTaskService.getAddress(appGrpPremisesDto);
                    if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                        comPoolAjaxQueryDto.setHciAddress(appGrpPremisesDto.getHciName() + " / " + address);
                    } else {
                        comPoolAjaxQueryDto.setHciAddress(address);
                    }
                }
            }
            map.put("result", "Success");
            map.put("result", ajaxResult);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }
}