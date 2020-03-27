<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>

    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.FillupChklistDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">

                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane" id="tabInfo" role="tabpanel">
                                <%@include file="/iais/inspectionncList/tabViewApp.jsp"%>
                            </div>
                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                            <%@include file="/iais/inspectionncList/tabDocuments.jsp"%>
                            </div>
                            <div class="tab-pane active" id="tabCheckList" role="tabpanel">
                                <%@include file="/iais/inspectionncList/inspectiondetail.jsp"%>
                                <div class="row">
                                    <div class="col-xs-12">

                                        <div align="right">
                                                <button type="button" class="btn btn-primary" onclick="javascript: doViewCheckList();">
                                                    View CheckList
                                                </button>
                                                <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
                                            <button type="button" class="btn btn-primary" onclick="javascript: doNext();">
                                                Save
                                            </button>
                                        </div>
                                        <input type="hidden" name = "saveflag" id="saveflag"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/include/validation.jsp" %>

<script type="text/javascript">
    function doNext(){
        $("#saveflag").val("save");
        SOP.Crud.cfxSubmit("mainForm", "save");
    }

    function doViewCheckList() {
        SOP.Crud.cfxSubmit("mainForm", "doChecklist");
    }
    function showCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comId = str+'comck'
        var comdivck =document.getElementById(divId);
        var divck =document.getElementById(comId);
        $("#"+divId).show();
        $("#"+comId).show();
    }
    function hideCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comdivId = str+'comck';
        var divck =document.getElementById(divId);
        var comdivck =document.getElementById(comdivId);
        $("#"+divId).hide();
        $("#"+comdivId).hide();

    }
</script>