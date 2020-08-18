</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
<div class="modal fade" id="isRenewedModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:60%; overflow: visible;bottom: inherit;right: inherit;">
    <div class="modal-dialog" role="document" style="width: 760px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-body" style="text-align: center">
                <div class="row">
                    <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem;">${LAEM}</span></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
</form>
</div>
<script>
    function guideSubmit(guideAction,toWhere){
        showWaiting();
        if("main" == toWhere){
            $("[name='crud_action_type']").val(guideAction);
        }else if ("second" == toWhere) {
            $("[name='guide_action_type']").val(guideAction);
        }
        $("#mainForm").submit();
    }

    function forwordMain(toWhere){
        showWaiting();
        window.location.href = "/main-web/eservice/INTERNET/MohAccessmentGuide/"+toWhere;
    }
    $("#applyLicence").click(function(){
        forwordMain("newApp1");
    });

    $("#withdrawApplication").click(function(){
        forwordMain("withdrawApp");
    });

    $("#resumeDraftApplication").click(function () {
        forwordMain("resumeDraftApp");
    });

    $("#renewLicence1").click(function () {
        forwordMain("renewLicUpdate");
    });

    $("#renewLicence2").click(function () {
        forwordMain("renewLicUpdate");
    });

    $("#ceaseLicence").click(function(){
        forwordMain("ceaseLic");
    });

    $("#amendLicence1_1").click(function(){
        forwordMain("amendLic1_1");
    });

    $("#amendLicence1_2").click(function(){
        forwordMain("amendLic1_2");
    });

    $("#amendLicence2").click(function(){
        forwordMain("amendLic2");
    });

    $("#amendLicence3_1").click(function(){
        forwordMain("amendLic3_1");
    });

    $("#amendLicence3_2").click(function(){
        forwordMain("amendLic3_2");
    });

    $("#amendLicence4_1").click(function(){
        forwordMain("amendLic4_1");
    });

    $("#amendLicence4_2").click(function(){
        forwordMain("amendLic4_2");
    });

    $("#updateAdminPersonnel").click(function(){
        forwordMain("updateAdminPers");
    });
</script>