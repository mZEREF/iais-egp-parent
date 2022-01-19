package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Field Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
@Slf4j
public final class FieldTag extends DivTagSupport {
    private static final long serialVersionUID = 3730754443745743902L;
    private static final String ENDLABLE = "</label>";
    private String value;
    private boolean required;
    private String info;
    private String index;
    private boolean withCheckbox;
    private String checkboxId;
    private String checkboxName;
    private String checkboxVal;
    private boolean checked;
    private String onclick;
    private int width;
    private boolean mandatory;

    public FieldTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        setValue(null);
        setRequired(false);
        setInfo(null);
        setWithCheckbox(false);
        setCheckboxId(null);
        setCheckboxName(null);
        setCheckboxVal(null);
        setChecked(false);
        setOnclick(null);
        setWidth(0);
        setMandatory(Boolean.FALSE);
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public int doStartTag() throws JspException {
        boolean isBackend = AccessUtil.isBackend();
        StringBuilder html = new StringBuilder();
        StringBuilder script = new StringBuilder();

        if (withCheckbox && !isBackend) {
           setWidth( width - 1);
            generateNotBEHtml(html);
        }
        if (index != null) {
            setWidth( width - 1);
            if (isBackend) {
                html.append("<label class=\"col-lg-1 col-sm-12 control-label float-left\">").append(index).append(ENDLABLE);
            } else {
                html.append("<label class=\"col-lg-1 col-md-12 col-sm-12 control-label float-left\">").append(index).append(ENDLABLE);
            }
        }
        if (isBackend) {
            html.append("<label class=\"col-lg-").append(width).append(" col-sm-12 control-label float-left");
        } else {
            html.append("<label class=\"col-xs-").append(width).append(" col-md-4 control-label");
        }

        generateHtml(html);
        if (isBackend && required) {
            html.append("<span style=\"color: red\"> *</span>");
        } else if (required) {
            html.append("<span style=\"color: red\"> *</span>");
        }

        if(mandatory){
            html.append("<span class=\"mandatory\">*</span>");
        }

        html.append(ENDLABLE);
        if (script.length() > 0) {
            html.append(script);
        }
        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("FieldTag: " + ex.getMessage(),ex);
        }
        return SKIP_BODY;
    }
    private void generateHtml(StringBuilder html){
        if (!StringUtil.isEmpty(cssClass)) {
            html.append(' ').append(cssClass);
        }
        if (!StringUtil.isEmpty(id)) {
            html.append("\" id=\"").append(id);
        }
        if (!StringUtil.isEmpty(style)) {
            html.append("\" style=\"").append(style);
        }
        html.append("\">");

        html.append(value);
        if (!StringUtil.isEmpty(info)) {
            html.append("&nbsp;<a class=\"btn-tooltip styleguide-tooltip\" ")
                    .append("style=\"z-index: 10\" ")
                    .append("href=\"javascript:void(0);\" ")
                    .append("data-placement=\"top\" ")
                    .append("data-toggle=\"tooltip\" ")
                    .append("data-html=\"true\" ")
                    .append("title=\"&lt;p&gt;")
                    .append(info)
                    .append("&lt;/p&gt;\">")
                    .append("i</a>&nbsp;");
        } else {
            html.append("&nbsp;");
        }


    }
    private void generateNotBEHtml(StringBuilder html){
        html.append("<div class=\"col-lg-1 col-md-12 col-sm-12 float-left\">");
        html.append("<label class=\"checkbox-custom check-primary\">");
        html.append("<input type=\"checkbox\"");
        if (StringUtil.isEmpty(checkboxId)) {
            setCheckboxId(MiscUtil.formatDummyId());
        }
        html.append(" id=\"").append(checkboxId).append('\"');
        if (!StringUtil.isEmpty(checkboxName)) {
            html.append(" name=\"").append(checkboxName).append('\"');
        }
        if (!StringUtil.isEmpty(checkboxVal)) {
            html.append(" value=\"").append(checkboxVal).append('\"');
        }
        if (checked) {
            html.append(" checked");
        }
        if (!StringUtil.isEmpty(onclick)) {
            html.append(" onclick=\"").append(onclick).append('\"');
        }
        html.append('>');
        html.append("<label for=\"").append(checkboxId).append("\" style=\"display: inline;\"></label>");
        html.append(ENDLABLE);
        html.append("</div>");
    }
    @Override
    public int doEndTag() {
        init();
        return EVAL_PAGE;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
    public void setInfo(String info) {
        this.info = info;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setWithCheckbox(boolean withCheckbox) {
        this.withCheckbox = withCheckbox;
    }

    public void setCheckboxId(String checkboxId) {
        this.checkboxId = checkboxId;
    }

    public void setCheckboxName(String checkboxName) {
        this.checkboxName = checkboxName;
    }

    public void setCheckboxVal(String checkboxVal) {
        this.checkboxVal = checkboxVal;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setMandatory(Boolean mandatory){this.mandatory = mandatory;}

}
