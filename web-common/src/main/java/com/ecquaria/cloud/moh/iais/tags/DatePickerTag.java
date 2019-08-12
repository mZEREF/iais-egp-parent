package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import sg.gov.moh.iais.common.utils.Formatter;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.Date;

/**
 * Date Picker Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
public final class DatePickerTag extends DivTagSupport {
    private static final long serialVersionUID = 1824510660131941025L;

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
    
    public DatePickerTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        name = null;
        value = null;
        dateVal = null;
        onchange = null;
        onblur = null;
        onclick = null;
        fromNow = false;
        toNow = false;
        workingDay = false;
        title = null;
        startDate = null;
        endDate = null;
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
            throw new JspTagException("DatePickerTag: " + ex.getMessage());
        }
        return SKIP_BODY;
    }
    
    public String generateHtml() {
        boolean isBE = AccessUtil.isBackend();
        if (StringUtil.isEmpty(this.id)) {
            this.id = String.valueOf(MiscUtil.getDummyId());
        }
        StringBuilder html = new StringBuilder();
        generateStartHtml(html,isBE);
        generateFromToHtml(html);
        generateEndHtml(html,isBE);
        return html.toString();
    }
    private void generateStartHtml(StringBuilder html,boolean isBE) {
        if (isBE) {
            html.append("<div class=\"input-group date form_date\"");
            html.append(" id=\"").append(id).append("Gp\"");
            if (!StringUtil.isEmpty(style)) {
                html.append(" style=\"").append(style).append("\"");
            }
            html.append(">");
        }
        if (isBE) {
            html.append("<input type=\"text\" class=\"form-control wxpicker").append(workingDay ? "-working" : "");
        } else {
            html.append("<input type=\"text\" class=\"date_picker form-control").append(workingDay ? "-working" : "");
        }
        if (!StringUtil.isEmpty(cssClass)) {
            html.append(" ").append(cssClass);
        }
        html.append("\"");
        if (!StringUtil.isEmpty(name)) {
            html.append(" name=\"").append(name).append("\"");
        }
        html.append(" id=\"").append(id).append("\"");
    }
    private void generateFromToHtml(StringBuilder html) {
        if (fromNow) {
            html.append(" data-date-start-date=\"0d\"");
        } else if (startDate != null) {
            html.append(" data-date-start-date=\"").append(startDate).append("\"");
        } else {
            html.append(" data-date-start-date=\"01/01/1900\"");
        }
        if (toNow) {
            html.append(" data-date-end-date=\"0d\"");
        } else if (endDate != null) {
            html.append(" data-date-end-date=\"").append(endDate).append("\"");
        }
    }
    private void generateEndHtml(StringBuilder html,boolean isBE) {
        if (!StringUtil.isEmpty(disableWeekDay)) {
            html.append(" data-date-days-of-week-disabled=\"").append(disableWeekDay).append("\"");
        }
        if (value != null) {
            String val = StringUtil.escapeHtml(value);
            html.append(" value=\"").append(val).append("\"");
        } else if (dateVal != null) {
            html.append(" value=\"").append(Formatter.formatDate(dateVal)).append("\"");
        }
        if (!StringUtil.isEmpty(onclick)) {
            html.append(" onclick=\"").append(onclick).append("\"");
        }
        if (!StringUtil.isEmpty(onchange)) {
            html.append(" onchange=\"").append(onchange).append("\"");
        }
        if (!StringUtil.isEmpty(onblur)) {
            html.append(" onblur=\"").append(onblur).append("\"");
        }
        if (!StringUtil.isEmpty(title)) {
            html.append(" title=\"").append(title).append("\"");
        }
        html.append(" placeholder=\"dd/mm/yyyy\" maxlength=\"10\"/>");
        if (isBE) {
            html.append("<span class=\"input-group-addon\">");
            html.append("<button type=\"button\" style=\"margin-right:0px\" class=\"btn btn-primary date-set wxdate-set\"><i class=\"fa fa-calendar\"></i></button></span></div>");
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
