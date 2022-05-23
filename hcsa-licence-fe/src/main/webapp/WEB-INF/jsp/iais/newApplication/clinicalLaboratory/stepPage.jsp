<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
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
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/businessContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST008'}">
                                                            <div class="vehicles">
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/vehiclesContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST009'}">
                                                            <div class="clinical-director">
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/clinicalDirectorContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST001'}">
                                                            <div class="laboratory-disciplines">
                                                                <p class="svc-title">${currStepName}</p>
                                                                <c:choose>
                                                                    <c:when test="${'RDS' ==currentSvcCode}">
                                                                        <p><iais:message key="NEW_ACK027"/></p>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <p><iais:message key="NEW_ACK022"/></p>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/laboratoryDisciplines.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST002'}">
                                                            <div class="clinical-governance-officer">
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/governanceOfficers.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST013'}">
                                                            <div class="section-leader">
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/sectionLeader.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST003'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/disciplineAllocation.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST010'}">
                                                            <div class="charges">
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/chargesContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST006'}">
                                                            <div class="clinical-governance-officer">
                                                                <!--start -->
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/servicePersonnel.jsp" />
                                                                <!--end -->
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST004'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/principalOfficers.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST014'}">
                                                            <div class="key-appointment-holder">
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/keyAppointmentHolder.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST007'}">
                                                            <div class="med-alert-person">
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/medAlertContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST005'}">
                                                            <div class="Service-related-Documents document-upload-gp">
                                                                <h2>Service-related Documents</h2>
                                                                <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/CR_Doc.jsp"/>
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
<input type="hidden" name="errorMapIs" id="errorMapIs" value="${errormapIs}">
