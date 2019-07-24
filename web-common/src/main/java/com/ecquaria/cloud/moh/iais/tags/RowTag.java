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

import sg.gov.moh.iais.common.utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Row Tag
 *
 *
 * @date        7/23/2019
 * @author      suocheng
 */
public final class RowTag extends DivTagSupport {
    private static final long serialVersionUID = 2507648507715155034L;

    private boolean inline;

    public RowTag() {
        super();
        init();
    }

    // resets local state
    protected void init() {
        super.init();
        inline = false;
    }

    public void release() {
        super.release();
        init();
    }

    public int doStartTag() throws JspException {
        StringBuffer html = new StringBuffer();
        html.append("<div class=\"form-group");
        if (inline) {
            html.append(" form-inline");
        }
        if (!StringUtil.isEmpty(cssClass)) {
            html.append(" ").append(cssClass);
        }
        html.append("\"");
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append("\"");
        }
        if (!StringUtil.isEmpty(style)) {
            html.append(" style=\"").append(style).append("\"");
        }
        html.append(">");
        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("RowTag: " + ex.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print("<div class=\"clear\"></div></div>");
        } catch (Exception ex) {
            throw new JspTagException("RowTag: " + ex.getMessage());
        }
        return EVAL_PAGE;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }
}
