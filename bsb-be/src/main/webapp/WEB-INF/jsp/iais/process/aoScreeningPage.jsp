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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-process.js"></script>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="action_type" value="">
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
                                                <%@include file="/WEB-INF/jsp/iais/process/common/applicationInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/process/common/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabDynamicContent" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/process/common/dynamicContent.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabScreening" role="tabpanel">
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
                                                                    <c:if test="${applicationInfo.processType=='PROTYPE001'}">
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="DO Remarks" required="false"></iais:field>
                                                                                <iais:value width="10"><p>${applicationMisc.remarks}</p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Risk Level of the Biological Agent/Toxin" required="false"></iais:field>
                                                                                <iais:value width="10"><p><iais:code code="${applicationInfo.facility.riskLevel}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Comments on Risk Level Assessment" required="false" width="12"/>
                                                                                <iais:value width="10"><p>${applicationInfo.facility.riskLevelComments}</p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="ERP Report" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="erpReport"></iais:datePicker>
                                                                                    <span class="error-msg" name="errorMsg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Red Teaming Report" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="redTeamingReport"></iais:datePicker>
                                                                                    <span class="error-msg" name="errorMsg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Lentivirus Report" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="lentivirusReport"></iais:datePicker>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Internal Inspection Report" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="internalInspectionReport"></iais:datePicker>
                                                                                    <span class="error-msg" name="errorMsg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Selected Approved Facility Certifier" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="selectedApprovedFacilityCertifier" name="selectedApprovedFacilityCertifier" disabled="false" codeCategory="CATE_ID_BSB_SELECTED_APPROVED_FACILITY_CERTIFER" firstOption="Please Select"></iais:select>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity Start Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="validityStartDate"></iais:datePicker>
                                                                                    <span class="error-msg" name="errorMsg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity End Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="validityEndDate"></iais:datePicker>
                                                                                    <span class="error-msg" name="errorMsg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Final Remarks" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Reviewing Decision" required="true"/>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="processingDecision" name="processingDecision" disabled="false" codeCategory="CATE_ID_BSB_AO_SCREENING_PROCESSING_DECISION" firstOption="Please Select"></iais:select>
                                                                                    <span data-err-ind="searchProcessingDecision" id="error_processingDecision" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="AO Remarks" required="false" width="12"/>
                                                                                <iais:value width="10">
                                                                                    <div class="input-group">
                                                                                        <div class="ax_default text_area">
                                                                                            <textarea name="remarks" cols="70" rows="7" maxlength="500"></textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    </c:if>
                                                                    <c:if test="${(applicationInfo.processType=='PROTYPE002' || applicationInfo.processType=='PROTYPE003' || applicationInfo.processType=='PROTYPE004' || applicationInfo.processType=='PROTYPE005')}">
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="DO Remarks" required="false"></iais:field>
                                                                                <iais:value width="10"><p>${applicationMisc.remarks}</p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Risk Level of the Biological Agent/Toxin" required="false"></iais:field>
                                                                                <iais:value width="10"><p><iais:code code="${applicationInfo.facility.riskLevel}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Comments on Risk Level Assessment" required="false" width="12"/>
                                                                                <iais:value width="10"><p>${applicationInfo.facility.riskLevelComments}</p></iais:value>
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
                                                                                    <iais:datePicker name="lentivirusReport"></iais:datePicker>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Internal Inspection Report" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="internalInspectionReport"></iais:datePicker>
                                                                                    <span class="error-msg" name="errorMsg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Selected Approved Facility Certifier" required="false"></iais:field>
                                                                                <iais:value width="10"><p><iais:code code="${applicationInfo.facility.selectedAfc}"></iais:code></p></iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity Start Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="validityStartDate"></iais:datePicker>
                                                                                    <span class="error-msg" name="errorMsg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Validity End Date" required="true"></iais:field>
                                                                                <iais:value width="10">
                                                                                    <iais:datePicker name="validityEndDate"></iais:datePicker>
                                                                                    <span class="error-msg" name="errorMsg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Final Remarks" required="false"></iais:field>
                                                                                <iais:value width="10">
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Reviewing Decision" required="true"/>
                                                                                <iais:value width="10">
                                                                                    <iais:select id="processingDecision" name="processingDecision" disabled="false" codeCategory="CATE_ID_BSB_AO_PROCESSING_PROCESSING_DECISION" firstOption="Please Select"></iais:select>
                                                                                    <span data-err-ind="searchProcessingDecision" id="error_processingDecision" class="error-msg"></span>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="AO Remarks" required="false" width="12"/>
                                                                                <iais:value width="10">
                                                                                    <div class="input-group">
                                                                                        <div class="ax_default text_area">
                                                                                            <textarea name="remarks" cols="70" rows="7" maxlength="500"></textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    </c:if>
                                                                </iais:section>
                                                                <div align="right">
                                                                    <button name="clearBtn" id="clearButton" type="button" class="btn btn-secondary">Clear</button>
                                                                    <button name="submitBtn" id="aoScreeningSubmitButton" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                                <%@include file="/WEB-INF/jsp/iais/process/common/processingHistory.jsp"%>
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
<%@include file="/WEB-INF/jsp/iais/process/common/uploadFile.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>