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
        <input type="hidden" name="ifProcess" id="ifProcess" value="${mohProcessPageValidation}">
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
                                                                            <iais:field value="DO Remarks" required="false"></iais:field>
                                                                            <iais:value width="10"><p>${mohProcessDto.aoProcessingDto.doRemarks}</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Risk Level of the Biological Agent/Toxin" required="false"></iais:field>
                                                                            <iais:value width="10"><p><iais:code code="${mohProcessDto.aoProcessingDto.riskLevel}"></iais:code></p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Comments on Risk Level Assessment" required="false"></iais:field>
                                                                            <iais:value width="10"><p>${mohProcessDto.aoProcessingDto.riskLevelComments}</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="DO Recommendation" required="false"></iais:field>
                                                                            <iais:value width="10"><p><iais:code code="${mohProcessDto.aoProcessingDto.doRecommendation}"></iais:code></p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Remarks" required="false" width="12"/>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea name="aoRemarks" cols="70" rows="7" maxlength="500"><c:out value="${mohProcessDto.aoProcessingDto.aoRemarks}"></c:out></textarea>
                                                                                        <span data-err-ind="aoRemarks" class="error-msg"></span>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Reviewing Decision" required="true"/>
                                                                            <iais:value width="10">
                                                                                <select name="reviewingDecision" id="reviewingDecision">
                                                                                    <option value="">Please Select</option>
                                                                                    <option value="MOHPRO007" <c:if test="${mohProcessDto.aoProcessingDto.reviewingDecision eq 'MOHPRO007'}">selected="selected"</c:if>>Approved</option>
                                                                                    <option value="MOHPRO003" <c:if test="${mohProcessDto.aoProcessingDto.reviewingDecision eq 'MOHPRO003'}">selected="selected"</c:if>>Rejected</option>
                                                                                    <option value="MOHPRO008" <c:if test="${mohProcessDto.aoProcessingDto.reviewingDecision eq 'MOHPRO008'}">selected="selected"</c:if>>Route Back to Duty Officer</option>
                                                                                    <option value="MOHPRO009" <c:if test="${mohProcessDto.aoProcessingDto.reviewingDecision eq 'MOHPRO009'}">selected="selected"</c:if>>Route to Higher Management</option>
                                                                                </select>
                                                                                <span data-err-ind="reviewingDecision" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Lentivirus Report" required="false"></iais:field>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="lentivirusReportDate" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.aoProcessingDto.lentivirusReportDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="lentivirusReportDate" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Internal Inspection Report" required="true"></iais:field>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="internalInspectionReportDate" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.aoProcessingDto.internalInspectionReportDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="internalInspectionReportDate" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Selected Approved Facility Certifier" required="false"></iais:field>
                                                                            <iais:value width="10"><p><iais:code code="${mohProcessDto.aoProcessingDto.selectedAfc}"></iais:code></p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Validity Start Date" required="true"></iais:field>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="validityStartDate" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.aoProcessingDto.validityStartDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="validityStartDate" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Validity End Date" required="true"></iais:field>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="validityEndDate" data-date-start-date="01/01/1900" value="<c:out value="${mohProcessDto.aoProcessingDto.validityEndDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="validityEndDate" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Final Remarks" required="false"></iais:field>
                                                                            <iais:value width="10">
                                                                                <div class="form-check">
                                                                                    <input type="radio" class="form-check-input" name="finalRemarks" id="finalRemarksYes" <c:if test="${mohProcessDto.aoProcessingDto.finalRemarks eq 'yes'}">checked="checked"</c:if> value="yes"/>
                                                                                    <label for="finalRemarksYes" class="form-check-label"><span class="check-square"></span>Yes</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="radio" class="form-check-input" name="finalRemarks" id="finalRemarksNo" <c:if test="${mohProcessDto.aoProcessingDto.finalRemarks eq 'no'}">checked="checked"</c:if> value="no"/>
                                                                                    <label for="finalRemarksNo" class="form-check-label"><span class="check-square"></span>No</label>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <div style="text-align: right">
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