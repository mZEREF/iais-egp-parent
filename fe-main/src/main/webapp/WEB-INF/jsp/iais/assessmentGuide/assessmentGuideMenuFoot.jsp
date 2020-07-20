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
</form>
</div>

<script>
    $(function () {
        $('.self-assessment-gp .self-assessment-item .table-gp .table input[type="radio"]').on('change', function() {
            $('.self-assessment-gp .self-assessment-item .table-gp .table tr.selectedRow').removeClass('selectedRow');
        });
    });

    function guideSubmit(guideAction,toWhere){
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
</script>