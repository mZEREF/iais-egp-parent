package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.Collection;
import java.util.List;

@Slf4j
public class SelectTag extends DivTagSupport {
    private static final long serialVersionUID = 4775176554493836788L;
    private static final String ENDOPTION  = "</option>";
    private String name;
    private String options;
    private String firstOption;
    private String codeCategory;
    private String filterCode;
    private String filterValue;
    private String onchange;
    private String value;
    private Collection<String> multiValues;
    private String otherOption;
    private String otherOptionValue = "00";
    private String hidden;
    private boolean needErrorSpan;
    private boolean disabled;
    private boolean needMask;
    private boolean needSort;
    private boolean multiSelect;
    private List<SelectOption> optionsSelections;


    public SelectTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected final void init() {
        setId(null);
        setName("");
        setCodeCategory("");
        setFilterCode("");
        setFilterValue("");
        setFirstOption(null);
        setOptions(null);
        setValue(null);
        setCssClass("");
        setStyle("");
        setOtherOption(null);
        setHidden("");
        setNeedErrorSpan(true);
        setNeedMask(false);
        setNeedSort(false);
        setMultiSelect(false);
        setMultiValues(null);
        setOptionsSelections(null);
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public int doStartTag(){
        try {
            if (!StringUtil.isEmpty(codeCategory)) {
                setCodeCategory( MasterCodeUtil.getCategoryId(codeCategory));
            }
            if (StringUtil.isEmpty(cssClass) && StringUtil.isEmpty(style)) {
               setCssClass("input-large");
            }
            StringBuilder html = new StringBuilder();



            html.append("<select name=\"").append(name).append('\"');
            if (!StringUtil.isEmpty(id)) {
                 setId( StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("id",
                         id, Object.class, this, pageContext)));
                html.append(" id=\"").append(id).append('\"');
            } else {
                html.append(" id=\"").append(name).append('\"');
            }
            if (!StringUtil.isEmpty(cssClass)) {
                html.append(" class=\"").append(cssClass).append('\"');
            }
            if (!StringUtil.isEmpty(style)) {
                html.append(" style=\"").append(style).append('\"');
            }
            if (multiSelect) {
                html.append(" multiple=\"multiple\"");
            }
            if (!StringUtil.isEmpty(onchange)) {
                html.append(" onchange=\"").append(onchange).append('\"');
            }

            if (disabled){
                html.append(" disabled=\"").append(disabled).append('\"');
            }

            if (!StringUtil.isEmpty(hidden)){
                html.append(" hidden=\"").append(onchange).append('\"');
            }

            html.append('>');

            generateOptionHtml(html);
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            log.error("", ex);
            throw new IaisRuntimeException("SelectTag: " + ex.getMessage(),ex);
        }
        release();

        return SKIP_BODY;
    }

    private void generateOptionHtml(StringBuilder html){
        List<SelectOption> sos = null;
        if (!StringUtil.isEmpty(options)) {
            sos = (List<SelectOption>) ParamUtil.getScopeAttr((HttpServletRequest) pageContext.getRequest(), options);
        } else if (!StringUtil.isEmpty(codeCategory)) {
            sos = MasterCodeUtil.retrieveOptionsByCate(codeCategory);
        } else if (!StringUtil.isEmpty(filterCode)) {
            sos = MasterCodeUtil.retrieveOptionsByFilter(filterCode);
        }else if(IaisCommonUtils.isNotEmpty(optionsSelections)){
            sos = optionsSelections;
        }
        if (needMask) {
            MaskUtil.maskSelectOptions(name, sos);
        }

        if (!StringUtil.isEmpty(firstOption)) {
            html.append("<option value=\"\">").append(StringUtil.escapeHtml(firstOption)).append(ENDOPTION);
        }

        //Use ',' to separate your master code keys
        if (!StringUtils.isEmpty(filterValue)){
            String[] filter = filterValue.split(",");
            for (int i = 0; i < filter.length; i++){
                int indx = i;
                sos.removeIf(selectOption -> (filter[indx].trim()).equals(selectOption.getValue()));
            }
        }

        if (sos != null) {
            if (needSort) {
                sos.sort(SelectOption::compareTo);
            }

            for (SelectOption option : sos) {
                String val = StringUtil.viewNonNullHtml(option.getValue());
                String txt = StringUtil.escapeHtml(option.getText());
                boolean multiChoose = false;
                if (!IaisCommonUtils.isEmpty(multiValues)) {
                    multiChoose = multiValues.contains(option.getValue());
                }
                String selected = option.getValue().equals(value) || multiChoose ? " selected" : "";
                html.append("<option value=\"").append(val).append('\"').append(selected).append('>').append(txt).append(ENDOPTION);
            }
        }

        if (! StringUtil.isEmpty(otherOption)) {
            String selected = otherOptionValue.equals(value) ? " selected" : "";
            html.append("<option value=\"").append(StringUtil.viewNonNullHtml(otherOptionValue))
                    .append('\"').append(selected).append('>').append(StringUtil.escapeHtml(otherOption)).append(ENDOPTION);
        }

        html.append("</select>");
        if (needErrorSpan) {
            html.append("<span id=\"error_").append(name).append('\"');
            html.append(" name=\"iaisErrorMsg\" class=\"error-msg\"></span>");
        }
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setName(String name)  {
        try {
            this.name = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("name",
                    name, Object.class, this, pageContext));
        } catch (JspException e) {
            log.error(e.getMessage());
        }
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }
    public void setOptions(String options) {
        this.options = options;
    }
    public void setFirstOption(String firstOption) {
        this.firstOption = firstOption;
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setCodeCategory(String codeCategory) {
        this.codeCategory = codeCategory;
    }
    public void setOtherOption(String otherOption) {
        this.otherOption = otherOption;
    }
    @Override
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    @Override
    public void setStyle(String style) {
		this.style = style;
	}
	public void setNeedErrorSpan(boolean needErrorSpan) {
        this.needErrorSpan = needErrorSpan;
    }
    public void setFilterCode(String filterCode) {
        this.filterCode = filterCode;
    }
	public void setNeedMask(boolean needMask) {
        this.needMask = needMask;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setNeedSort(boolean needSort) {
        this.needSort = needSort;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public void setMultiValues(Collection<String> multiValues) {
        this.multiValues = multiValues;
    }

    public void setOptionsSelections(List<SelectOption> optionsSelections) {
        this.optionsSelections = optionsSelections;
    }
}
