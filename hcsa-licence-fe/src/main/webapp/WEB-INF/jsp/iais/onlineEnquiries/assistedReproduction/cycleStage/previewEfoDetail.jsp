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
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                </iais:row>
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
                        <%ArSuperDataSubmissionDto arSuperDsDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,"arSuperDataSubmissionDto");%>
                        <%=IaisCommonUtils.getYearsAndMonths(arSuperDsDto.getEfoCycleStageDto().getYearNum(), arSuperDsDto.getEfoCycleStageDto().getMonthNum())%>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Is it Medically Indicated?" mandatory="false"/>
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
                        <iais:code code="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"/>
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
            </div>
        </div>
    </div>
</div>