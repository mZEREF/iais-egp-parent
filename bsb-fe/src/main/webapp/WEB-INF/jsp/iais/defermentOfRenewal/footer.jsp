<div class="form-group " style="z-index: 10">
    <div class="row">
        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
            <input type="checkbox" name="check" id="check" value="Y" <c:if test="${deferRenewDto.check eq 'Y'}">checked="checked"</c:if> />
        </div>
        <div class="col-xs-10 control-label">
            <label for="check" style="display: block;">
                I, hereby declare that all the information I have provided here is true and accurate. If any of the information given herein changes or becomes inaccurate in any way, I shall immediately notify MOH Biosafety Branch of such change or inaccuracy
            </label>
            <span data-err-ind="check" class="error-msg"></span>
        </div>
    </div>
</div>
<div class="application-tab-footer">
    <div class="row">
        <div class="col-xs-12 col-sm-6 ">
            <a class="back" id="previous" href="/bsb-web/eservice/INTERNET/MohBSBInboxFac"><em class="fa fa-angle-left"></em> Previous</a>
        </div>
        <div class="col-xs-12 col-sm-6">
            <div class="button-group">
                <a class="btn btn-primary next" onclick="doSubmit()">SUBMIT</a>
            </div>
        </div>
    </div>
</div>