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

<%@ include file="common/topHeader.jsp" %>

<c:set var="canEdit" value="${TOP_CURRENT_STEP.showEdit}" scope="request"/>

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
                                <c:set var="currCode" value="${TOP_CURRENT_STEP.code}" scope="request"/>
                                <%--------------------${currCode}----------------%>
                                <c:choose>
                                    <c:when test="${currCode == 'TOPT001'}">
                                        <%@ include file="section/patientDetails.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'TOPT002'}">
                                        <%@ include file="section/familyPlanning.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'TOPT003'}">
                                        <%@ include file="section/preTerminationOfPregnancyCounselling.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'TOPT004'}">
                                        <%@ include file="section/presentTerminationOfPregnancy.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'TOPT005'}">
                                        <%@ include file="section/postTerminationOfPregnancyCounselling.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'TOPT010'}">
                                        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                            <%@ include file="section/previewPatientDetails.jsp" %>
                                            <%@ include file="section/previewFamilyPlanning.jsp" %>
                                            <%@ include file="section/previewPreTermination.jsp" %>
                                            <%@ include file="section/previewPresentTermination.jsp" %>
                                            <%@ include file="section/previewPostTermination.jsp" %>
                                        </div>
                                    </c:when>
                                </c:choose>
                                <%@ include file="common/topFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>