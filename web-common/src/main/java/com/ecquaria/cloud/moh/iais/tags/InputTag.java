package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * InputTag
 *
 * @author suocheng
 * @date 9/27/2019
 */
public class InputTag extends DivTagSupport{
    private static final long serialVersionUID = -5949499873439472499L;

    private String type;
    private String name;
    private String value;


    public InputTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        type = null;
        name = null;
        value = null;
    }

    @Override
    public void release() {
        super.release();
        init();
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder html = new StringBuilder();
        html.append("<input type=\"").append(type);
        html.append("\"");
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append("\"");
        }
        if (!StringUtil.isEmpty(name)) {
            html.append(" name=\"").append(name).append("\"");
        }
        if (!StringUtil.isEmpty(style)) {
            html.append(" style=\"").append(style).append("\"");
        }

        if (!StringUtil.isEmpty(cssClass)) {
            html.append(" class=\"").append(cssClass).append("\"");
        }

        if (!StringUtil.isEmpty(value)) {
            html.append(" value=\"").append(value).append("\"");
        }
        html.append(">");

        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("ValueTag: " + ex.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print("</input>");
        } catch (Exception ex) {
            throw new JspTagException("ValueTag: " + ex.getMessage());
        }
        return EVAL_PAGE;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
