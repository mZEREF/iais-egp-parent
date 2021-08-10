function doClear() {
    $("#selectSearchChkMsg").hide();
    $('input[name="searchNo"]').val("");
    $('input[type="radio"]').prop("checked", false);
}

function doAdvancedSearch() {
    showWaiting();
    var chk = $("[name='searchChk']:checked");
    var dropIds = new Array();
    chk.each(function () {
        dropIds.push($(this).val());
    });
    if (dropIds.length === 0) {
        $("#selectSearchChkMsg").show();
        dismissWaiting();
    } else {
        SOP.Crud.cfxSubmit("mainForm", "advSearch");
    }
}


function doSearch() {
    showWaiting();
    var chk = $("[name='searchChk']:checked");
    var dropIds = new Array();
    chk.each(function () {
        dropIds.push($(this).val());
    });
    if (dropIds.length === 0) {
        $("#selectSearchChkMsg").show();
        dismissWaiting();
    } else {
        SOP.Crud.cfxSubmit("mainForm", "doSearch");
    }
}

function doAppInfo() {
    showWaiting();
    SOP.Crud.cfxSubmit("mainForm", "appDetail");
}

$(function (){
    $('select[name="scheduleType"]').change(function (){
        var schedule = $('select[name="scheduleType"] option:selected').val();
        $.post('/bsb-be/bio-info/bio.do',
            {schedule: schedule},
            function (data){
                var result = data.result;
                if(result == 'success'){
                    var queryResult = data.queryResult;
                    console.log(queryResult);
                    var optionString = "";
                    var optionString1 = "";
                    for (var i = 0; i < queryResult.length; i++) {
                        optionString += "<option value=\""  + queryResult[i] + "\">" + queryResult[i] + "</option>";
                        optionString1+= "<li data-value=\""+queryResult[i]+"\" class=\"option\">"+queryResult[i]+"</li>"
                    }
                    $("select[name = 'biologicalAgent']").html("<option value=\"\">Please select<\/option>"+optionString);
                    $("select[name = 'biologicalAgent']").next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>"+optionString1);
                }else{

                }
            }
        )
    })
})

function doAdvSearch() {
    showWaiting();
    SOP.Crud.cfxSubmit("mainForm", "doAdvSearch");
}

function doBack() {
    showWaiting();
    SOP.Crud.cfxSubmit("mainForm", "back");
}

function doAdvClear() {
    $('input[type="text"]').val("");
    $('select[firstOption="Please Select"] option:first').prop("selected",'selected');
    $(".current").text("Please Select");
    $("#error_to_date").hide();
    $("#error_start_to_date").hide();
    $("#error_expiry_date").hide();
}

function doAdvAfterSearch() {
    showWaiting();
    SOP.Crud.cfxSubmit("mainForm", "afterSearch");
}

function doAdvAfterClear() {
    $('input[type="text"]').val("");
    $('select[firstOption="Please Select"] option:first').prop("selected",'selected');
    $("input[name = 'from_date']").val("");
    $("input[name = 'to_date']").val("");
    $("input[name = 'approval_from_date']").val("");
    $("input[name = 'approval_to_date']").val("")
    $("input[name = 'approval_sub_from_date']").val("");
    $("input[name = 'approval_sub_To_date']").val("");
    $("input[name = 'approved_from_date']").val("");
    $("input[name = 'approved_to_date']").val("");
    $("input[name = 'fac_expiry_from_date']").val("");
    $("input[name = 'fac_expiry_to_date']").val("");
}
