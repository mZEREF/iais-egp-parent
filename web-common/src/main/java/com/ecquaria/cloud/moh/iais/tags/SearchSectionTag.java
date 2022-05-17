package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * SearchSection Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
public final class SearchSectionTag extends DivTagSupport {

    private static final long serialVersionUID = 1L;
    private static final String ENDDIV = "</div>";
    private String title;
    private boolean collapse;
    private boolean reload;
    private boolean remove;
    private boolean hide;

    private String onclick;
    private String placeholder;
    private String fieldName;
    private String value;

    public SearchSectionTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        setTitle(null);
        setFieldName("");
        setValue("");
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
        // header
        html.append("<div class=\"epd_tb collapsed\">");
        if (isBE) {
             generateBEHtml(html);
        } else {
            generateFEHtml(html);
        }
        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException(StringUtil.changeForLog("RowTag: " + ex.getMessage()),ex);
        }
        return EVAL_BODY_INCLUDE;
    }
    private void generateBEHtml(StringBuilder html){
        html.append("<div class=\"dash-header epdtitle\">");
        html.append("<div class=\"row\">");
        html.append("<div class=\"col-sm-12 col-md-12 col-lg-9 wsearch\">").append(title).append(ENDDIV);
        html.append("<div class=\"col-sm-12 col-md-12 col-lg-3 searchspacing\">");
        if (!StringUtil.isEmpty(onclick)) {
            html.append("<div class=\"search-iconic-input\">");
            html.append("<a href=\"javascript:void(0);\"");

            html.append(" onclick=\"").append(onclick).append('\"');

            html.append("><i class=\"fa fa-search\"></i></a>");
            html.append("<input type=\"text\" class=\"form-control search-box\" placeholder=\"").append(placeholder).append('\"');
            html.append(" name=\"").append(fieldName).append('\"');
            if (!StringUtil.isEmpty(value)) {
                html.append(" value=\"").append(StringUtil.escapeHtml(value)).append('\"');
            }
            html.append("></div>");
        }
        html.append(ENDDIV);
        html.append(ENDDIV);
        html.append(ENDDIV);
        // body
        html.append("<div class=\"dash-body no-dash-pads epdct_tb\"");
        if (hide) {
            html.append(" style=\"display:none;\"");
        }
        html.append('>');
    }
    private void generateFEHtml(StringBuilder html){
        html.append("<div class=\"epdtitle epdtitle_tb1\">");
        html.append("<div class=\"floatleft bold\"><span><font12>").append(title).append("</font12></span></div>");
        html.append("<div class=\"floatright\">");
        if (!StringUtil.isEmpty(onclick)) {
            html.append("<div class=\"search-iconic-input\">");
            html.append("<a href=\"javascript:void(0);\"");
            html.append(" onclick=\"").append(onclick).append('\"');
            html.append("><i class=\"fa fa-search\"></i></a>");
            html.append("<input type=\"text\" class=\"searchapp searchwidth no-form-control\" placeholder=\"").append(placeholder).append('\"');
            html.append(" alt=\"").append(placeholder).append('\"');
            html.append(" name=\"").append(fieldName).append('\"');
            if (!StringUtil.isEmpty(value)) {
                html.append(" value=\"").append(StringUtil.escapeHtml(value)).append('\"');
            }
            html.append("></div>");
        }

        html.append(ENDDIV);
        html.append("<div class=\"clear\"></div>");
        html.append(ENDDIV);
        // body
        html.append("<div class=\"epdct_tb\"");
        if (hide) {
            html.append(" style=\"display:none;\"");
        }
        html.append('>');
    }
    @Override
    public int doEndTag(){
        try {
            pageContext.getOut().print("</div></div>");
        } catch (Exception ex) {
            throw new IaisRuntimeException("RowTag: " + ex.getMessage(),ex);
        }
        return EVAL_PAGE;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    public boolean isCollapse() {
        return collapse;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
