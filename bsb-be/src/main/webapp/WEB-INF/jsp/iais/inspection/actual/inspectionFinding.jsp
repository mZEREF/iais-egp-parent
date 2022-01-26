<%--@elvariable id="insFindingList" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsFindingFormDto"--%>
<%--@elvariable id="insOutcome" type="sg.gov.moh.iais.egp.bsb.dto.entity.InspectionOutcomeDto"--%>

<%@page import="sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants" %>

<%-- This select is used to init nice-select, if we don't have at least one nice-select,
     our js generated nice-select will not have click event --%>
<select name="doNotShow" style="display: none" disabled aria-label="forNiceSelect">
    <option value="error">ERROR</option>
</select>

<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <div>
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <th scope="col" style="width: 5%">S/N</th>
                        <th scope="col" style="width: 25%">Checklist Item</th>
                        <th scope="col" style="width: 15%">Finding Type</th>
                        <th scope="col" style="width: 30%">MOH Remarks</th>
                        <th scope="col" style="width: 15%">Deadline</th>
                        <th scope="col" style="width: 10%"></th>
                    </tr>
                    </thead>
                    <tbody id="findingTBody">
                    <c:if test="${not empty insFindingList.itemDtoList}">
                        <c:forEach var="finding" items="${insFindingList.itemDtoList}" varStatus="status">
                        <tr id="findingTr--v--${status.index}">
                            <td style="border: 0; padding-top: 5px; padding-bottom: 0">${status.index + 1}</td>
                            <td style="border: 0; padding-top: 5px; padding-bottom: 0">${finding.itemText}<span data-err-ind="itemValue--v--${status.index}" class="error-msg"></span></td>
                            <td style="border: 0; padding-top: 5px; padding-bottom: 0">
                                <select name="findingType--v--${status.index}" aria-label="finding type">
                                    <option value="${InspectionConstants.VALUE_FINDING_TYPE_NC}" <c:if test="${finding.findingType eq InspectionConstants.VALUE_FINDING_TYPE_NC}">selected="selected"</c:if>>Non-compliance</option>
                                    <option value="${InspectionConstants.VALUE_FINDING_TYPE_FOLLOW_UP}" <c:if test="${finding.findingType eq InspectionConstants.VALUE_FINDING_TYPE_FOLLOW_UP}">selected="selected"</c:if>>Follow-up item</option>
                                </select>
                                <span data-err-ind="findingType--v--${status.index}" class="error-msg"></span>
                            </td>
                            <td style="border: 0; padding-top: 5px; padding-bottom: 0"><textarea name="findingRemark--v--${status.index}" cols="40" rows="1" maxlength="300" aria-label="MoH remarks"><c:out value="${finding.remarks}"/></textarea>
                                <span data-err-ind="remarks--v--${status.index}" class="error-msg"></span>
                            </td>
                            <td style="border: 0; padding-top: 5px; padding-bottom: 0">
                                <input type="text" style="margin-bottom: 0" autocomplete="off" name="deadline--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${finding.deadline}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" aria-label="deadline"/>
                                <span data-err-ind="deadline--v--${status.index}" class="error-msg"></span>
                            </td>
                            <td style="border: 0; padding-top: 5px; padding-bottom: 0"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeFindingBtn"></em></td>
                        </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
                <div style="text-align: right">
                    <button name="addFinding" id="addFinding" type="button" class="btn btn-secondary">Add</button>
                    <button name="submitFindingBtn" id="submitFindingBtn" type="button" class="btn btn-primary">SUBMIT FINDINGS</button>
                </div>
            </div>

            <div class="alert alert-info" role="alert" style="margin-top: 20px"><strong>
                <h4>Outcome of Inspection</h4>
            </strong></div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-xs-12 col-md-4 control-label">Deficiency</label>
                    <div class="col-sm-7 col-md-5 col-xs-10">
                        <div class="input-group">
                            <div class="col-sm-4" style="margin-top: 8px">
                                <input type="radio" name="deficiency" id="majorDeficiency" value="${InspectionConstants.VALUE_OUTCOME_DEFICIENCY_MAJOR}" <c:if test="${insOutcome.deficiency eq InspectionConstants.VALUE_OUTCOME_DEFICIENCY_MAJOR}">checked="checked"</c:if> />
                                <label for="majorDeficiency" class="label-normal">Major</label>
                            </div>
                            <div class="col-sm-4" style="margin-top: 8px">
                                <input type="radio" name="deficiency" id="minorDeficiency" value="${InspectionConstants.VALUE_OUTCOME_DEFICIENCY_MINOR}" <c:if test="${insOutcome.deficiency eq InspectionConstants.VALUE_OUTCOME_DEFICIENCY_MINOR}">checked="checked"</c:if> />
                                <label for="minorDeficiency" class="label-normal">Minor</label>
                            </div>
                            <div class="col-sm-4" style="margin-top: 8px">
                                <input type="radio" name="deficiency" id="nilDeficiency" value="${InspectionConstants.VALUE_OUTCOME_DEFICIENCY_NIL}" <c:if test="${insOutcome.deficiency eq InspectionConstants.VALUE_OUTCOME_DEFICIENCY_NIL}">checked="checked"</c:if> />
                                <label for="nilDeficiency" class="label-normal">NIL</label>
                            </div>
                            <span data-err-ind="deficiency" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="form-group">
                    <label class="col-xs-12 col-md-4 control-label">Follow-up required</label>
                    <div class="col-sm-7 col-md-5 col-xs-10">
                        <div class="input-group">
                            <div class="col-sm-4" style="margin-top: 8px">
                                <input type="radio" name="followUpReq" id="needFollowUp" value="${InspectionConstants.VALUE_OUTCOME_FOLLOW_UP_REQUIRED_YES}" <c:if test="${insOutcome.followUpRequired eq InspectionConstants.VALUE_OUTCOME_FOLLOW_UP_REQUIRED_YES}">checked="checked"</c:if> />
                                <label for="needFollowUp" class="label-normal">Yes</label>
                            </div>
                            <div class="col-sm-4" style="margin-top: 8px">
                                <input type="radio" name="followUpReq" id="noNeedFollowUp" value="${InspectionConstants.VALUE_OUTCOME_FOLLOW_UP_REQUIRED_NO}" <c:if test="${insOutcome.followUpRequired eq InspectionConstants.VALUE_OUTCOME_FOLLOW_UP_REQUIRED_NO}">checked="checked"</c:if> />
                                <label for="noNeedFollowUp" class="label-normal">No</label>
                            </div>
                            <span data-err-ind="followUpRequired" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="form-group">
                    <label class="col-xs-12 col-md-4 control-label">Outcome</label>
                    <div class="col-sm-7 col-md-5 col-xs-10">
                        <div class="input-group">
                            <div class="col-sm-4" style="margin-top: 8px">
                                <input type="radio" name="outcome" id="passOutcome" value="${InspectionConstants.VALUE_OUTCOME_OUTCOME_PASS}" <c:if test="${insOutcome.outcome eq InspectionConstants.VALUE_OUTCOME_OUTCOME_PASS}">checked="checked"</c:if> />
                                <label for="passOutcome" class="label-normal">Pass</label>
                            </div>
                            <div class="col-sm-4" style="margin-top: 8px">
                                <input type="radio" name="outcome" id="passWithCondOutcome" value="${InspectionConstants.VALUE_OUTCOME_OUTCOME_PASS_WITH_CONDITION}" <c:if test="${insOutcome.outcome eq InspectionConstants.VALUE_OUTCOME_OUTCOME_PASS_WITH_CONDITION}">checked="checked"</c:if> />
                                <label for="passWithCondOutcome" class="label-normal">Pass with condition(s)</label>
                            </div>
                            <div class="col-sm-4" style="margin-top: 8px">
                                <input type="radio" name="outcome" id="failOutcome" value="${InspectionConstants.VALUE_OUTCOME_OUTCOME_FAIL}" <c:if test="${insOutcome.outcome eq InspectionConstants.VALUE_OUTCOME_OUTCOME_FAIL}">checked="checked"</c:if> />
                                <label for="failOutcome" class="label-normal">Fail</label>
                            </div>
                            <span data-err-ind="outcome" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="form-group">
                    <label for="remarks" class="col-xs-12 col-md-4 control-label">Remarks (if any)</label>
                    <div class="col-sm-7 col-md-5 col-xs-10">
                        <div class="input-group">
                            <textarea id="remarks" name="remarks" cols="60" rows="3" maxlength="300"><c:out value="${insOutcome.remarks}"/></textarea>
                            <span data-err-ind="remarks" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <div style="text-align: right">
                    <button name="submitOutcomeBtn" id="submitOutcomeBtn" type="button" class="btn btn-primary">SUBMIT OUTCOME</button>
                </div>
            </div>
        </div>
    </div>
</div>