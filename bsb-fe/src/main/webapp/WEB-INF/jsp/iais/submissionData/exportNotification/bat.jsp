<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(exportNotification.exportNotList.size())}">

<input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
<input type="hidden" id="section_repeat_section_id_prefix" value="expSection" readonly disabled>
<input type="hidden" id="section_repeat_header_title_prefix" value="Agent / Toxin " readonly disabled>
<input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
<input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>
<input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
<input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
<input type="hidden" id="documentList" name="documentList" value="document-upload-list">

<div class="panel panel-default">
    <div class="panel-heading"><strong>Biological Agent/Toxin</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class="col-xs-12 col-sm-12">
                <div id="sectionGroup">
                    <c:forEach var="item" items="${exportNotification.exportNotList}" varStatus="status">
                        <section id="expSection--v--${status.index}">
                            <c:if test="${exportNotification.exportNotList.size() > 1}">
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
                                                 cssClass="scheduleTypeDropdown${status.index}"
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
                                                 cssClass="batDropdown${status.index}"
                                                 options="" firstOption="Please Select"
                                                 value="${item.bat}"/>
                                    <span data-err-ind="bat--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div id="agentEpFifth--v--${status.index}" style="display: none">
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="transferType--v--${status.index}">Type of Transfer</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                            <%--Displayed for First, Second, Third and Fourth Schedule biological agent--%>
                                        <iais:select name="transferType--v--${status.index}"
                                                     cssClass="transferTypeDropdown${status.index}"
                                                     id="transferType--v--${status.index}"
                                                     value="${item.transferType}"
                                                     codeCategory="CATE_ID_BSB_DATA_SUBMISSION_DISPOSAL_TYPE"
                                                     firstOption="Please Select"/>
                                        <span data-err-ind="transferType--v--${status.index}" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                            <div id="agentFifth--v--${status.index}" style="display: none">
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="transferQty--v--${status.index}">Quantity to Transfer</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                            <%--Displayed for Fifth Schedule toxin--%>
                                        <input type="number" name="transferQty--v--${status.index}"
                                               id="transferQty--v--${status.index}" value="${item.transferQty}"
                                               maxlength="11"
                                               οninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                                        <span data-err-ind="transferQty--v--${status.index}" class="error-msg"></span>
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
                                                     cssClass="meaUnitDropDown${status.index}"
                                                     value="${item.meaUnit}"
                                                     codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                                     firstOption="Please Select"/>
                                        <span data-err-ind="meaUnit--v--${status.index}" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>

                            <div id="batDocument--v--${status.index}" style="display: none">
                                <div class="form-group" >
                                    <%@include file="../common/batDocument.jsp" %>
                                </div>
                            </div>
                        </section>
                    </c:forEach>
                </div>
                <div class="form-group">
                    <div class="col-12">
                        <a class="btn btn-secondary" id="addNewSection" href="javascript:void(0);">ADD
                            AGENT/TOXIN</a>
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
                            <label for="receivedFacility">Receiving Facility</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <textarea id="receivedFacility"
                                      style="width: 100%;margin-bottom: 15px;" rows="6"
                                      name="receivedFacility"
                                      maxlength="300">${exportNotification.receivedFacility}</textarea>
                            <span data-err-ind="receivedFacility" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="receivedCountry">Receiving Country</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <iais:select name="receivedCountry"
                                         cssClass="receivedCountryDropdown"
                                         id="receivedCountry"
                                         value="${exportNotification.receivedCountry}"
                                         codeCategory="CATE_ID_NATIONALITY"
                                         firstOption="Please Select"/>
                            <span data-err-ind="receivedCountry" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="exportDate">Date of Exportation</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" autocomplete="off" name="exportDate" id="exportDate" data-date-start-date="01/01/1900"  placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" value="${exportNotification.exportDate}"/>
                            <span data-err-ind="exportDate--v--${status.index}" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="provider">Name of Courier Service Provider</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="provider" id="provider" maxlength="100" value="${exportNotification.provider}">
                            <span data-err-ind="provider" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="flightNo">Flight No.</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="flightNo" id="flightNo" maxlength="20" value="${exportNotification.flightNo}">
                            <span data-err-ind="flightNo" class="error-msg"></span>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="remarks">Remarks</label>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <textarea id="remarks" style="width: 100%;margin-bottom: 15px;"
                                      rows="6"
                                      name="remarks"
                                      maxlength="300">${exportNotification.remarks}</textarea>
                            <span data-err-ind="remarks" class="error-msg"></span>
                        </div>
                    </div>
                    <div class = "form-group" id="others">
                        <%@include file="../common/docOtherCondition.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>