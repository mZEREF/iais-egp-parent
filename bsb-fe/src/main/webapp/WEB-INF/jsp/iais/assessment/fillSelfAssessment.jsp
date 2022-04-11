<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-assessment.js"></script>


<%@include file="dashboard.jsp" %>


<div class="main-content">
    <div class="container">
        <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="action_type" value="">
            <input type="hidden" name="action_value" value="">
            <input type="hidden" name="action_additional" value="">
            <span class="error-msg"><c:out value="${error_message}"/></span>
            <div class="dashboard-gp" style="margin-top: 30px">
                <c:forEach var="checklistConfigDto" items="${checklistConfigDtos}" varStatus="status">
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile" id="myBody">
                            <a data-tab="#tabInbox"
                               onclick="javascript:switchNextStep('config-${status.index}');">
                                <c:choose>
                                    <c:when test="${checklistConfigDto.common eq true}">
                                        <p class="dashboard-txt">General Regulation</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="dashboard-txt">BSB Regulation</p>
                                    </c:otherwise>
                                </c:choose>
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div>
                <%@include file="../chklst/checkListAnswer.jsp" %>
            </div>

            <div>
                <div class="alignctr" style="text-align: left">
                    <a id="back" href="javascript:void(0)"><em class="fa fa-angle-left"> </em> Back</a>
                </div>
                <c:if test="${cur_action eq SelfAssessmentConstants.ACTION_FILL or
                            cur_action eq SelfAssessmentConstants.ACTION_EDIT}">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-secondary" id="clear" href="javascript:void(0);">Clear</a>
                        <a class="btn btn-primary" id="save" href="javascript:void(0);">Submit</a>
                    </div>
                </c:if>
            </div>
        </form>
    </div>
</div>