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
    private static final long serialVersionUID = 1L;
    private static final String ACTIVE = " active";
    private String field;
    private String value;
    private String param;
    private String jsFunc;
    private boolean needSort;
    private boolean isFE;
    private String customSpacing;

    public SortableHeaderTag() {
        super();
        clearValue();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        clearValue();
    }

    private void clearValue() {
        setField("");
        setValue("");
        setParam("");
        setJsFunc("");
        setNeedSort(true);
        setIsFE(false);
        setCustomSpacing("");
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
            setParam( "searchParam");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<th ");
        if(needSort){
            sb.append(" class=\"sorting\" ");
        }
        if(!StringUtil.isEmpty(style)){
            sb.append("style=\"").append(style).append("\" >");
        }else{
            sb.append('>');
        }

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
                    if (SearchParam.ASCENDING.equals(type)){
                        isActiveUp = ACTIVE;
                    } else if (SearchParam.DESCENDING.equals(type)){
                        isActiveDown = ACTIVE;
                    }
                }
            }

            generateHtml(sb,isActiveUp,isActiveDown);
        }
        if(isFE){
            sb.append("<p style=\"margin-top: 6px;\">");
            sb.append(StringUtil.viewHtml(value));
            sb.append("</p>");
        }else{
            if(StringUtil.isNotEmpty(customSpacing)){
                sb.append("<p style=\"margin-left:").append(customSpacing).append("px;\">");
            }else {
                sb.append("<p>");
            }
            sb.append(StringUtil.viewHtml(value));
            sb.append("</p>");
        }
        sb.append("</th>");

        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(sb.toString()));
        } catch (Exception ex) {
            log.error("", ex);
            throw new JspTagException("SortableHeaderTag: " + ex.getMessage(),ex);
        }
        release();

        return SKIP_BODY;
    }

    private void generateHtml(StringBuilder sb,String isActiveUp,String isActiveDown){
        if (StringUtil.isEmpty(jsFunc)) {
            setJsFunc("sortRecords");
        }
        sb.append("<a class=\"sort-up" );
        sb.append(isActiveUp).append('\"').append(" href=\"");
        if (!ACTIVE.equals(isActiveUp)) {
            sb.append("javascript:");
            sb.append(jsFunc);
            sb.append("('");
            sb.append(field);
            sb.append("', '");
            sb.append(SearchParam.ASCENDING);
            sb.append("');");
        } else {
            sb.append('#');
        }
        sb.append("\"  title=\"Sort up\"><span class=\"glyphicon glyphicon-chevron-up\"></span></a>");
        sb.append("<a class=\"sort-down ");
        sb.append(isActiveDown).append('\"').append(" href=\"");
        if (!ACTIVE.equals(isActiveDown)) {
            sb.append("javascript:");
            sb.append(jsFunc);
            sb.append("('");
            sb.append(field);
            sb.append("', '");
            sb.append(SearchParam.DESCENDING);
            sb.append("');");
        } else {
            sb.append('#');
        }
        sb.append("\" title=\"Sort down\"><span class=\"glyphicon glyphicon-chevron-down\" style=\"font-size: 10px\"></span></a></span>");


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
    public void setIsFE(boolean isFE) {
        this.isFE = isFE;
    }
    public void setCustomSpacing(String customSpacing) {
        this.customSpacing = customSpacing;
    }
}
