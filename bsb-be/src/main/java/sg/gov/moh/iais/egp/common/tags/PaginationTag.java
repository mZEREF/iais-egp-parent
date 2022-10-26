package sg.gov.moh.iais.egp.common.tags;

import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import org.springframework.util.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;


/* This tag may be improved in the future */
public class PaginationTag extends SimpleTagSupport {
    private static final String FREQUENT_LI_START =  "<li><a href=\"#\" onclick=\"";
    private static final String FREQUENT_END_TAG = "');\">";
    private static final String FREQUENT_END_A_LI = "</a></li>";

    private int size;
    private int pageNo;
    private int pageAmt;
    private long totalElements;

    private String jsFunc;





    @Override
    public void doTag() throws JspException {
        try {
            if (pageAmt > 0) {
                StringBuilder sb = generateHtml(size, pageNo, pageAmt, totalElements);
                getJspContext().getOut().print(sb.toString());
            }
        } catch (Exception e) {
            throw new JspException("Pagination error", e);
        }
    }


    private StringBuilder generateHtml(int size, int pageNo, int pageAmt, long totalElements) {
        if (StringUtils.isEmpty(jsFunc)) {
            jsFunc = "changePage";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<input type=\"hidden\" name = \"pageJumpNoTextchangePage\" value=\"\" id = \"pageJumpNoTextchangePage\">");
        sb.append("<div class=\"row table-info-display\">");
        sb.append("<div class=\"col-xs-12 col-md-4 text-left\">");
        sb.append("<p class=\"count table-count\">");
        long maxRec = totalElements < pageNo * size ? totalElements : pageNo * size;
        long statRec = totalElements < ((pageNo - 1) * size + 1) ? totalElements : ((pageNo - 1) * size + 1);
        sb.append(statRec).append('-').append(maxRec);
        sb.append(" out of ");
        sb.append(totalElements);
        sb.append(" items");
        sb.append("<div class=\"form-group\">");
        sb.append("<div class=\"col-xs-12 col-md-3\">");
        sb.append("<select class=\"table-select\" id = \"pageJumpNoPageSize\" name = \"pageJumpNoPageSize\" >");

        int[] pageSizeArr = SystemParamUtil.toPageSizeArray();
        if (pageSizeArr != null && pageSizeArr.length > 0){
            for (int s : pageSizeArr){
                if (s == size){
                    sb.append("<option selected value=\"") .append(s) .append("\">").append(s).append("</option>");
                }else {
                    sb.append("<option value=\"") .append(s)  .append("\">").append(s).append("</option>");
                }
            }
        }

        sb.append("</select>");
        sb.append("</div></div></p></div>");

        sb.append("<div class=\"col-xs-12 col-md-8 text-right\">");
        sb.append("<div class=\"nav\">").append("<ul class=\"pagination\">");

        if (pageNo > 1) {
            sb.append(FREQUENT_LI_START).append(jsFunc).append("('").append(1).append("');\"><span aria-hidden=\"true\"><i class=\"fa fa-angle-double-left\"></i></span></a></li>");
            sb.append(FREQUENT_LI_START).append(jsFunc).append("('").append(pageNo - 1).append("');\"><span aria-hidden=\"true\"><i class=\"fa fa-angle-left\"></i></span></a></li>");
            sb.append(FREQUENT_LI_START).append(jsFunc).append("('").append(pageNo - 1).append(FREQUENT_END_TAG);
            sb.append(pageNo-1);
            sb.append(FREQUENT_END_A_LI);
            sb.append("<li class=\"active\"><a href=\"#\"  onclick=\"javascript:void(0);\">");
            sb.append(pageNo);
            sb.append(FREQUENT_END_A_LI);
            if(pageNo + 1 <= pageAmt){
                sb.append(FREQUENT_LI_START).append(jsFunc).append("('").append(pageNo + 1).append(FREQUENT_END_TAG);
                sb.append(pageNo+1);
                sb.append(FREQUENT_END_A_LI);
            }
            if (pageNo + 1 < pageAmt) {
                sb.append("...");
            }
        } else {
            sb.append("<li><a href=\"javascript:void(0);\" aria-label=\"First\"><span aria-hidden=\"false\"><i class=\"fa fa-angle-double-left\"></i></span></a></li>");
            sb.append("<li><a href=\"javascript:void(0);\" aria-label=\"Previous\"><span aria-hidden=\"false\"><i class=\"fa fa-angle-left\"></i></span></a></li>");
            sb.append("<li class=\"active\"><a href=\"#\">");
            sb.append(pageNo);
            sb.append(FREQUENT_END_A_LI);
            if(pageNo+1 <= pageAmt){
                sb.append(FREQUENT_LI_START).append(jsFunc).append("('").append(pageNo + 1).append(FREQUENT_END_TAG);
                sb.append(pageNo+1);
                sb.append(FREQUENT_END_A_LI);
            }
            if(pageNo+2 <= pageAmt){
                sb.append(FREQUENT_LI_START).append(jsFunc).append("('").append(pageNo + 2).append(FREQUENT_END_TAG);
                sb.append(pageNo+2);
                sb.append(FREQUENT_END_A_LI);
            }
            if (pageNo + 2 < pageAmt) {
                sb.append("...");
            }
        }
        if (pageNo < pageAmt) {
            sb.append(FREQUENT_LI_START).append(jsFunc).append("('").append(pageNo + 1).append("');\"><i class=\"fa fa-angle-right\"></i></a></li>");
            sb.append(FREQUENT_LI_START).append(jsFunc).append("('").append(pageAmt).append("');\"><i class=\"fa fa-angle-double-right\"></i></a></li>");
        } else {
            sb.append("<li><a href=\"javascript:void(0);\"><i class=\"fa fa-angle-right\"></i></a></li>");
            sb.append("<li><a href=\"javascript:void(0);\"><i class=\"fa fa-angle-double-right\"></i></a></li>");
        }
        sb.append("</ul></div></div></div>");
        return sb;
    }


    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPageAmt(int pageAmt) {
        this.pageAmt = pageAmt;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setJsFunc(String jsFunc) {
        this.jsFunc = jsFunc;
    }
}
