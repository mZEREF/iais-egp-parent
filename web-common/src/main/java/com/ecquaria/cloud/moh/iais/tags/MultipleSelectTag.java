package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.List;

@Slf4j
public class MultipleSelectTag extends DivTagSupport {
    private static final long serialVersionUID = 3178008461063547760L;
    private String name;
    private String options;
    private boolean needMask;
    private String selectValue;
    private boolean needErrorSpan;
    public MultipleSelectTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        id = null;
        name = null;
        cssClass = "";
        style = "";
        selectValue = "";
        needMask = false;
        needErrorSpan = false;
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public int doStartTag() throws JspException {
        try {
            if (StringUtil.isEmpty(cssClass) && StringUtil.isEmpty(style)) {
                cssClass = "checkbox-custom check-primary";
                style = "margin-left: 2px";
            }
            StringBuilder html = new StringBuilder();
            generateOptionHtml(html);
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            log.error("", ex);
            throw new JspTagException("MultipleSelectTag: " + ex.getMessage());
        }
        release();

        return SKIP_BODY;
    }

    private void generateOptionHtml(StringBuilder html){
        List<SelectOption> sos = null;
        if (!StringUtil.isEmpty(options)) {
            sos = (List<SelectOption>) ParamUtil.getScopeAttr((HttpServletRequest) pageContext.getRequest(), options);
        }
        if (needMask) {
            MaskUtil.maskSelectOptions(name, sos);
        }
        html.append(" <div style=\"height: 200px; border: 1px solid darkgrey;overflow: scroll\" id=\"").append(name).append("Clear").append("\">");
        if (sos != null) {
            int index = 0;
            for (SelectOption option : sos) {
                String val = StringUtil.viewNonNullHtml(option.getValue());
                String txt = StringUtil.escapeHtml(option.getText());
                String selected = "";
                if(!StringUtil.isEmpty(selectValue)){
                    String[] values = selectValue.split("#");
                    for(String value : values){
                        if(option.getValue().equals(value)){
                            selected = "checked";
                            break;
                        }
                    }
                }
                html.append("<label class=\"").append(cssClass).append("\" style=\"").append(style).append("\">\n").append("                                <input value=\"").append(val).append("\" id=\"").append(name).append(index).append("\" name=\"").append(name).append("\" type=\"checkbox\"").append(selected).append(">\n").append("                                <label for=\"").append(name).append(index).append("\">\n").append("                                    <span>").append(txt).append("</span>\n").append("                                </label>\n").append("                            </label><br/>");
                index++;
            }
        }
        html.append("</div>");
        if (needErrorSpan) {
            html.append("<span id=\"error_").append(name).append('\"');
            html.append(" name=\"iaisErrorMsg\" class=\"error-msg\"></span>");
        }
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setName(String name) throws JspException {
        this.name = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("name",
                name, Object.class, this, pageContext));
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }
    public void setOptions(String options) {
        this.options = options;
    }
    @Override
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    @Override
    public void setStyle(String style) {
		this.style = style;
	}
	public void setNeedMask(boolean needMask) {
        this.needMask = needMask;
    }
    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }
}
