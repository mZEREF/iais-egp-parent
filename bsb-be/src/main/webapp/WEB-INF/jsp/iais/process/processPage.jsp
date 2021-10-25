<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-process.js"></script>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="ifProcess" id="ifProcess" value="${mohProcess}">
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
                                                <a href="#tabInfo" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                            </li>
                                            <li id="documents" role="presentation">
                                                <a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li id="inspectionOrCertification" role="presentation">
                                                <a href="#tabInspectionOrCertification" id="doInspectionOrCertification" aria-controls="tabInspectionOrCertification" role="tab" data-toggle="tab">Inspection Or Certification</a>
                                            </li>
                                            <li id="dynamicContent" role="presentation">
                                                <a href="#tabDynamicContent" id="doContent" aria-controls="tabDynamicContent" role="tab" data-toggle="tab">Dynamic Content</a>
                                            </li>
                                            <li id="process" role="presentation">
                                                <a href="#tabProcessing" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide">
                                                    <a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabInspectionOrCertification" aria-controls="tabInspectionOrCertification" role="tab" data-toggle="tab">Inspection Or Certification</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDynamicContent" aria-controls="tabDynamicContent" role="tab" data-toggle="tab">Dynamic Content</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/process/applicationInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabInspectionOrCertification" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/process/inspectionOrCertification.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabDynamicContent" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/process/dynamicContent.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                                <br/><br/>
                                                <div class="alert alert-info" role="alert">
                                                    <strong>
                                                        <h4>Processing Status Update</h4>
                                                    </strong>
                                                </div>
                                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp" id="beInboxFilter">
                                                                <iais:section title="">
                                                                    <div>&nbsp</div>
                                                                    <%--<c:if test="${submitDetailsDto.processType=='PROTYPE001' && submitDetailsDto.applicationStatus=='BSBAPST001'}">
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Risk Level of the Biological Agent/Toxin" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="riskLevel" name="riskLevel" disabled="false" codeCategory="CATE_ID_BSB_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT" firstOption="Please Select" value="${mohProcessDto.riskLevel}"></iais:select>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_riskLevel"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Comments on Risk Level Assessment" required="false" width="12"/>
                                                                                <iais:value width="10">
                                                                                    <div class="input-group">
                                                                                        <div class="ax_default text_area">
                                                                                            <textarea name="commentsOnRiskLevelAssessment" cols="70" rows="7" maxlength="1000"><c:out value="${mohProcessDto.riskLevelComments}"></c:out></textarea>
                                                                                            <span class="error-msg" name="iaisErrorMsg" id="error_commentsOnRiskLevelAssessment"></span>
                                                                                        </div>
                                                                                    </div>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="ERP Report" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="erpReport" dateVal="${mohProcessDto.erpReportDt}"></iais:datePicker>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Red Teaming Report" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="redTeamingReport" dateVal="${mohProcessDto.redTeamingReportDt}"></iais:datePicker>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Lentivirus Report" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="lentivirusReport" dateVal="${mohProcessDto.lentivirusReportDt}"></iais:datePicker>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Internal Inspection Report" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="internalInspectionReport" dateVal="${mohProcessDto.internalInspectionReportDt}"></iais:datePicker>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Selected Approved Facility Certifier" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="selectedApprovedFacilityCertifier" name="selectedApprovedFacilityCertifier" disabled="false" codeCategory="CATE_ID_BSB_SELECTED_APPROVED_FACILITY_CERTIFER" firstOption="Please Select" value="${mohProcessDto.selectedAfc}"></iais:select>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity Start Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="validityStartDate" dateVal="${mohProcessDto.validityStartDt}"></iais:datePicker>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_validityStartDt"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity End Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="validityEndDate"  dateVal="${mohProcessDto.validityEndDt}"></iais:datePicker>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_validityEndDt"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Processing Decision" required="true"/>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="processingDecision" name="processingDecision" disabled="false" codeCategory="CATE_ID_BSB_DO_SCREENING_PROCESSING_DECISION" firstOption="Please Select" value="${mohProcessDto.processDecision}"></iais:select>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_processDecision"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Final Remarks" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="finalRemarks" name="finalRemarks" disabled="false" codeCategory="CATE_ID_BSB_FINAL_REMARKS" firstOption="Please Select" value="${mohProcessDto.finalRemarks}"></iais:select>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_finalRemarks"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Remarks" required="false" width="12"/>
                                                                                <iais:value width="10">
                                                                                    <div class="input-group">
                                                                                        <div class="ax_default text_area">
                                                                                            <textarea name="remarks" cols="70" rows="7" maxlength="300"><c:out value="${mohProcessDto.remarks}"></c:out></textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    </c:if>--%>
                                                                    <c:if test="${submitDetailsDto.applicationStatus=='BSBAPST001'}">
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Risk Level of the Biological Agent/Toxin" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="riskLevel" name="riskLevel" disabled="false" codeCategory="CATE_ID_BSB_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT" firstOption="Please Select" value="${mohProcessDto.riskLevel}"></iais:select>
                                                                                    <span data-err-ind="riskLevel" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <div id="commentFalse"><iais:field value="Comments on Risk Level Assessment" required="false" width="12"/></div>
                                                                                <div id="commentTrue"><iais:field value="Comments on Risk Level Assessment" required="true" width="12"/></div>
                                                                                <iais:value width="10">
                                                                                    <div class="input-group">
                                                                                        <div class="ax_default text_area">
                                                                                            <textarea name="commentsOnRiskLevelAssessment" cols="70" rows="7" maxlength="1000"><c:out value="${mohProcessDto.riskLevelComments}"></c:out></textarea>
                                                                                        </div>
                                                                                        <span data-err-ind="riskLevelComments" class="error-msg"></span>
                                                                                    </div>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Lentivirus Report" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <input type="text" autocomplete="off" name="lentivirusReport" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.lentivirusReportDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Internal Inspection Report" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <input type="text" autocomplete="off" name="internalInspectionReport" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.internalInspectionReportDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                    <span data-err-ind="internalInspectionReportDt" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Selected Approved Facility Certifier" required="false"></iais:field>
                                                                                <iais:value width="10"><p><iais:code code="${application.facility.selectedAfc}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity Start Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <input type="text" autocomplete="off" name="validityStartDate" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.validityStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                    <span data-err-ind="validityStartDt" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity End Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <input type="text" autocomplete="off" name="validityEndDate" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.validityEndDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                    <span data-err-ind="validityEndDt" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Final Remarks" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:select name="finalRemarks" disabled="false" codeCategory="CATE_ID_BSB_FINAL_REMARKS" firstOption="Please Select" value="${mohProcessDto.finalRemarks}"></iais:select>
                                                                                    <span data-err-ind="finalRemarks" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Processing Decision" required="true"/>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="processingDecision" name="processingDecision" disabled="false" codeCategory="CATE_ID_BSB_DO_PROCESSING_PROCESSING_DECISION" firstOption="Please Select" value="${mohProcessDto.processDecision}"></iais:select>
                                                                                    <span data-err-ind="processDecision" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Remarks" required="false" width="12"/>
                                                                                <iais:value width="10">
                                                                                    <div class="input-group">
                                                                                        <div class="ax_default text_area">
                                                                                            <textarea name="remarks" cols="70" rows="7" maxlength="300"><c:out value="${mohProcessDto.remarks}"></c:out></textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    </c:if>
                                                                    <%--<c:if test="${submitDetailsDto.processType=='PROTYPE001' && submitDetailsDto.applicationStatus=='BSBAPST002'}">
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="DO Remarks" required="false"></iais:field>
                                                                                <iais:value width="10"><p>${submitDetailsDto.applicationMisc.remarks}</p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Final Remarks" required="false"></iais:field>
                                                                                <iais:value width="10"><p><iais:code code="${submitDetailsDto.applicationMisc.finalRemarks}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Risk Level of the Biological Agent/Toxin" required="false"></iais:field>
                                                                                <iais:value width="10"><p><iais:code code="${application.facility.riskLevel}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Comments on Risk Level Assessment" required="false" width="12"/>
                                                                                <iais:value width="10"><p>${application.facility.riskLevelComments}</p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="ERP Report" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="erpReport" dateVal="${mohProcessDto.erpReportDt}"></iais:datePicker>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_erpReportDt"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Red Teaming Report" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="redTeamingReport" dateVal="${mohProcessDto.redTeamingReportDt}"></iais:datePicker>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_redTeamingReportDt"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Lentivirus Report" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="lentivirusReport" dateVal="${mohProcessDto.lentivirusReportDt}"></iais:datePicker>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Internal Inspection Report" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="internalInspectionReport" dateVal="${mohProcessDto.internalInspectionReportDt}"></iais:datePicker>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_internalInspectionReportDt"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Selected Approved Facility Certifier" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="selectedApprovedFacilityCertifier" name="selectedApprovedFacilityCertifier" disabled="false" codeCategory="CATE_ID_BSB_SELECTED_APPROVED_FACILITY_CERTIFER" firstOption="Please Select" value="${mohProcessDto.selectedAfc}"></iais:select>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity Start Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="validityStartDate" dateVal="${mohProcessDto.validityStartDt}"></iais:datePicker>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_validityStartDt"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity End Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="validityEndDate"  dateVal="${mohProcessDto.validityEndDt}"></iais:datePicker>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_validityEndDt"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Reviewing Decision" required="true"/>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="processingDecision" name="processingDecision" disabled="false" codeCategory="CATE_ID_BSB_AO_SCREENING_PROCESSING_DECISION" firstOption="Please Select" value="${mohProcessDto.processDecision}"></iais:select>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_processDecision"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Final Remarks" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:select name="finalRemarks" disabled="false" codeCategory="CATE_ID_BSB_FINAL_REMARKS" firstOption="Please Select" value="${mohProcessDto.finalRemarks}"></iais:select>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_finalRemarks"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="AO Remarks" required="false" width="12"/>
                                                                                <iais:value width="10">
                                                                                    <div class="input-group">
                                                                                        <div class="ax_default text_area">
                                                                                            <textarea name="remarks" cols="70" rows="7" maxlength="500"><c:out value="${mohProcessDto.remarks}"></c:out></textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    </c:if>--%>
                                                                    <c:if test="${submitDetailsDto.applicationStatus=='BSBAPST002'}">
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="DO Remarks" required="false"></iais:field>
                                                                                <iais:value width="10"><p>${submitDetailsDto.applicationMisc.remarks}</p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Final Remarks" required="false"></iais:field>
                                                                                <iais:value width="10"><p><iais:code code="${submitDetailsDto.applicationMisc.finalRemarks}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Risk Level of the Biological Agent/Toxin" required="false"></iais:field>
                                                                                <iais:value width="10"><p><iais:code code="${submitDetailsDto.applicationMisc.riskLevel}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Comments on Risk Level Assessment" required="false" width="12"/>
                                                                                <iais:value width="10"><p>${submitDetailsDto.applicationMisc.riskLevelComments}</p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="DO Recommendation" required="false" width="12"/>
                                                                                <iais:value width="10"><p></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Lentivirus Report" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <input type="text" autocomplete="off" name="lentivirusReport" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.lentivirusReportDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Internal Inspection Report" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <input type="text" autocomplete="off" name="internalInspectionReport" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.internalInspectionReportDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                    <span data-err-ind="internalInspectionReportDt" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Selected Approved Facility Certifier" required="false"></iais:field>
                                                                                <iais:value width="10"><p><iais:code code="${submitDetailsDto.applicationMisc.selectedAfc}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity Start Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <input type="text" autocomplete="off" name="validityStartDate" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.validityStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                    <span data-err-ind="validityStartDt" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity End Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <input type="text" autocomplete="off" name="validityEndDate" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.validityEndDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                    <span data-err-ind="validityEndDt" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Reviewing Decision" required="true"/>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="processingDecision" name="processingDecision" disabled="false" codeCategory="CATE_ID_BSB_AO_PROCESSING_PROCESSING_DECISION" firstOption="Please Select" value="${mohProcessDto.processDecision}"></iais:select>
                                                                                    <span data-err-ind="processDecision" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Final Remarks" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:select name="finalRemarks" disabled="false" codeCategory="CATE_ID_BSB_FINAL_REMARKS" firstOption="Please Select" value="${mohProcessDto.finalRemarks}"></iais:select>
                                                                                    <span data-err-ind="finalRemarks" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="AO Remarks" required="false" width="12"/>
                                                                                <iais:value width="10">
                                                                                    <div class="input-group">
                                                                                        <div class="ax_default text_area">
                                                                                            <textarea name="remarks" cols="70" rows="7" maxlength="500"><c:out value="${mohProcessDto.remarks}"></c:out></textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    </c:if>
                                                                    <c:if test="${submitDetailsDto.applicationStatus=='BSBAPST003'}">
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Risk Level of the Biological Agent/Toxin" required="false"></iais:field>
                                                                                <iais:value width="10"><p><iais:code code="${application.facility.riskLevel}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Comments on Risk Level Assessment" required="false" width="12"/>
                                                                                <iais:value width="10"><p>${application.facility.riskLevelComments}</p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="DO Recommendation" required="false" width="12"/>
                                                                                <iais:value width="10"><p></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="AO Review Decision" required="false"/>
                                                                                <iais:value width="10"><p><iais:code code="${application.status}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <c:if test="${submitDetailsDto.applicationMisc.finalRemarks=='FR001'}">
                                                                            <div>
                                                                                <iais:row>
                                                                                    <iais:field value="AO Remarks" required="false" width="12"/>
                                                                                    <iais:value width="10"><p>${submitDetailsDto.applicationMisc.remarks}</p></iais:value>
                                                                                </iais:row>
                                                                            </div>
                                                                            <div>
                                                                                <iais:row>
                                                                                    <iais:field value="Final Remarks" required="false"></iais:field>
                                                                                    <iais:value width="10"><p><iais:code code="${submitDetailsDto.applicationMisc.finalRemarks}"></iais:code></p></iais:value>
                                                                                </iais:row>
                                                                            </div>
                                                                        </c:if>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity Start Date" required="false"></iais:field>
                                                                                <iais:value width="10"><p><fmt:formatDate value='${application.facility.validityStartDt}' pattern='dd/MM/yyyy'/></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity End Date" required="false"></iais:field>
                                                                                <iais:value width="10"><p><fmt:formatDate value='${application.facility.validityEndDt}' pattern='dd/MM/yyyy'/></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="HM Remarks" required="false" width="12"/>
                                                                                <iais:value width="10">
                                                                                    <div class="input-group">
                                                                                        <div class="ax_default text_area">
                                                                                            <textarea name="remarks" cols="70" rows="7" maxlength="500"><c:out value="${mohProcessDto.remarks}"></c:out></textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Processing Decision" required="true"/>
                                                                                <iais:value width="10">
                                                                                    <span data-err-ind="processDecision" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    </c:if>
                                                                </iais:section>
                                                                <div align="right">
                                                                    <a style="float:left;padding-top: 1.1%;" class="back" href="#" id="processBack"><em class="fa fa-angle-left"></em> Back</a>
                                                                    <%--<c:if test="${(submitDetailsDto.processType=='PROTYPE002' || submitDetailsDto.processType=='PROTYPE003' || submitDetailsDto.processType=='PROTYPE004' || submitDetailsDto.processType=='PROTYPE005') && submitDetailsDto.applicationStatus=='BSBAPST003'}">
                                                                        <button name="finalAssessmentBtn" id="finalAssessmentButton" type="button" class="btn btn-primary">Final Assessment</button>
                                                                    </c:if>--%>
                                                                    <button name="submitButton" id="submitButton" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                                <%@include file="/WEB-INF/jsp/iais/process/processingHistory.jsp"%>
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
</div>
<%@include file="/WEB-INF/jsp/iais/doDocument/uploadFile.jsp" %>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>