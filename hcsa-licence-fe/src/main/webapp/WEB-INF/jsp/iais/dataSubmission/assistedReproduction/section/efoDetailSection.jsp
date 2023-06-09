<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/efoSection.js"></script>

<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Oocyte Freezing Only Cycle
            </strong>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <input type="hidden" id="startYear"  name="startYear" value="${arSuperDataSubmissionDto.ofoCycleStageDto.yearNum}">
                <input type="hidden" id="startMonth" name="startMonth" value="${arSuperDataSubmissionDto.ofoCycleStageDto.monthNum}">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Premises where Oocyte Freezing Only Cycle is Performed" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:optionText value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Date of Freezing" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:datePicker id="efoDateStarted" name="efoDateStarted" dateVal="${arSuperDataSubmissionDto.ofoCycleStageDto.startDate}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Patient's Age at Date of Freezing" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6"  display="true">
                        <span style="display: block"><span id="freezingYear">${arSuperDataSubmissionDto.ofoCycleStageDto.yearNum}</span> Years and <span id="freezingMonth">${arSuperDataSubmissionDto.ofoCycleStageDto.monthNum}</span> Month</span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Is it Medically Indicated?" info="${MessageUtil.getMessageDesc('DS_MSG025')}" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="indicatedRadio"
                                   value="1"
                                   id="radioYes"
                                   <c:if test="${empty arSuperDataSubmissionDto.ofoCycleStageDto.isMedicallyIndicated || arSuperDataSubmissionDto.ofoCycleStageDto.isMedicallyIndicated ==1 }">checked</c:if>
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
                                   <c:if test="${arSuperDataSubmissionDto.ofoCycleStageDto.isMedicallyIndicated == 0}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="radioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
                <input type="hidden" name="oldReason" value="${arSuperDataSubmissionDto.ofoCycleStageDto.reason}" />
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Reason" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <div id="reasonDisplay1" <c:if test="${arSuperDataSubmissionDto.ofoCycleStageDto.isMedicallyIndicated == 0}">style="display: none"</c:if> >
                            <iais:select cssClass="reasonSelect"  name="reasonSelect" firstOption="Please Select" options="efoReasonSelectOption" value="${arSuperDataSubmissionDto.ofoCycleStageDto.reason}" />
                        </div>
                        <div id="reasonDisplay0" <c:if test="${arSuperDataSubmissionDto.ofoCycleStageDto.isMedicallyIndicated == 1}">style="display: none"</c:if> >
                            <input type="text" maxlength="100" name="textReason" value="<c:out value="${arSuperDataSubmissionDto.ofoCycleStageDto.reason}"/>" >
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_reason"></span>
                    </iais:value>
                </iais:row>
                <div id="othersReason" <c:if test="${arSuperDataSubmissionDto.ofoCycleStageDto.reason!='EFOR004'}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Reason (Others)" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="100"   name="othersReason" value="<c:out value="${arSuperDataSubmissionDto.ofoCycleStageDto.otherReason}"/>" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_othersReason"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row id="cryopresNum">
                    <iais:field width="6" cssClass="col-md-6" value="No.Cryopreserved" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:input type="text" maxLength="2" value="${arSuperDataSubmissionDto.ofoCycleStageDto.cryopresNumStr==null?arSuperDataSubmissionDto.ofoCycleStageDto.cryopresNum:arSuperDataSubmissionDto.ofoCycleStageDto.cryopresNumStr}" id="cryopresNum" name="cryopresNum" />
                        <span class="error-msg" name="iaisErrorMsg" id="error_cryopresNum"></span>
                    </iais:value>
                </iais:row>

                <div id = "Others"  <c:if test="${arSuperDataSubmissionDto.ofoCycleStageDto.cryopresNum ne 0}">style="display: none"</c:if>>
                    <iais:row id="others">
                        <iais:field width="6" cssClass="col-md-6" value="Others" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="100"   name="others" value="<c:out value="${arSuperDataSubmissionDto.ofoCycleStageDto.others}"/>" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_others"></span>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>
