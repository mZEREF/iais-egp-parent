<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(disposalNotification.disposalNotList.size())}">

<input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
<input type="hidden" id="section_repeat_section_id_prefix" value="disSection" readonly disabled>
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
                    <c:forEach var="item" items="${disposalNotification.disposalNotList}" varStatus="status">
                        <section id="disSection--v--${status.index}">
                            <c:if test="${disposalNotification.disposalNotList.size() > 1}">
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
                                        <label for="disposedQty--v--${status.index}">Quantity Disposed</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                            <%--Displayed for Fifth Schedule toxin--%>
                                        <input type="number" name="disposedQty--v--${status.index}"
                                               id="disposedQty--v--${status.index}" value="${item.disposedQty}"
                                               maxlength="11"
                                               οninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                                        <span data-err-ind="disposedQty--v--${status.index}" class="error-msg"></span>
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
                                <div class="col-sm-5 control-label">
                                    <label>Complete Destruction/Disposal</label>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <label><p>Yes</p></label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="destructMethod--v--${status.index}">Method of Destruction</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <textarea id="destructMethod--v--${status.index}"
                                              style="width: 100%;margin-bottom: 15px;"
                                              rows="6"
                                              name="destructMethod--v--${status.index}"
                                              maxlength="300">${item.destructMethod}</textarea>
                                    <span data-err-ind="destructMethod--v--${status.index}" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="destructDetails--v--${status.index}">Details and/or procedures of destruction</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <textarea id="destructDetails--v--${status.index}"
                                              style="width: 100%;margin-bottom: 15px;"
                                              rows="6"
                                              name="destructDetails--v--${status.index}"
                                              maxlength="1000">${item.destructDetails}</textarea>
                                    <span data-err-ind="destructDetails--v--${status.index}" class="error-msg"></span>
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
                            <label for="remarks">Remarks</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <textarea id="remarks" style="width: 100%;margin-bottom: 15px;"
                                      rows="6"
                                      name="remarks"
                                      maxlength="300">${disposalNotification.remarks}</textarea>
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