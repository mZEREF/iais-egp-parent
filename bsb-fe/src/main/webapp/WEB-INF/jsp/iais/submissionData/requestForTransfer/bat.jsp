<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(transferReq.transferLists.size())}">

<input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
<input type="hidden" id="section_repeat_section_id_prefix" value="reqTSection" readonly disabled>
<input type="hidden" id="section_repeat_header_title_prefix" value="Agent / Toxin " readonly disabled>
<input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
<input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>
<input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">

<div class="panel panel-default">
    <div class="panel-heading"><strong>Biological Agent/Toxin</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class="col-xs-12 col-sm-12">
                <div id="sectionGroup">
                    <c:forEach var="reqT" items="${transferReq.transferLists}" varStatus="status">
                        <section id="reqTSection--v--${status.index}">
                            <c:if test="${transferReq.transferLists.size() > 1}">
                                <div class="form-group">
                                    <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">
                                        Agent / Toxin ${status.index + 1}</h3>
                                    <c:if test="${status.index gt 0}">
                                        <div class="col-sm-1"><h4 class="text-danger"><em
                                                data-current-idx="${status.index}"
                                                class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em>
                                        </h4></div>
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
                                                 id="scheduleType--v--${status.index}" onchange="stChange(this)"
                                                 value="${reqT.scheduleType}"
                                                 options="scheduleType"
                                                 firstOption="Please Select"/>
                                    <span data-err-ind="scheduleType--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="batCode--v--${status.index}">Biological Agent/Toxin</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <iais:select name="batCode--v--${status.index}" id="batCode--v--${status.index}"
                                                 cssClass="batCodeDropdown${status.index}"
                                                 firstOption="Please Select"
                                                 value="${reqT.batCode}"/>
                                    <span data-err-ind="batCode--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>

                            <div id="agentEpFifth--v--${status.index}" style="display: none">
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="expectedBatQty--v--${status.index}">Expected Quantity of Biological
                                            Agent</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                        <input type="text" name="expectedBatQty--v--${status.index}"
                                               id="expectedBatQty--v--${status.index}"
                                               maxlength="66" value="${reqT.expectedBatQty}">
                                        <span data-err-ind="expectedBatQty--v--${status.index}" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>

                            <div id="agentFifth--v--${status.index}" style="display: none">
                                    <%--display for fifth schedule--%>
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="expReceivedQty--v--${status.index}">Expected Quantity to Receive</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                        <input type="number" name="expReceivedQty--v--${status.index}"
                                               id="expReceivedQty--v--${status.index}" value="${reqT.expReceivedQty}"
                                               maxlength="11"
                                               οninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                                        <span data-err-ind="expReceivedQty--v--${status.index}" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="meaUnit--v--${status.index}">Unit of Measurement</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                        <iais:select name="meaUnit--v--${status.index}"
                                                     cssClass="meaUnitDropdown${status.index}"
                                                     id="meaUnit--v--${status.index}"
                                                     value="${reqT.meaUnit}"
                                                     codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                                     firstOption="Please Select"/>
                                        <span data-err-ind="meaUnit--v--${status.index}" class="error-msg"></span>
                                    </div>
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
                <div class="form-group" style="margin-top: 20px">
                    <h3>Additional Details</h3>
                    <div class="col-sm-5 control-label">
                        <label for="transferFacility">Transferring Facility</label>
                    </div>
                    <div class="col-sm-6 col-md-7">
                        <label id="transferFacility" name="transferFacility">${facilityInfo.facName}</label>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
