package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Link Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
public class LinkTag extends DivTagSupport {
    private static final long serialVersionUID = 1L;
    private static final String  ENDDIV = "</div>";


    private String icon;
    private String onclick;
    private String title;

    public LinkTag() {
        super();
        cleanFields();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        cleanFields();
    }

    private void cleanFields() {
        setIcon(null);
        setOnclick(null);
        setTitle(null);
    }

    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public int doStartTag() throws JspException {
        boolean isBackend = AccessUtil.isBackend();
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"col-xs-7 col-sm-6 col-md-4\">");
        html.append("<a href=\"javascript:void(0);\"");
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append('\"');
        }
        if (!StringUtil.isEmpty(onclick)) {
            html.append(" onclick=\"").append(onclick).append('\"');
        }
        html.append("><div class=\"").append(icon).append("\"></div>");
        if (!StringUtil.isEmpty(title)) {
            if (isBackend) {
                if (title.length() > 4) {
                    html.append("<div class=\"hovertext hoverposition1\">").append(title).append(ENDDIV);
                } else {
                    html.append("<div class=\"hovertext\">").append(title).append(ENDDIV);
                }
            } else {
                html.append("<div class=\"hovertext hoverposition\">").append(title).append(ENDDIV);
            }
        }
        html.append("</a>");
        html.append(ENDDIV);
        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("LinkTag: " + ex.getMessage(),ex);
        }
        return SKIP_BODY;
    }
    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
