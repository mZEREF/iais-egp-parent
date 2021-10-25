<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<div class="main-content">
    <form class="form-horizontal" id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content" id="clearSelect">
                        <div class="bg-title">
                            <h2>Cancel Audit</h2>
                        </div>
                        <iais:row>
                            <iais:field value="Facility Name" width="15" required="false"/>
                            <iais:value width="10">
                                <c:forEach var="item" items="${auditList}" varStatus="status">
                                    <c:choose>
                                        <c:when test="${status.last}">
                                            ${item.facility.facilityName}
                                        </c:when>
                                        <c:otherwise>
                                            ${item.facility.facilityName},
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Activity Type" width="15" required="false"/>
                            <iais:value width="10">
                                <c:forEach var="item" items="${auditList}">
                                    <c:forEach var="activity" items="${item.facility.facilityActivities}" varStatus="status">
                                        <c:choose>
                                            <c:when test="${status.last}">
                                                <iais:code code="${activity.activityType}"></iais:code>
                                            </c:when>
                                            <c:otherwise>
                                                <iais:code code="${activity.activityType}"></iais:code>,
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>;
                                    <br/>
                                </c:forEach>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Audit Date" width="15" required="false"/>
                            <iais:value width="10">
                                <c:forEach var="item" items="${auditList}" varStatus="status">
                                    <c:choose>
                                        <c:when test="${status.last}">
                                            <fmt:formatDate value="${item.auditDt}" pattern="dd/MM/yyyy"></fmt:formatDate>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatDate value="${item.auditDt}" pattern="dd/MM/yyyy"></fmt:formatDate>,
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Audit Type" width="15" required="false"/>
                            <iais:value width="10">
                                <c:forEach var="item" items="${auditList}" varStatus="status">
                                    <c:choose>
                                        <c:when test="${status.last}">
                                            <iais:code code="${item.auditType}"></iais:code>
                                        </c:when>
                                        <c:otherwise>
                                            <iais:code code="${item.auditType}"></iais:code>,
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Cancellation Reasons" width="15" required="true"/>
                            <iais:value width="10">
                                <textarea id="reasons"
                                          name="reason"
                                          cols="70"
                                          rows="4"
                                          maxlength="300"></textarea>
                                <span id="error_reasons"
                                      name="iaisErrorMsg"
                                      class="error-msg"></span>
                            </iais:value>
                        </iais:row>

                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                            <div align="right">
                                <button name="submitBtn3" id="submitCancelAudit" type="button" class="btn btn-primary">
                                    Submit
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</div>