<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(transferReq.transferLists.size())}">

<input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
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
                    <c:forEach var="reqT" items="${transferReq.transferLists}" varStatus="status">
                    <section id="reqTSection--v--${status.index}">
                        <c:if test="${transferReq.transferLists.size() > 1}">
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
                            <label for="batCode--v--${status.index}">Biological Agent/Toxin Code</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="batCode--v--${status.index}" id="batCode--v--${status.index}" maxlength="20" value="">
                            <span data-err-ind="batCode--v--${status.index}" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="expectedBatQty--v--${status.index}">Expected Quantity of Biological Agent</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="expectedBatQty--v--${status.index}" id="expectedBatQty--v--${status.index}"
                                   maxlength="66" value="">
                            <span data-err-ind="expectedBatQty--v--${status.index}" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="expReceivedQty--v--${status.index}">Expected Quantity to Receive</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="number" name="expReceivedQty--v--${status.index}" id="expReceivedQty--v--${status.index}" value=""
                                   maxlength="11"
                                   οninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                            <span data-err-ind="expReceivedQty--v--${status.index}" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="measurementUnit--v--${status.index}">Unit of Measurement</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <iais:select name="measurementUnit--v--${status.index}" id="measurementUnit--v--${status.index}"
                                         value=""
                                         codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                         firstOption="Please Select"/>
                            <span data-err-ind="measurementUnit--v--${status.index}" class="error-msg"></span>
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
