<%@ include file="./common/ldtHeader.jsp" %>
<c:set value="${LdtSuperDataSubmissionDto.dsLaboratoryDevelopTestDto}" var="dsLaboratoryDevelopTestDto"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div class="row form-horizontal">
                                    <iais:row>
                                        <iais:field value="Name of Laboratory" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select cssClass="Salutation" name="hciCode" id="hciCode"
                                                         options="premissOptions" firstOption="Please Select"
                                                         value="${dsLaboratoryDevelopTestDto.hciCode}"/>
                                            <span class="error-msg" name="errorMsg" id="error_hciCode"></span>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field value="Name of LDT Test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="ldtTestName" id="ldtTestName" maxlength="50"
                                                   value="${dsLaboratoryDevelopTestDto.ldtTestName}"/>
                                            <span class="error-msg" name="errorMsg" id="error_ldtTestName"></span>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field value="Intended Purpose of Test" width="11" required="true"/>
                                        <iais:value width="11">
                                               <textarea id="intendedPurpose" style="width: 100%;margin-bottom: 15px;"
                                                         rows="6" name="intendedPurpose"
                                                         maxlength="500">${dsLaboratoryDevelopTestDto.intendedPurpose}</textarea>
                                            <span class="error-msg" name="errorMsg" id="error_intendedPurpose"></span>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field value="Date LDT was made or will be made available" width="11"
                                                    required="true"/>
                                        <iais:value width="11">
                                            <iais:datePicker id="ldtDate" name="ldtDate"
                                                             dateVal="${dsLaboratoryDevelopTestDto.ldtDate}"/>
                                            <span class="error-msg" name="errorMsg" id="error_ldtDate"></span>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field value="Person responsible for the test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="responsePerson" id="responsePerson"
                                                   value="${dsLaboratoryDevelopTestDto.responsePerson}" maxlength="66"/>
                                            <span class="error-msg" name="errorMsg" id="error_responsePerson"></span>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field value="Designation" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="designation" id="designation"
                                                   value="${dsLaboratoryDevelopTestDto.designation}" maxlength="20"/>
                                            <span class="error-msg" name="errorMsg" id="error_designation"></span>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <label class="col-xs-11 col-md-4 control-label">Status of Test
                                            <span style="color: red"> *</span>
                                            <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip"
                                               data-html="true" href="javascript:void(0);"
                                               title='Active - Clinical laboratory continues to offer this LDT in their laboratory.
                                                  Inactive - Clinical laboratory has ceased to make available this LDT in their laboratory.'
                                               style="z-index: 10"
                                               data-original-title="">i</a>
                                        </label>
                                        <iais:value width="10">
                                            <div class="form-check col-md-6" style="padding: 0px;">
                                                <input class="form-check-input"
                                                       type="radio"
                                                       name="testStatus"
                                                       value="1"
                                                       id="testStatusActive"
                                                       <c:if test="${dsLaboratoryDevelopTestDto.testStatus == '1'}">checked</c:if>
                                                       aria-invalid="false">
                                                <label class="form-check-label"
                                                       for="testStatusActive"><span
                                                        class="check-circle"></span>Active</label>
                                            </div>
                                            <div class="form-check col-md-6" style="padding: 0px;">
                                                <input class="form-check-input"
                                                       type="radio"
                                                       name="testStatus"
                                                       value="0"
                                                       id="testStatusInactive"
                                                       <c:if test="${dsLaboratoryDevelopTestDto.testStatus == '0'}">checked</c:if>
                                                       aria-invalid="false">
                                                <label class="form-check-label"
                                                       for="testStatusInactive"><span
                                                        class="check-circle"></span>Inactive</label>
                                            </div>
                                            <span id="error_testStatus" name="error_testStatus"
                                                  class="error-msg col-md-12"
                                                  style="padding: 0px;"></span>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field value="Remarks" width="11" required="false"/>
                                        <iais:value width="11">
                                            <textarea id="remarks" style="width: 100%;margin-bottom: 15px;" rows="6"
                                                      name="remarks"
                                                      maxlength="300">${dsLaboratoryDevelopTestDto.remarks}</textarea>
                                            <span class="error-msg" name="errorMsg" id="error_remarks"></span>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="${LdtSuperDataSubmissionDto.appType eq 'DSTY_005'}">
                                        <c:set var="dataSubmission"
                                               value="${LdtSuperDataSubmissionDto.dataSubmissionDto}"/>
                                        <iais:row>
                                            <iais:field width="11" value="Reason for Amendment" id="amendReasonLabel"
                                                        mandatory="true"/>
                                            <iais:value width="11">
                                                <iais:select name="amendReason" firstOption="Please Select"
                                                             codeCategory="DATA_SUBMISSION_CYCLE_STAGE_AMENDMENT"
                                                             value="${dataSubmission.amendReason}"
                                                             cssClass="amendReasonSel"
                                                             onchange="toggleOnSelect(this, 'PCS_002', 'amendReasonOtherDiv')"/>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row id="amendReasonOtherDiv"
                                                  style="${dataSubmission.amendReason ne 'PCS_002' ? 'display:none'  : null }">
                                            <iais:field width="11" value="Reason for Amendment (Others)"
                                                        mandatory="true"/>
                                            <iais:value width="11">
                                                <iais:input maxLength="50" type="text" name="amendReasonOther"
                                                            id="amendReasonOther"
                                                            value="${dataSubmission.amendReasonOther}"/>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<c:if test="${hasDraft}">
    <iais:confirm
            msg="DS_MSG010"
            callBack="submit('resume');" popupOrder="_draftModal" yesBtnDesc="Resume from draft"
            cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" needFungDuoJi="false"
            cancelBtnDesc="Continue" cancelFunc="submit('delete')"/>
</c:if>
<%@include file="./common/ldtFooter.jsp" %>
