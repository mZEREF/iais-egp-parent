package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;

import javax.servlet.jsp.JspException;
import java.util.Date;

/**
 * Date Picker Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
public final class DatePickerTag extends DivTagSupport {
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;
    private Date dateVal;
    private String onchange;
    private String onclick;
    private String onblur;
    private boolean fromNow;
    private boolean toNow;
    private boolean workingDay;
    private String title;
    private String disableWeekDay; // 0 - 6
    private String startDate;
    private String endDate;
    private boolean needErrorSpan;
    private boolean disabled;
    
    public DatePickerTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        setName(null);
        setValue(null);
        setDateVal(null);
        setOnchange(null);
        setOnblur(null);
        setOnclick(null);
        setFromNow(false);
        setToNow(false);
        setWorkingDay(false);
        setTitle(null);
        setStartDate(null);
        setEndDate(null);
        setNeedErrorSpan(true);
        setDisabled(false);
    }

    public void setNeedErrorSpan(boolean needErrorSpan) {
        this.needErrorSpan = needErrorSpan;
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().print(generateHtml());
        } catch (Exception ex) {
            throw new IaisRuntimeException(ex);
        }
        return SKIP_BODY;
    }
    
    public String generateHtml() {
        if (StringUtil.isEmpty(this.id)) {
            this.id = String.valueOf(MiscUtil.getDummyId());
        }
        StringBuilder html = new StringBuilder();
        generateStartHtml(html);
        generateFromToHtml(html);
        generateEndHtml(html);
        return html.toString();
    }
    private void generateStartHtml(StringBuilder html) {
        html.append("<input type=\"text\" autocomplete=\"off\" class=\"date_picker form-control").append(workingDay ? "-working" : "");
        if (!StringUtil.isEmpty(cssClass)) {
            html.append(' ').append(cssClass);
        }
        html.append('\"');
        if (!StringUtil.isEmpty(name)) {
            html.append(" name=\"").append(name).append('\"');
        }
        html.append(" id=\"").append(id).append('\"');
    }
    private void generateFromToHtml(StringBuilder html) {
        if (fromNow) {
            html.append(" data-date-start-date=\"0d\"");
        } else if (startDate != null) {
            html.append(" data-date-start-date=\"").append(startDate).append('\"');
        } else {
            html.append(" data-date-start-date=\"01/01/1900\"");
        }
        if (toNow) {
            html.append(" data-date-end-date=\"0d\"");
        } else if (endDate != null) {
            html.append(" data-date-end-date=\"").append(endDate).append('\"');
        }
    }
    private void generateEndHtml(StringBuilder html) {
        if (!StringUtil.isEmpty(disableWeekDay)) {
            html.append(" data-date-days-of-week-disabled=\"").append(disableWeekDay).append('\"');
        }
        if (value != null) {
            String val = StringUtil.escapeHtml(value);
            html.append(" value=\"").append(val).append('\"');
        } else if (dateVal != null) {
            html.append(" value=\"").append(Formatter.formatDate(dateVal)).append('\"');
        }
        if (!StringUtil.isEmpty(onclick)) {
            html.append(" onclick=\"").append(onclick).append('\"');
        }
        if (!StringUtil.isEmpty(onchange)) {
            html.append(" onchange=\"").append(onchange).append('\"');
        }
        if (!StringUtil.isEmpty(onblur)) {
            html.append(" onblur=\"").append(onblur).append('\"');
        }
        if (!StringUtil.isEmpty(title)) {
            html.append(" title=\"").append(title).append('\"');
        }
        if(disabled){
            html.append(" disabled=\"").append(disabled).append('\"');
        }
        html.append(" placeholder=\"dd/mm/yyyy\" maxlength=\"10\"/>");
        if (needErrorSpan) {
            html.append("<span id=\"error_").append(name).append('\"');
            html.append(" name=\"iaisErrorMsg\" class=\"error-msg\"></span>");
        }
    }
    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setDateVal(Date dateVal) {
        this.dateVal = dateVal;
    }
    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }
    public void setFromNow(boolean fromNow) {
        this.fromNow = fromNow;
    }
    public void setToNow(boolean toNow) {
        this.toNow = toNow;
    }
    public void setWorkingDay(boolean workingDay) {
        this.workingDay = workingDay;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDisableWeekDay(String disableWeekDay) {
        this.disableWeekDay = disableWeekDay;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setOnblur(String onblur) {
        this.onblur = onblur;
    }
}
