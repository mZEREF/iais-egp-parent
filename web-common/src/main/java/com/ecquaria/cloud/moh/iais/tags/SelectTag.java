/*
 *   This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.common.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;


public class SelectTag extends TagSupport {
    private static final long serialVersionUID = -4091387584439337660L;
    private static Logger log = Logger.getLogger(SelectTag.class.getName());

    private String name;
    private String options;
    private String firstOption;
    private String codeCategory;
    private String filterCode;
    private String onchange;
    private String value;
    private String cssClass;
    private String otherOption;
    private String otherOptionValue = "00";
    private String style;
    private boolean needErrorSpan;
    private boolean needMask;

    public SelectTag() {
        super();
        init();
    }

    // resets local state
    private void init() {
        id = null;
        name = null;
        codeCategory = "";
        filterCode = "";
        firstOption = null;
        options = null;
        value = null;
        cssClass = "";
        style = "";
        otherOption = null;
        needErrorSpan = true;
        needMask = false;
    }

    // Releases any resources we may have (or inherit)
    public void release() {
        super.release();
        init();
    }

    public int doStartTag() throws JspException {
        try {
            if (!StringUtil.isEmpty(codeCategory)) {
                codeCategory = MasterCodeUtil.getCategoryId(codeCategory);
            }
            if (StringUtil.isEmpty(cssClass) && StringUtil.isEmpty(style)) {
                cssClass = "input-large";
            }
            StringBuffer html = new StringBuffer();
            html.append("<select name=\"").append(name).append("\"");
            if (!StringUtil.isEmpty(id)) {
                id = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("id",
                        id.toString(), Object.class, this, pageContext));
                html.append(" id=\"").append(id).append("\"");
            } else {
                html.append(" id=\"").append(name).append("\"");
            }
            if (!StringUtil.isEmpty(cssClass)) {
                html.append(" class=\"").append(cssClass).append("\"");
            }
            if (!StringUtil.isEmpty(style)) {
                html.append(" style=\"").append(style).append("\"");
            }
            if (!StringUtil.isEmpty(onchange)) {
                html.append(" onchange=\"").append(onchange).append("\"");
            }
            html.append(">");
            List<SelectOption> sos = null;
            if (!StringUtil.isEmpty(options)) {
                sos = (List<SelectOption>) ParamUtil.getScopeAttr((HttpServletRequest) pageContext.getRequest(), options);
            } else if (!StringUtil.isEmpty(codeCategory)) {
                sos = MasterCodeUtil.retrieveOptionsByCate(codeCategory);
            } else if (!StringUtil.isEmpty(filterCode)) {
                sos = MasterCodeUtil.retrieveOptionsByFilter(filterCode);
            }
//            if (needMask) {
//                MaskUtil.maskSelectOptions(name, sos);
//            }
            if (!StringUtil.isEmpty(firstOption)) {
                html.append("<option value=\"\">").append(StringUtil.escapeHtml(firstOption)).append("</option>");
            }
            if (sos != null) {
                for (SelectOption option : sos) {
                    String val = StringUtil.viewNonNullHtml(option.getValue());
                    String txt = StringUtil.escapeHtml(option.getText());
                    String selected = option.getValue().equals(value) ? " selected" : "";
                    html.append("<option value=\"").append(val).append("\"").append(selected).append(">").append(txt).append("</option>");
                }
            }
            
            if (! StringUtil.isEmpty(otherOption)) {
            	String selected = otherOptionValue.equals(value) ? " selected" : "";
            	html.append("<option value=\"").append(StringUtil.viewNonNullHtml(otherOptionValue))
            	    .append("\"").append(selected).append(">").append(StringUtil.escapeHtml(otherOption)).append("</option>");
            }
            
            html.append("</select>");
            if (needErrorSpan) {
                html.append("<div style=\"color:#b94a48\" class=\"help-inline-block\"><span id=\"error_").append(name).append("\"");
                html.append(" name=\"emsErrorMsg\"></span></div>");
            }

            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        }catch (RuntimeException e){
        	throw e;
        } catch (Exception ex) {
            log.error("", ex);
            throw new JspTagException("SelectTag: " + ex.getMessage());
        }
        release();

        return SKIP_BODY;
    }

    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setName(String name) throws JspException {
        this.name = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("name",
                name.toString(), Object.class, this, pageContext));
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setOptions(String options) {
        this.options = options;
    }
    public void setFirstOption(String firstOption) {
        this.firstOption = firstOption;
    }
    /*
    public void setOptions(SelectOption[] options) {
        this.options = options;
    }
    */
    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }
    public void setValue(String value) throws JspException {
        this.value = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("value",
                value.toString(), Object.class, this, pageContext));
    }
    public void setCodeCategory(String codeCategory) throws JspException {
        this.codeCategory = codeCategory;
    }
    public void setOtherOption(String otherOption) {
        this.otherOption = otherOption;
    }
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    public void setStyle(String style) {
		this.style = style;
	}
	public void setNeedErrorSpan(boolean needErrorSpan) {
        this.needErrorSpan = needErrorSpan;
    }
    public void setNeedMask(boolean needMask) {
        this.needMask = needMask;
    }
    public void setFilterCode(String filterCode) {
        this.filterCode = filterCode;
    }
	
}
