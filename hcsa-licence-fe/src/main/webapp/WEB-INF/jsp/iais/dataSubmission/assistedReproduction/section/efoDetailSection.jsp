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
                    <p><label ><c:out value="${AR_DATA_SUBMISSION.patientInfoDto.patient.name}"/><c:out value="(${AR_DATA_SUBMISSION.patientInfoDto.patient.idNumber})"/></label></p>
                </h3>
                <iais:row>
                    <iais:field width="6" value="Premises where egg freezing only cycle is performed" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" label="true">
                        <c:out value="${AR_DATA_SUBMISSION.efoCycleStageDto.performed}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Date Started" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:datePicker id="efoDateStarted" name="efoDateStarted" dateVal="${AR_DATA_SUBMISSION.efoCycleStageDto.startDate}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Patient's Age as of This Treatment" mandatory="false"/>
                    <label class="col-xs-6 col-md-6 control-label">
                        <c:out value="${AR_DATA_SUBMISSION.efoCycleStageDto.yearNum} Years and ${AR_DATA_SUBMISSION.efoCycleStageDto.monthNum} Months"/>
                    </label>
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
                                   <c:if test="${empty AR_DATA_SUBMISSION.efoCycleStageDto.isMedicallyIndicated || AR_DATA_SUBMISSION.efoCycleStageDto.isMedicallyIndicated ==1 }">checked</c:if>
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
                                   <c:if test="${AR_DATA_SUBMISSION.efoCycleStageDto.isMedicallyIndicated == 0}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="radioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Reasons" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="reasonSelect"  name="reasonSelect" firstOption="Please Select" options="efoReasonSelectOption" value="${AR_DATA_SUBMISSION.efoCycleStageDto.reason}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_reason"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" value="" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <div id="othersReason" <c:if test="${AR_DATA_SUBMISSION.efoCycleStageDto.reason!='EFOR004'}">style="display: none"</c:if> >
                            <input type="text" maxlength="20"   name="othersReason" value="${AR_DATA_SUBMISSION.efoCycleStageDto.othersReason}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_othersReason"></span>
                        </div>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>
