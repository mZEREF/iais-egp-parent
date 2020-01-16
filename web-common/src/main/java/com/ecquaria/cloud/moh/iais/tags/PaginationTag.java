package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * PaginationTag.java
 *
 * @author jinhua
 *
 */
@Slf4j
public class PaginationTag extends DivTagSupport {
    private static final long serialVersionUID = 3640193450482281014L;
    private static final String STARTLI =  "<li><a href=\"#\" onclick=\"javascript:";
    private static final String ENDTAG = "');\">";
    private String param;
    private String result;
    private String jsFunc;
    private boolean needRowNum;
    
    public PaginationTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        param = null;
        result = null;
        jsFunc = null;
        needRowNum = false;
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
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

        if (obj instanceof SearchParam) {
                SearchParam sp = (SearchParam) obj;
                pageNo = sp.getPageNo();
                pageSize = sp.getPageSize();
        }

        SearchResult<?> sr = (SearchResult<?>) ParamUtil.getScopeAttr((HttpServletRequest)pageContext.getRequest(),
                result);

        try {
            if (pageSize != 0 && sr != null) {
                int pageCount = sr.getPageCount(pageSize);
                if (pageCount > 0) {
                    StringBuilder sb =generateHtml(sr,pageNo,pageCount,pageSize);
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
    private StringBuilder generateHtml(SearchResult<?> sr,int pageNo,int pageCount,int pageSize){
        if (StringUtil.isEmpty(jsFunc)) {
            jsFunc = "changePage";
        }
        String pageNumTextName = "pageJumpNoText" + jsFunc;
        String jumpPageFuncName = "jumpToPage" + jsFunc;
        StringBuilder sb = new StringBuilder();
        sb.append("<input type=\"hidden\" name = \"pageJumpNoTextchangePage\" value=\"\" id = \"pageJumpNoTextchangePage\">");
        sb.append("<div class=\"row table-info-display\">");
        sb.append("<div class=\"col-xs-12 col-md-4 text-left\">");
        sb.append("<p class=\"count table-count\">");
        sb.append(pageNo).append("-").append(pageCount);
        sb.append(" out of ");
        sb.append(sr.getRowCount());
        sb.append(" items");
        sb.append("<div class=\"form-group\">");
        sb.append("<div class=\"col-xs-12 col-md-3\">");
        sb.append("<select class=\"table-select\" id = \"pageJumpNoPageSize\" name = \"pageJumpNoPageSize\" >");
//        if(pageSize==5){
//            sb.append("<option selected value=\"5\">5</option>");
//        }else{
//            sb.append("<option  value=\"5\">5</option>");
//        }
        if(pageSize==10){
            sb.append("<option selected value=\"10\">10</option>");
        }else{
            sb.append("<option value=\"10\">10</option>");
        }
        if(pageSize==20){
            sb.append("<option selected value=\"20\">20</option>");
        }else{
            sb.append("<option value=\"20\">20</option>");
        }
        if(pageSize==30){
            sb.append("<option selected value=\"30\">30</option>");
        }else{
            sb.append("<option value=\"30\">30</option>");
        }
        if(pageSize==40){
            sb.append("<option selected value=\"40\">40</option>");
        }else{
            sb.append("<option value=\"40\">40</option>");
        }
        sb.append("</select>");
        sb.append("</div></div></p></div>");

        sb.append("<div class=\"col-xs-12 col-md-8 text-right\">");
        sb.append("<div class=\"nav\">").append("<ul class=\"pagination\">");
        if (pageNo > 1) {
            //sb.append(STARTLI).append(jsFunc).append("('1');\"></a></li>");
            sb.append(STARTLI).append(jsFunc).append("('").append(pageNo - 1).append("');\"><span aria-hidden=\"true\"><i class=\"fa fa-chevron-left\"></i></span></a></li>");
            sb.append(STARTLI).append(jsFunc).append("('").append(pageNo - 1).append(ENDTAG);
            sb.append(pageNo-1);
            sb.append("</a></li>");
            sb.append("<li class=\"active\"><a href=\"#\"  onclick=\"javascript:");
            sb.append(jsFunc).append("('").append(pageNo).append(ENDTAG);
            sb.append(pageNo);
            sb.append("</a></li>");
            if(pageNo+1<=pageCount){
                sb.append(STARTLI).append(jsFunc).append("('").append(pageNo + 1).append(ENDTAG);
                sb.append(pageNo+1);
                sb.append("</a></li>");
            }
        } else {
            sb.append("<li><a href=\"#\" aria-label=\"Previous\"><span aria-hidden=\"false\"><i class=\"fa fa-chevron-left\"></i></span></a></li>");
            sb.append("<li class=\"active\"><a href=\"#\">");
            sb.append(pageNo);
            sb.append("</a></li>");
            if(pageNo+1<=pageCount){
                sb.append(STARTLI).append(jsFunc).append("('").append(pageNo + 1).append(ENDTAG);
                sb.append(pageNo+1);
                sb.append("</a></li>");
            }
            if(pageNo+2<=pageCount){
                sb.append(STARTLI).append(jsFunc).append("('").append(pageNo + 2).append(ENDTAG);
                sb.append(pageNo+2);
                sb.append("</a></li>");
            }
            //sb.append("<li><a href=\"#\" aria-label=\"Next\"><span aria-hidden=\"true\"><i class=\"fa fa-chevron-right\"></i></span></a></li>");
        }
//        sb.append("<li><input type=\"text\" name=\"").append(pageNumTextName).append("\" id=\"");
//        sb.append(pageNumTextName).append("\" class=\"input-pagejump\"></li>");
//        sb.append("<li><input type=\"button\" class=\"btn btn-mini btn-primary\" value=\"Go\" onclick=\"javascript:").append(jumpPageFuncName);
//        sb.append("();\"/></li>");
        if (pageNo < pageCount) {
            sb.append(STARTLI).append(jsFunc).append("('").append(pageNo + 1).append("');\"><i class=\"fa fa-chevron-right\"></i></a></li>");
           // sb.append(STARTLI).append(jsFunc).append("('").append(pageCount).append("');\"><i class=\"fa fa-chevron-right\"></i></a></li>");
        } else {
           // sb.append("<li><a href=\"javascript:void(0);\"><i class=\"fa fa-chevron-left\"></i></a></li>");
            sb.append("<li><a href=\"javascript:void(0);\"><i class=\"fa fa-chevron-right\"></i></a></li>");
        }
        sb.append("</ul></div></div></div>");
        sb.append("<script type=\"text/javascript\">");
//        sb.append("$('.current').html(");
//        sb.append(pageSize);
//        sb.append(");");
        sb.append("$('#pageJumpNoPageSize').change(function(){");
        sb.append("jumpToPagechangePage();");
        sb.append(" });");
        sb.append("function changePage(action){");
        sb.append("$('#pageJumpNoTextchangePage').val(action);");
        sb.append("jumpToPagechangePage();");
        sb.append("}");
//        sb.append("$(\"#pageJumpNoText\").keyup(function(){var str=$(this).val();var newstr='';");
//        sb.append("for(i=0;i<str.length;i++){var j=str.charCodeAt(i);if(j>47&&j<58){newstr+=String.fromCharCode(j);}}");
//        sb.append("$(this).val(newstr);});");
//        sb.append("function ").append(jumpPageFuncName).append("(){");
//        sb.append("var pageNo = $(\"#").append(pageNumTextName).append("\").val();");
//        sb.append("var reg = /^\\d+$/;");
//        sb.append("if(!reg.test(pageNo)){");
//        sb.append("$(\"#").append(pageNumTextName).append("\").val('');");
//        sb.append( "return; ");
//        sb.append( "}");
//        sb.append( "if(pageNo != ''){if(pageNo > ");
//        sb.append(pageCount);
//        sb.append("){pageNo=").append(pageCount).append(";}").append(" else if(pageNo < 1){");
//        sb.append("pageNo=1;}");
//        sb.append("changePage(pageNo);}}");
        sb.append("</script>");
        return sb;
    }
    @Override
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
