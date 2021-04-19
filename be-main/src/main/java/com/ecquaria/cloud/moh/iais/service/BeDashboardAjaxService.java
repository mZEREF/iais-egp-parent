package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/4/19 10:26
 **/
public interface BeDashboardAjaxService {

    /**
      * @author: shicheng
      * @Date 2021/4/19
      * @Param: groupNo, loginContext, map
      * @return: void
      * @Descripation: Common Pool Dropdown
      */
    void getCommonDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, String actionValue);
}
