<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<style>
    .font-size-14{
        font-size: 14px;
    }
    .app-font-size-16{
        font-size: 16px;
    }
</style>
<c:set var="isRfi" value="${requestInformationConfig != null}" />
<c:set var="isRFC" value="${'APTY005' == AppSubmissionDto.appType}" />
<c:set var="subLicenseeDto" value="${AppSubmissionDto.subLicenseeDto}"/>
<div class="tab-pane" id="tabApp" role="tabpanel">
    <form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input id="action" type="hidden" name="crud_action_type" value="">
        <input type="hidden" id="hiddenIndex" name="hiddenIndex" value=""/>
        <input id="EditValue" type="hidden" name="EditValue" value="" />
        <input type="hidden"  name="switch_value" value=""/>
        <div class="main-content">
            <%@include file="../common/dashboard.jsp" %>
            <div class="container">
                <div class="row">
                    <div class="col-xs-12">

                        <div class="tab-gp steps-tab">
                            <div class="tab-content">
                                <div class="tab-pane active" id="previewTab" role="tabpanel">
                                    <div class="preview-gp">
                                        <div class="row">
                                            <br/><br/><br/>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                    <%@include file="../common/previewLicensee.jsp"%>
                                                    <%@include file="../common/previewPremises.jsp"%>
                                                    <%@include file="../common/previewPrimary.jsp"%>
                                                    <div class="panel panel-default svc-content">
                                                        <div class="panel-heading"  id="headingServiceInfo" role="tab">
                                                            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed"  role="button" data-toggle="collapse" href="#collapseServiceInfo${status.index}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information </a></h4>
                                                        </div>

                                                        <div class=" panel-collapse collapse" id="collapseServiceInfo" role="tabpanel" aria-labelledby="headingServiceInfo">
                                                            <div class="panel-body">
                                                                <p class="mb-0">
                                                                    <div class="text-right app-font-size-16">
                                                                        <a href="#" id="doSvcEdit"><em class="fa fa-pencil-square-o"></em>Edit</a>
                                                                    </div>
                                                                </p>
                                                                <div class="panel-main-content">
                                                                    <%@include file="../common/previewSvcInfo.jsp"%>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                    <div class="application-tab-footer">
                                        <div class="row">
                                            <div class="col-xs-12 col-sm-3">
                                                <c:if test="${DoDraftConfig == null}">
                                                <a id = "Back" class="back" ><em class="fa fa-angle-left"></em> Back</a>
                                                </c:if>
                                                <c:if test="${RFC_DRAFT_NO!=null}">
                                                    <a class="back"  id="RFC_BACK"><em class="fa fa-angle-left"></em> Back</a>
                                                </c:if>
                                            </div>
                                            <div class="col-xs-12 col-sm-9">
                                                <div class="button-group">
                                                    <a class="btn btn-secondary" id="rfcPrint" href="javascript:void(0);">Print</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <div class="row">
        </div>
    </form>
</div>
<script>
    $(document).ready(function () {
        $('#subLicenseeEdit').click(function () {
            showWaiting();
            $('#EditValue').val('licensee');
            $('#menuListForm').submit();
        });

        $('#premisesEdit').click(function () {
            showWaiting();
            $('#EditValue').val('premises');
            $('#menuListForm').submit();
        });

        $('#docEdit').click(function () {
            showWaiting();
            $('#EditValue').val('doc');
            $('#menuListForm').submit();
        });

        $('#doSvcEdit').click(function () {
            showWaiting();
            $('#EditValue').val('service');
            $('#action').val('serviceForms');
            $('#menuListForm').submit();
        });

        $('#Back').click(function () {
            showWaiting();
            $('[name="switch_value"]').val('back');
            $('#menuListForm').submit();
        });

        $('#rfcPrint').click(function () {
            // window.print();
            var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohFePrintView/1/?appType=APTY005",request)%>';
            window.open(url,'_blank');
        });
        $('#RFC_BACK').click(function (){
            location.href="https://${pageContext.request.serverName}/main-web<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohInternetInbox?initPage=initApp",request)%>";
        });

    });
</script>