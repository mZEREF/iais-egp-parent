<input type="hidden" name="sectionAmt" value="${transferNotDto.transferNotList.size()}">

<input type="hidden" id="section_repeat_amt_input_name" value="sectionAmt" readonly disabled>
<input type="hidden" id="section_repeat_section_id_prefix" value="notTSection" readonly disabled>
<input type="hidden" id="section_repeat_header_title_prefix" value="Agent / Toxin " readonly disabled>
<input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
<input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>

<div class="panel panel-default">
    <div class="panel-heading"><strong>Biological Agent/Toxin</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class = "col-xs-12 col-sm-12">
                <div id="sectionGroup">
                    <c:forEach var="reqT" items="${transferNotDto.transferNotList}" varStatus="status">
                        <section id="notTSection--v--${status.index}">
                            <c:if test="${transferNotDto.transferNotList.size() > 1}">
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
                                                 value="" onchange="stChange(this)"
                                                 codeCategory="CATE_ID_BSB_SCH_TYPE"
                                                 firstOption="Please Select"/>
                                    <span data-err-ind="scheduleType--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="batCode--v--${status.index}">Biological Agent/Toxin Code</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <input type="text" name="batCode--v--${status.index}" id="batCode--v--${status.index}" maxlength="20" value="">
                                    <span data-err-ind="batCode--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>

                            <div id="agentEpFifth--v--${status.index}" style="display: none">
                           <%--Displayed for First, Second, Third and Fourth Schedule biological agent--%>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="transferType--v--${status.index}">Type of Transfer</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <iais:select name="transferType--v--${status.index}" id="transferType--v--${status.index}"
                                                 value=""
                                                 codeCategory="CATE_ID_BSB_DATA_SUBMISSION_DISPOSAL_TYPE"
                                                 firstOption="Please Select"/>
                                    <span data-err-ind="transferType--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>

                            <%--Displayed for First, Second, Third and Fourth Schedule biological agent--%>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="batQty--v--${status.index}">Quantity of Biological Agent</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <input type="number" name="batQty--v--${status.index}" id="batQty--v--${status.index}" value="">
                                    <span data-err-ind="batQty--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            </div>

                            <div id="agentFifth--v--${status.index}" style="display: none">
                           <%--Displayed for Fifth Schedule toxin--%>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="transferQty--v--${status.index}">Quantity to Transfer</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <input type="number" name="transferQty--v--${status.index}" id="transferQty--v--${status.index}" value=""
                                           maxlength="11"
                                           oninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                                    <span data-err-ind="transferQty--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>

                                <%--Displayed for Fifth Schedule toxin--%>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="mstUnit--v--${status.index}">Unit of Measurement</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <iais:select name="mstUnit--v--${status.index}" id="mstUnit--v--${status.index}"
                                                 value=""
                                                 codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                                 firstOption="Please Select"/>
                                    <span data-err-ind="mstUnit--v--${status.index}" class="error-msg"></span>
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
                            <label>facility name</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label>Receiving Facility</label>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <label><p>Receiving Facility</p></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="expectedTfDate">Date of Expected Transfer</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" autocomplete="off" name="expectedTfDate" id="expectedTfDate" data-date-start-date="01/01/1900"  placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                            <span data-err-ind="expectedTfDate" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="expArrivalTime">Expected Arrival Time at Receiving Facility</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="expArrivalTime" id="expArrivalTime" maxlength="10" value=""/>
                            <span data-err-ind="expArrivalTime" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="providerName">Name of Courier Service Provider</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="providerName" id="providerName" maxlength="100" value=""/>
                            <span data-err-ind="providerName" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="remarks">Remarks</label>
                        </div>
                        <div class="col-sm-6 col-md-7">
                           <textarea id="remarks" style="width: 100%;margin-bottom: 15px;" rows="6" name="remarks" maxlength="500"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

