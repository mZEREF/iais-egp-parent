<iais:row>
    <iais:field width="6" cssClass="col-md-6" value="Was a Disposal?" mandatory="true"/>
    <iais:value cssClass="col-md-3" >
        <div class="form-check" style="padding-left: 0;">
            <input class="form-check-input"
                   type="radio"
                   name="hasDisposal"
                   value="1"
                   id="hasDisposalYes"
                   <c:if test="${arSuperDataSubmissionDto.disposalStageDto ne null}">checked</c:if>
                   aria-invalid="false">
            <label class="form-check-label"
                   for="hasDisposalYes"><span
                    class="check-circle"></span>Yes</label>
        </div>
    </iais:value>
    <iais:value cssClass="col-md-3" >
        <div class="form-check" style="padding-left: 0;">
            <input class="form-check-input" type="radio"
                   name="hasDisposal"
                   value="0"
                   id="hasDisposalNo"
                   <c:if test="${arSuperDataSubmissionDto.disposalStageDto eq null}">checked</c:if>
                   aria-invalid="false">
            <label class="form-check-label"
                   for="hasDisposalNo"><span
                    class="check-circle"></span>No</label>
        </div>
    </iais:value>
</iais:row>