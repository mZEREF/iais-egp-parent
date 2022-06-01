<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>

<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <form id="mainForm" method="post" ${currentStep == 'SVST005' ? 'enctype="multipart/form-data"' : '' } action="<%=process.runtime.continueURL()%>">
        <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
        <div class="main-content">
            <div class="row">
                <div class="center-content">
                    <div class="col-xs-12 intranet-content">
                        <div class="tab-gp steps-tab tab-be">
                            <%@ include file="/WEB-INF/jsp/iais/application/common/navTabs.jsp" %>
                            <div class="tab-content">
                                <div class="tab-pane in active" id="serviceInformationTab" role="tabpanel">
                                    <%--<div class="multiservice">--%>
                                    <div class="tab-gp side-tab clearfix">
                                        <%@ include file="../common/formTabs.jsp" %>
                                        <div class="tab-content" ${(empty hcsaServiceDtoList || hcsaServiceDtoList.size() <= 1) ?
                                                'style="width:100%;"' : ''}>
                                            <div class="tab-pane active" id="clinicalLab" role="tabpanel">
                                                <%@ include file="../common/step.jsp" %>
                                                <div class="application-service-steps">
                                                    <c:choose>
                                                        <c:when test="${currentStep == 'SVST012'}">
                                                            <div class="business">
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/businessContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST008'}">
                                                            <div class="vehicles">
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/vehiclesContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST009'}">
                                                            <div class="clinical-director">
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/clinicalDirectorContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST001'}">
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
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/laboratoryDisciplines.jsp" />
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST002'}">
                                                            <div class="clinical-governance-officer">
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/governanceOfficers.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST013'}">
                                                            <div class="section-leader">
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/sectionLeader.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST003'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/disciplineAllocation.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST010'}">
                                                            <div class="charges">
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/chargesContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST006'}">
                                                            <div class="clinical-governance-officer">
                                                                <!--start -->
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/servicePersonnel.jsp" />
                                                                <!--end -->
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST004'}">
                                                            <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/principalOfficers.jsp"/>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST014'}">
                                                            <div class="key-appointment-holder">
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/keyAppointmentHolder.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST007'}">
                                                            <div class="med-alert-person">
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/medAlertContent.jsp"/>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${currentStep == 'SVST005'}">
                                                            <div class="Service-related-Documents document-upload-gp">
                                                                <h2>Service-related Documents</h2>
                                                                <jsp:include page="/WEB-INF/jsp/iais/application/serviceInfo/CR_Doc.jsp"/>
                                                            </div>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <%--</div>--%>
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
</div>
<input type="hidden" name="errorMapIs" id="errorMapIs" value="${errormapIs}">
