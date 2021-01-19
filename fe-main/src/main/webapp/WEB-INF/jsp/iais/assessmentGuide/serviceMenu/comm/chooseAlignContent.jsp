<style>
    /*.choose-align-checkbox-gp{*/
        /*padding: 2px 14px;*/
        /*border:1px solid #666666;*/
        /*border-radius: 14px;*/
    /*}*/
    .margin-top-10{
        margin-top: 10px;
    }
</style>
<div class="row">
    <div class="col-xs-12 col-md-7">
        <h3>
            Do you want to align the expiry date of your new licence(s)?
        </h3>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-md-7">
        <div class="self-assessment-checkbox-gp gradient-light-grey">
            <div class="form-check" style="height: 10px;">
                <input class="form-check-input" type="radio" name="isAlign" value="1" aria-invalid="false" <c:if test="${appSelectSvc.align}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
        </div>
        <div class="self-assessment-checkbox-gp gradient-light-grey margin-top-10">
            <div class="form-check" style="height: 10px;">
                <input class="form-check-input" type="radio" name="isAlign" value="0" aria-invalid="false" <c:if test="${!appSelectSvc.align}">checked="checked"</c:if> >
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </div>
        </div>
    </div>
</div>