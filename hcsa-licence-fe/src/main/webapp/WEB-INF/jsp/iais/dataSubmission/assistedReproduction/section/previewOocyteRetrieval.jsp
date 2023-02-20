<c:set var="oocyteRetrievalStageDto" value="${arSuperDataSubmissionDto.oocyteRetrievalStageDto}"/>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OocyteRetrievalStageDto" %>
<%
    ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
    OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
%>
<%--@elvariable id="arSuperDataSubmissionDto" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto"--%>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" href="#cycleDetails" data-toggle="collapse" >
                Oocyte Retrieval
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="6" value="Severe Ovarian Hyperstimulation Syndrome" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:if test="${oocyteRetrievalStageDto.isOvarianSyndrome}">Yes</c:if>
                        <c:if test="${not oocyteRetrievalStageDto.isOvarianSyndrome}">No</c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Oocyte(s) was retrieved from?" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:if test="${oocyteRetrievalStageDto.isFromPatient}"><p>Patient</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromPatientTissue}"><p>Patient's Ovarian
                            Tissue</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromDonor}"><p>Directed Donor</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromDonorTissue}"><p>Directed Donor's Ovarian
                            Tissue</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isNoDirectedDonor}"><p>Non Directed Donor</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isNoDirectedDonorTissue}"><p>Non Directed Donor's Ovarian
                            Tissue</p></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Mature)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="${oocyteRetrievalStageDto.matureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Immature)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="${oocyteRetrievalStageDto.immatureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Others)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="${oocyteRetrievalStageDto.otherRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Total)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="<%=oocyteRetrievalStageDto.getTotalNum()%>"/>
                    </iais:value>
                </iais:row>
                <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/common/patientInventoryTable.jsp"/>
            </div>
        </div>
    </div>
</div>