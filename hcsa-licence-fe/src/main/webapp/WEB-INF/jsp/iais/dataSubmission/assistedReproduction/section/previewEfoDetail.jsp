<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" href="#efoDetails" data-toggle="collapse">
                Egg Freezing Only Cycle
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
                    <iais:field width="5" value="Premises where egg freezing only cycle is performed" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date Started" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <fmt:formatDate value="${arSuperDataSubmissionDto.efoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Age as of This Treatment" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <%ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);%>
                        <%=IaisCommonUtils.getYearsAndMonths(arSuperDataSubmissionDto.getEfoCycleStageDto().getYearNum(), arSuperDataSubmissionDto.getEfoCycleStageDto().getMonthNum())%>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Is it Medically Indicated?" info="${MessageUtil.getMessageDesc('DS_MSG025')}" mandatory="false"/>
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
                        <iais:field width="5" value="" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.otherReason}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>