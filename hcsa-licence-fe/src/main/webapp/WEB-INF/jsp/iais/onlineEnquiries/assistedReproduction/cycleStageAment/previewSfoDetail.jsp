<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#efoDetails">
                Sperm Freezing Only Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="comPart.jsp" %>

                <iais:row>
                    <iais:field width="4" value="Premises where Sperm Freezing Only Cycle is Performed" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:optionText value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:optionText value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Date of Freezing" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <fmt:formatDate value="${arSuperDataSubmissionDto.efoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <fmt:formatDate value="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Patient's Age at Date of Freezing" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <%ArSuperDataSubmissionDto arSuperDsDto = (ArSuperDataSubmissionDto) ParamUtil.getRequestAttr(request,"arSuperDataSubmissionDto");%>
                        <%=IaisCommonUtils.getYearsAndMonths(arSuperDsDto.getEfoCycleStageDto().getYearNum(), arSuperDsDto.getEfoCycleStageDto().getMonthNum())%>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <%ArSuperDataSubmissionDto arSuperDsVersion = (ArSuperDataSubmissionDto) ParamUtil.getRequestAttr(request,"arSuperDataSubmissionDtoVersion");%>
                        <%=IaisCommonUtils.getYearsAndMonths(arSuperDsVersion.getEfoCycleStageDto().getYearNum(), arSuperDsVersion.getEfoCycleStageDto().getMonthNum())%>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-4 col-md-4 control-label col-md-4">Is it Medically Indicated?
                        <a id="medicallyYes" class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="<span style='font-size: 1.5rem;'>
                            <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==1}">
                                ${MessageUtil.getMessageDesc("DS_MSG037")}</c:if>
                            <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==0}">
                                ${MessageUtil.getMessageDesc("DS_MSG043")}</c:if></span>"
                           style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==1 }">
                            Yes</c:if>
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==0 }">
                            No</c:if>
                    </iais:value>
                    <iais:value width="4"  display="true">
                        <c:if test="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.isMedicallyIndicated ==1 }">
                            Yes</c:if>
                        <c:if test="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.isMedicallyIndicated ==0 }">
                            No</c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Reason" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==1 }">
                            <iais:code code="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"/></c:if>
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==0 }">
                            <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"/></c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.isMedicallyIndicated ==1 }">
                            <iais:code code="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.reason}"/></c:if>
                        <c:if test="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.isMedicallyIndicated ==0 }">
                            <c:out value="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.reason}"/></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Reason (Others)" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.reason=='EFOR004'}">
                            <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.otherReason}"/>
                        </c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.reason=='EFOR004'}">
                            <c:out value="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.otherReason}"/>
                        </c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No.Cryopreserved" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.cryopresNum}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.cryopresNum}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
