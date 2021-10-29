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
                                        <iais:field value="Schedule Type" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select name="scheduleType" id="scheduleType"
                                                         value=""
                                                         codeCategory="CATE_ID_BSB_SCH_TYPE"
                                                         firstOption="Please Select"/>
                                            <span class="error-msg" name="errorMsg" id="error_scheduleType"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Biological Agent/Toxin" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select name="BAT" id="BAT"
                                                         options="" firstOption="Please Select"
                                                         value=""/>
                                            <span class="error-msg" name="errorMsg" id="error_BAT"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Quantity to Receive" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="number" name="receivedQty" id="receivedQty" value=""
                                                   maxlength="11"
                                                   οninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                                            <span class="error-msg" name="errorMsg" id="error_receivedQty"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Unit of Measurement" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select name="measurementUnit" id="measurementUnit"
                                                         value=""
                                                         codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                                         firstOption="Please Select"/>
                                            <span class="error-msg" name="errorMsg" id="error_measurementUnit"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Mode of Procurement" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select name="modeProcurement" id="modeProcurement"
                                                         value=""
                                                         codeCategory="CATE_ID_BSB_DATA_SUBMISSION_MODE_OF_MEASUREMENT"
                                                         firstOption="Please Select"/>
                                            <span class="error-msg" name="errorMsg" id="error_modeProcurement"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Facility Name" width="11" required="false"/>
                                        <iais:value width="11">
                                            <label><p>facility name</p></label>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Name of Source Facility" width="11" required="true"/>
                                        <iais:value width="11">
                                            <textarea id="sourceFacilityName" style="width: 100%;margin-bottom: 15px;"
                                                      rows="6"
                                                      name="sourceFacilityName"
                                                      maxlength="300"></textarea>
                                            <span class="error-msg" name="errorMsg"
                                                  id="error_sourceFacilityName"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Address of Source Facility" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="sourceFacilityAddress" id="sourceFacilityAddress"
                                                   maxlength="100" value="">
                                            <span class="error-msg" name="errorMsg"
                                                  id="error_sourceFacilityAddress"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Contact Person of Source Facility" width="11"
                                                    required="false"/>
                                        <iais:value width="11">
                                            <textarea id="sourceFacilityContactPerson"
                                                      style="width: 100%;margin-bottom: 15px;"
                                                      rows="6"
                                                      name="sourceFacilityContactPerson"
                                                      maxlength="300"></textarea>
                                            <span class="error-msg" name="errorMsg"
                                                  id="error_sourceFacilityContactPerson"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Email of Contact Person" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="contactPersonEmail" id="contactPersonEmail"
                                                   maxlength="66" value="">
                                            <span class="error-msg" name="errorMsg"
                                                  id="error_sourceFacilityContactPersonEmail"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Tel No of Contact Person" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="contactPersonTel" id="contactPersonTel"
                                                   maxlength="20" value="">
                                            <span class="error-msg" name="errorMsg"
                                                  id="error_sourceFacilityContactPersonTel"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Name of Courier Service Provider" width="11"
                                                    required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="provider" id="provider" maxlength="100" value="">
                                            <span class="error-msg" name="errorMsg" id="errorProvider"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Flight No." width="11" required="false"/>
                                        <iais:value width="11">
                                            <input type="text" name="flightNo" id="flightNo" maxlength="20" value="">
                                            <span class="error-msg" name="errorMsg" id="errorFlightNo"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Actual Arrival Date" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:datePicker id="actualArrivalDate" name="actualArrivalDate"
                                                             dateVal=""></iais:datePicker>
                                            <span class="error-msg" name="errorMsg" id="error_actualArrivalDate"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Actual Arrival Time" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="actualArrivalTime" id="actualArrivalTime"
                                                   maxlength="5" value="">
                                            <span class="error-msg" name="errorMsg" id="error_actualArrivalTime"></span>
                                        </iais:value>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field value="Remarks" width="11" required="false"/>
                                        <iais:value width="11">
                                            <textarea id="remarks"
                                                      style="width: 100%;margin-bottom: 15px;"
                                                      rows="6"
                                                      name="remarks"
                                                      maxlength="300">${laboratoryDevelopTestDto.remarks}</textarea>
                                            <span class="error-msg" name="errorMsg" id="error_remarks"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Document Type" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select name="documentType" id="documentType"
                                                         value=""
                                                         codeCategory="CATE_ID_BSB_DATA_SUBMISSION_DOCUMENT_TYPE"
                                                         firstOption="Please Select"/>
                                            <span class="error-msg" name="errorMsg" id="errorDocumentType"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <%-- Multi-File Upload --%>
                                        <iais:field value="Attachment" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="file">
                                            <span class="error-msg" name="errorMsg" id="errorAttachment"></span>
                                        </iais:value>
                                    </iais:row>
                                    <div class="form-group ">
                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                            <%--<c:if test="${previewSubmit.declare eq 'Y'}">checked="checked"</c:if>--%>
                                            <input type="checkbox" name="declare" id="declare" value="Y"/>
                                        </div>
                                        <div class="col-xs-10 control-label">
                                            <label for="declare">I have obtained the necessary import permit(s) and/or
                                                approval(s) from the relevant authorities for this importation</label>
                                            <span data-err-ind="declare" class="error-msg"></span>
                                        </div>
                                    </div>
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
                <a class="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-md-6 text-right">
                <button class="btn btn-primary save" id="savebtn" onclick="javascript:save()">NEXT</button>
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>