package com.ecquaria.cloud.moh.iais.tags;

/*
 *author: yichen
 *date time:11/9/2019 1:54 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public final class CheckBoxTag extends DivTagSupport {
    private static final long serialVersionUID = 1L;

    private String name;
    private String labelName;
    private String checkboxId;

    private String codeCategory;
    private String ariaInvalid;
    private String labelClass;
    private String forName;
    private String spanClass;
    private String checked;
    private String value;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    private HttpServletRequest request;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

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
        setName(null);
        setLabelName("");
        setCheckboxId("");
        setCodeCategory(null);
        setAriaInvalid("");
        setLabelClass("");
        setForName("");
        setSpanClass("");
        setValue("");
    }


    @Override
    public int doStartTag() {
        StringBuilder html = new StringBuilder();

        try {
            if (!StringUtil.isEmpty(codeCategory)) {
                html.append("<div class=\"form-check-gp\">");
                //not in <c:forEach> style
                setCodeCategory(MasterCodeUtil.getCategoryId(codeCategory));
                html.append("<label>");
                html.append(labelName);
                html.append("</label>");
                if (!StringUtil.isEmpty(codeCategory)) {
                    List<SelectOption> valueOptions = MasterCodeUtil.retrieveOptionsByCate(codeCategory);

                    for (SelectOption i : valueOptions){
                        String text = i.getText();
                        html.append("<div class=\"form-check\">");
                        html.append("<input class=\"form-check-input\" ");

                        html.append("name=\"").append(name).append('\"');

                        if (!StringUtils.isEmpty(checkboxId)){
                            html.append("id=\"").append(checkboxId).append("\" ");
                        }

                        html.append(" type=\"checkbox\" ");

                        html.append(" value=\"").append(text).append('\"');
                        if (!StringUtils.isEmpty(ariaInvalid)){
                            html.append("aria-invalid=\"").append(ariaInvalid).append("\">");
                        }

                        //re display
                        if (request != null){
                            String[] selectVal = (String[]) ParamUtil.getRequestAttr(request, name);
                            if (selectVal != null && selectVal.length > 0){
                                for (String s : selectVal){
                                    if (s.equals(text)){
                                        html.append(" checked=\"checked\" ");
                                    }
                                }
                            }
                        }

                        html.append('>');

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
            } else {
                if (request == null) {
                    request = (HttpServletRequest) pageContext.getRequest();
                }
                //in <c:forEach> style
                html.append("<input name =\"").append(name).append('\"').append("id = \"").append(checkboxId).append('\"')
                        .append("type=\"checkbox\"").append("value=").append('\"').append(MaskUtil.maskValue(name, value)).append('\"');
                LinkedHashSet<String> set = (LinkedHashSet<String>) ParamUtil.getSessionAttr(request, forName);
                if (set != null && set.contains(value)){
                    html.append(" checked = \"checked\"");
                }
                html.append("data-redisplay-name=").append(forName);
                html.append(" onchange = ajaxCallSelectCheckbox.apply(this) />");
            }

            log.info(html.toString());
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException("CheckBoxTag: " + e.getMessage(),e);
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
