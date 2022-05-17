package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Section Tag
 *
 *
 * @date        8/9/2019
 * @author      suocheng
 */
public final class SectionTag extends DivTagSupport {
    private static final long serialVersionUID = 1L;

    private String title;
    private boolean collapse;
    private boolean reload;
    private boolean remove;
    private boolean removeToHide;
    private boolean hide;
    private boolean enable;

    public SectionTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        setTitle(null);
        setCollapse(false);
        setReload(false);
        setRemove(false);
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
            html.append("<div class=\"dash-box\"");
        } else {
            html.append("<div");
        }
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append('\"');
        }
        if (!StringUtil.isEmpty(style)) {
            html.append(" style=\"").append(style).append('\"');
        }
        html.append('>');
        // header
        if (isBE) {
            html.append("<div class=\"dash-header\">").append(title);
        } else if (!StringUtil.isEmpty(title)) {
            html.append("<h5 class=\"formtitle\"><font14>").append(title).append("</font14></h5>");
        }
        if (collapse) {
            html.append("<div class=\"panel-control\">");
            if (hide) {
                html.append("<a data-toggle=\"tooltip\" class=\"panel-collapse\" title=\"Expand/Collapse\" href=\"javascript:void(0);\"><i class=\"icon-arrow-down\"></i></a>");
            } else {
                html.append("<a data-toggle=\"tooltip\" class=\"panel-collapse\" title=\"Expand/Collapse\" href=\"javascript:void(0);\"><i class=\"icon-arrow-up\"></i></a>");
            }
            html.append("</div>");
        }
        html.append("</div>");
        // body
        if (isBE) {
            html.append("<div class=\"dash-body\"");
        } else {
            html.append("<div class=\"ctnpanel ctnpanelbottom1\"");
        }
        if (hide) {
            html.append(" style=\"display:none;\"");
        }
        html.append('>');
        if (!isBE) {
            html.append("<div class=\"formpanel\">");
            html.append("<div class=\"expspace\">");
        }
        html.append("<div class=\"form-horizontal\">");
        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("RowTag: " + ex.getMessage(),ex);
        }
        return EVAL_BODY_INCLUDE;
    }
    @Override
    public int doEndTag() throws JspException {
        boolean isBE = AccessUtil.isBackend();
        try {
            pageContext.getOut().print(isBE ? "</div></div></div>" : "</div></div></div></div>");
        } catch (Exception ex) {
            throw new JspTagException("RowTag: " + ex.getMessage(),ex);
        }
        return EVAL_PAGE;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean isCollapse() {
        return collapse;
    }
    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }
    public boolean isReload() {
        return reload;
    }
    public void setReload(boolean reload) {
        this.reload = reload;
    }
    public boolean isRemove() {
        return remove;
    }
    public void setRemove(boolean remove) {
        this.remove = remove;
    }
    public void setRemoveToHide(boolean removeToHide) {
        this.removeToHide = removeToHide;
    }
    public void setHide(boolean hide) {
        this.hide = hide;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isRemoveToHide() {
        return removeToHide;
    }

    public boolean isHide() {
        return hide;
    }

    public boolean isEnable() {
        return enable;
    }
}
