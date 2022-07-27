<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

%>
<webui:setLayout name="iais-internet"/>
<%@ include file="/WEB-INF/jsp/iais/common/dashboard.jsp" %>
<form method="post" id="mainForm" class="__egovform" ${currentStep == 'SVST005' ? 'enctype="multipart/form-data"' : '' } action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="/WEB-INF/jsp/iais/application/common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="serviceInformationTab" role="tabpanel">
                                <div class="multiservice">
                                    <div class="tab-gp side-tab clearfix">
                                        <%@ include file="../common/formTabs.jsp" %>
                                        <div class="tab-content" ${(empty hcsaServiceDtoList || hcsaServiceDtoList.size() <= 1) ?
                                        'style="width:100%;"' : ''}>
                                            <div class="tab-pane active" id="clinicalLab" role="tabpanel">
                                                <%@ include file="../common/step.jsp" %>
                                                <div class="application-service-steps">
                                                    <c:choose>
                                                        <c:when test="${currentStep == 'SVST012'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/businessInfo/businessContent.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST008'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/others/vehiclesContent.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST009'}">
                                                            <div class="clinical-director">
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/keyPersonnel/clinicalDirectorContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <%--<c:when test="${currentStep == 'SVST001'}">
                                                            <div class="laboratory-disciplines">
                                                                <p class="app-title">${currStepName}</p>
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
                                                        </c:when>--%>
                                                        <c:when test="${currentStep == 'SVST002'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/keyPersonnel/governanceOfficers.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST013'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/svcPersonnel/sectionLeader.jsp"/>
                                                        </c:when>
                                                        <%--<c:when test="${currentStep == 'SVST003'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/newApplication/clinicalLaboratory/disciplineAllocation.jsp"/>
                                                        </c:when>--%>
                                                        <c:when test="${currentStep == 'SVST010'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/others/chargesContent.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST006'}">
                                                            <!--start -->
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/svcPersonnel/servicePersonnel.jsp" />
                                                            <!--end -->
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST004'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/keyPersonnel/principalOfficers.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST014'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/keyPersonnel/keyAppointmentHolder.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST007'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/keyPersonnel/medAlertContent.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST015'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/otherInfo/otherInformation.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST016'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/supplementaryForm/supplementaryForm.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST017'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/specialServicesForm/specialServicesForm.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST005'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/document/document.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST018'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/outsourced/outsourcedProviders.jsp"/>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%@ include file="../common/next.jsp" %>
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
