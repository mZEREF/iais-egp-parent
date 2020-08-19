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
                                            <p class="print"><div style="font-size: 16px;"><a href="/hcsa-licence-web/eservice/INTERNET/PrintLicenceFe"> <em class="fa fa-print"></em>Print</a></div></p>
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
                                            </div>
                                            <c:if test="${AppSubmissionDto.appType == 'APTY005' && requestInformationConfig == null}">
                                                <div class="row">
                                                    <div class="col-xs-5">
                                                        Please indicate an effective date of change for your licence information to be updated.The date of change will be effected on the indicated date or approval date, whichever is the later date
                                                    </div>
                                                    <div class="col-xs-7">
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
                                            <%--<c:if test="${GroupLicenceConfig != null && AppSubmissionDto.appType == 'APTY002' && !AppSubmissionDto.onlySpecifiedSvc && requestInformationConfig == null}">--%>
                                            <c:if test="${GroupLicenceConfig != null && AppSubmissionDto.appType == 'APTY002' && requestInformationConfig == null}">
                                                <div class="form-check">
                                                    <input class="form-check-input" id="groupLicence" type="checkbox" <c:if test="${AppSubmissionDto.groupLic}">checked="checked"</c:if> name="isGroupLic" aria-invalid="false" value="1">
                                                    <label class="form-check-label" for="groupLicence"><span class="check-square"></span>Would you like to apply as a group licence? </label>
                                                </div>
                                            </c:if>
                                            <div class="form-check">
                                                <input class="form-check-input" id="verifyInfoCheckbox" type="checkbox" name="verifyInfoCheckbox" value="1" aria-invalid="false" <c:if test="${AppSubmissionDto.userAgreement}">checked="checked"</c:if> >
                                                <label class="form-check-label" for="verifyInfoCheckbox"><span class="check-square"></span><iais:message key="ACK_DEC001"></iais:message></label>
                                            </div>
                                            <div>
                                                <span id="error_fieldMandatory"  class="error-msg"></span>
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
                                                    <input type="text" style="display: none; " id="saveDraftSuccess" value="${saveDraftSuccess}">

                                                        <a class="btn btn-secondary" id = "SaveDraft"  >Save as Draft</a>

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
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>

<iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft" yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>


<script type="text/javascript">

    $(document).ready(function() {
        //Binding method
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }
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
            submit('preview','saveDraft',null);
        });
        $('#Next').click(function(){
            let jQuery = $('#verifyInfoCheckbox').prop("checked");
            if(!jQuery){
                $('#error_fieldMandatory').html("The field is mandatory");
                return;
            }else if(jQuery) {
                $('#error_fieldMandatory').html("");
                submit('payment','doSubmit',null);
            }

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

    function cancel() {
        $('#saveDraft').modal('hide');

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
        var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohServiceRelatedInformation/1/PrepareView",request)%>&'+maskName+'='+maskId+'&maskName='+maskName+'&iframeId='+iframeId;
        var iframeHtml = "<iframe id=\""+ iframeId+ "\"  class=\"svc-iframe\" title=\"\" src=\""+ url +"\"  width=\"100%\" frameborder =\"0\" ></iframe>"
        $svcEle.find('input[name="svcCount"]').after(iframeHtml);

    });

</script>
