<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(receiveNotification.receiptNotList.size())}">

<input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
<input type="hidden" id="section_repeat_section_id_prefix" value="notTSection" readonly disabled>
<input type="hidden" id="section_repeat_header_title_prefix" value="Agent / Toxin " readonly disabled>
<input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
<input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>

<div class="panel panel-default">
    <div class="panel-heading"><strong>Biological Agent/Toxin</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class="col-xs-12 col-sm-12">
                <div id="sectionGroup">
                    <c:forEach var="item" items="${receiveNotification.receiptNotList}" varStatus="status">
                        <section id="recSection--v--${status.index}">
                            <c:if test="${receiveNotification.receiptNotList.size() > 1}">
                                <div class="form-group">
                                    <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Agent / Toxin ${status.index + 1}</h3>
                                    <c:if test="${status.index gt 0}">
                                        <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                                    </c:if>
                                </div>
                            </c:if>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="scheduleType--v--${status.index}">Schedule Type</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <iais:select name="scheduleType--v--${status.index}"
                                                 id="scheduleType--v--${status.index}"
                                                 value="${item.scheduleType}" onchange="schTypeChange(this)"
                                                 options="scheduleType"
                                                 firstOption="Please Select"/>
                                    <span data-err-ind="scheduleType--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="bat--v--${status.index}">Biological Agent/Toxin</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <iais:select name="bat--v--${status.index}" id="bat--v--${status.index}"
                                                 options="" firstOption="Please Select"
                                                 value="${item.bat}"/>
                                    <span data-err-ind="bat--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div id="agentFifth--v--${status.index}" style="display: none">
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="receivedQty--v--${status.index}">Quantity to Receive</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                            <%--Displayed for Fifth Schedule toxin--%>
                                        <input type="number" name="receivedQty--v--${status.index}"
                                               id="receivedQty--v--${status.index}" value="${item.receiveQty}"
                                               maxlength="11"
                                               οninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                                        <span data-err-ind="receivedQty--v--${status.index}" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="meaUnit--v--${status.index}">Unit of Measurement</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                            <%--Displayed for Fifth Schedule toxin--%>
                                        <iais:select name="meaUnit--v--${status.index}" id="meaUnit--v--${status.index}"
                                                     value="${item.meaUnit}"
                                                     codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                                     firstOption="Please Select"/>
                                        <span data-err-ind="meaUnit--v--${status.index}" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <%@include file="../common/batDocument.jsp" %>
                            </div>
                        </section>
                    </c:forEach>
                </div>
                <div class="form-group">
                    <div class="col-12">
                        <a class="btn btn-secondary" id="addNewSection" href="javascript:void(0);">ADD AGENT/TOXIN</a>
                    </div>
                </div>
                <div class="row form-horizontal">
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label>Facility Name</label>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <label>${facilityInfo.facName}</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="modeProcurement">Mode of Procurement</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <iais:select name="modeProcurement"
                                         id="modeProcurement"
                                         value="${receiveNotification.modeProcurement}"
                                         codeCategory="CATE_ID_BSB_DATA_SUBMISSION_MODE_OF_PROCUREMENT"
                                         firstOption="Please Select"/>
                            <span data-err-ind="modeProcurement--v--${status.index}" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="sourceFacilityName">Name of Source Facility</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <textarea id="sourceFacilityName"
                                      style="width: 100%;margin-bottom: 15px;" rows="6"
                                      name="sourceFacilityName"
                                      maxlength="300">${receiveNotification.sourceFacilityName}</textarea>
                            <span data-err-ind="sourceFacilityName" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="sourceFacilityAddress">Address of Source Facility</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="sourceFacilityAddress" id="sourceFacilityAddress" maxlength="100" value="${receiveNotification.sourceFacilityAddress}">
                            <span data-err-ind="sourceFacilityAddress" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="sourceFacilityContactPerson">Contact Person of Source Facility</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <textarea id="sourceFacilityContactPerson"
                                      style="width: 100%;margin-bottom: 15px;" rows="6"
                                      name="sourceFacilityContactPerson"
                                      maxlength="300">${receiveNotification.sourceFacilityContactPerson}</textarea>
                            <span data-err-ind="sourceFacilityContactPerson" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="contactPersonEmail">Email of Contact Person</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="contactPersonEmail" id="contactPersonEmail" maxlength="66" value="${receiveNotification.contactPersonEmail}">
                            <span data-err-ind="contactPersonEmail" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="contactPersonTel">Tel No of Contact Person</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="contactPersonTel" id="contactPersonTel" maxlength="20" value="${receiveNotification.contactPersonTel}">
                            <span data-err-ind="contactPersonTel" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="provider">Name of Courier Service Provider</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="provider" id="provider" maxlength="100" value="${receiveNotification.provider}">
                            <span data-err-ind="provider" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="flightNo">Flight No.</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="flightNo" id="flightNo" maxlength="20" value="${receiveNotification.flightNo}">
                            <span data-err-ind="flightNo" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="actualArrivalDate">Actual Arrival Date</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <iais:datePicker id="actualArrivalDate" name="actualArrivalDate" dateVal="${receiveNotification.actualArrivalDate}"></iais:datePicker>
                            <span data-err-ind="actualArrivalDate" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="actualArrivalTime">Actual Arrival Time</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="actualArrivalTime" id="actualArrivalTime" maxlength="5" value="${receiveNotification.actualArrivalTime}">
                            <span data-err-ind="actualArrivalTime" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="remarks">Remarks</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <textarea id="remarks" style="width: 100%;margin-bottom: 15px;"
                                      rows="6"
                                      name="remarks"
                                      maxlength="300">${receiveNotification.remarks}</textarea>
                            <span data-err-ind="remarks" class="error-msg"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>