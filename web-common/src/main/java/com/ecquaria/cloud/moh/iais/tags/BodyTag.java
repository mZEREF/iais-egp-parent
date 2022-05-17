package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;

import javax.servlet.jsp.JspException;

/**
 * Body Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
public final class BodyTag extends DivTagSupport {
    private static final long serialVersionUID = 1L;

    private boolean disabled;

    public BodyTag() {
        super();
        init();
    }

    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public int doStartTag() throws JspException {
        StringBuilder html = new StringBuilder();
        boolean isBE = AccessUtil.isBackend();
        if (isBE) {
            html.append("<iframe id=\"txtArea1\" style=\"display:none\"></iframe>");
            html.append("<div class=\"dash-content dash-r-bg");
        } else {
            html.append("<div class=\"subcontent col-lg-12 col-sm-12");
        }
        html.append('\"');
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append('\"');
        }
        if (!StringUtil.isEmpty(style)) {
            html.append(" style=\"").append(style).append('\"');
        }
        html.append('>');
        // body
        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new IaisRuntimeException(ex);
        }
        return EVAL_BODY_INCLUDE;
    }
    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print("</div>");
        } catch (Exception ex) {
            throw new IaisRuntimeException(ex);
        }
        return EVAL_PAGE;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
