package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.base.TableRowHtmlGenerater;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PageRecords;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * CommonActionAjax
 *
 * @author Jinhua
 * @date 2020/5/27 18:04
 */
@Controller
@RequestMapping("/commonAjax/*")
public class CommonActionAjax {
    @RequestMapping(value="changeMemoryPage.do", method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> changeMemoryPage(HttpServletRequest request,
                                   HttpServletResponse response)  {
        Map<String,Object> map = new HashMap();

        int pageNo = ParamUtil.getInt(request, "pageNum", 1);
        String pageDiv = ParamUtil.getString(request, "pageDiv");
        String checkIdStr = ParamUtil.getString(request, "checkId");
        PaginationHandler<?> handler = (PaginationHandler<?>) ParamUtil.getSessionAttr(request, pageDiv + "__SessionAttr");
        // Update Checked
        if (!StringUtil.isEmpty(checkIdStr) && handler.getDisplayData() != null && !handler.getDisplayData().isEmpty()) {
            String[] checkIds = checkIdStr.split(",");
            for (PageRecords cu : handler.getDisplayData()) {
                cu.setChecked(false);
            }
            if (checkIds.length > 1) {
                for (int i = 1; i < checkIds.length; i++) {
                    long id = Long.parseLong(checkIds[i]);
                    for (PageRecords row : handler.getDisplayData()) {
                        if (row.getId() == id) {
                            row.setChecked(true);
                        }
                    }
                }
            }
        }
        handler.doPaging(pageNo);
        StringBuilder sb = new StringBuilder();
        boolean allChecked = !handler.getDisplayData().isEmpty();
        for (int i = 0; i < handler.getDisplayData().size(); i++) {
            PageRecords pr = handler.getDisplayData().get(i);
            Object obj = pr.getRecord();
            if (obj instanceof TableRowHtmlGenerater) {
                int startLength = sb.length();
                sb.append("<tr>");
                if (handler.getCheckType() == PaginationHandler.CHECK_TYPE_CHECKBOX) {
                    sb.append("<td><input type=\"checkbox\" id=\"").append(pageDiv).append("Check").append(i);
                    sb.append("\" name=\"").append(pageDiv).append("Check").append("\" value=\"");
                    sb.append(pr.getId()).append('"');
                    sb.append(" onclick=\"javascript:memoryCheckBoxChange('").append(pageDiv);
                    sb.append("', this);\"");
                    if (pr.isChecked()) {
                        sb.append(" checked");
                    } else {
                        allChecked = false;
                    }
                    if (handler.isCheckBoxDisable()) {
                        sb.append(" disabled");
                    }
                    sb.append("/>").append("</td>");
                } else if (handler.getCheckType() == PaginationHandler.CHECK_TYPE_RADIO) {
                    sb.append("<td><input type=\"radio\" id=\"").append(pageDiv).append("Check").append(i);
                    sb.append("\" name=\"").append(pageDiv).append("Check").append("\" value=\"");
                    sb.append(pr.getId()).append('"');
                    if (pr.isChecked()) {
                        sb.append(" checked");
                    }
                    sb.append("/>").append("</td>");
                }
                if (handler.isCheckInTrail()) {
                    sb.insert(startLength + 4, ((TableRowHtmlGenerater) obj).generateTableRowHtml());
                } else {
                    sb.append(((TableRowHtmlGenerater) obj).generateTableRowHtml());
                }
                sb.append("</tr>");
            }
        }
        if (handler.getCheckType() == PaginationHandler.CHECK_TYPE_CHECKBOX) {
            map.put("checkAllRemove", allChecked ? "0" : "1");
        }
        map.put("recHtml", sb.toString());
        map.put("pageDivId", pageDiv);
        map.put("recDivId", handler.getRecordsDiv());
        map.put("pageHtml", handler.getPaginationHtml());

        return map;
    }
}
