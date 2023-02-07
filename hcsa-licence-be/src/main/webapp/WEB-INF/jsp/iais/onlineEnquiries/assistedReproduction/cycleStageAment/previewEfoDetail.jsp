<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#efoDetails">
                Oocyte Freezing Only Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="comPart.jsp" %>

                <iais:row>
                    <iais:field width="4" value="Premises where Oocyte Freezing Only Cycle is Performed" mandatory="false"/>
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
                        <fmt:formatDate value="${arSuperDataSubmissionDto.ofoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <fmt:formatDate value="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Patient's Age at Date of Freezing" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <%ArSuperDataSubmissionDto arSuperDsDto = (ArSuperDataSubmissionDto) ParamUtil.getRequestAttr(request,"arSuperDataSubmissionDto");%>
                        <%=IaisCommonUtils.getYearsAndMonths(arSuperDsDto.getOfoCycleStageDto().getYearNum(), arSuperDsDto.getOfoCycleStageDto().getMonthNum())%>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <%ArSuperDataSubmissionDto arSuperDsVersion = (ArSuperDataSubmissionDto) ParamUtil.getRequestAttr(request,"arSuperDataSubmissionDtoVersion");%>
                        <%=IaisCommonUtils.getYearsAndMonths(arSuperDsVersion.getOfoCycleStageDto().getYearNum(), arSuperDsVersion.getOfoCycleStageDto().getMonthNum())%>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Is it Medically Indicated?" info="${MessageUtil.getMessageDesc('DS_MSG025')}" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDto.ofoCycleStageDto.isMedicallyIndicated ==1 }">
                            Yes</c:if>
                        <c:if test="${arSuperDataSubmissionDto.ofoCycleStageDto.isMedicallyIndicated ==0 }">
                            No</c:if>
                    </iais:value>
                    <iais:value width="4"  display="true">
                        <c:if test="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.isMedicallyIndicated ==1 }">
                            Yes</c:if>
                        <c:if test="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.isMedicallyIndicated ==0 }">
                            No</c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Reason" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDto.ofoCycleStageDto.isMedicallyIndicated ==1 }">
                            <iais:code code="${arSuperDataSubmissionDto.ofoCycleStageDto.reason}"/></c:if>
                        <c:if test="${arSuperDataSubmissionDto.ofoCycleStageDto.isMedicallyIndicated ==0 }">
                            <c:out value="${arSuperDataSubmissionDto.ofoCycleStageDto.reason}"/></c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.isMedicallyIndicated ==1 }">
                            <iais:code code="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.reason}"/></c:if>
                        <c:if test="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.isMedicallyIndicated ==0 }">
                            <c:out value="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.reason}"/></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Reason (Others)" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDto.ofoCycleStageDto.reason=='EFOR004'}">
                            <c:out value="${arSuperDataSubmissionDto.ofoCycleStageDto.otherReason}"/>
                        </c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.reason=='EFOR004'}">
                            <c:out value="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.otherReason}"/>
                        </c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field  width="4" value="No.Cryopreserved" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.ofoCycleStageDto.cryopresNum}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.cryopresNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Others" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.ofoCycleStageDto.others}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.ofoCycleStageDto.others}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>