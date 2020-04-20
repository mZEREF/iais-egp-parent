<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_tab" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="previewTab" role="tabpanel">
                                <div class="preview-gp">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-10">
                                            <p></p>
                                        </div>
                                        <div class="col-xs-12 col-md-2 text-right">
                                            <p class="print"><a href="" onclick="preview()"> <em class="fa fa-print"></em>Print</a></p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                <%@include file="../common/previewPremises.jsp"%>
                                                <%@include file="../common/previewPrimary.jsp"%>
                                                <c:forEach var="hcsaServiceDto" items="${hcsaServiceDtoList}" varStatus="status" >
                                                    <div class="panel panel-default svc-content">

                                                        <div class="panel-heading  <c:if test="${fn:contains(serviceConfig,hcsaServiceDto.id)}">incompleted</c:if> <c:if test="${fn:contains(serviceConfig,hcsaServiceDto.id)==false}">completed</c:if>  "  id="headingServiceInfo" role="tab">
                                                            <h4 class="panel-title" onclick="setIframeUrl('<iais:mask name="svc${status.index}" value="${hcsaServiceDto.id}"/>','svc${status.index}',$(this),'svcIframe${status.index}')"><a  class="svc-pannel-collapse collapsed"  role="button" data-toggle="collapse" href="#collapseServiceInfo${status.index}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information - ${hcsaServiceDto.svcName}</a></h4>
                                                        </div>

                                                        <div class=" panel-collapse collapse" id="collapseServiceInfo${status.index}" role="tabpanel" aria-labelledby="headingServiceInfo${status.index}" >
                                                            <div class="panel-body">
                                                                <c:if test="${AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.serviceEdit}">
                                                                    <p class="text-right mb-0">
                                                                        <a href="#" class="doSvcEdit"><em class="fa fa-pencil-square-o"></em>Edit</a>
                                                                        <input type="hidden" value="${hcsaServiceDto.svcCode}" name="svcCode" />
                                                                    </p>
                                                                </c:if>
                                                                <input type="hidden" value="0" name="svcCount" />
                                                                <iframe id="svcIframe${status.index}"  class="svc-iframe" title="" src=""  width="100%" ></iframe>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                            <c:if test="${GroupLicenceConfig != null && AppSubmissionDto.appType == 'APTY002'}">
                                                <div class="form-check">
                                                    <input class="form-check-input" id="groupLicence" type="checkbox" name="isGroupLic" aria-invalid="false" value="1">
                                                    <label class="form-check-label" for="groupLicence"><span class="check-square"></span>Would you like to apply as a group licence? </label>
                                                </div>
                                            </c:if>
                                            <div class="form-check">
                                                <input class="form-check-input" id="verifyInfoCheckbox" type="checkbox" name="verifyInfoCheckbox" aria-invalid="false">
                                                <label class="form-check-label" for="verifyInfoCheckbox"><span class="check-square"></span>I hereby certify that the information I provided is all correct and accurate</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6"><a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a></div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <c:if test="${requestInformationConfig==null}">
                                                    <input type="text" style="display: none" id="selectDraftNo" value="${selectDraftNo}">
                                                    <a class="btn btn-secondary" id = "SaveDraft" data-toggle="modal" data-target= "#saveDraft" >Save as Draft</a>
                                                </c:if>
                                                <a class="next btn btn-primary" id = "Next">SUBMIT & PAY </a></div>
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
</form>
<c:if test="${ not empty selectDraftNo}">
    <iais:confirm msg="There is an existing draft for the chosen service, if you choose to continue, the draft application will be discarded." callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
</c:if>
<script type="text/javascript">

    $(document).ready(function() {
        //Binding method
        $('#premisesEdit').click(function(){
            submit('premises',null,null);
        });
        $('#docEdit').click(function(){
            submit('documents',null,null);
        });
        $('#Back').click(function(){
            submit('serviceForms',null,null);
        });
        $('#SaveDraft').click(function(){
            if($('#selectDraftNo').val()==''||$('#selectDraftNo').val()==null){
                submit('preview','saveDraft',null);
            }
        });
        $('#Next').click(function(){
            submit('payment','doSubmit',null);
        });

        $('.doSvcEdit').click(function () {
            showWaiting();
            var svcCode = $(this).next().val();
            submitForms('preview',null,null,svcCode);
        });

        <c:if test="${'APTY005' ==AppSubmissionDto.appType && requestInformationConfig == null}">
        <c:if test="${AppSubmissionDto.appEditSelectDto.premisesEdit}">
        $('#docEdit').unbind();
        $('.doSvcEdit').unbind();
        $('#Back').unbind();
        $('#Back').click(function(){
            submit('premises',null,null);
        });
        </c:if>
        <c:if test="${AppSubmissionDto.appEditSelectDto.docEdit}">
        $('#premisesEdit').unbind();
        $('.doSvcEdit').unbind();
        $('#Back').unbind();
        $('#Back').click(function(){
            submit('documents',null,null);
        });
        </c:if>
        <c:if test="${AppSubmissionDto.appEditSelectDto.serviceEdit}">
        $('#premisesEdit').unbind();
        $('#docEdit').unbind();
        </c:if>
        </c:if>


    });

    function preview(){
        window.print();
    };

    function saveDraft() {
        submit('preview','saveDraft',$('#selectDraftNo').val());
    }
    function cancelSaveDraft() {
        submit('preview','saveDraft','cancelSaveDraft');
    }

    function setIframeUrl(maskId,maskName,svcDOM,iframeId) {
        var $svcContenEle = svcDOM.closest('div.svc-content');
        var svcCount = $svcContenEle.find('input[name="svcCount"]').val();
        if(0 != svcCount){
            return;
        }
        showWaiting();
        $svcContenEle.find('input[name="svcCount"]').val(1);
        var url ='${pageContext.request.contextPath}<%=RedirectUtil.changeUrlToCsrfGuardUrlUrl("/eservice/INTERNET/MohServiceRelatedInformation/1/PrepareView",request)%>&'+maskName+'='+maskId+'&maskName='+maskName+'&iframeId='+iframeId;
        var iframe =  $svcContenEle.find('.svc-iframe');
        iframe.prop('src',url);
    }


</script>
