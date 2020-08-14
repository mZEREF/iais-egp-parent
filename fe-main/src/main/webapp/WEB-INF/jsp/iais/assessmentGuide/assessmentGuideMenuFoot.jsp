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
<!-- Modal -->
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
<!--Modal End-->
</form>
</div>

<script>
    $(function () {
        $('.self-assessment-gp .self-assessment-item .table-gp .table input[type="radio"]').on('change', function() {
            $('.self-assessment-gp .self-assessment-item .table-gp .table tr.selectedRow').removeClass('selectedRow');
        });

        if ('${licIsRenewed}' || '${licIsAppealed}' || '${licIsAmend}') {
            $('#isRenewedModal').modal('show');
        }
    });

    function guideSubmit(guideAction,toWhere){
        showWaiting();
        if("main" == toWhere){
            $("[name='crud_action_type']").val(guideAction);
        }else if ("second" == toWhere) {
            $("[name='guide_action_type']").val(guideAction);
        }
        $("#mainForm").submit();
    }

    $("#applyLicence").click(function(){
        guideSubmit("new1","main");
    });

    $("#withdrawApplication").click(function(){
        guideSubmit("withdraw","main");
    });

    $("#resumeDraftApplication").click(function () {
        guideSubmit("resume","main");
    });

    $("#renewLicence1").click(function () {
        guideSubmit("renew","main");
    });

    $("#renewLicence2").click(function () {
        guideSubmit("renewUp","main");
    });

    $("#ceaseLicence").click(function(){
        guideSubmit("cease","main");
    });

    $("#amendLicence1_1").click(function(){
        guideSubmit("amend1_1","main");
    });

    $("#amendLicence1_2").click(function(){
        guideSubmit("amend1_2","main");
    });

    $("#amendLicence2").click(function(){
        guideSubmit("amend2","main");
    });

    $("#amendLicence3_1").click(function(){
        guideSubmit("amend3_1","main");
    });

    $("#amendLicence3_2").click(function(){
        guideSubmit("amend3_2","main");
    });

    $("#amendLicence4_1").click(function(){
        guideSubmit("amend4_1","main");
    });

    $("#amendLicence4_2").click(function(){
        guideSubmit("amend4_2","main");
    });
    $("#updateAdminPersonnel").click(function(){
        guideSubmit("upAdmin","main");
    });
</script>