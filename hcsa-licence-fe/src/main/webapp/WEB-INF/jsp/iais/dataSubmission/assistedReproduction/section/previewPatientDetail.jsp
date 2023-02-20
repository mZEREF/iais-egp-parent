<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="patient" value="${patientInfoDto.patient}" />
<c:set var="previous" value="${patientInfoDto.previous}" />

<c:set var="isNew" value="${'DSTY_002' == arSuperDataSubmissionDto.appType}" />
<c:set var="isRFC" value="${'DSTY_005' == arSuperDataSubmissionDto.appType}" />
<c:set var="showPrevious" value="${patient.previousIdentification}" />

<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#patientDetails">
                Details of Patient
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <c:set var="person" value="${patient}" />
                <%@include file="previewPersonSection.jsp" %>
                <c:if test="${isNew}">
                <div id="previousData" <c:if test="${patient.previousIdentification}">style="display:none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Is AR Centre aware of patient's previous identification? "/>
                        <iais:value width="7" display="true">No</iais:value>
                    </iais:row>
                </div>
                </c:if>
                <c:if test="${showPrevious}">
                    <c:set var="person" value="${previous}" />
                    <%@include file="previewPatientPreviousSection.jsp" %>
                </c:if>
            </div>
        </div>
    </div>
</div>