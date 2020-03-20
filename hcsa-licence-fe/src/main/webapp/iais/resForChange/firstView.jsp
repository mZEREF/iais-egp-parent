<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<style>
    .font-size-14{
        font-size: 14px;
    }
</style>
<div class="tab-pane" id="tabApp" role="tabpanel">
    <form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
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
                                                    <%@include file="../common/previewPremises.jsp"%>
                                                    <%@include file="../common/previewPrimary.jsp"%>
                                                    <div class="panel panel-default svc-content">
                                                        <div class="panel-heading"  id="headingServiceInfo" role="tab">
                                                            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed"  role="button" data-toggle="collapse" href="#collapseServiceInfo${status.index}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information </a></h4>
                                                        </div>

                                                        <div class=" panel-collapse collapse" id="collapseServiceInfo" role="tabpanel" aria-labelledby="headingServiceInfo">
                                                            <div class="panel-body">
                                                                <p class="text-right mb-0">
                                                                    <a href="#" id="doSvcEdit"><em class="fa fa-pencil-square-o"></em>Edit</a>
                                                                </p>
                                                                <div class="panel-main-content">
                                                                    <%@include file="../common/previewSvcDisciplines.jsp"%>
                                                                    <%@include file="../common/firstPreviewSvcGovernanceOfficer.jsp"%>
                                                                    <%@include file="../common/previewSvcAllocation.jsp"%>
                                                                    <%@include file="../common/firstPreviewSvcPrincipalOfficers.jsp"%>
                                                                    <%@include file="../common/previewSvcDocument.jsp"%>
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
                                            </div>
                                            <div class="col-xs-12 col-sm-3">
                                                <div class="button-group">
                                                    <a class="btn btn-secondary" href="#" >Print</a>
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

    });

    $('#premisesEdit').click(function () {
        $('#EditValue').val('premises');
        $('#menuListForm').submit();
    });

    $('#docEdit').click(function () {
        $('#EditValue').val('doc');
        $('#menuListForm').submit();
    });

    $('#doSvcEdit').click(function () {
        $('#EditValue').val('service');
        $('#action').val('serviceForms');
        $('#menuListForm').submit();
    });

    $('#Back').click(function () {
        $('[name="switch_value"]').val('back');
        $('#menuListForm').submit();
    });




</script>