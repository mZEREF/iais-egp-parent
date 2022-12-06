package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
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
    private static final long serialVersionUID = 1L;

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
        StringBuilder sb = new StringBuilder();
        sb.append("<input type=\"hidden\" name = \"pageJumpNoTextchangePage\" value=\"\" id = \"pageJumpNoTextchangePage\">");
        sb.append("<div class=\"row table-info-display\">");
        sb.append("<div class=\"col-xs-12 col-md-6 text-left\">");
        sb.append("<p class=\"count table-count\">");
        int maxRec = Math.min(sr.getRowCount(), pageNo * pageSize);
        int statRec = Math.min(sr.getRowCount(), ((pageNo - 1) * pageSize + 1));
        sb.append(statRec).append('-').append(maxRec);
        sb.append(" out of ");
        sb.append(sr.getRowCount());
        sb.append(" items");
        sb.append("<div class=\"form-group\">");
        sb.append("<div class=\"col-xs-12 col-md-3\">");
        sb.append("<select class=\"table-select\" id = \"pageJumpNoPageSize\" name = \"pageJumpNoPageSize\" >");

        //Don't catch null NullPointerException
        addPageSizeOptions(pageSize, sb);

        sb.append("</select>");
        sb.append("</div></div></p></div>");

        sb.append("<div class=\"col-xs-12 col-md-6 text-right\">");
        sb.append("<div class=\"nav\">").append("<ul class=\"pagination\">");
        PaginationHandler.addPageNumLis(pageNo, pageCount, jsFunc, sb);
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

    private void addPageSizeOptions(int pageSize, StringBuilder sb) {
        int[] pageSizeArr = SystemParamUtil.toPageSizeArray();
        if (pageSizeArr != null && pageSizeArr.length > 0) {
            for (int s : pageSizeArr) {
                if (s == pageSize) {
                    sb.append("<option selected value=\"").append(s).append("\">").append(s).append("</option>");
                } else {
                    sb.append("<option value=\"").append(s).append("\">").append(s).append("</option>");
                }
            }
        }
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
