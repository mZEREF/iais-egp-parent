$(function () {
    $("#searchBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("#mainForm").submit();
    });


    $("#clearBtn").click(function() {
        $('input[name="searchKeyword"]').val("");
        $('input[name="searchKeywordType"]').prop("checked",false);
        $("#searchFacilityClassification option:first").prop("selected", 'selected');
        $("#searchFacilityActivityType option:first").prop("selected", 'selected');
        $("#searchFacilityStatus option:first").prop("selected", 'selected');
        $("#taskListSearchFilter .current").text("All");
    });


    $("#commonRoleId").change(function () {
        showWaiting();
        $("[name='action_type']").val("changeRole");
        $("[name='action_value']").val(this.value);
        $("#mainForm").submit();
    });

    $("#reqInfoBtn").click(function (){
        if ($("input:radio:checked").length > 0) {
            showWaiting();
            $("[name='action_type']").val("adhocRfi");
            $("#mainForm").submit();
        } else {
            $('#adhocRfiAlert').modal('show');
        }
    });

    $("#newBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("new");
        $("#mainForm").submit();
    });
});

function adhocRfiCancel() {
    $('#adhocRfiAlert').modal('hide');
}

function doView(id){
    showWaiting();
    console.log(1)
    $("[name='action_type']").val("view");
    $("[name='action_value']").val(id);
    $("#mainForm").submit();
}

function doBack(){
    showWaiting();
    $("[name='action_type']").val("cancel");
    $("#mainForm").submit();
}
function doSubmit(){
    showWaiting();
    $("[name='action_type']").val("validate");
    $("#mainForm").submit();
}
function checkTitleInfo(){
    if($('input[type = checkbox][name="info"]').prop('checked')){
        $('#information').attr("style","display: block");
    }else {
        $("#information").attr("style","display: none");
    }
}
function checkTitleDoc(){
    if($('input[type = checkbox][name="doc"]').prop('checked')){
        $('#stDoc').attr("style","display: block");
    }else {
        $('#stDoc').attr("style","display: none");
    }
}