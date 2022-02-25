<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#efoDetails">
                Egg Freezing Only Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="4" cssClass="col-md-4"  value="" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                    <c:if test="${not empty arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}">
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <select id="oldDsSelect" name="oldDsSelect">
                                <c:forEach items="${arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}" var="oldDs" varStatus="index">
                                    <option  <c:if test="${oldDs.dataSubmissionDto.id == arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}">checked</c:if> value ="${oldDs.dataSubmissionDto.id}">V ${oldDs.dataSubmissionDto.version}</option>
                                </c:forEach>
                            </select>
                        </iais:value>
                    </c:if>
                </iais:row>

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
                        <%ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getRequestAttr(request,"arSuperDataSubmissionDto");%>
                        <%=IaisCommonUtils.getYearsAndMonths(arSuperDataSubmissionDto.getEfoCycleStageDto().getYearNum(), arSuperDataSubmissionDto.getEfoCycleStageDto().getMonthNum())%>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <%ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getRequestAttr(request,"arSuperDataSubmissionDto");%>
                        <%=IaisCommonUtils.getYearsAndMonths(arSuperDataSubmissionDto.getEfoCycleStageDto().getYearNum(), arSuperDataSubmissionDto.getEfoCycleStageDto().getMonthNum())%>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Is it Medically Indicated?" mandatory="false"/>
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
                        <iais:code code="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.reason}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.otherReason}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.efoCycleStageDto.otherReason}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>