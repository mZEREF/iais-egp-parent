
package com.ecquaria.cloud.moh.iais.tags;

import sg.gov.moh.iais.common.utils.Formatter;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Dates Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
public final class DatesTag extends DivTagSupport {
    private static final long serialVersionUID = 1824510660131941025L;

    private String name;
    private Serializable value;
    private String onchange;
    private int width;
    private boolean last2Years;
    private int fromYear;
    private boolean last1Years;

    public DatesTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        name = null;
        value = null;
        onchange = null;
        width = 0;
        last2Years = false;
        fromYear = 0;
        last1Years = false;
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
            throw new JspTagException("DatesTag: " + ex.getMessage());
        }
        return SKIP_BODY;
    }

    public String generateHtml() throws ParseException {
        String id = this.id;
        if (StringUtil.isEmpty(id)) {
            id = String.valueOf(MiscUtil.getDummyId());
        }

        String val = null;
        int valDay = 0;
        int valMonth = 0;
        int valYear = 0;
        if (value != null) {
            if (value instanceof Date) {
                val = Formatter.formatDate((Date) value);
                valDay = Integer.parseInt(Formatter.formatDateTime((Date) value, "dd"));
                valMonth = Integer.parseInt(Formatter.formatDateTime((Date) value, "MM"));
                valYear = Integer.parseInt(Formatter.formatDateTime((Date) value, "yyyy"));
            } else if (!StringUtil.isEmpty(value.toString())) {
                val = value.toString();
                Date date = Formatter.parseDate(val);
                valDay = Integer.parseInt(Formatter.formatDateTime((Date) date, "dd"));
                valMonth = Integer.parseInt(Formatter.formatDateTime((Date) date, "MM"));
                valYear = Integer.parseInt(Formatter.formatDateTime((Date) date, "yyyy"));
            }
        }

        StringBuffer change = new StringBuffer();
        change.append("javascript:prefillDatsTag('").append(id).append("');");
        if (!StringUtil.isEmpty(onchange)) {
            change.append(onchange.replaceAll("javascript:", ""));
        }

        int width = this.width;
        if (width <= 0) {
            width = 4;
        }

        StringBuffer html = new StringBuffer();
        html.append("<input type=\"hidden\" name=\"").append(name).append("\" id=\"").append(id).append("\" value=\"").append(StringUtil.getNonNull(val)).append("\"/>");
        // day
        html.append("<div class=\"col-md-").append(width).append("\" id=\"").append(id).append("DayDv\"").append(" style=\"z-index:10;\">");
        html.append("<select id=\"").append(id).append("Day\" name=\"").append(name).append("Day\" onchange=\"").append(change).append("\">");
        html.append("<option value=\"\">DD</option>");
        for (int i = 1; i <= 31; i++) {
            String text = Formatter.formatNumber(i, "00");
            html.append("<option value=\"").append(text).append("\"").append(i == valDay ? " selected" : "").append(">").append(text).append("</option>");
        }
        html.append("</select>");
        html.append("</div>");
        // month
        html.append("<div class=\"col-md-").append(width).append("\" id=\"").append(id).append("MonthDv\"").append("\" style=\"z-index:10;\">");;
        html.append("<select id=\"").append(id).append("Month\" name=\"").append(name).append("Month\" onchange=\"").append(change).append("\">");
        html.append("<option value=\"\">MM</option>");
        for (int i = 1; i <= 12; i++) {
            String text = Formatter.formatNumber(i, "00");
            html.append("<option value=\"").append(text).append("\"").append(i == valMonth ? " selected" : "").append(">").append(text).append("</option>");
        }
        html.append("</select>");
        html.append("</div>");
        // year
        Calendar calendar = Calendar.getInstance();
        html.append("<div class=\"col-md-").append(width).append("\" id=\"").append(id).append("YearDv\"").append("\" style=\"z-index:10;\">");;
        html.append("<select id=\"").append(id).append("Year\" name=\"").append(name).append("Year\" onchange=\"").append(change).append("\">");
        html.append("<option value=\"\">YYYY</option>");
        int startYear = calendar.get(Calendar.YEAR);
        if(fromYear >= 0) {
            startYear = startYear - fromYear;
        }
        if(last2Years) {
        	for (int i = startYear; i >= (last2Years ? (calendar.get(Calendar.YEAR) - 1) : (calendar.get(Calendar.YEAR) - 100)); i--) {
        		html.append("<option value=\"").append(i).append("\"").append(i == valYear ? " selected" : "").append(">").append(i).append("</option>");
        	}
        } else {
        	for (int i = startYear; i > (last1Years ? (calendar.get(Calendar.YEAR) - 1) : (calendar.get(Calendar.YEAR) - 100)); i--) {
        		html.append("<option value=\"").append(i).append("\"").append(i == valYear ? " selected" : "").append(">").append(i).append("</option>");
        	}
        }
        html.append("</select>");
        html.append("</div>");
        return html.toString();
    }
    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(Serializable value) {
        this.value = value;
    }
    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    
    public void setLast2Years(boolean last2Years) {
        this.last2Years = last2Years;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

	public boolean isLast1Years() {
		return last1Years;
	}

	public void setLast1Years(boolean last1Years) {
		this.last1Years = last1Years;
	}
    
}
