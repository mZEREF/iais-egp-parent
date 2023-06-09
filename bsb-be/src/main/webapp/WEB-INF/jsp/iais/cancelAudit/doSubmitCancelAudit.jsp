<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<div class="main-content">
    <form class="form-horizontal" id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content" id="clearSelect">
                        <div class="bg-title">
                            <h2>Cancel Audit</h2>
                        </div>
                        <%--@elvariable id="cancelAuditDto" type="sg.gov.moh.iais.egp.bsb.dto.audit.CancelAuditDto"--%>
                        <c:forEach var="item" items="${cancelAuditDto.auditDtos}" varStatus="status">
                            <section id="conSection--v--${status.index}">
                                <c:if test="${cancelAuditDto.auditDtos.size() > 1}">
                                    <div class="form-group">
                                        <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Facility ${status.index + 1}</h3>
                                    </div>
                                </c:if>
                                <iais:row>
                                    <iais:field value="Facility Name" width="15" required="false"/>
                                    <iais:value width="10">${item.facName}</iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Activity Type" width="15" required="false"/>
                                    <iais:value width="10"><c:out value="${item.activityType}"/></iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Audit Date" width="15" required="false"/>
                                    <iais:value width="10"><fmt:formatDate value="${item.auditDate}" pattern="dd/MM/yyyy"/></iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Audit Type" width="15" required="false"/>
                                    <iais:value width="10"><iais:code code="${item.auditType}"/></iais:value>
                                </iais:row>
                            </section>
                        </c:forEach>
                        <iais:row>
                            <iais:field value="Cancellation Reasons" width="15" required="true"/>
                            <iais:value width="10">
                                <textarea id="cancelReason"
                                          name="cancelReason"
                                          cols="70"
                                          rows="4"
                                          maxlength="300">${cancelReason}</textarea>
                                <span data-err-ind="cancelReason" class="error-msg"></span>
                            </iais:value>
                        </iais:row>
                        <div class="row">
                            <div class="col-xs-12 col-sm-6"><a class="back" href="/bsb-web/eservicecontinue/INTRANET/AuditCancellationList"><em class="fa fa-angle-left"></em> Back</a></div>
                            <div style="text-align: right">
                                <button name="nextBtn" id="nextBtn" type="button" class="btn btn-primary">Submit</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>