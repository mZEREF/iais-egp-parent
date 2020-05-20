package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Success Tag
 *
 *
 * @date        7/24/2019
 * @author      suocheng
 */
public final class SuccessTag extends DivTagSupport {
    private static final long serialVersionUID = 2507648507715155034L;

    private boolean canClose;

    public SuccessTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        canClose = false;
        super.init();
    }
    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public int doStartTag() throws JspException {
        String id = this.id;
        if (StringUtil.isEmpty(id)) {
            id = "alert" + MiscUtil.getDummyId();
        }

        StringBuilder html = new StringBuilder();
        html.append("<div class=\"alert alert-success\" role=\"alert\"");
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append('\"');
        }
        if (!StringUtil.isEmpty(style)) {
            html.append(" style=\"").append(style).append('\"');
        }
        html.append('>');
        if (canClose) {
            html.append("<button type=\"button\" class=\"close\" onclick=\"javascript:$('#").append(id).append("').slideToggle('fast');\" aria-label=\"Close\">");
            html.append("<span>&times;</span></button>");
        }

        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("RowTag: " + ex.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }
    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print("</div>");
        } catch (Exception ex) {
            throw new JspTagException("RowTag: " + ex.getMessage());
        }
        return EVAL_PAGE;
    }

    public void setCanClose(boolean canClose) {
        this.canClose = canClose;
    }
}
