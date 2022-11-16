<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/efoSection.js"></script>

<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Sperm Freezing Only Cycle
            </strong>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <input type="hidden" id="startYear"  name="startYear" value="${arSuperDataSubmissionDto.efoCycleStageDto.yearNum}">
                <input type="hidden" id="startMonth" name="startMonth" value="${arSuperDataSubmissionDto.efoCycleStageDto.monthNum}">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Premises where Sperm Freezing Only Cycle is Performed" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:optionText value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Date of Freezing" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:datePicker id="efoDateStarted" name="efoDateStarted" dateVal="${arSuperDataSubmissionDto.efoCycleStageDto.startDate}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Patient's Age at Date of Freezing" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <span style="display: block"><span id="freezingYear">${arSuperDataSubmissionDto.efoCycleStageDto.yearNum}</span> Years and <span id="freezingMonth">${arSuperDataSubmissionDto.efoCycleStageDto.monthNum}</span> Month</span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Is it Medically Indicated?" info="${MessageUtil.getMessageDesc('DS_MSG037')}" mandatory="true"/>
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
                <input type="hidden" name="oldReason" value="${arSuperDataSubmissionDto.efoCycleStageDto.reason}" />
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Reason" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <div id="reasonDisplay1" <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated == 0}">style="display: none"</c:if> >
                            <iais:select cssClass="reasonSelect"  name="reasonSelect" firstOption="Please Select" options="sfoReasonSelectOption" value="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"></iais:select>
                        </div>
                        <div id="reasonDisplay0" <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated == 1}">style="display: none"</c:if> >
                            <input type="text" maxlength="66" name="textReason" value="${arSuperDataSubmissionDto.efoCycleStageDto.reason}" >
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_reason"></span>
                    </iais:value>
                </iais:row>
                <div id="othersReason" <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.reason!='EFOR004'}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Reason (Others)" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="100"   name="othersReason" value="${arSuperDataSubmissionDto.efoCycleStageDto.otherReason}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_othersReason"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row id="cryopresNum">
                    <iais:field  width="6" cssClass="col-md-6" value="No.Cryopreserved" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:input type="text" maxLength="2" value="${arSuperDataSubmissionDto.efoCycleStageDto.cryopresNum}" name="cryopresNum" />
                    </iais:value>
                </iais:row>
                <%@include file="hasDisposalRow.jsp"%>
            </div>
        </div>
    </div>
</div>

