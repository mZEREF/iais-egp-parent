<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    String webroot= IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <form method="post" id="mainForm" enctype="multipart/form-data"   action=<%=process.runtime.continueURL()%>  class="form-horizontal" >
        <input type="hidden" name="crud_action_additional" id="crud_action_additional"/>
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

                                            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                                <li id="infoli" class="" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" id="doInfoTab" data-toggle="tab" >Info</a></li>
                                                <li id="documentsli" class="" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                                                                     data-toggle="tab">Documents</a></li>
                                                <li id="checkListli" class="" role="presentation"><a onclick="checkChecklistTab()" id="checkList" aria-controls="tabCheckList" role="tab"
                                                                                                     data-toggle="tab">Checklist</a></li>
                                                <li id="emailViewli" class="active" role="presentation" ><a href="#tabReport" aria-controls="tabReport" role="tab"
                                                                                                            data-toggle="tab">Inspection Report</a></li>
                                                <li id="processingli" class="" role="presentation"><a onclick="checkProcessingTab()" id="processing" aria-controls="tabProcessing" role="tab"
                                                                                                      data-toggle="tab">Processing</a></li>
                                            </ul>
                                            <div class="tab-content">
                                                <div class="tab-pane" id="tabInfo" role="tabpanel">
                                                    <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabViewApp.jsp"%>
                                                </div>
                                                <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                    <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabDocuments.jsp"%>
                                                </div>
                                                <div class="tab-pane active" id="tabReport" role="tabpanel">
                                                    <%@include file="/WEB-INF/jsp/iais/report/inspectorReport.jsp"%>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                                                            <div align="right">
                                                                <button type="button" class="btn btn-primary" onclick="javascript: doNext();">
                                                                    Save
                                                                </button>
                                                                <br/>
                                                                <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
                                                            </div>
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
        SOP.Crud.cfxSubmit("mainForm");
    }

    function checkChecklistTab(){
        showWaiting();
        $('#crud_action_additional').val('editChecklist');
        document.getElementById('mainForm').submit();
    }

    function checkProcessingTab(){
        showWaiting();
        $('#crud_action_additional').val('processing');
        document.getElementById('mainForm').submit();
    }
</script>