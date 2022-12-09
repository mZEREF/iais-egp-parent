package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.base.TableRowHtmlGenerater;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PageRecords;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * CommonActionAjax
 *
 * @author Jinhua
 * @date 2020/5/27 18:04
 */
@Controller
@RequestMapping("/commonAjax/*")
public class CommonActionAjax {

    private static final String SESSION_ATT = "__SessionAttr";

    private static final String CHECKED = "Check";

    @GetMapping(value="changeMemoryPage.do")
    public @ResponseBody Map<String,Object> changeMemoryPage(HttpServletRequest request,
                                   HttpServletResponse response)  {


        int pageNo = ParamUtil.getInt(request, "pageNum", 1);
        String pageDiv = ParamUtil.getString(request, "pageDiv");
        String checkIdStr = ParamUtil.getString(request, "checkId");
        PaginationHandler<?> handler = (PaginationHandler<?>) ParamUtil.getSessionAttr(request, pageDiv + SESSION_ATT);

        Map<String,Object> map = changeMemoryPageImpl(pageNo, pageDiv, checkIdStr, handler);
        ParamUtil.setSessionAttr(request, pageDiv + SESSION_ATT, handler);

        return map;
    }

    @GetMapping(value="changeMemoryPageSize.do")
    public @ResponseBody Map<String,Object> changeMemoryPageSize(HttpServletRequest request,
                                                             HttpServletResponse response)  {
        String pageDiv = ParamUtil.getString(request, "pageDiv");
        int pageSize = ParamUtil.getInt(request, "newSize");
        PaginationHandler<?> handler = (PaginationHandler<?>) ParamUtil.getSessionAttr(request, pageDiv + SESSION_ATT);
        handler.setPageSize(pageSize);
        Map<String,Object> map = changeMemoryPageImpl(1, pageDiv, null, handler);
        ParamUtil.setSessionAttr(request, pageDiv + SESSION_ATT, handler);
        return map;
    }

    private Map<String,Object> changeMemoryPageImpl(int pageNo, String pageDiv, String checkIdStr,
                                                    PaginationHandler<?> handler) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();

        // Update Checked
        extracted(checkIdStr, handler);
        handler.doPaging(pageNo);
        StringBuilder sb = new StringBuilder();
        boolean allChecked = !handler.getDisplayData().isEmpty();
        allChecked = isAllChecked(pageDiv, handler, sb, allChecked);
        if (handler.getCheckType() == PaginationHandler.CHECK_TYPE_CHECKBOX) {
            map.put("checkAllRemove", allChecked ? "0" : "1");
        }
        map.put("recHtml", sb.toString());
        map.put("pageDivId", pageDiv);
        map.put("recDivId", handler.getRecordsDiv());
        map.put("pageHtml", handler.getPaginationHtml());
        return map;
    }

    private boolean isAllChecked(String pageDiv, PaginationHandler<?> handler, StringBuilder sb, boolean allChecked) {
        for (int i = 0; i < handler.getDisplayData().size(); i++) {
            PageRecords<?> pr = handler.getDisplayData().get(i);
            Object obj = pr.getRecord();
            if (obj instanceof TableRowHtmlGenerater) {
                int startLength = sb.length();
                sb.append("<tr>");
                if (handler.getCheckType() == PaginationHandler.CHECK_TYPE_CHECKBOX) {
                    sb.append("<td><input type=\"checkbox\" id=\"").append(pageDiv).append(CHECKED).append(i);
                    sb.append("\" name=\"").append(pageDiv).append(CHECKED).append("\" value=\"");
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
                    sb.append("<td><div class=\"form-check\"><input class=\"form-check-input\" type=\"radio\" id=\"").append(pageDiv).append(CHECKED).append(i);
                    sb.append("\" name=\"").append(pageDiv).append(CHECKED).append("\" value=\"");
                    sb.append(pr.getId()).append('"');
                    if (pr.isChecked()) {
                        sb.append(" checked");
                    }
                    sb.append("/>").append("<label class=\"form-check-label\"><span class=\"check-circle\"></span></label></div></td>");
                }
                if (handler.isCheckInTrail()) {
                    sb.insert(startLength + 4, ((TableRowHtmlGenerater) obj).generateTableRowHtml());
                } else {
                    sb.append(((TableRowHtmlGenerater) obj).generateTableRowHtml());
                }
                sb.append("</tr>");
            }
        }
        return allChecked;
    }

    private void extracted(String checkIdStr, PaginationHandler<?> handler) {
        if (!StringUtil.isEmpty(checkIdStr) && handler.getDisplayData() != null && !handler.getDisplayData().isEmpty()) {
            String[] checkIds = checkIdStr.split(",");
            if (handler.getCheckType() == PaginationHandler.CHECK_TYPE_RADIO && !"NA".equals(checkIdStr)) {
                handler.uncheckAllData();
            } else {
                for (PageRecords<?> cu : handler.getDisplayData()) {
                    cu.setChecked(false);
                }
            }

            if (checkIds.length > 1) {
                for (int i = 1; i < checkIds.length; i++) {
                    long id = Long.parseLong(checkIds[i]);
                    for (PageRecords<?> row : handler.getDisplayData()) {
                        if (row.getId() == id) {
                            row.setChecked(true);
                        }
                    }
                }
            }
        }
    }
}
