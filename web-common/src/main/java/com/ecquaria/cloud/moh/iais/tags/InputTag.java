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
    private static final long serialVersionUID = -698233506237359748L;

    private String type;
    private String name;
    private String value;
    private String autocomplete;
    private String maxLength;
    private String onclick;
    private String onchange;
    private String placeholder;
    private boolean needErrorSpan;
    private boolean needDisabled;
    private String onInput;
    public InputTag() {
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
        setType(null);
        setName(null);
        setValue(null);
        setAutocomplete("off");
        setMaxLength(null);
        setOnclick(null);
        setOnchange(null);
        setNeedErrorSpan(true);
        setNeedDisabled(false);
        setPlaceholder(null);
        setOnInput(null);
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
        html.append('\"');
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append('\"');
        }
        if (!StringUtil.isEmpty(name)) {
            html.append(" name=\"").append(name).append('\"');
        }
        if (!StringUtil.isEmpty(style)) {
            html.append(" style=\"").append(style).append('\"');
        }

        if (!StringUtil.isEmpty(cssClass)) {
            html.append(" class=\"").append(cssClass).append('\"');
        }

        if (!StringUtil.isEmpty(value)) {
            html.append(" value=\"").append(value).append('\"');
        }
        if(!StringUtil.isEmpty(autocomplete)){
            html.append(" maxlength=\"").append(maxLength).append('\"');
        }
        if(!StringUtil.isEmpty(autocomplete)){
            html.append(" autocomplete=\"").append(autocomplete).append('\"');
        }
        if(needDisabled){
            html.append(" disabled=\"true\"");
        }
        if (!StringUtil.isEmpty(placeholder)) {
            html.append(" placeholder=\"").append(placeholder).append('\"');
        }
        if (!StringUtil.isEmpty(onclick)) {
            html.append(" onclick=\"").append(onclick).append('\"');
        }
        if (!StringUtil.isEmpty(onchange)) {
            html.append(" onchange=\"").append(onchange).append('\"');
        }

        if (!StringUtil.isEmpty(onInput)) {
            html.append(" onInput=\"").append(onInput).append('\"');
        }
        html.append('>');


        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("ValueTag: " + ex.getMessage(),ex);
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            StringBuilder html = new StringBuilder("</input>");
            if (needErrorSpan) {
                html.append("<span id=\"error_").append(name).append('\"');
                html.append(" name=\"iaisErrorMsg\" class=\"error-msg\"></span>");
            }
            pageContext.getOut().print(html.toString());

        } catch (Exception ex) {
            throw new JspTagException("ValueTag: " + ex.getMessage(),ex);
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
    public void setAutocomplete(String autocomplete) {
        if(!StringUtil.isEmpty(autocomplete)){
            this.autocomplete = autocomplete;
        }
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }
    public void setNeedDisabled(boolean needDisabled){ this.needDisabled = needDisabled;}
    public void setNeedErrorSpan(boolean needErrorSpan) {
        this.needErrorSpan = needErrorSpan;
    }
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }
    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
    public void setOnInput(String onInput) {
        this.onInput = onInput;
    }

}
