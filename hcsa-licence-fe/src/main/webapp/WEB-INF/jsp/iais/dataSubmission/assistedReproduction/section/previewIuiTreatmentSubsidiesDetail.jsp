<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#patientDetails">
                IUI Treatment Co-funding
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <c:set var="iuiTreatmentSubsidiesDto" value="${arSuperDataSubmissionDto.iuiTreatmentSubsidiesDto}" />
                <iais:row>
                    <iais:field width="5" value="Please indicate IUI Co-funding"/>
                    <iais:value width="7" cssClass="col-xs-5 col-md-6 control-label">
                        <span class="col-xs-6 col-md-6 control-label" style="font-size: 16px"><iais:code code="${iuiTreatmentSubsidiesDto.artCoFunding}"/></span>
                    </iais:value>
                </iais:row>
            <c:if test="${isDisplayAppeal}">
                <iais:row>
                    <iais:field width="5" value="Is there an Appeal?" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal == true }">Yes</c:if>
                        <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal == false }">No</c:if>
                    </iais:value>
                </iais:row>
            </c:if>
            </div>
            <%@include file="../common/patientInventoryTable.jsp" %>
        </div>
    </div>
</div>
