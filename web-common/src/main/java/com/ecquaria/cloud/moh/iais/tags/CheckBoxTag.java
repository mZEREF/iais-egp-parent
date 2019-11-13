package com.ecquaria.cloud.moh.iais.tags;

/*
 *author: yichen
 *date time:11/9/2019 1:54 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.util.List;

@Slf4j
public final class CheckBoxTag extends DivTagSupport {
    private static final long serialVersionUID = -130423270882193278L;

    private String name;
    private String labelName;
    private String checkboxId;

    private String codeCategory;
    private String value;
    private String ariaInvalid;
    private String labelClass;
    private String forName;
    private String spanClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getCheckboxId() {
        return checkboxId;
    }

    public void setCheckboxId(String checkboxId) {
        this.checkboxId = checkboxId;
    }

    public String getCodeCategory() {
        return codeCategory;
    }

    public void setCodeCategory(String codeCategory) {
        this.codeCategory = codeCategory;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAriaInvalid() {
        return ariaInvalid;
    }

    public void setAriaInvalid(String ariaInvalid) {
        this.ariaInvalid = ariaInvalid;
    }

    public String getLabelClass() {
        return labelClass;
    }

    public void setLabelClass(String labelClass) {
        this.labelClass = labelClass;
    }

    public String getForName() {
        return forName;
    }

    public void setForName(String forName) {
        this.forName = forName;
    }

    public String getSpanClass() {
        return spanClass;
    }

    public void setSpanClass(String spanClass) {
        this.spanClass = spanClass;
    }

    // resets local state
    @Override
    protected void init() {
        name = null;
        labelName = "";
        checkboxId = "";
        codeCategory = null;
        value = null;
        ariaInvalid = "";
        labelClass = "";
        forName = "";
        spanClass = "";
    }


    @Override
    public int doStartTag() throws JspTagException {
        try {
            if (!StringUtil.isEmpty(codeCategory)) {
                codeCategory = MasterCodeUtil.getCategoryId(codeCategory);
            }
            StringBuilder html = new StringBuilder();
            boolean isBackend = AccessUtil.isBackend();
            if (!isBackend){
                html.append("<div class=\"form-check-gp\">");
                html.append("<label>");
                html.append(labelName);
                html.append("</label>");


                if (!StringUtil.isEmpty(codeCategory)) {
                    List<SelectOption> valueOptions = MasterCodeUtil.retrieveOptionsByCate(codeCategory);

                    for (SelectOption i : valueOptions){
                        String text = i.getText();
                        html.append("<div class=\"form-check\">");
                        html.append("<input class=\"form-check-input\" ");

                        html.append("name=\"").append(name).append("\"");

                        if (!StringUtils.isEmpty(checkboxId)){
                            html.append("id=\"").append(checkboxId).append("\" ");
                        }

                        html.append(" type=\"checkbox\" ");

                        if (!StringUtils.isEmpty(ariaInvalid)){
                            html.append("aria-invalid=\"").append(ariaInvalid).append("\">");
                        }

                        html.append(">");

                        if(!StringUtils.isEmpty(labelClass)){
                            html.append("<label class=\"").append(labelClass).append("\" ");
                        }else{
                            html.append("<label class=\"").append("form-check-label").append("\" ");
                        }

                        html.append("for = \"").append(forName).append("\">");

                        if (!StringUtils.isEmpty(spanClass)){
                            html.append("<span class=\"").append(spanClass).append("\">").append("</span>");
                        }else{
                            html.append("<span class=\"").append("check-square").append("\" >").append("</span>");
                        }

                        html.append(text);
                        html.append("</label>");
                        html.append("</div>");
                    }
                }

                html.append("</div>");

                log.info(html.toString());
            }

            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (IOException e) {
            log.error("", e);
            throw new JspTagException("CheckBoxTag: " + e.getMessage());
        }

        release();
        return EVAL_BODY_INCLUDE;
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }
}
