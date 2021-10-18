package com.ecquaria.cloud.moh.iais.dto.memorypage;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * PaginationHandler
 *
 * @author Jinhua
 * @date 2020/5/27 17:50
 */

public class PaginationHandler<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 6513594824654010214L;

    private static final String STARTLI =  "<li><a href=\"#\" onclick=\"javascript:";
    private static final String ENDTAG = "');\">";

    public static final String JS_FUNCTION_NAME             = "changeMemoryPage";
    public static final int CHECK_TYPE_NO_CHECK             = 0;
    public static final int CHECK_TYPE_CHECKBOX             = 1;
    public static final int CHECK_TYPE_RADIO                = 2;

    private int currentPageNo;
    private int totalRows;
    private int totalPage;
    private int pageSize;
    private String paginationHtml;
    private String allCheckHtml;
    private String paginationDiv;
    private String recordsDiv;
    private int[] pageSizeDrop;
    private boolean needRowNum;
    private boolean checkInTrail;
    private boolean checkBoxDisable;
    private int checkType;
    private ArrayList<PageRecords<T>> allData = IaisCommonUtils.genNewArrayList();
    private ArrayList<PageRecords<T>> displayData = IaisCommonUtils.genNewArrayList();

    public PaginationHandler(String paginationDiv, String recordsDiv) {
        this.paginationDiv = paginationDiv;
        this.recordsDiv = recordsDiv;
        this.pageSize = SystemParamUtil.getDefaultPageSize();
        this.pageSizeDrop = SystemParamUtil.toPageSizeArray();
        this.currentPageNo = 1 ;
        doPaging();
        ParamUtil.setSessionAttr(MiscUtil.getCurrentRequest(), paginationDiv + "__SessionAttr", this);
        checkAllHtml();
    }

    public PaginationHandler(String paginationDiv, String recordsDiv, int checkType) {
        this.paginationDiv = paginationDiv;
        this.recordsDiv = recordsDiv;
        this.checkType = checkType;
        this.pageSize = SystemParamUtil.getDefaultPageSize();
        this.pageSizeDrop = SystemParamUtil.toPageSizeArray();
        this.currentPageNo = 1 ;
        doPaging();
        ParamUtil.setSessionAttr(MiscUtil.getCurrentRequest(), paginationDiv + "__SessionAttr", this);
        checkAllHtml();
    }

    public PaginationHandler(String paginationDiv, String recordsDiv, Collection<T>allData) {
        this.paginationDiv = paginationDiv;
        this.recordsDiv = recordsDiv;
        this.pageSize = SystemParamUtil.getDefaultPageSize();
        this.pageSizeDrop = SystemParamUtil.toPageSizeArray();
        setAllData(allData);
        this.currentPageNo = 1 ;
        doPaging();
        ParamUtil.setSessionAttr(MiscUtil.getCurrentRequest(), paginationDiv + "__SessionAttr", this);
        checkAllHtml();
    }

    public void keepCurrentPageChecked() {
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        if (request != null && !displayData.isEmpty()) {
            String[] checkedStr = ParamUtil.getStrings(request, paginationDiv + "Check");
            if (checkType == CHECK_TYPE_RADIO && checkedStr != null && checkedStr.length > 0) {
                for (PageRecords<T> obj : allData) {
                    obj.setChecked(false);
                }
            }
            for (PageRecords<T> obj : displayData) {
                obj.setChecked(false);
            }
            if (checkedStr != null && checkedStr.length > 0) {
                for (int i = 0; i < checkedStr.length; i++) {
                    long checkId = Long.parseLong(checkedStr[i]);
                    for (PageRecords<T> obj : displayData) {
                        if (obj.getId() == checkId) {
                            obj.setChecked(true);
                            break;
                        }
                    }
                }
            }
        }
        ParamUtil.setSessionAttr(request,paginationDiv + "__SessionAttr",this);
    }

    public void doPaging(int pageNo) {
        this.currentPageNo = pageNo;
        doPaging();
    }

    public final void doPaging() {
        this.totalRows = allData.size();
        this.totalPage = (this.totalRows % this.pageSize == 0) ? (this.totalRows/this.pageSize) : (this.totalRows/this.pageSize) + 1 ;
        currentPageNo = currentPageNo > totalPage ? totalPage : currentPageNo;
        currentPageNo = currentPageNo < 1 ? 1 : currentPageNo;
        displayData.clear();
        int start = pageSize * (currentPageNo - 1);
        int end = start + pageSize > totalRows ? totalRows : start + pageSize;
        for (int i = start; i < end; i++) {
            displayData.add(allData.get(i));
        }
        generatePaginationHtml();
    }

    public String generatePaginationHtml(boolean needRowNum) {
        setNeedRowNum(needRowNum);

        return generatePaginationHtml();
    }

    public String generatePaginationHtml() {
        StringBuilder sb = new StringBuilder();
        String jumpPageFuncName = "jumpToPage" + paginationDiv;
        String functionParam = "('" + paginationDiv + "'," + checkType + ",";

        sb.append("<input type=\"hidden\" name = \"pageJumpNoTextchangePage\" value=\"\" id = \"pageJumpNoTextchangePage\">");
        sb.append("<div class=\"row table-info-display\">");
        sb.append("<div class=\"col-xs-12 col-md-6 text-left\">");
        sb.append("<p class=\"count table-count\">");
        int maxRec = totalRows < currentPageNo * pageSize ? totalRows : currentPageNo * pageSize;
        int statRec = totalRows < ((currentPageNo - 1) * pageSize + 1) ? totalRows : ((currentPageNo - 1) * pageSize + 1);
        sb.append(statRec).append('-').append(maxRec);
        sb.append(" out of ");
        sb.append(totalRows);
        sb.append(" items");
        sb.append("<div class=\"form-group\">");
        sb.append("<div class=\"col-xs-12 col-md-3\">");
        sb.append("<select class=\"table-select\" id=\"memPageSize").append(paginationDiv);
        sb.append("\" name=\"memPageSize").append(paginationDiv).append("\">");
        if (pageSizeDrop != null && pageSizeDrop.length > 0) {
            for (int siz : pageSizeDrop) {
                if (pageSize == siz) {
                    sb.append("<option selected value=\"").append(siz).append("\">").append(siz).append("</option>");
                } else {
                    sb.append("<option value=\"").append(siz).append("\">").append(siz).append("</option>");
                }
            }
        } else {
            sb.append("<option selected value=\"10\">10</option>");
        }
        sb.append("</select>");
        sb.append("</div></div></p></div>");
        sb.append("<div class=\"col-xs-12 col-md-6 text-right\">");
        sb.append("<div class=\"nav\">").append("<ul class=\"pagination\">");
        if (currentPageNo > 1) {
            sb.append(STARTLI).append(jumpPageFuncName).append("('").append(1).append("');\"><span aria-hidden=\"true\"><i class=\"fa fa-angle-double-left\"></i></span></a></li>");
            sb.append(STARTLI).append(jumpPageFuncName).append("('").append(currentPageNo - 1).append("');\"><span aria-hidden=\"true\"><i class=\"fa fa-angle-left\"></i></span></a></li>");
            sb.append(STARTLI).append(jumpPageFuncName).append("('").append(currentPageNo - 1).append(ENDTAG);
            sb.append(currentPageNo - 1);
            sb.append("</a></li>");
            sb.append("<li class=\"active\"><a href=\"#\"  onclick=\"javascript:");
            sb.append(jumpPageFuncName).append("('").append(currentPageNo).append(ENDTAG);
            sb.append(currentPageNo);
            sb.append("</a></li>");
            if(currentPageNo + 1 <= totalPage){
                sb.append(STARTLI).append(jumpPageFuncName).append("('").append(currentPageNo + 1).append(ENDTAG);
                sb.append(currentPageNo + 1);
                sb.append("</a></li>");
            }
            if (currentPageNo + 1 < totalPage) {
                sb.append("...");
            }
        } else {
            sb.append("<li><a href=\"javascript:void(0);\" aria-label=\"First\"><span aria-hidden=\"false\"><i class=\"fa fa-angle-double-left\"></i></span></a></li>");
            sb.append("<li><a href=\"javascript:void(0);\" aria-label=\"Previous\"><span aria-hidden=\"false\"><i class=\"fa fa-angle-left\"></i></span></a></li>");
            sb.append("<li class=\"active\"><a href=\"#\">");
            sb.append(currentPageNo);
            sb.append("</a></li>");
            if(currentPageNo + 1 <= totalPage){
                sb.append(STARTLI).append(jumpPageFuncName).append("('").append(currentPageNo + 1).append(ENDTAG);
                sb.append(currentPageNo + 1);
                sb.append("</a></li>");
            }
            if(currentPageNo + 2 <= totalPage){
                sb.append(STARTLI).append(jumpPageFuncName).append("('").append(currentPageNo + 2).append(ENDTAG);
                sb.append(currentPageNo + 2);
                sb.append("</a></li>");
            }
            if (currentPageNo + 2 < totalPage) {
                sb.append("...");
            }
        }
        if (currentPageNo < totalPage) {
            sb.append(STARTLI).append(jumpPageFuncName).append("('").append(currentPageNo + 1).append("');\"><i class=\"fa fa-angle-right\"></i></a></li>");
            sb.append(STARTLI).append(jumpPageFuncName).append("('").append(totalPage).append("');\"><i class=\"fa fa-angle-double-right\"></i></a></li>");
        } else {
            sb.append("<li><a href=\"javascript:void(0);\"><i class=\"fa fa-angle-right\"></i></a></li>");
            sb.append("<li><a href=\"javascript:void(0);\"><i class=\"fa fa-angle-double-right\"></i></a></li>");
        }
        sb.append("</ul></div></div></div>");

        sb.append("<script type=\"text/javascript\">");
        sb.append("function ").append(jumpPageFuncName).append("(pageNo){");
        sb.append( "if(pageNo != ''){if(pageNo > ");
        sb.append(totalPage);
        sb.append("){pageNo=").append(totalPage).append(";}").append(" else if(pageNo < 1){");
        sb.append("pageNo=1;}");
        sb.append(JS_FUNCTION_NAME).append(functionParam).append("pageNo);}}");
        sb.append("$('#").append("memPageSize").append(paginationDiv).append("').change(function() {");
        sb.append("memoryPageSizeChange('").append(paginationDiv).append("', this.value);});");
        sb.append("</script>");

        this.paginationHtml = sb.toString();

        return this.paginationHtml;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public String getPaginationHtml() {
        return paginationHtml;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
        checkAllHtml();
    }

    public String getPaginationDiv() {
        return paginationDiv;
    }

    public boolean isCheckBoxDisable() {
        return checkBoxDisable;
    }

    public void setCheckBoxDisable(boolean checkBoxDisable) {
        this.checkBoxDisable = checkBoxDisable;
        checkAllHtml();
    }

    public void setDefaultChecked(Collection<T> col) {
        if (allData != null) {
            for (PageRecords<T> pr : allData) {
                if (col.contains(pr.getRecord())) {
                    pr.setChecked(true);
                }
            }
            checkAllHtml();
        }
    }

    public String getRecordsDiv() {
        return recordsDiv;
    }

    public void setNeedRowNum(boolean needRowNum) {
        this.needRowNum = needRowNum;
        generatePaginationHtml();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        doPaging();
    }

    public boolean isCheckInTrail() {
        return checkInTrail;
    }

    public void setCheckInTrail(boolean checkInTrail) {
        this.checkInTrail = checkInTrail;
    }

    public ArrayList<PageRecords<T>> getDisplayData() {
        return displayData;
    }

    public String getAllCheckHtml() {
        return allCheckHtml;
    }

    public final void setAllData(Collection<T> allData) {
        this.allData.clear();
        if (allData != null && allData.size() > 0) {
            for (T obj : allData) {
                PageRecords<T> pr = new PageRecords<T>(obj);
                this.allData.add(pr);
            }
        }
        doPaging();
    }

    public void setAllData(List<T> allData, List<Boolean> checkList) {
        this.allData.clear();
        if (allData != null && allData.size() > 0) {
            for (int i = 0; i < allData.size(); i++) {
                T obj = allData.get(i);
                boolean checked = checkList.get(i);
                PageRecords<T> pr = new PageRecords<T>(obj);
                pr.setChecked(checked);
                this.allData.add(pr);
            }
        }
        doPaging();
    }

    public void addData(T data) {
        if (data != null) {
            PageRecords<T> pr = new PageRecords<T>(data);
            this.allData.add(pr);
            doPaging();
        }
    }

    public void addAll(Collection<T> data) {
        if (data != null && data.size() > 0) {
            for (T obj : data) {
                PageRecords<T> pr = new PageRecords<T>(obj);
                this.allData.add(pr);
            }
            doPaging();
        }
    }

    public void uncheckAllData() {
        if (allData != null && allData.size() > 0) {
            for (PageRecords<T> pr : allData) {
                pr.setChecked(false);
            }
        }
    }

    public void removeAllCheckedData() {
        if (allData != null && allData.size() > 0) {
            for (int i = allData.size() - 1; i >= 0; i--) {
                PageRecords<T> pr = allData.get(i);
                if (pr.isChecked()) {
                    allData.remove(i);
                }
            }
            doPaging();
        }
    }

    public ArrayList<T> getAllData() {
        ArrayList<T> list = IaisCommonUtils.genNewArrayList();
        for (PageRecords<T> pr : allData) {
            list.add(pr.getRecord());
        }

        return list;
    }

    public ArrayList<T> getAllCheckedData() {
        ArrayList<T> list = IaisCommonUtils.genNewArrayList();
        for (PageRecords<T> pr : allData) {
            if (pr.isChecked()) {
                list.add(pr.getRecord());
            }
        }

        return list;
    }

    public ArrayList<T> getDisplayCheckedData() {
        ArrayList<T> list = IaisCommonUtils.genNewArrayList();
        for (PageRecords<T> pr : displayData) {
            if (pr.isChecked()) {
                list.add(pr.getRecord());
            }
        }

        return list;
    }

    public final String checkAllHtml() {
        StringBuilder sb = new StringBuilder();
        if (checkType == PaginationHandler.CHECK_TYPE_CHECKBOX) {
            sb.append("<input type=\"checkbox\" id=\"").append(paginationDiv).append("CheckAll");
            sb.append("\" name=\"").append(paginationDiv).append("CheckAll").append('\"');
            sb.append(" onclick=\"javascript:checkAllMemoryCheck('").append(paginationDiv).append("');\"");
            boolean allChecked = !displayData.isEmpty();
            for (PageRecords<T> pr : displayData) {
                if (!pr.isChecked()) {
                    allChecked = false;
                    break;
                }
            }
            if (allChecked) {
                sb.append(" checked");
            }
            if (checkBoxDisable) {
                sb.append(" disabled");
            }
            sb.append("/>");
        }
        allCheckHtml = sb.toString();

        return allCheckHtml;
    }

    public void preLoadingPage() {
        @SuppressWarnings("unchecked")
        HashSet<String> set = (HashSet<String>) ParamUtil.getRequestAttr(MiscUtil.getCurrentRequest(),
                "memoryPagingLoading__Flag_Attr");
        if (set == null) {
            set = IaisCommonUtils.genNewHashSet();
        }
        set.add(paginationDiv);
        ParamUtil.setRequestAttr(MiscUtil.getCurrentRequest(), "memoryPagingLoading__Flag_Attr", set);
    }

    public void clearSessionAttr() {
        ParamUtil.setSessionAttr(MiscUtil.getCurrentRequest(), paginationDiv + "__SessionAttr", null);
    }

}
