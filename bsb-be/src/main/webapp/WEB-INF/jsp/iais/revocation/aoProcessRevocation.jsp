<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-revocation.js"></script>
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
                                                <a href="#tabInfo"
                                                   aria-controls="tabInfo"
                                                   role="tab"
                                                   data-toggle="tab">Info</a>
                                            </li>
                                            <li class="complete" id="document" role="presentation">
                                                <a href="#tabDocuments"
                                                   aria-controls="tabDocuments" role="tab"
                                                   data-toggle="tab">Documents</a></li>
                                            <li class="incomplete" id="process" role="presentation">
                                                <a href="#tabProcessing"
                                                   aria-controls="tabProcessing" role="tab"
                                                   data-toggle="tab">Processing</a></li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo"
                                                                             role="tab"
                                                                             data-toggle="tab">Info</a></div>
                                                <div class="swiper-slide"><a href="#tabDocuments" id="doDocument"
                                                                             aria-controls="tabDocuments"
                                                                             role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide"><a href="#tabProcessing" id="doProcess"
                                                                             aria-controls="tabProcessing"
                                                                             role="tab" data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="revocationDetailInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="../doDocument/tabDocuments.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                                <span id="error_document" name="iaisErrorMsg" class="error-msg"></span>
                                                <br/><br/>
                                                <div class="alert alert-info" role="alert">
                                                    <strong>
                                                        <h4>Processing Status Update</h4>
                                                    </strong>
                                                </div>
                                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                                    <input type="hidden" name="sopEngineTabRef"
                                                           value="<%=process.rtStatus.getTabRef()%>">
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp">
                                                                <iais:section title="">
                                                                    <c:forEach var="item" items="${revocationDetail}">
                                                                        <input name="applicationId" id="applicationId"
                                                                               value="${item.id}" hidden>
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Current Status"
                                                                                            required="false"/>
                                                                                <iais:value width="10">
                                                                                    <p><iais:code
                                                                                            code="${item.facility.facilityStatus}"/></p>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    </c:forEach>
                                                                    <div>
                                                                        <iais:row>
                                                                            <div id="ReasonFalse"><iais:field
                                                                                    value="Reason for Revocation"
                                                                                    required="false"
                                                                                    width="12"/></div>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea id="ReasonId"
                                                                                                  name="reason"
                                                                                                  cols="70"
                                                                                                  rows="7"
                                                                                                  maxlength="500"><c:forEach var="miscList" items="${applicationMiscList}" varStatus="status"><c:choose><c:when test="${status.last}"><iais:code code="${miscList.reason}"/>:${miscList.reasonContent}</c:when><c:otherwise><iais:code code="${miscList.reason}"/>:${miscList.reasonContent};</c:otherwise></c:choose></c:forEach></textarea>
                                                                                        <span id="error_reason"
                                                                                              name="iaisErrorMsg"
                                                                                              class="error-msg"></span>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>

                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="DO Remarks" required="false"/>
                                                                            <iais:value width="10">
                                                                                <p>
                                                                                    <c:forEach var="miscs" items="${applicationMiscList}" varStatus="status">
                                                                                        <c:choose>
                                                                                            <c:when test="${status.last}">
                                                                                                ${miscs.remarks}
                                                                                            </c:when>
                                                                                            <c:otherwise>
                                                                                                ${miscs.remarks};
                                                                                            </c:otherwise>
                                                                                        </c:choose>
                                                                                    </c:forEach>
                                                                                </p>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>

                                                                    <div>
                                                                        <iais:row>
                                                                            <div id="RemarksFalse"><iais:field
                                                                                    value="AO Remarks"
                                                                                    required="false"
                                                                                    width="12"/></div>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea id="AORemarks"
                                                                                                  name="AORemarks"
                                                                                                  cols="70"
                                                                                                  rows="7"
                                                                                                  maxlength="500"></textarea>
                                                                                        <span id="error_AORemarks"
                                                                                              name="iaisErrorMsg"
                                                                                              class="error-msg"></span>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="processingDecision">
                                                                        <iais:row>
                                                                            <iais:field value="Processing Decision"
                                                                                        required="true"/>
                                                                            <iais:value width="10">
                                                                                <iais:select name="decision"
                                                                                             id="decision"
                                                                                             codeCategory="CATE_ID_BSB_PROCESSING_DECISION"
                                                                                             firstOption="Please Select"/>
                                                                                <span id="error_decision"
                                                                                      name="iaisErrorMsg"
                                                                                      class="error-msg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <a style="float:left;padding-top: 1.1%;" class="back"
                                                                   id="backToTask" href="#"><em
                                                                        class="fa fa-angle-left"></em> Back</a>
                                                                <div align="right">
                                                                    <button name="submitBtn" id="submitButton"
                                                                            type="button" class="btn btn-primary">
                                                                        Submit
                                                                    </button>
                                                                </div>
                                                                <div>&nbsp;</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                                <%@include file="/WEB-INF/jsp/iais/revocation/processHistory.jsp" %>
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
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<%@include file="../doDocument/uploadFile.jsp" %>
