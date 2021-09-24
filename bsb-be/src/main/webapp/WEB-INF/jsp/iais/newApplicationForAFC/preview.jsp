<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type_tab" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="previewTab" role="tabpanel">
                                <div class="preview-gp">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-10">
                                            <p></p>
                                        </div>
                                        <div class="col-xs-12 col-md-2 text-right">
                                            <p class="print">
                                            <div style="font-size: 16px;">
                                                <a onclick="preview()" href="javascript:void(0);">
                                                    <em class="fa fa-print"></em>Print
                                                </a>
                                            </div>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="panel-group" id="accordion" role="tablist"
                                                 aria-multiselectable="true">
                                                <%@include file="common/previewOrganizationInfo.jsp" %>
                                                <br>
                                                <%@include file="common/previewPrimaryDocument.jsp" %>
                                            </div>
                                            <div class="row">

                                                <div class=" form-group form-horizontal formgap">
                                                    <br><br>
                                                    <div class="col-sm-1">
                                                        <label class="control-label control-set-font control-font-label">Remark</label>
                                                    </div>
                                                    <div class="col-sm-4 col-md-7">
                                                        <div class="">
                                                            <textarea id="Remark"
                                                                      name="Remark"
                                                                      cols="61"
                                                                      rows="6"
                                                                      maxlength="500"></textarea>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-7">
                                                <span class="error-msg" name="iaisErrorMsg" id="remarkError"></span>
                                            </div>
                                        </div>
                                        <br/>

                                        <div>
                                            <input class="form-check-input" id="verifyInfoCheckbox1"
                                                   type="checkbox" name="verifyInfoCheckbox" value="1"
                                                   aria-invalid="false">
                                            <label class="form-check-label" for="verifyInfoCheckbox1">
                                                <span>Declaration of compliance with MOH requirements including those
                                                    stipulated in the checklist</span>
                                            </label>
                                        </div>
                                        <div>
                                            <span id="error_checkbox1" class="error-msg"></span>
                                        </div>

                                        <div>
                                            <input class="form-check-input" id="verifyInfoCheckbox2"
                                                   type="checkbox" name="verifyInfoCheckbox" value="1"
                                                   aria-invalid="false">
                                            <label class="form-check-label" for="verifyInfoCheckbox2">
                                                <span class="check-square">Declaration on the accuracy of submission</span>
                                            </label>
                                        </div>
                                        <div>
                                            <span id="error_checkbox2" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <a class="back" id="Back" href="/main-web/eservicecontinue/INTERNET/NewApplicationForAFC/1/AdministratorInfo">
                                            <em class="fa fa-angle-left"></em> Back
                                        </a>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <div class="button-group">
                                            <a class="btn btn-secondary" id="SaveDraft" href="javascript:void(0);">Save as Draft</a>
                                            <a class="btn btn-primary next" id="Cancle" href="/main-web/eservice/INTERNET/MohInternetInbox">Cancle</a>
                                            <a class="btn btn-primary next" id="Next" href="javascript:void(0);">Next</a>
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
    <iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft"
                  yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary"
                  yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>
</c:if>
<iais:confirm msg="${RFC_ERROR_NO_CHANGE}" callBack="cancel()" needCancel="false" popupOrder="rfc_ERROR"></iais:confirm>
<iais:confirm msg="${SERVICE_CONFIG_CHANGE}" callBack="cancel()" needCancel="false"
              popupOrder="SERVICE_CONFIG_CHANGE"></iais:confirm>
<script type="text/javascript">

    $(document).ready(function () {
        //Binding method
        /*$('#Back').click(function () {
            showWaiting();
            submit('serviceForms', null, null);
        });*/
        $('#SaveDraft').click(function () {
            /*showWaiting();
            submit('preview', 'saveDraft', null);*/
        });
        $('#Next').click(function () {
            var state1=$("#verifyInfoCheckbox1").prop("checked");
            var state2=$("#verifyInfoCheckbox2").prop("checked");
            if (state1 && state2){
                showWaiting();
                $("#mainForm").submit();
            }
            if (!state1){
                $("#error_checkbox1").html("Please check the declaration");
            }else{
                $("#error_checkbox1").html("");
            }
            if (!state2){
                $("#error_checkbox2").html("Please check the declaration");
            }else{
                $("#error_checkbox2").html("");
            }
        });
    });


</script>
