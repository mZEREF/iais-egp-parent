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

import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.StringUtil;

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
    private static final long serialVersionUID = -8544224262301150921L;

    private String code;
    private String codeType;
    private String value;
    private String annoId;
    private boolean required;
    private String info;
    private String index;
    private boolean withCheckbox;
    private String checkboxId;
    private String checkboxName;
    private String checkboxVal;
    private boolean checked;
    private String onclick;

    public FieldTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        code = null;
        value = null;
        annoId = null;
        required = false;
        info = null;
        index = null;
        withCheckbox = false;
        checkboxId = null;
        checkboxName = null;
        checkboxVal = null;
        checked = false;
        onclick = null;
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

        int width = isBackend ? 3 : 4;
        if (withCheckbox) {
            if (!isBackend) {
                width = width - 1;
                html.append("<div class=\"col-lg-1 col-md-12 col-sm-12 float-left\">");
                html.append("<label class=\"checkbox-custom check-primary\">");
                html.append("<input type=\"checkbox\"");
                if (StringUtil.isEmpty(checkboxId)) {
                    checkboxId = MiscUtil.formatDummyId();
                }
                html.append(" id=\"").append(checkboxId).append("\"");
                if (!StringUtil.isEmpty(checkboxName)) {
                    html.append(" name=\"").append(checkboxName).append("\"");
                }
                if (!StringUtil.isEmpty(checkboxVal)) {
                    html.append(" value=\"").append(checkboxVal).append("\"");
                }
                if (checked) {
                    html.append(" checked");
                }
                if (!StringUtil.isEmpty(onclick)) {
                    html.append(" onclick=\"").append(onclick).append("\"");
                }
                html.append(">");
                html.append("<label for=\"").append(checkboxId).append("\" style=\"display: inline;\"></label>");
                html.append("</label>");
                html.append("</div>");
            }
        }
        if (index != null) {
            width = width - 1;
            if (isBackend) {
                html.append("<label class=\"col-lg-1 col-sm-12 control-label float-left\">").append(index).append("</label>");
            } else {
                html.append("<label class=\"col-lg-1 col-md-12 col-sm-12 control-label float-left\">").append(index).append("</label>");
            }
        }

        if (isBackend) {
            html.append("<label class=\"col-lg-").append(width).append(" col-sm-12 control-label float-left");
        } else {
            html.append("<label class=\"col-lg-").append(width).append(" col-md-12 col-sm-12 control-label float-left");
        }

        if (!StringUtil.isEmpty(cssClass)) {
            html.append(" ").append(cssClass);
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
            html.append("&nbsp;<span><i class=\"fa fa-info-circle\" data-toggle=\"tooltip\" data-html=\"true\" data-placement=\"bottom\" title=\"")
            .append(info).append("\"></i></span>&nbsp;");
        }

        if (isBackend && required) {
            html.append("<span class=\"text-mandatory\">*</span>");
        } else if (required) {
            html.append("<span class=\"orangetxt\">*</span>");
        }
        html.append("</label>");
        if (script.length() > 0) {
            html.append(script);
        }
        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("FieldTag: " + ex.getMessage());
        }
        return SKIP_BODY;
    }
    @Override
    public int doEndTag() {
        init();
        return EVAL_PAGE;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public void setAnnoId(String annoId) {
        this.annoId = annoId;
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
}
