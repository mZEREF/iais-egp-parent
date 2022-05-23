<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#efoDetails">
                Egg Freezing Only Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="comPart.jsp" %>

                <iais:row>
                    <iais:field width="4" value="Premises where egg freezing only cycle is performed" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Date Started" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <fmt:formatDate value="${arSuperDataSubmissionDto.efoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <fmt:formatDate value="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Patient's Age as of This Treatment" mandatory="false"/>
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
                    <iais:field width="4" value="Is it Medically Indicated?" info="${MessageUtil.getMessageDesc('DS_MSG025')}" mandatory="false"/>
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
                    <iais:field width="4" value="" mandatory="false"/>
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
            </div>
        </div>
    </div>
</div>