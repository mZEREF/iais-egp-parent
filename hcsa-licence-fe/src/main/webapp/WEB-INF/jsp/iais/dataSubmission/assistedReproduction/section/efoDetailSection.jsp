<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/efoSection.js"></script>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Egg Freezing Only Cycle
            </strong>
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
                    <iais:field width="6" value="Premises where egg freezing only cycle is performed" mandatory="true"/>
                    <iais:value width="6"  display="true">
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Date Started" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:datePicker id="efoDateStarted" name="efoDateStarted" dateVal="${arSuperDataSubmissionDto.efoCycleStageDto.startDate}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Patient's Age as of This Treatment" mandatory="false"/>
                    <iais:value width="6"  display="true">
                        <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.yearNum} Years and ${arSuperDataSubmissionDto.efoCycleStageDto.monthNum} Months"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Is it Medically Indicated?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="indicatedRadio"
                                   value="1"
                                   id="radioYes"
                                   <c:if test="${empty arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated || arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==1 }">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="radioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input" type="radio"
                                   name="indicatedRadio" value="0" id="radioNo"
                                   <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated == 0}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="radioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Reason" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="reasonSelect"  name="reasonSelect" firstOption="Please Select" options="efoReasonSelectOption" value="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_reason"></span>
                    </iais:value>
                </iais:row>
                <div id="othersReason" <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.reason!='EFOR004'}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" value="" mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="othersReason" value="${arSuperDataSubmissionDto.efoCycleStageDto.otherReason}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_othersReason"></span>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>
