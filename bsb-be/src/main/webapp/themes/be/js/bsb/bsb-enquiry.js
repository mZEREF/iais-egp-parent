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
        $("[name='action_type']").val("advSearch");
        $("#mainForm").submit();
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
        $("[name='action_type']").val("doSearch");
        $("#mainForm").submit();
    }
}

function doAppInfo() {
    showWaiting();
    $("[name='action_type']").val("appDetail");
    $("#mainForm").submit();
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
    $("[name='action_type']").val("doAdvSearch");
    $("#mainForm").submit();
}

function doAdvAfterSearch(){
    showWaiting();
    $("[name='action_type']").val("doAdvSearch");
    $("#mainForm").submit();
}

function doBack() {
    showWaiting();
    $("[name='action_type']").val("back");
    $("#mainForm").submit();
}

function doAdvClear() {
    $('input[type="text"]').val("");
    $('select[firstOption="Please Select"] option:first').prop("selected",'selected');
    $(".current").text("Please Select");
    $("#error_to_date").hide();
    $("#error_start_to_date").hide();
    $("#error_expiry_date").hide();
    $("#clearFilterForSearch input[type='checkbox']").prop('checked', false);
    $("#clearFilterForSearch .multi-select-button").html("-- Select --");
    $("#facilityType option").prop('selected',false);
    $("#natureOfTheSample option").prop('selected',false);
}

function doAdvAfterSearch() {
    showWaiting();
    $("[name='action_type']").val("afterSearch");
    $("#mainForm").submit();
}

function doAdvAfterClear() {
    showWaiting();
    $('input[type="text"]').val("");
    $('#applicationType option:first').prop("selected", true);
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
    $("#clearFilterForSearch input[type='checkbox']").prop('checked', false);
    $("#clearFilterForSearch .multi-select-button").html("-- Select --");
    $("#facilityType option").prop('selected',false);
    $("#natureOfTheSample option").prop('selected',false);
}
