<%@ include file="./common/ldtHeader.jsp" %>
<c:set value="${LdtSuperDataSubmissionDto.dsLaboratoryDevelopTestDto}" var="dsLaboratoryDevelopTestDto"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type">
    <div class="main-content">
        <div class="container">
            <p class="print"><div style="font-size: 16px;text-align: right;"><a href="javascript:void(0)" onclick="printWDPDF()"> <em class="fa fa-print"></em>Print</a></div></p>
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div class="row form-horizontal">
                                    <iais:row>
                                        <iais:field value="Name of Laboratory" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select cssClass="Salutation disabled" name="hciCode" id="hciCode"  options="premissOptions" firstOption="Please Select" value="${dsLaboratoryDevelopTestDto.hciCode}"/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Name of LDT Test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="ldtTestName" id="ldtTestName"  maxlength="50" value="${dsLaboratoryDevelopTestDto.ldtTestName}" disabled/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Intended Purpose of Test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <textarea id="intendedPurpose" style="width: 100%;margin-bottom: 15px;" rows="6" name="intendedPurpose"
                                                      maxlength="500" disabled>${dsLaboratoryDevelopTestDto.intendedPurpose}</textarea>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Date LDT was made or will be made available" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:datePicker disabled ="true"  id="ldtDate" name="ldtDate" dateVal="${dsLaboratoryDevelopTestDto.ldtDate}"/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Person responsible for the test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="responsePerson" id="responsePerson"  value="${dsLaboratoryDevelopTestDto.responsePerson}" disabled maxlength="66"/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="designation" id="designation" value="${dsLaboratoryDevelopTestDto.designation}" maxlength="20" disabled/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-11 col-md-4 control-label">Status of Test <span style="color: red"> *</span>
                                            <a id = "tooltip" class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                                               title='Active - Clinical laboratory continues to offer this LDT in their laboratory.
                                                  Inactive - Clinical laboratory has ceased to make available this LDT in their laboratory.'
                                               style="z-index: 10"
                                               data-original-title="">i</a>
                                        </label>
                                        <iais:value width="5">
                                            <input class=" " id="testStatus" type="radio" name="testStatus" <c:if test="${dsLaboratoryDevelopTestDto.testStatus == '1'}"> checked="checked"</c:if> aria-invalid="false" value="1" disabled> Active
                                        </iais:value>
                                        <iais:value width="5">
                                            <input class=" " id="testStatus" type="radio" name="testStatus" <c:if test="${dsLaboratoryDevelopTestDto.testStatus == '0'}"> checked="checked"</c:if> aria-invalid="false" value="0" disabled> Inactive
                                        </iais:value>
                                        <iais:value width="11" style="padding-top:12px">
                                        </iais:value>

                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Remarks" width="11" required="false"/>
                                        <iais:value width="11">
                                            <textarea id="remarks" style="width: 100%;margin-bottom: 15px;" rows="6" name="remarks"
                                                      maxlength="300" disabled>${dsLaboratoryDevelopTestDto.remarks}</textarea>
                                        </iais:value>
                                    </iais:row>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@include file="./common/ldtFooter.jsp" %>