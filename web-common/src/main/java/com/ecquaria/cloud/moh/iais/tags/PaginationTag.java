package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
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
    private static final long serialVersionUID = -4571952182420979630L;
    private static final String STARTLI =  "<li><a href=\"#\" onclick=\"javascript:";
    private static final String ENDTAG = "');\">";
    private String param;
    private String result;
    private String jsFunc;
    private boolean needRowNum;

    public PaginationTag() {
        super();
        clearFields();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        clearFields();
    }

    private void clearFields() {
        setParam(null);
        setResult(null);
        setJsFunc(null);
        setNeedRowNum(false);
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
            setParam("searchParam");
        }
        if (StringUtil.isEmpty(result)) {
            setResult("searchResult");
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
                    StringBuilder sb = generateHtml(sr,pageNo,pageCount,pageSize);
                    pageContext.getOut().print(sb.toString());
                }
            }
        } catch (Exception ex) {
            log.error("", ex);
            throw new JspTagException("PaginationTag: " + ex.getMessage(),ex);
        }
        release();

        return SKIP_BODY;
    }
    private StringBuilder generateHtml(SearchResult<?> sr,int pageNo,int pageCount,int pageSize){
        if (StringUtil.isEmpty(jsFunc)) {
            setJsFunc("changePage");
        }
        String pageNumTextName = "pageJumpNoText" + jsFunc;
        String jumpPageFuncName = "jumpToPage" + jsFunc;
        StringBuilder sb = new StringBuilder();
        sb.append("<input type=\"hidden\" name = \"pageJumpNoTextchangePage\" value=\"\" id = \"pageJumpNoTextchangePage\">");
        sb.append("<div class=\"row table-info-display\">");
        sb.append("<div class=\"col-xs-12 col-md-6 text-left\">");
        sb.append("<p class=\"count table-count\">");
        int maxRec = sr.getRowCount() < pageNo * pageSize ? sr.getRowCount() : pageNo * pageSize;
        int statRec = sr.getRowCount() < ((pageNo - 1) * pageSize + 1) ? sr.getRowCount() : ((pageNo - 1) * pageSize + 1);
        sb.append(statRec).append('-').append(maxRec);
        sb.append(" out of ");
        sb.append(sr.getRowCount());
        sb.append(" items");
        sb.append("<div class=\"form-group\">");
        sb.append("<div class=\"col-xs-12 col-md-3\">");
        sb.append("<select class=\"table-select\" id = \"pageJumpNoPageSize\" name = \"pageJumpNoPageSize\" >");

        //Don't catch null NullPointerException
        int[] pageSizeArr = SystemParamUtil.toPageSizeArray();
        if (pageSizeArr != null && pageSizeArr.length > 0){
            for (int s : pageSizeArr){
                if (s == pageSize){
                    sb.append("<option selected value=\"") .append(s) .append("\">").append(s).append("</option>");
                }else {
                    sb.append("<option value=\"") .append(s)  .append("\">").append(s).append("</option>");
                }
            }
        }

        sb.append("</select>");
        sb.append("</div></div></p></div>");

        sb.append("<div class=\"col-xs-12 col-md-6 text-right\">");
        sb.append("<div class=\"nav\">").append("<ul class=\"pagination\">");
        if (pageNo > 1) {
            sb.append(STARTLI).append(jsFunc).append("('").append(1).append("');\"><span aria-hidden=\"true\"><i class=\"fa fa-angle-double-left\"></i></span></a></li>");
            sb.append(STARTLI).append(jsFunc).append("('").append(pageNo - 1).append("');\"><span aria-hidden=\"true\"><i class=\"fa fa-angle-left\"></i></span></a></li>");
            sb.append(STARTLI).append(jsFunc).append("('").append(pageNo - 1).append(ENDTAG);
            sb.append(pageNo-1);
            sb.append("</a></li>");
            sb.append("<li class=\"active\"><a href=\"#\"  onclick=\"javascript:void(0);\">");
            sb.append(pageNo);
            sb.append("</a></li>");
            if(pageNo + 1 <= pageCount){
                sb.append(STARTLI).append(jsFunc).append("('").append(pageNo + 1).append(ENDTAG);
                sb.append(pageNo+1);
                sb.append("</a></li>");
            }
            if (pageNo + 1 < pageCount) {
                sb.append("...");
            }
        } else {
            sb.append("<li><a href=\"javascript:void(0);\" aria-label=\"First\"><span aria-hidden=\"false\"><i class=\"fa fa-angle-double-left\"></i></span></a></li>");
            sb.append("<li><a href=\"javascript:void(0);\" aria-label=\"Previous\"><span aria-hidden=\"false\"><i class=\"fa fa-angle-left\"></i></span></a></li>");
            sb.append("<li class=\"active\"><a href=\"#\">");
            sb.append(pageNo);
            sb.append("</a></li>");
            if(pageNo+1 <= pageCount){
                sb.append(STARTLI).append(jsFunc).append("('").append(pageNo + 1).append(ENDTAG);
                sb.append(pageNo+1);
                sb.append("</a></li>");
            }
            if(pageNo+2 <= pageCount){
                sb.append(STARTLI).append(jsFunc).append("('").append(pageNo + 2).append(ENDTAG);
                sb.append(pageNo+2);
                sb.append("</a></li>");
            }
            if (pageNo + 2 < pageCount) {
                sb.append("...");
            }
        }
        if (pageNo < pageCount) {
            sb.append(STARTLI).append(jsFunc).append("('").append(pageNo + 1).append("');\"><i class=\"fa fa-angle-right\"></i></a></li>");
            sb.append(STARTLI).append(jsFunc).append("('").append(pageCount).append("');\"><i class=\"fa fa-angle-double-right\"></i></a></li>");
        } else {
            sb.append("<li><a href=\"javascript:void(0);\"><i class=\"fa fa-angle-right\"></i></a></li>");
            sb.append("<li><a href=\"javascript:void(0);\"><i class=\"fa fa-angle-double-right\"></i></a></li>");
        }
        sb.append("</ul></div></div></div>");
        sb.append("<script type=\"text/javascript\">");
        sb.append("$('#pageJumpNoPageSize').change(function(){");
        sb.append("jumpToPagechangePage();");
        sb.append(" });");
        sb.append("function changePage(action){");
        sb.append("$('#pageJumpNoTextchangePage').val(action);");
        sb.append("jumpToPagechangePage();");
        sb.append('}');
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
