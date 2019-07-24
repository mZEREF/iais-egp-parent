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

import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import org.apache.log4j.Logger;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.common.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * PaginationTag.java
 *
 * @author jinhua
 *
 */
public class PaginationTag extends TagSupport {
    private static final long serialVersionUID = 3640193450482281014L;
    private static Logger log = Logger.getLogger(PaginationTag.class.getName());

    private String param;
    private String result;
    private String jsFunc;
    private boolean needRowNum;
    
    public PaginationTag() {
        super();
        init();
    }

    // resets local state
    private void init() {
        param = null;
        result = null;
        jsFunc = null;
        needRowNum = false;
    }

    // Releases any resources we may have (or inherit)
    public void release() {
        super.release();
        init();
    }

    public int doStartTag() throws JspException {
        int pageNo = 1;
        int pageSize = 0;

        if (StringUtil.isEmpty(param)) {
            param = "searchParam";
        }
        if (StringUtil.isEmpty(result)) {
            result = "searchResult";
        }

        Object obj = ParamUtil.getScopeAttr((HttpServletRequest)pageContext.getRequest(), param);

        if (obj != null) {
            if (obj instanceof SearchParam) {
                SearchParam sp = (SearchParam) obj;
                pageNo = sp.getPageNo();
                pageSize = sp.getPageSize();
            }
        }

        SearchResult<?> sr = (SearchResult<?>) ParamUtil.getScopeAttr((HttpServletRequest)pageContext.getRequest(), result);

        try {
            if (pageSize != 0 && sr != null) {
                int pageCount = sr.getPageCount(pageSize);
                if (pageCount > 0) {
                    if (StringUtil.isEmpty(jsFunc)) {
                        jsFunc = "changePage";
                    }
                    String pageNumTextName = "pageJumpNoText" + jsFunc;
                    String jumpPageFuncName = "jumpToPage" + jsFunc;
                    StringBuilder sb = new StringBuilder();
                    sb.append("<div class=\"pull-left\">");
                    sb.append("<span class=\"src-result\">");
                    if (needRowNum) {
                        sb.append("<strong>").append(sr.getRowCount()).append("</strong> Record(s) Found,");
                    }
                    sb.append(" Showing Page <strong>");
                    sb.append(pageNo).append(" of ").append(pageCount);
                    sb.append("</strong></span></div>");
                    sb.append("<div class=\"pull-right\">").append("<div style=\"margin-top:0;\" class=\"btn-toolbar\">");
                    sb.append("<div class=\"btn-group\">").append("<div class=\"pagination pagination-pagejump pagination-custom\"><ul>");
                    if (pageNo > 1) {
                        sb.append("<li><a href=\"#\" onclick=\"javascript:").append(jsFunc).append("('1');\"><i class=\"icon-double-angle-left\"></i></a></li>");
                        sb.append("<li><a href=\"#\" onclick=\"javascript:").append(jsFunc).append("('").append(pageNo - 1).append("');\"><i class=\"icon-angle-left\"></i></a></li>");
                    } else {
                        sb.append("<li><a href=\"javascript:void(0);\"><i class=\"icon-double-angle-left\"></i></a></li>");
                        sb.append("<li><a href=\"javascript:void(0);\"><i class=\"icon-angle-left\"></i></a></li>");
                    }
                    sb.append("<li><input type=\"text\" name=\"").append(pageNumTextName).append("\" id=\"");
                    sb.append(pageNumTextName).append("\" class=\"input-pagejump\"></li>");
                    sb.append("<li><input type=\"button\" class=\"btn btn-mini btn-primary\" value=\"Go\" onclick=\"javascript:").append(jumpPageFuncName);
                    sb.append("();\"/></li>");
                    if (pageNo < pageCount) {
                        sb.append("<li><a href=\"#\" onclick=\"javascript:").append(jsFunc).append("('").append(pageNo + 1).append("');\"><i class=\"icon-angle-right\"></i></a></li>");
                        sb.append("<li><a href=\"#\" onclick=\"javascript:").append(jsFunc).append("('").append(pageCount).append("');\"><i class=\"icon-double-angle-right\"></i></a></li>");
                    } else {
                        sb.append("<li><a href=\"javascript:void(0);\"><i class=\"icon-angle-right\"></i></a></li>");
                        sb.append("<li><a href=\"javascript:void(0);\"><i class=\"icon-double-angle-right\"></i></a></li>");
                    }
                    sb.append("</ul></div></div></div></div>");
                    sb.append("<script type=\"text/javascript\">");
                    sb.append("$(\"#pageJumpNoText\").keyup(function(){var str=$(this).val();var newstr='';");
                    sb.append("for(i=0;i<str.length;i++){var j=str.charCodeAt(i);if(j>47&&j<58){newstr+=String.fromCharCode(j);}}");
                    sb.append("$(this).val(newstr);});");
                    sb.append("function ").append(jumpPageFuncName).append("(){");
                    sb.append("var pageNo = $(\"#").append(pageNumTextName).append("\").val();");
                    sb.append("var reg = /^\\d+$/;");
                    sb.append("if(!reg.test(pageNo)){");
                    sb.append("$(\"#").append(pageNumTextName).append("\").val('');");
                    sb.append( "return; ");
                    sb.append( "}");
                    sb.append( "if(pageNo != ''){if(pageNo > ");
                    sb.append(pageCount);
                    sb.append("){pageNo=").append(pageCount).append(";}").append(" else if(pageNo < 1){");
                    sb.append("pageNo=1;}");
                    sb.append("changePage(pageNo);}}");
                    sb.append("</script>");
                    pageContext.getOut().print(sb.toString());
                }
            }
        } catch (Exception ex) {
            log.error("", ex);
            throw new JspTagException("PaginationTag: " + ex.getMessage());
        }
        release();

        return SKIP_BODY;
    }

    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setJsFunc(String jsFunc) {
        this.jsFunc = jsFunc;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setNeedRowNum(boolean needRowNum) {
        this.needRowNum = needRowNum;
    }
    
}
