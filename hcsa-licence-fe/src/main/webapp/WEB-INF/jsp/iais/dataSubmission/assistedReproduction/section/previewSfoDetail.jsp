<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" href="#efoDetails" data-toggle="collapse">
                Sperm Freezing Only Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Premises where Sperm Freezing Only Cycle is Performed" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:optionText value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Freezing" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <fmt:formatDate value="${arSuperDataSubmissionDto.efoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Age at Date of Freezing" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <%ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);%>
                        <%=IaisCommonUtils.getYearsAndMonths(arSuperDataSubmissionDto.getEfoCycleStageDto().getYearNum(), arSuperDataSubmissionDto.getEfoCycleStageDto().getMonthNum())%>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-5 col-md-4 control-label">Is it Medically Indicated?
                        <a id="medicallyYes" class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="<span style='font-size: 1.5rem;'>
                            <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==1}">
                                ${MessageUtil.getMessageDesc("DS_MSG037")}</c:if>
                            <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==0}">
                                ${MessageUtil.getMessageDesc("DS_MSG043")}</c:if></span>"
                           style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==1 }">
                            Yes</c:if>
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==0 }">
                            No</c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Reason" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==1 }">
                            <iais:code code="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"/></c:if>
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==0 }">
                            <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"/></c:if>
                    </iais:value>
                </iais:row>
                <div id="othersReason" <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.reason!='EFOR004'}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Reason (Others)" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.otherReason}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field  width="5" value="No.Cryopreserved" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.cryopresNum}"/>
                    </iais:value>
                </iais:row>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>
