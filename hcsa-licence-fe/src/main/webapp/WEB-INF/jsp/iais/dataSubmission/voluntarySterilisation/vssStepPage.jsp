<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<webui:setLayout name="iais-internet"/>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process2 =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/vssHeader.jsp" %>

<c:set var="canEdit" value="${VSS_CURRENT_STEP.showEdit}" scope="request"/>

<form method="post" id="mainForm" action="<%=process2.runtime.continueURL()%>">
    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane in active">
                                <c:set var="currCode" value="${VSS_CURRENT_STEP.code}" scope="request"/>
                               <%-- ------------------${currCode}----------------%>
                                <c:choose>
                                    <c:when test="${currCode == 'VSST001'}">
                                        <%@ include file="section/treatmentDetails.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'VSST002'}">
                                        <%@ include file="section/particularsConsentOrCourtOrder.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'VSST003'}">
                                        <%@ include file="section/particularsSterilizationSection.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'VSST010'}">
                                        <%@ include file="section/previewTreatmentDetails.jsp" %>
                                        <%@ include file="section/previewParticularsSterilizationSection.jsp" %>
                                        <%@ include file="section/previewParticularsConsentOrCourtOrder.jsp" %>
                                    </c:when>
                                </c:choose>
                                <%@ include file="common/vssFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>