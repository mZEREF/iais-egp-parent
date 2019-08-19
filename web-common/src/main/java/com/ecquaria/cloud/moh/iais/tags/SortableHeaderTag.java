package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.Map;
@Slf4j
public class SortableHeaderTag extends DivTagSupport {
    private static final long serialVersionUID = 6803399323616533101L;
    private static final String ACTIVE = " active";
    private String field;
    private String value;
    private String param;
    private String jsFunc;
    private boolean needSort;

    public SortableHeaderTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        field = "";
        value = "";
        param = "";
        jsFunc = "";
        needSort = true;
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public int doStartTag() throws JspException {
        if (StringUtil.isEmpty(param)) {
            param = "searchParam";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<th class=\"sorting\">");
        sb.append(StringUtil.viewHtml(value));
        if (needSort) {
            sb.append("<span class=\"column-sort\">");
            String isActiveUp = "";
            String isActiveDown = "";
            SearchParam searchParam = (SearchParam) ParamUtil.getScopeAttr((HttpServletRequest) pageContext.getRequest(),
                    param);
            if (searchParam != null) {
                Map<String, String> sortMap = searchParam.getSortMap();
                if (sortMap.containsKey(field.toUpperCase())) {
                    String type = sortMap.get(field.toUpperCase());
                    if (SearchParam.ASCENDING.equals(type))
                        isActiveUp = ACTIVE;
                    else if (SearchParam.DESCENDING.equals(type))
                        isActiveDown = ACTIVE;
                }
            }
            generateHtml(sb,isActiveUp,isActiveDown);
        }
        sb.append("</th>");

        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(sb.toString()));
        } catch (Exception ex) {
            log.error("", ex);
            throw new JspTagException("SortableHeaderTag: " + ex.getMessage());
        }
        release();

        return SKIP_BODY;
    }

    private void generateHtml(StringBuilder sb,String isActiveUp,String isActiveDown){
        if (StringUtil.isEmpty(jsFunc)) {
            jsFunc = "sortRecords";
        }
        sb.append("<a class=\"sort-up");
        sb.append(isActiveUp).append("\"").append(" href=\"");
        if (!ACTIVE.equals(isActiveUp)) {
            sb.append("javascript:");
            sb.append(jsFunc);
            sb.append("('");
            sb.append(field);
            sb.append("', '");
            sb.append(SearchParam.ASCENDING);
            sb.append("');");
        } else {
            sb.append("#");
        }
        sb.append("\"  title=\"Sort up\"></a>");
        sb.append("<a class=\"sort-down");
        sb.append(isActiveDown).append("\"").append(" href=\"");
        if (!ACTIVE.equals(isActiveDown)) {
            sb.append("javascript:");
            sb.append(jsFunc);
            sb.append("('");
            sb.append(field);
            sb.append("', '");
            sb.append(SearchParam.DESCENDING);
            sb.append("');");
        } else {
            sb.append("#");
        }
        sb.append("\" title=\"Sort down\"></a></span>");
    }
    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setField(String field) {
        this.field = field;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setParam(String param) {
        this.param = param;
    }
    public void setJsFunc(String jsFunc) {
		this.jsFunc = jsFunc;
	}
    public void setNeedSort(boolean needSort) {
        this.needSort = needSort;
    }
}
