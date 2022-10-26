<br/><br/>
<div class="alert alert-info" role="alert">
    <strong>
        <h4>Processing Status Update</h4>
    </strong>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <div class="form-horizontal">
                <%--@elvariable id="mohProcessDto" type="sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto"--%>
                <div class="form-group">
                    <label class="col-xs-12 col-md-4 control-label">Current Status</label>
                    <div class="col-sm-7 col-md-5 col-xs-10">
                        <p><iais:code code="${mohProcessDto.submissionDetailsInfo.applicationStatus}"/></p>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="form-group">
                    <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision / Recommendation <span style="color: red">*</span></label>
                    <div class="col-sm-7 col-md-5 col-xs-10">
                        <div class="input-group">
                            <select name="processingDecision" class="processingDecisionDropdown" id="processingDecision">
                                <option value="">Please Select</option>
                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_SCREENED_BY_DO_PROCEED_TO_NEXT_STAGE}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_SCREENED_BY_DO_PROCEED_TO_NEXT_STAGE}">selected="selected"</c:if>>Screened by Duty Officer. Proceed to next stage.</option>
                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}">selected="selected"</c:if>>Request for Information</option>
                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">selected="selected"</c:if>>Reject</option>
                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_INSPECTION_AND_CERTIFICATION}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_INSPECTION_AND_CERTIFICATION}">selected="selected"</c:if>>Inspection/Certification</option>
                            </select>
                            <span data-err-ind="processingDecision" class="error-msg" ></span>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <div id="insAndCerDiv" style="display: ${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_INSPECTION_AND_CERTIFICATION ? '' : 'none'};">
                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label">Will MOH conduct on-site inspection? <span style="color: red">*</span></label>
                        <div class="col-sm-7 col-md-5 col-xs-10 control-label">
                            <div class="form-check col-xs-4 col-sm-5 col-lg-3" style="margin-top: 6px; padding: 0;">
                                <input class="form-check-input" type="radio" id="inspectionRequiredY" name="inspectionRequired" value = "${MasterCodeConstants.YES}" <c:if test="${MasterCodeConstants.YES eq mohProcessDto.inspectionRequired}">checked="checked"</c:if>/>
                                <label for="inspectionRequiredY" class="form-check-label"><span class="check-circle"></span>Yes</label>
                            </div>
                            <div class="form-check col-xs-4 col-sm-5 col-lg-3" style="margin-top: 6px; padding: 0;">
                                <input class="form-check-input" type="radio" id="inspectionRequiredN" name="inspectionRequired" value = "${MasterCodeConstants.NO}" <c:if test="${MasterCodeConstants.NO eq mohProcessDto.inspectionRequired}">checked="checked"</c:if>/>
                                <label for="inspectionRequiredN" class="form-check-label"><span class="check-circle"></span>No</label>
                            </div>
                            <span data-err-ind="inspectionRequired" class="error-msg" ></span>
                        </div>
                    </div>
                    <c:if test="${mohProcessDto.facilityDetailsInfo.classification eq 'FACCLA001' or mohProcessDto.facilityDetailsInfo.classification eq 'FACCLA002'}">
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Will MOH-AFC conduct the certification? <span style="color: red">*</span></label>
                            <div class="col-sm-7 col-md-5 col-xs-10 control-label">
                                <div class="form-check col-xs-4 col-sm-5 col-lg-3" style="margin-top: 6px; padding: 0;">
                                    <input class="form-check-input" type="radio" id="certificationRequiredY" name="certificationRequired" value = "${MasterCodeConstants.YES}" <c:if test="${MasterCodeConstants.YES eq mohProcessDto.certificationRequired}">checked="checked"</c:if>/>
                                    <label for="certificationRequiredY" class="form-check-label"><span class="check-circle"></span>Yes</label>
                                </div>
                                <div class="form-check col-xs-4 col-sm-5 col-lg-3" style="margin-top: 6px; padding: 0;">
                                    <input class="form-check-input" type="radio" id="certificationRequiredN" name="certificationRequired" value = "${MasterCodeConstants.NO}" <c:if test="${MasterCodeConstants.NO eq mohProcessDto.certificationRequired}">checked="checked"</c:if>/>
                                    <label for="certificationRequiredN" class="form-check-label"><span class="check-circle"></span>No</label>
                                </div>
                                <span data-err-ind="certificationRequired" class="error-msg" ></span>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="form-group" id="selectMohUserDiv" <c:if test="${mohProcessDto.processingDecision ne MasterCodeConstants.MOH_PROCESS_DECISION_SCREENED_BY_DO_PROCEED_TO_NEXT_STAGE}">style="display: none;"</c:if>>
                    <label for="selectMohUser" class="col-xs-12 col-md-4 control-label">Select Approving Officer <span style="color: red">*</span></label>
                    <div class="col-sm-7 col-md-5 col-xs-10">
                        <div class="input-group">
                            <select name="selectMohUser" class="selectMohUserDropdown" id="selectMohUser">
                                <option value="">Please Select</option>
                                <c:forEach var="selection" items="${mohProcessDto.selectRouteToMoh}">
                                    <option value="${selection.value}" <c:if test="${mohProcessDto.selectMohUser eq selection.value}">selected="selected"</c:if>>${selection.text}</option>
                                </c:forEach>
                            </select>
                            <span data-err-ind="selectMohUser" class="error-msg" ></span>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <common:rfi-new processingDecision="${mohProcessDto.processingDecision}" commentsToApplicant="${mohProcessDto.commentsToApplicant}" pageAppEditSelectDto="${mohProcessDto.pageAppEditSelectDto}">
                </common:rfi-new>
                <div class="form-group">
                    <label for="remarks" class="col-xs-12 col-md-4 control-label">Remarks</label>
                    <div class="col-sm-7 col-md-5 col-xs-10">
                        <div class="input-group">
                            <textarea id="remarks" name="remarks" cols="70" rows="7" maxlength="1000"><c:out value="${mohProcessDto.remarks}"/></textarea>
                            <span data-err-ind="remark" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="form-group" id="reasonForRejectionDiv" <c:if test="${mohProcessDto.processingDecision ne MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">style="display: none;"</c:if>>
                    <label for="reasonForRejection" class="col-xs-12 col-md-4 control-label">Reason for rejection <span style="color: red">*</span></label>
                    <div class="col-sm-7 col-md-5 col-xs-10">
                        <div class="input-group">
                            <textarea id="reasonForRejection" name="reasonForRejection" cols="70" rows="7" maxlength="1000"><c:out value="${mohProcessDto.reasonForRejection}"/></textarea>
                            <span data-err-ind="reasonForRejection" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <div id="errorApprovalNone" style="display: none">
                    <span class="error-msg">Please approve at least one Approval</span>
                </div>
            </div>

            <%@include file="../common/footer.jsp" %>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/jsp/iais/common/processHistory.jsp" %>