<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../dashboard.jsp" %>
<form method="post" id="mainForm" class="__egovform" ${currentStep == 'SVST005' ? 'enctype="multipart/form-data"' : '' } action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="../navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="serviceInformationTab" role="tabpanel">
                                <div class="multiservice">
                                    <div class="tab-gp side-tab clearfix">
                                        <%@ include file="../formTabs.jsp" %>
                                        <div class="tab-content" ${(empty hcsaServiceDtoList || hcsaServiceDtoList.size() <= 1) ?
                                        'style="width:100%;"' : ''}>
                                            <div class="tab-pane active" id="clinicalLab" role="tabpanel">
                                                <%@ include file="step.jsp" %>
                                                <div class="application-service-steps">
                                                    <c:choose>
                                                        <c:when test="${currentStep == 'SVST012'}">
                                                            <div class="business">
                                                                <%@include file="businessContent.jsp"%>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST008'}">
                                                            <div class="vehicles">
                                                                <%@include file="vehiclesContent.jsp"%>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST009'}">
                                                            <div class="clinical-director">
                                                                <%@include file="clinicalDirectorContent.jsp"%>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST001'}">
                                                            <div class="laboratory-disciplines">
                                                                <p style="font-weight: 600;font-size: 2.2rem">${currStepName}</p>
                                                                <c:choose>
                                                                    <c:when test="${'RDS' ==currentSvcCode}">
                                                                        <p><iais:message key="NEW_ACK027"/></p>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <p><iais:message key="NEW_ACK022"/></p>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <%@include file="laboratoryDisciplines.jsp"%>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST002'}">
                                                            <div class="clinical-governance-officer">
                                                                <%@include file="governanceOfficers.jsp"%>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST013'}">
                                                            <div class="section-leader">
                                                                <%@include file="sectionLeader.jsp"%>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST003'}">
                                                            <%@include file="disciplineAllocation.jsp"%>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST010'}">
                                                            <div class="charges">
                                                                <%@include file="chargesContent.jsp"%>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST006'}">
                                                            <div class="clinical-governance-officer">
                                                                <!--start -->
                                                                <%@include file="servicePersonnel.jsp" %>
                                                                <!--end -->
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST004'}">
                                                            <%@include file="principalOfficers.jsp"%>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST014'}">
                                                            <div class="key-appointment-holder">
                                                                <%@include file="keyAppointmentHolder.jsp"%>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST007'}">
                                                            <div class="med-alert-person">
                                                                <%@include file="medAlertContent.jsp"%>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST005'}">
                                                            <div class="Service-related-Documents">
                                                                <h2>Service-related Documents</h2>
                                                                <%@include file="CR_Doc.jsp"%>
                                                            </div>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%@ include file="next.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>

