<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="active" id="info" role="presentation">
                                                <a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                            </li>
                                            <li id="documents" role="presentation">
                                                <a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li id="dynamicContent" role="presentation">
                                                <a href="#tabDynamicContent" aria-controls="tabDynamicContent" role="tab" data-toggle="tab">Dynamic Content</a>
                                            </li>
                                            <li id="process" role="presentation">
                                                <a href="#tabScreening" aria-controls="tabScreening" role="tab" data-toggle="tab">Screening</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide">
                                                    <a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDynamicContent" id="doDynamicContent" aria-controls="tabDynamicContent" role="tab" data-toggle="tab">Dynamic Content</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabProcessing" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Screening</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/approving/applicationInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/approving/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabDynamicContent" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/approving/dynamicContent.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabScreening" role="tabpanel">
                                                <span id="error_document" name="iaisErrorMsg" class="error-msg"></span>
                                                <br/><br/>
                                                <div class="alert alert-info" role="alert">
                                                    <strong>
                                                        <h4>Processing Status Update</h4>
                                                    </strong>
                                                </div>
                                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp">
                                                                <iais:section title="">
                                                                    <div id="Remarks">
                                                                        <iais:row>
                                                                            <iais:field value="Remarks" required="false" width="12"/><
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea id="remarksId" name="remarks" cols="70" rows="7" maxlength="300"></textarea>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="RiskLeveloftheBiologicalAgentOrToxin">
                                                                        <iais:row>
                                                                            <iais:field value="Risk Level of the Biological Agent/Toxin" required="true"></iais:field>
                                                                            <iais:value width="6">
                                                                                <select id="RiskLeveloftheBiologicalAgentOrToxinSelectId">
                                                                                    <option value="-1">Please Select</option>
                                                                                    <option value="1">Enhanced risk</option>
                                                                                    <option value="2">Normal risk</option>
                                                                                </select>
                                                                                <span class="error-msg" name="errorMsg" id="error_RiskLeveloftheBiologicalAgentOrToxin"></span>
                                                                                <%--                                                                                <iais:select cssClass="nextStage" name="nextStage" id="nextStage" options="nextStages" firstOption="Please Select" value="">Screened by DO</iais:select>--%>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="CommentsOnRiskLevelAssessment">
                                                                        <iais:row>
                                                                            <iais:field value="Comments on Risk Level Assessment" required="false" width="12"/>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea id="riskLevelId" name="riskLevel" cols="70" rows="7" maxlength="1000"></textarea>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="ProcessingDecision">
                                                                        <iais:row>
                                                                            <iais:field value="Processing Decision" required="true"/>
                                                                            <iais:value width="6">
                                                                                <select id="ProcessingDecisionSelectId">
                                                                                    <option value="-1">Please Select</option>
                                                                                    <option value="1">Screened by DO</option>
                                                                                    <option value="2">Request for Information</option>
                                                                                    <option value="3">Reject</option>
                                                                                </select>
                                                                                <span class="error-msg" name="errorMsg" id="error_ProcessingDecision"></span>
<%--                                                                                <iais:select cssClass="nextStage" name="nextStage" id="nextStage" options="nextStages" firstOption="Please Select" value="">Screened by DO</iais:select>--%>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="ERPReport">
                                                                        <iais:row>
                                                                            <iais:field value="ERP Report" required="false"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker name="eRPReport" value=""></iais:datePicker>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="RedTeamingReport">
                                                                        <iais:row>
                                                                            <iais:field value="Red Teaming Report" required="false"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker name="redTeamingReport" value=""></iais:datePicker>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="LentivirusReport">
                                                                        <iais:row>
                                                                           <iais:field value="Lentivirus Report" required="false"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker name="lentivirusReport" value=""></iais:datePicker>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="InternalInspectionReport">
                                                                        <iais:row>
                                                                            <iais:field value="Internal Inspection Report" required="false"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker name="internalInspectionReport" value=""></iais:datePicker>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="SelectedApprovedFacilityCertifier">
                                                                        <iais:row>
                                                                            <iais:field value="Selected Approved Facility Certifier" required="false"></iais:field>
                                                                            <iais:value width="6">
                                                                                <select id="SelectedApprovedFacilityCertifierSelectId">
                                                                                    <option value="-1">Please Select</option>
                                                                                    <option value="1">Approve</option>
                                                                                    <option value="2">Reject</option>
                                                                                </select>
                                                                                <%--                                                                                <iais:select cssClass="nextStage" name="nextStage" id="nextStage" options="nextStages" firstOption="Please Select" value="">Screened by DO</iais:select>--%>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="ValidityStartDate">
                                                                        <iais:row>
                                                                            <iais:field value="Validity Start Date" required="true"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker id="validityStartDate" name="validityStartDate" value=""></iais:datePicker>
                                                                                <span class="error-msg" name="errorMsg" id="error_ValidityStartDate"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="ValidityEndDate">
                                                                        <iais:row>
                                                                            <iais:field value="Validity End Date" required="true"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker id="validityEndDate" name="validityEndDate" value=""></iais:datePicker>
                                                                                <span class="error-msg" name="errorMsg" id="error_ValidityEndDate"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <a style="float:left;padding-top: 1.1%;" class="back" href="/system-admin-web/eservice/INTRANET/MohDutyApprovingInbox?"><em class="fa fa-angle-left"></em> Back</a>
                                                                <div align="right">
                                                                    <button name="clearBtn" id="clearButton" type="button" class="btn btn-secondary">Clear</button>
                                                                    <button name="submitBtn" id="submitButton" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                                <%@include file="/WEB-INF/jsp/iais/approving/screenHistory.jsp"%>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <iais:confirm msg="GENERAL_ACK018" needCancel="false" callBack="tagConfirmCallbacksupportReport()" popupOrder="supportReport"></iais:confirm>
</div>
<%@include file="/WEB-INF/jsp/iais/approving/uploadFile.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    $("#submitButton").click(function (){


        $("input[name='field2']").val("xxx");


        var processingDecisionIndex = $("#ProcessingDecisionSelectId option:selected").val();
        var riskLeveloftheBiologicalAgentOrToxinIndex = $("#RiskLeveloftheBiologicalAgentOrToxinSelectId option:selected").val();
        var validityStartDate = $("#validityStartDate").val();
        var validityEndDate = $("#validityEndDate").val();
        $(".error-msg").html("");
        if(validityStartDate == null || validityStartDate == ""){
            $("#error_validityStartDate").text("This field is mandatory");
        }
        if(validityEndDate == null || validityEndDate == ""){
            $("#error_validityEndDate").text("This field is mandatory");
        }
        if(processingDecisionIndex == -1){
            $("#error_ProcessingDecision").text("This field is mandatory");
        }
        if(riskLeveloftheBiologicalAgentOrToxinIndex == -1){
            $("#error_RiskLeveloftheBiologicalAgentOrToxin").text("This field is mandatory");
        }

        if(validityStartDate != null && validityStartDate != "" && validityEndDate != null && validityEndDate != "" && processingDecisionIndex != -1 && riskLeveloftheBiologicalAgentOrToxinIndex != -1){
            if(processingDecisionIndex == 1){
                SOP.Crud.cfxSubmit("mainForm", "ScreenedByDO");
            }else if(processingDecisionIndex == 2){
                SOP.Crud.cfxSubmit("mainForm", "RequestForInformation");
            }else if(processingDecisionIndex == 3){
                SOP.Crud.cfxSubmit("mainForm", "Reject");
            }
        }

    })
</script>