<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArTreatmentSubsidiesStageDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.service.client.ArFeClient" %>
<%@ page import="com.ecquaria.cloud.helper.SpringContextHelper" %>
<%@ page import="java.util.List" %>
<%
    ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
    ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto = arSuperDataSubmissionDto.getArTreatmentSubsidiesStageDto();
    String coFunding = arTreatmentSubsidiesStageDto.getCoFunding();
    CycleDto cycleDto = arSuperDataSubmissionDto.getCycleDto();
    ArFeClient arFeClient = SpringContextHelper.getContext().getBean(ArFeClient.class);
    List<ArTreatmentSubsidiesStageDto> oldArTreatmentSubsidiesStageDtos = arFeClient.getArTreatmentSubsidiesStagesByPatientInfo(cycleDto.getPatientCode(), cycleDto.getHciCode(), cycleDto.getCycleType()).getEntity();
    int freshCount = 0;
    int frozenCount = 0;
    for (ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto1 : oldArTreatmentSubsidiesStageDtos) {
        if ("ATSACF002".equals(arTreatmentSubsidiesStageDto1.getCoFunding())) {
            freshCount++;
        } else if ("ATSACF003".equals(arTreatmentSubsidiesStageDto1.getCoFunding())) {
            frozenCount++;
        }
    }
    boolean isDisplayAppeal = ("ATSACF002".equals(coFunding) && freshCount >= 3) ||
            ("ATSACF003".equals(coFunding) && frozenCount >= 3);
    ParamUtil.setRequestAttr(request, "isDisplayAppeal", isDisplayAppeal);
%>
<c:set var="arTreatmentSubsidiesStageDto" value="${arSuperDataSubmissionDto.arTreatmentSubsidiesStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign} ">
        <h4 class="panel-title">
            <a class="collapsed" href="#cycleDetails" data-toggle="collapse">
                AR Treatment Co-funding
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="6" value="Is the ART cycle being co-funded" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:code code="${arTreatmentSubsidiesStageDto.coFunding}"/>
                    </iais:value>
                </iais:row>
                <c:if test="${isDisplayAppeal}">
                    <iais:row>
                        <iais:field width="6" value="Is there an approved appeal?" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${arTreatmentSubsidiesStageDto.isThereAppeal?'Yes':'No'}"/>
                        </iais:value>
                    </iais:row>
                </c:if>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>