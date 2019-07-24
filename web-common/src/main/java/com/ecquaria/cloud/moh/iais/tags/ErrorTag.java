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

import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Error Tag
 *
 *
 * @date        7/24/2019
 * @author      suocheng
 */
public final class ErrorTag extends DivTagSupport {
    private static final long serialVersionUID = 2507648507715155034L;

    private boolean canClose;

    public ErrorTag() {
        super();
        init();
    }

    // resets local state
    protected void init() {
        canClose = false;
        super.init();
    }

    public void release() {
        super.release();
        init();
    }

    public int doStartTag() throws JspException {
        String id = this.id;
        if (StringUtil.isEmpty(id)) {
            id = "alert" + MiscUtil.getDummyId();
        }

        StringBuffer html = new StringBuffer();
        html.append("<div class=\"alert alert-danger\" role=\"alert\"");
        if (!StringUtil.isEmpty(id)) {
            html.append(" id=\"").append(id).append("\"");
        }
        if (!StringUtil.isEmpty(style)) {
            html.append(" style=\"").append(style).append("\"");
        }
        html.append(">");
        if (canClose) {
            html.append("<button type=\"button\" class=\"close\" onclick=\"javascript:$('#").append(id).append("').slideToggle('fast');\" aria-label=\"Close\">");
            html.append("<span>&times;</span></button>");
        }

        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString()));
        } catch (Exception ex) {
            throw new JspTagException("RowTag: " + ex.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print("</div>");
        } catch (Exception ex) {
            throw new JspTagException("RowTag: " + ex.getMessage());
        }
        return EVAL_PAGE;
    }

    public void setCanClose(boolean canClose) {
        this.canClose = canClose;
    }
}
