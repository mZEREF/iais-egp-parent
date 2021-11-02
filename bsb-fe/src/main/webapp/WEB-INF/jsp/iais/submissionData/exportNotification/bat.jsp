<input type="hidden" name="sectionAmt" value="${exportNotification.exportLists.size()}">

<input type="hidden" id="section_repeat_amt_input_name" value="sectionAmt" readonly disabled>
<input type="hidden" id="section_repeat_section_id_prefix" value="reqTSection" readonly disabled>
<input type="hidden" id="section_repeat_header_title_prefix" value="Agent / Toxin " readonly disabled>
<input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
<input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>

<div class="panel panel-default">
    <div class="panel-heading"><strong>Biological Agent/Toxin</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class = "col-xs-12 col-sm-12">
                <div id="sectionGroup">
                    <c:forEach var="item" items="${exportNotification.exportLists}" varStatus="status">
                        <section id="reqTSection--v--${status.index}">
                            <c:if test="${exportNotification.exportLists.size() > 1}">
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
                                    <iais:select name="scheduleType--v--${status.index}" id="scheduleType--v--${status.index}"
                                                 value=""
                                                 codeCategory="CATE_ID_BSB_SCH_TYPE"
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
                                                 value=""/>
                                    <span data-err-ind="bat--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="transferType--v--${status.index}">Type of Transfer</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                        <%--Schedule Type 不是 Fifth Schedule，而且biological type为agent时，不显示--%>
                                            <iais:select name="transferType--v--${status.index}" id="transferType--v--${status.index}"
                                                         value=""
                                                         codeCategory="CATE_ID_BSB_DATA_SUBMISSION_DISPOSAL_TYPE"
                                                         firstOption="Please Select"/>
                                    <span data-err-ind="transferType--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="transferQty--v--${status.index}">Quantity to Transfer</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                        <%--Schedule Type 为 Fifth Schedule且biological type为toxin时，显示--%>
                                        <%--不能为负--%>
                                    <input type="number" name="transferQty--v--${status.index}" id="transferQty--v--${status.index}" value=""
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
                                        <%--Schedule Type 为 Fifth Schedule且biological_type为toxin时，显示--%>
                                    <iais:select name="meaUnit--v--${status.index}" id="meaUnit--v--${status.index}"
                                                 value=""
                                                 codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                                 firstOption="Please Select"/>
                                    <span data-err-ind="meaUnit--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label>Facility Name</label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="receivedFacility--v--${status.index}">Receiving Facility</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <textarea id="receivedFacility--v--${status.index}" style="width: 100%;margin-bottom: 15px;" rows="6"
                                              name="receivedFacility--v--${status.index}"
                                              maxlength="300"></textarea>
                                    <span data-err-ind="receivedFacility--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="receivedCountry--v--${status.index}">Receiving Country</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <iais:select name="receivedCountry--v--${status.index}" id="receivedCountry--v--${status.index}"
                                                 value=""
                                                 codeCategory=""
                                                 firstOption="Please Select"/>
                                    <span data-err-ind="receivedCountry--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="exportDate--v--${status.index}">Date of Exportation</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <iais:datePicker id="exportDate--v--${status.index}" name="exportDate--v--${status.index}"
                                                     dateVal=""></iais:datePicker>
                                    <span data-err-ind="exportDate--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="provider--v--${status.index}">Name of Courier Service Provider</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <input type="text" name="provider--v--${status.index}" id="provider--v--${status.index}"
                                           maxlength="100" value="">
                                    <span data-err-ind="provider--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="flightNo--v--${status.index}">Flight No.</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <input type="text" name="flightNo--v--${status.index}" id="flightNo--v--${status.index}"
                                           maxlength="20" value="">
                                    <span data-err-ind="flightNo--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="remarks--v--${status.index}">Remarks</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <textarea id="remarks--v--${status.index}" style="width: 100%;margin-bottom: 15px;" rows="6"
                                              name="remarks--v--${status.index}"
                                              maxlength="300"></textarea>
                                    <span data-err-ind="remarks--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="documentType--v--${status.index}">Document Type</label>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <select name="documentType--v--${status.index}" id="documentType--v--${status.index}">
                                        <option value="3DOCTYPE001">Inventory: Biological Agents</option>
                                        <option value="3DOCTYPE002">Inventory: Toxins</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="attachment--v--${status.index}">Attachment</label>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <input type="file" name="attachment--v--${status.index}" id="attachment--v--${status.index}">
                                </div>
                            </div>
                            <div class="form-group ">
                                <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                        <%--<c:if test="${previewSubmit.declare eq 'Y'}">checked="checked"</c:if>--%>
                                    <input type="checkbox" name="declare" id="declare" value="Y"/>
                                </div>
                                <div class="col-xs-10 control-label">
                                    <label for="declare">I will ensure that: (a) the packaging of the materials
                                        is carried out in accordance with the requirements stipulated in the
                                        BATA Transportation Regulations, where applicable; and (b) the relevant
                                        export permit and/or approval(s) has been obtained prior to exportation
                                        of the materials</label>
                                    <span data-err-ind="declare" class="error-msg"></span>
                                </div>
                            </div>
                        </section>
                    </c:forEach>
                </div>
                <div class="form-group">
                    <div class="col-12">
                        <a class="btn btn-secondary" id="addNewSection" href="javascript:void(0);">ADD AGENT/TOXIN</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>