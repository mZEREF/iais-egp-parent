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
                                                <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp"%>
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
                                                                            <iais:field value="Risk Level of the Biological Agent/Toxin" required="false"></iais:field>
                                                                            <iais:value width="10"><p><iais:code code="${hmScreeningDto.riskLevel}"></iais:code></p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Comments on Risk Level Assessment" required="false"></iais:field>
                                                                            <iais:value width="10"><p>${hmScreeningDto.riskLevelComments}</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="DO Recommendation" required="false"></iais:field>
                                                                            <iais:value width="10"><p><iais:code code="${hmScreeningDto.doRecommendation}"></iais:code></p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="AO Review Decision" required="false"></iais:field>
                                                                            <iais:value width="10"><p><iais:code code="${hmScreeningDto.aoReviewDecision}"></iais:code></p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="AO Remarks" required="false"></iais:field>
                                                                            <iais:value width="10"><p>${hmScreeningDto.aoRemarks}</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Remarks" required="false" width="12"/>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea name="remarks" cols="70" rows="7" maxlength="500"><c:out value="${hmScreeningDto.hmRemarks}"></c:out></textarea>
                                                                                        <span data-err-ind="remarks" class="error-msg"></span>
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
                                                                                    <option value="MOHPRO007" <c:if test="${hmScreeningDto.processingDecision eq 'MOHPRO007'}">selected="selected"</c:if>>Approve</option>
                                                                                    <option value="MOHPRO003" <c:if test="${hmScreeningDto.processingDecision eq 'MOHPRO003'}">selected="selected"</c:if>>Reject</option>
                                                                                </select>
                                                                                <span data-err-ind="processingDecision" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Validity Start Date" required="false"></iais:field>
                                                                            <iais:value width="10"><p>${hmScreeningDto.validityStartDate}</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Validity End Date" required="false"></iais:field>
                                                                            <iais:value width="10"><p>${hmScreeningDto.validityEndDate}</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <div style="text-align: right">
                                                                    <button name="finalAssessmentBtn" id="finalAssessmentButton" type="button" class="btn btn-primary">Final Assessment</button>
                                                                    <button name="submitButton" id="submitButton" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                                <%@include file="/WEB-INF/jsp/iais/common/processHistory.jsp" %>
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
<%@include file="/WEB-INF/jsp/iais/doDocument/internalFileUploadModal.jsp"%>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>