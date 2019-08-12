package com.ecquaria.cloud.moh.iais.tags;

import sg.gov.moh.iais.common.utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
/**
 * Action Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
public final class ActionTag extends DivTagSupport {
    private static final long serialVersionUID = 2507648507715155034L;
    
    private String validator;

    public ActionTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        this.validator = null;
    }
    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public int doStartTag() throws JspException {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"alignctr\"");
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append("\"");
        }
        if (!StringUtil.isEmpty(style)) {
            html.append(" style=\"").append(style).append("\"");
        }
        html.append(">");
        try {
            if (!StringUtil.isEmpty(validator)) {
                html.append("<input type=\"hidden\" class=\"not-clear\" id=\"form_validator\" value=\"").append(StringUtil.obscured(validator)).append("\"/>");
            }
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("ActionTag: " + ex.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }
    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print("</div>");
        } catch (Exception ex) {
            throw new JspTagException("ActionTag: " + ex.getMessage());
        }
        return EVAL_PAGE;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

}
