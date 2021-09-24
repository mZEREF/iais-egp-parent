<style>
    .cgo-header {
        font-size: 18px;
    }
</style>
<div class="application-tab-footer">
    <div class="row">
        <div class="col-xs-12 col-sm-6">
            <a class="back" id="Back" href="/main-web/eservice/INTERNET/NewApplicationForAFC"><em
                    class="fa fa-angle-left"></em> Back</a>
        </div>
        <div class="col-xs-12 col-sm-6">
            <div class="button-group">
                <a class="btn btn-secondary" id="SaveDraft" href="javascript:void(0);">Save as Draft</a>
                <a class="btn btn-primary" id="Cancle" href="/main-web/eservice/INTERNET/MohInternetInbox">Cancel</a>
                <a class="btn btn-primary" id="Next" href="javascript:void(0);">Next</a>
                <input name="nextStep" value="" type="hidden">
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        /*$('#Back').click(function () {
            // showWaiting();
        });*/

        /*$('#SaveDraft').click(function () {
            showWaiting();
            submitForms('${serviceStepDto.currentStep.stepCode}', 'saveDraft', null, controlFormLi);
        });*/

        $('#Next').click(function () {
            var num = 0;
            num = validateOrganization(num);
            if (num == 0) {
                showWaiting();
                var mainForm = document.getElementById("mainForm");
                mainForm.submit();
            }else{

            }
        });
    });

</script>