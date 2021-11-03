<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Declarations
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <div class="col-xs-12"><iais:message key="DS_DEC001" /></div>
                    <div class="form-check col-md-3 col-xs-3">
                        <input class="form-check-input" <c:if test="${declaration == 'Yes'}">checked="checked"</c:if>
                               type="radio" name="declaration" value="Yes" aria-invalid="false">
                        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                    </div>
                    <div class="form-check col-md-3 col-xs-3">
                        <input class="form-check-input" <c:if test="${declaration == 'No'}">checked="checked"</c:if>
                               type="radio" name="declaration" value="No" aria-invalid="false">
                        <label class="form-check-label" ><span class="check-circle"></span>No</label>
                    </div>
                    <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_declaration"></span>
                </iais:row>
            </div>
        </div>
    </div>
</div>