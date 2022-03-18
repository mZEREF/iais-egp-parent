<c:if test="${empty declaration}">
    <c:set var="dataSubmissionDto" value="${dpSuperDataSubmissionDto.dataSubmissionDto}" />
</c:if>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#ar-declaration">
                Declaration
            </a>
        </h4>
    </div>
    <div id="ar-declaration" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <div class="col-xs-12">Please indicate if this is a late submission and provide reasons below if yes.</div>
                    <div class="form-check col-md-3 col-xs-3">
                        <input class="form-check-input" <c:if test="${dataSubmissionDto.declaration == 'Yes'}">checked="checked"</c:if>
                               type="radio" name="declaration" value="Yes" aria-invalid="false">
                        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                    </div>
                    <div class="form-check col-md-3 col-xs-3">
                        <input class="form-check-input" <c:if test="${dataSubmissionDto.declaration == 'No'}">checked="checked"</c:if>
                               type="radio" name="declaration" value="No" aria-invalid="false">
                        <label class="form-check-label" ><span class="check-circle"></span>No</label>
                    </div>
                    <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_declaration"></span>
                </iais:row>
                <iais:row>
                    <div class="col-xs-12">Reason for Late Submission</div>
                </iais:row>
                <iais:row>
                    <iais:value cssClass="col-sm-12 col-md-12 col-xs-12">
                        <textarea id="remarks" rows="6" maxlength="500" name="remarks" class="form-control">${dataSubmissionDto.remarks}</textarea>
                        <span class="error-msg" name="errorMsg" id="error_remarks"></span>
                    </iais:value>
                </iais:row>
                <div><iais:message key="DS_DEC002" escape="false"/></div>
            </div>
        </div>
    </div>
</div>