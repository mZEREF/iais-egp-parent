package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Value Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
public final class ValueTag extends DivTagSupport {
    private static final long serialVersionUID = 2507648507715155034L;

    private int width;
    private boolean label;
    private boolean offset;
    private boolean align;

    public ValueTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        width = 0;
        label = false;
        offset = false;
        align = false;
    }

    @Override
    public void release() {
        super.release();
        init();
    }

    @Override
    public int doStartTag() throws JspException {
        boolean isFrontend = !AccessUtil.isBackend();
        int width0 = this.width;
//        if (width0 <= 0) {
//            width0 = isFrontend ? 5 : 9;
//        }
        if (width0 > 10) {
            width0 = 10;
        }
        int offsetWidth = isFrontend ? 5 : 2;
        StringBuilder clazz = new StringBuilder();
        if (label) {
            clazz.append("float-left control-label");
        } else {
            if(isFrontend){
                if(width>10){
                    clazz.append("col-sm-7 col-md-6 col-xs-").append(width0);
                }else if(width>5 && width<=10){
                    clazz.append("col-sm-7 col-md-5 col-xs-").append(width0);
                }else if (width>0){
                    clazz.append("col-sm-4 col-md-2 col-xs-").append(width0);
                }

            }else{
                clazz.append("float-left col-sm-12 col-md-").append(width0);
            }

        }
        if (align) {
            clazz.append(" col-md-align");
        }
        if (!StringUtil.isEmpty(cssClass)) {
            clazz.append(" ").append(cssClass);
        }

        StringBuilder html = new StringBuilder();
        html.append("<div class=\"").append(clazz);
        if (offset) {
            html.append(" offset-top col-md-offset-").append(offsetWidth);
        }
        html.append("\"");
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append("\"");
        }
        if (!StringUtil.isEmpty(style)) {
            html.append(" style=\"").append(style).append("\"");
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
            pageContext.getOut().print("</div>");
        } catch (Exception ex) {
            throw new JspTagException("ValueTag: " + ex.getMessage());
        }
        return EVAL_PAGE;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public void setLabel(boolean label) {
        this.label = label;
    }
    public void setOffset(boolean offset) {
        this.offset = offset;
    }
    public void setAlign(boolean align) {
        this.align = align;
    }
}
