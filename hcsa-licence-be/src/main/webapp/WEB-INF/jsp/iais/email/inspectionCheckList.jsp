<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
           String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm" enctype="multipart/form-data"   action=<%=process.runtime.continueURL()%>  class="form-horizontal" >

    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                <div class="intranet-content">
<iais:body>
    <div class="">
    <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">

                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane" id="tabInfo" role="tabpanel">
                                <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabViewApp.jsp"%>
                            </div>
                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                            <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabDocuments.jsp"%>
                            </div>
                            <div class="tab-pane active" id="tabCheckList" role="tabpanel">
                                <%@include file="/WEB-INF/jsp/iais/inspectionncList/inspectiondetail.jsp"%>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohBackendInbox?fromOther=1"><em class="fa fa-angle-left"></em> Back</a>
                                        <input type="hidden" name="viewchk" id = "viewchk">
                                        <div align="right">
                                                <button type="button" class="btn btn-primary" onclick="javascript: doViewCheckList();">
                                                    View CheckList
                                                </button>
                                            <c:if test="${applicationViewDto.applicationDto.status != ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW}">
                                            <button type="button" class="btn btn-primary" onclick="javascript: doNext();">
                                                Save
                                            </button>
                                          </c:if>
                                            <br/>
                                            <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
                                        </div>
                                        <input type="hidden" name = "saveflag" id="saveflag"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </iais:body>
    </div>
</div>
</div></div></div>
</form>
</div>
<%@include file="/WEB-INF/jsp/iais/inspectionncList/uploadFile.jsp" %>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>


<script type="text/javascript">
    function doNext(){
         showWaiting();
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