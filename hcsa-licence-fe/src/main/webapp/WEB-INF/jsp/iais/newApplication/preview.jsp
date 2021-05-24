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
                                            <p class="print"><div style="font-size: 16px;"><a onclick="preview()" href="javascript:void(0);"> <em class="fa fa-print"></em>Print</a></div></p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                <%@include file="../common/previewPremises.jsp"%>
                                                <%@include file="../common/previewPrimary.jsp"%>
                                                <c:forEach var="hcsaServiceDto" items="${hcsaServiceDtoList}" varStatus="status" >
                                                    <div class="panel panel-default svc-content">
                                                        <input type="hidden" name="iframeId" value="svcIframe${status.index}"/>
                                                        <input type="hidden" name="maskId" value="<iais:mask name="svc${status.index}" value="${hcsaServiceDto.id}"/>"/>
                                                        <input type="hidden" name="maskName" value="svc${status.index}"/>

                                                        <div class="panel-heading  <c:if test="${fn:contains(serviceConfig,hcsaServiceDto.id)}">incompleted</c:if> <c:if test="${fn:contains(serviceConfig,hcsaServiceDto.id)==false}">completed</c:if>  "  id="headingServiceInfo" role="tab">
                                                            <h4 class="panel-title svcTitle"><a  class="svc-pannel-collapse collapsed"  role="button" data-toggle="collapse" href="#collapseServiceInfo${status.index}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information - ${hcsaServiceDto.svcName}</a></h4>
                                                        </div>

                                                        <div class=" panel-collapse collapse" id="collapseServiceInfo${status.index}" role="tabpanel" aria-labelledby="headingServiceInfo${status.index}" >
                                                            <div class="panel-body">
                                                                <c:if test="${AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.serviceEdit}">
                                                                    <p class="mb-0">
                                                                        <div class="text-right app-font-size-16">
                                                                            <a href="#" class="doSvcEdit"><em class="fa fa-pencil-square-o"></em>Edit</a>
                                                                            <input type="hidden" value="${hcsaServiceDto.svcCode}" name="svcCode" />
                                                                        </div>
                                                                    </p>
                                                                </c:if>
                                                                <input type="hidden" value="0" name="svcCount" />
                                                                <%--<iframe id="svcIframe${status.index}"  class="svc-iframe" title="" src=""  width="100%" frameborder ="0" ></iframe>--%>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                                <c:choose>
                                                    <c:when test="${AppSubmissionDto.appType == 'APTY005' && RFC_eqHciNameChange!='RFC_eqHciNameChange'}">

                                                    </c:when>
                                                    <c:otherwise>
                                                        <%@include file="../common/declarations.jsp"%>
                                                    </c:otherwise>
                                                </c:choose>

                                            </div>
                                            <c:if test="${AppSubmissionDto.appType == 'APTY005' && requestInformationConfig == null}">
                                                <div class="row">
                                                    <div class="col-md-7"  style="text-align: justify;width: 70%">
                                                        Please indicate the date which you would like the changes to be effective (subject to approval). If not indicated, the effective date will be the approval date of the change.
                                                    </div>
                                                    <div class="col-md-5" style="width: 30%">
                                                        <iais:datePicker cssClass="rfcEffectiveDate" name="rfcEffectiveDate" value="${AppSubmissionDto.effectiveDateStr}" />
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-xs-5">
                                                    </div>
                                                    <div class="col-xs-7">
                                                        <span class="error-msg" name="iaisErrorMsg" id="error_rfcEffectiveDate"></span>
                                                    </div>
                                                </div>
                                                <br/>
                                            </c:if>
                                            <c:if test="${AppSubmissionDto.appType == 'APTY005' && RFC_eqHciNameChange!='RFC_eqHciNameChange'}">
                                                <div class="form-check">
                                                    <input class="form-check-input" id="verifyInfoCheckbox" type="checkbox" name="verifyInfoCheckbox" value="1" aria-invalid="false" <c:if test="${AppSubmissionDto.userAgreement}">checked="checked"</c:if> >
                                                    <label class="form-check-label" for="verifyInfoCheckbox"><span class="check-square"></span><iais:message key="ACK_DEC001"></iais:message></label>
                                                </div>
                                                <div>
                                                    <span id="error_fieldMandatory"  class="error-msg"></span>
                                                </div>

                                                <div>
                                                    <span id="error_charityHci"  class="error-msg"></span>
                                                </div>
                                            </c:if>
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
                                                    <input type="text" style="display: none; " id="saveDraftSuccess" value="${saveDraftSuccess}">

                                                        <a class="btn btn-secondary" id = "SaveDraft"  >Save as Draft</a>

                                                </c:if>
                                                <c:choose>
                                                    <c:when test="${requestInformationConfig != null}">
                                                        <a class="next btn btn-primary" id = "Next">Submit </a></div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a class="next btn btn-primary" id = "Next">Submit</a></div>
                                                    </c:otherwise>
                                                </c:choose>
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
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>

<c:if test="${!('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
    <iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft" yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>
</c:if>
<input type="hidden" id="rfcPendingApplication" value="${rfcPendingApplication}">
<div class="modal fade" id="rfcPending" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                <div class="modal-title" style="font-size:2rem;">Confirmation Box</div>--%>
<%--            </div>--%>
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12" ><span style="font-size: 2rem;">The changes you have made affect licences with pending application</span></div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" class="btn btn-secondary col-md-6" data-dismiss="modal" onclick="cancel()">Continue amending</button>
                <button type="button" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="exitAndSave()">Exit and save as draft</button>
            </div>
        </div>
    </div>
</div>
<%--<div class="modal fade" id="rfc_ERROR" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-body" style="text-align: center;">
                <div class="row">
                    <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem;">${RFC_ERROR_NO_CHANGE}</span></div>
                </div>
            </div>
        </div>
    </div>
</div>--%>
<iais:confirm msg="${RFC_ERROR_NO_CHANGE}" callBack="cancel()"  needCancel="false" popupOrder="rfc_ERROR"></iais:confirm>
<iais:confirm msg="${SERVICE_CONFIG_CHANGE}" callBack="cancel()"  needCancel="false" popupOrder="SERVICE_CONFIG_CHANGE"></iais:confirm>
<input type="hidden" value="${RFC_ERROR_NO_CHANGE}" id="RFC_ERROR_NO_CHANGE">
<input type="hidden" value="${RFC_ERR004}" id="RFC_ERR004">
<input type="hidden" id="SERVICE_CONFIG_HAVE_CHANGE" value="${SERVICE_CONFIG_CHANGE}">
<script type="text/javascript">

    $(document).ready(function() {
        //Binding method
        if($('#RFC_ERROR_NO_CHANGE').val()!=''){
            $('#rfc_ERROR').modal('show');
        }
        if($('#SERVICE_CONFIG_HAVE_CHANGE').val()!=''){
            $('#SERVICE_CONFIG_CHANGE').modal('show');
        }
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }
        if($('#rfcPendingApplication').val()=='errorRfcPendingApplication'){
            $('#rfcPending').modal('show');
        }
        $('#premisesEdit').click(function(){
            showWaiting();
            submit('premises',null,null);
        });
        $('#docEdit').click(function(){
            showWaiting();
            submit('documents',null,null);
        });
        $('#Back').click(function(){
            showWaiting();
            submit('serviceForms',null,null);
        });
        $('#SaveDraft').click(function(){
            showWaiting();
            submit('preview','saveDraft',null);
        });
        $('#Next').click(function(){
            <c:if test="${AppSubmissionDto.appType == 'APTY005' && RFC_eqHciNameChange!='RFC_eqHciNameChange'}">
            var canSubmit = true;
            let jQuery = $('#verifyInfoCheckbox').prop("checked");
            if(!jQuery){
                $('#error_fieldMandatory').html($('#RFC_ERR004').val());
                return;
            }else{
                $('#error_fieldMandatory').html("");
            }
            </c:if>
            showWaiting();
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
        // window.print();
        var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohFePrintView/1/",request)%>';
        window.open(url,'_blank');
    };

    function saveDraft() {
        submit('preview','saveDraft',$('#selectDraftNo').val());
    }
    function cancelSaveDraft() {
        submit('preview','saveDraft','cancelSaveDraft');
    }

    function cancel() {
        $('#saveDraft').modal('hide');
        $('#rfcPending').modal('hide');
        $('#rfc_ERROR').modal('hide');
        $('#SERVICE_CONFIG_CHANGE').modal('hide');
    }
    function exitAndSave() {
        submit('premises','saveDraft','exitSaveDraft');
    }

    function jumpPage() {
        submit('premises','saveDraft','jumpPage');
    }

    $('.svcTitle').click(function () {
        var $svcEle = $(this).closest('div.svc-content');
        var svcCount = $svcEle.find('input[name="svcCount"]').val();
        if(0 != svcCount){
            return;
        }
        showWaiting();
        var maskName = $svcEle.find('input[name="maskName"]').val();
        var maskId = $svcEle.find('input[name="maskId"]').val();
        var iframeId = $svcEle.find('input[name="iframeId"]').val();
        $svcEle.find('input[name="svcCount"]').val(1);
        var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohServiceRelatedInformation/1/PrepareView",request)%>';
        if(url.indexOf('/PrepareView?') != -1){
            url = url + '&'+maskName+'='+maskId+'&maskName='+maskName+'&iframeId='+iframeId;
        }else{
            url = url + '?' +maskName+'='+maskId+'&maskName='+maskName+'&iframeId='+iframeId;
        }
        var iframeHtml = "<iframe id=\""+ iframeId+ "\"  class=\"svc-iframe\" title=\"\" src=\""+ url +"\"  width=\"100%\" frameborder =\"0\" ></iframe>"
        $svcEle.find('input[name="svcCount"]').after(iframeHtml);

    });

</script>
