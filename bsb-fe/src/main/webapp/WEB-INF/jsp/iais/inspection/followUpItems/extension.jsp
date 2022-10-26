<%--@elvariable id="followUpViewDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpViewDto"--%>
<div class="form-group" style="margin-top: 25px;">
    <div class="col-xs-4 control-label">
        <label for="requestExtension">Request extension of due date <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Request for extension of due date must be supported by strong justifications and is subject to approval by MOH. Please provide the reason for the request and the estimated duration of extension required.</p>">i</a></label>
        <span data-err-ind="declare" class="error-msg"></span>
    </div>
    <div class="col-xs-1" style="padding: 10px 0 20px 15px;">
        <input type="checkbox" name="requestExtension" id="requestExtension" value="Y" <c:if test="${followUpViewDto.requestExtension eq 'Y'}">checked="checked"</c:if> />
    </div>
</div>
<div class="form-group" id="reasonMandatory" <c:if test="${followUpViewDto.requestExtension ne 'Y'}">style="display: none"</c:if>>
    <div class="col-xs-4 control-label">
        <label for="reasonForExtension">Reason for extension</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-xs-6">
        <textarea maxLength="300" class="col-xs-12" name="reasonForExtension" id="reasonForExtension" rows="5"><c:out value="${followUpViewDto.reasonForExtension}"/></textarea>
        <span data-err-ind="reasonForExtension" class="error-msg"></span>
    </div>
</div>