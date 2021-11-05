<input type="hidden" name="sectionAmt" value="${consumeNotification.consumptionNotList.size()}">

<input type="hidden" id="section_repeat_amt_input_name" value="sectionAmt" readonly disabled>
<input type="hidden" id="section_repeat_section_id_prefix" value="reqTSection" readonly disabled>
<input type="hidden" id="section_repeat_header_title_prefix" value="Agent / Toxin " readonly disabled>
<input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
<input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>

<div class="panel panel-default">
    <div class="panel-heading"><strong>Biological Agent/Toxin</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class="col-xs-12 col-sm-12">
                <div id="sectionGroup">
                    <c:forEach var="item" items="${consumeNotification.consumptionNotList}" varStatus="status">
                        <section id="reqTSection--v--${status.index}">
                            <c:if test="${consumeNotification.consumptionNotList.size() > 1}">
                                <div class="form-group">
                                    <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Agent / Toxin ${status.index + 1}</h3>
                                    <c:if test="${status.index gt 0}">
                                        <div class="col-sm-1"><h4 class="text-danger"><em id="currentIdx" data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
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
                                                 value="" onchange="schTypeChange(this)"
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
                            <div id="agentEpFifth--v--${status.index}" style="display: none">
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="consumeType--v--${status.index}">Type of Consumption</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                            <%--Displayed for First, Second, Third and Fourth Schedule biological agent--%>
                                        <iais:select name="consumeType--v--${status.index}"
                                                     id="consumeType--v--${status.index}"
                                                     value=""
                                                     codeCategory="CATE_ID_BSB_DATA_SUBMISSION_DISPOSAL_TYPE"
                                                     firstOption="Please Select"/>
                                        <span data-err-ind="consumeType--v--${status.index}" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="attachment--v--${status.index}">Attachment</label>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                        <input type="file" name="attachment--v--${status.index}"
                                               id="attachment--v--${status.index}">
                                    </div>
                                </div>
                            </div>
                            <div id="agentFifth--v--${status.index}" style="display: none">
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="consumedQty--v--${status.index}">Quantity Consumed</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                            <%--Displayed for Fifth Schedule toxin--%>
                                        <input type="number" name="consumedQty--v--${status.index}"
                                               id="consumedQty--v--${status.index}" value=""
                                               maxlength="11"
                                               οninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                                        <span data-err-ind="consumedQty--v--${status.index}" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="meaUnit--v--${status.index}">Unit of Measurement</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                        <iais:select name="meaUnit--v--${status.index}" id="meaUnit--v--${status.index}"
                                                     value=""
                                                     codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                                     firstOption="Please Select"/>
                                        <span data-err-ind="meaUnit--v--${status.index}" class="error-msg"></span>
                                    </div>
                                </div>
<%--                                <div class="form-group">--%>
<%--                                    <div class="col-sm-5 control-label">--%>
<%--                                        <label for="attachment--v--${status.index}">Attachment</label>--%>
<%--                                    </div>--%>
<%--                                    <div class="col-sm-6 col-md-7">--%>
<%--                                        <input type="file" name="attachment--v--${status.index}"--%>
<%--                                               id="attachment--v--${status.index}">--%>
<%--                                    </div>--%>
<%--                                </div>--%>
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
                            <label><p>facility name</p></label>
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
                                  maxlength="500"></textarea>
                            <span data-err-ind="remarks" class="error-msg"></span>
                        </div>
                    </div>
<%--                    <div class="form-group">--%>
<%--                        <div class="col-sm-5 control-label">--%>
<%--                            <label for="documentType">Document Type</label>--%>
<%--                        </div>--%>
<%--                        <div class="col-sm-6 col-md-7">--%>
<%--                            <select name="documentType" id="documentType">--%>
<%--                                <option value="0">Please Select</option>--%>
<%--                                <option value="3DOCTYPE001">Inventory: Biological Agents</option>--%>
<%--                                <option value="3DOCTYPE002">Inventory: Toxins</option>--%>
<%--                            </select>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                    <div class="form-group">--%>
<%--                        <div class="col-sm-5 control-label">--%>
<%--                            <label for="attachment">Attachment</label>--%>
<%--                        </div>--%>
<%--                        <div class="col-sm-6 col-md-7">--%>
<%--                            <input type="file" name="attachment" id="attachment">--%>
<%--                        </div>--%>
<%--                    </div>--%>
                </div>
            </div>
        </div>
    </div>
</div>