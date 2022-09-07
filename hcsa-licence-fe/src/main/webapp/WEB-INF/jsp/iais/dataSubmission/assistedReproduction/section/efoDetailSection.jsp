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
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Premises where Oocyte Freezing Only Cycle is Performed" mandatory="true"/>
                    <iais:value width="6"  display="true">
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Freezing" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker id="efoDateStarted" name="efoDateStarted" dateVal="${arSuperDataSubmissionDto.efoCycleStageDto.startDate}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Age at Date of Freezing" mandatory="false"/>
                    <iais:value width="6"  display="true">
                        <%ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,"arSuperDataSubmissionDto");%>
                        <%=IaisCommonUtils.getYearsAndMonths(arSuperDataSubmissionDto.getEfoCycleStageDto().getYearNum(), arSuperDataSubmissionDto.getEfoCycleStageDto().getMonthNum())%>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Is it Medically Indicated?" info="${MessageUtil.getMessageDesc('DS_MSG025')}" mandatory="true"/>
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
                    <iais:field width="5" value="Reason" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <div id="reasonDisplay1" <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated == 0}">style="display: none"</c:if> >
                            <iais:select cssClass="reasonSelect"  name="reasonSelect" firstOption="Please Select" options="efoReasonSelectOption" value="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"></iais:select>
                        </div>
                        <div id="reasonDisplay0" <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated == 1}">style="display: none"</c:if> >
                            <input type="text" maxlength="66" name="textReason" value="${arSuperDataSubmissionDto.efoCycleStageDto.reason}" >
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_reason"></span>
                    </iais:value>
                </iais:row>
                <div id="othersReason" <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.reason!='EFOR004'}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <input type="text" maxlength="100"   name="othersReason" value="${arSuperDataSubmissionDto.efoCycleStageDto.otherReason}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_othersReason"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row id="cryopresNum">
                    <iais:field  width="5" value="No.Cryopreserved" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input type="text" maxLength="50" value="${arSuperDataSubmissionDto.efoCycleStageDto.cryopresNum}" name="cryopresNum" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
