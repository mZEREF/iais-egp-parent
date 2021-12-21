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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
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
                                                    <a href="#tabDynamicContent" aria-controls="tabDynamicContent" role="tab" data-toggle="tab">Dynamic Content</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
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
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Remarks" required="false" width="12"/>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea name="remarks" cols="70" rows="7" maxlength="300"><c:out value="${mohProcessDto.remarks}"></c:out></textarea>
                                                                                        <span data-err-ind="remarks" class="error-msg"></span>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Risk Level of the Biological Agent/Toxin" required="true"></iais:field>
                                                                            <iais:value width="10">
                                                                                <iais:select id="riskLevel" name="riskLevel" codeCategory="CATE_ID_BSB_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT" firstOption="Please Select" value="${mohProcessDto.riskLevel}"></iais:select>
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
                                                                                        <textarea name="riskLevelComments" cols="70" rows="7" maxlength="1000"><c:out value="${mohProcessDto.riskLevelComments}"></c:out></textarea>
                                                                                        <span data-err-ind="riskLevelComments" class="error-msg" ></span>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Processing Decision" required="true"/>
                                                                            <iais:value width="10">
                                                                                <select name="processingDecision" id="processingDecision">
                                                                                    <option value="">Please Select</option>
                                                                                    <option value="MOHPRO001" <c:if test="${mohProcessDto.processingDecision eq 'MOHPRO001'}">selected="selected"</c:if>>screened by do</option>
                                                                                    <option value="MOHPRO002" <c:if test="${mohProcessDto.processingDecision eq 'MOHPRO002'}">selected="selected"</c:if>>request for information</option>
                                                                                    <option value="MOHPRO003" <c:if test="${mohProcessDto.processingDecision eq 'MOHPRO003'}">selected="selected"</c:if>>reject</option>
                                                                                </select>
                                                                                <span data-err-ind="processingDecision" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="ERP Report" required="false"></iais:field>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="erpReportDt" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.erpReportDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="erpReportDt" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Red Teaming Report" required="false"></iais:field>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="redTeamingReportDt" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.redTeamingReportDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="redTeamingReportDt" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Lentivirus Report" required="false"></iais:field>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="lentivirusReportDt" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.lentivirusReportDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="lentivirusReportDt" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Internal Inspection Report" required="false"></iais:field>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="internalInspectionReportDt" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.internalInspectionReportDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="internalInspectionReportDt" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Selected Approved Facility Certifier" required="false"></iais:field>
                                                                            <iais:value width="10">
                                                                                <iais:select id="selectedApprovedFacilityCertifier" name="selectedAfc" codeCategory="CATE_ID_BSB_SELECTED_APPROVED_FACILITY_CERTIFER" firstOption="Please Select" value="${mohProcessDto.selectedAfc}"></iais:select>
                                                                                <span data-err-ind="selectedAfc" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Validity Start Date" required="true"></iais:field>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="validityStartDt" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.validityStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="validityStartDt" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Validity End Date" required="true"></iais:field>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="validityEndDt" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.validityEndDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="validityEndDt" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <div align="right">
                                                                    <button name="submitButton" id="submitButton" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
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