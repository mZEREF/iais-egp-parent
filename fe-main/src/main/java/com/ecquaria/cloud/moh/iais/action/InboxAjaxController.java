package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.impl.InboxServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-30 13:39
 **/
@Slf4j
@Controller
@RequestMapping("/inbox")
public class InboxAjaxController implements LoginAccessCheck {

    @Autowired
    private InboxServiceImpl inboxService;

    @PostMapping(value="appInbox.do")
    public @ResponseBody Map<String,Object> doSomething(HttpServletRequest request, HttpServletResponse response)  {
        SearchParam appParam = (SearchParam) ParamUtil.getSessionAttr(request,InboxConst.APP_PARAM);
        Map<String,Object> map = new HashMap<>();
        if (appParam != null){
            appParam.addFilter("appType", request.getParameter("appNoPath"),true);
            QueryHelp.setMainSql("interInboxQuery","applicationQuery",appParam);
            SearchResult<InboxAppQueryDto> appResult = inboxService.appDoQuery(appParam);
            ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, appParam);
            ParamUtil.setRequestAttr(request,InboxConst.APP_RESULT, appResult);
            map.put("appResult", "Success");
        }else {
            map.put("appResult", "Fail");
        }
        return map;
    }
}