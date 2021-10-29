<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
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
                                            <iais:select cssClass="Salutation" name="hciCode" id="hciCode"  options="personnelOptions" firstOption="Please Select" value="${laboratoryDevelopTestDto.hciCode}"/>
                                            <span class="error-msg" name="errorMsg" id="error_hciCode"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Name of LDT Test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="ldtTestName" id="ldtTestName"  maxlength="50" value="${laboratoryDevelopTestDto.ldtTestName}"/>
                                            <span class="error-msg" name="errorMsg" id="error_ldtTestName"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Intended Purpose of Test" width="11" required="true"/>
                                            <iais:value width="11">
                                               <textarea id="intendedPurpose" style="width: 100%;margin-bottom: 15px;" rows="6" name="intendedPurpose"
                                                        maxlength="500" >${laboratoryDevelopTestDto.intendedPurpose}</textarea>
                                                <span class="error-msg" name="errorMsg" id="error_intendedPurpose"></span>
                                            </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Date LDT was made or will be made available" width="11" required="true"/>
                                            <iais:value width="11">
                                                <iais:datePicker id="ldtDate" name="ldtDate" dateVal="${laboratoryDevelopTestDto.ldtDate}"></iais:datePicker>
                                                <span class="error-msg" name="errorMsg" id="error_ldtDate"></span>
                                            </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Person responsible for the test" width="11" required="true"/>
                                        <iais:value width="11">
                                                <input type="text" name="responsePerson" id="responsePerson"  value="${laboratoryDevelopTestDto.responsePerson}" maxlength="66"/>
                                                <span class="error-msg" name="errorMsg" id="error_responsePerson"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="designation" id="designation" value="${laboratoryDevelopTestDto.designation}" maxlength="20"/>
                                            <span class="error-msg" name="errorMsg" id="error_designation"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                    <label class="col-xs-11 col-md-4 control-label">Status of Test <span style="color: red"> *</span>
                                        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                                           title='Active - Clinical laboratory continues to offer this LDT in their laboratory.
                                                  Inactive - Clinical laboratory has ceased to make available this LDT in their laboratory.'
                                           style="z-index: 10"
                                           data-original-title="">i</a>
                                    </label>
                                        <iais:value width="5">
                                            <input class=" " id="testStatus" type="radio" name="testStatus" <c:if test="${laboratoryDevelopTestDto.testStatus == '1'}"> checked="checked"</c:if> aria-invalid="false" value="1"> Active
                                        </iais:value>
                                        <iais:value width="5">
                                            <input class=" " id="testStatus" type="radio" name="testStatus" <c:if test="${laboratoryDevelopTestDto.testStatus == '0'}"> checked="checked"</c:if> aria-invalid="false" value="0"> Inactive
                                        </iais:value>
                                        <iais:value width="11" style="padding-top:12px">
                                            <span class="error-msg" name="errorMsg" id="error_testStatus"></span>
                                        </iais:value>

                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Remarks" width="11" required="false"/>
                                        <iais:value width="11">
                                            <textarea id="remarks" style="width: 100%;margin-bottom: 15px;" rows="6" name="remarks"
                                                      maxlength="300" >${laboratoryDevelopTestDto.remarks}</textarea>
                                            <span class="error-msg" name="errorMsg" id="error_remarks"></span>
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
    <div class="row">
        <div class="container">
            <div class="col-xs-12 col-md-6 text-left">
                <a class="back" href="${backUrl}"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-md-6 text-right">
                <button class="btn btn-primary save" id="savebtn" onclick="javascript:save()">NEXT</button>
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>