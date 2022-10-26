$(function (){

    if(haveSuitableDraftData){
        showWaiting();
        $("#existDraftModal").modal('show');
    }

    $("input[name=appointed]").change(function (){
        var id = $(this).attr("id");
        if(id === 'hasAppointedCertifier'){
            $("#appointedCertifierSection").show();
        } else if(id === 'notAppointedCertifier'){
            $("#appointedCertifierSection").hide();
        }
    });

    $("select[name=afc]").change(function (){
        var afc = $(this).val();
        var isShow = false;
        for (const item of lastTwoAfcJson) {
            if(item === afc){
                isShow = true;
            }
        }
        if(isShow) {
            $("#afcReasonSection").show();
        } else {
            $("#afcReasonSection").hide();
        }
    });

    $("#submit").click(function () {
        showWaiting();
        $("input[name='action_type']").val("submit");
        $("#mainForm").submit();
    });

    $("#saveDraft").click(function () {
        showWaiting();
        $("input[name='action_type']").val("draft");
        $("#mainForm").submit();
    });

    $("#modal-resume").click(function (){
        $('#existDraftModal').modal('hide');
        showWaiting();
        $("input[name='action_type']").val("page");
        $("input[name='action_load_draft']").val("N");
        $("#mainForm").submit();
    });


    $("#modal-continue").click(function (){
        $('#existDraftModal').modal('hide');
        showWaiting();
        $("input[name='action_type']").val("page");
        $("input[name='action_load_draft']").val("Y");
        $("#mainForm").submit();
    });
});

function printAfcSelection(id) {
    window.open("/bsb-web/eservice/INTERNET/MohBsbAfcSelection/1/Print?printId=" + id);
}