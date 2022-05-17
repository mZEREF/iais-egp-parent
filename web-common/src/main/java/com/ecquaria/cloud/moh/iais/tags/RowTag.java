package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Row Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
public final class RowTag extends DivTagSupport {
    private static final long serialVersionUID = 1L;

    private boolean inline;

    public RowTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        setInline(false);
    }
    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public int doStartTag() throws JspException {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"form-group");
        if (inline) {
            html.append(" form-inline");
        }
        if (!StringUtil.isEmpty(cssClass)) {
            html.append(' ').append(cssClass);
        }
        html.append('\"');
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append('\"');
        }
        if (!StringUtil.isEmpty(style)) {
            html.append(" style=\"").append(style).append('\"');
        }
        html.append('>');
        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("RowTag: " + ex.getMessage(),ex);
        }
        return EVAL_BODY_INCLUDE;
    }
    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print("<div class=\"clear\"></div></div>");
        } catch (Exception ex) {
            throw new JspTagException("RowTag: " + ex.getMessage(),ex);
        }
        return EVAL_PAGE;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }
}
