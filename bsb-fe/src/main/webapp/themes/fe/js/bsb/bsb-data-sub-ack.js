function facChange(obj){
   var maskFacId  = $("#facSelect").val();
    if (maskFacId === 'Please Select' || maskFacId === '') {
        var dataSub = $("#dataSubNo" );
        dataSub.html("<option value=\"\">Please select<\/option>");
        dataSub.next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>");
    } else {
        $.post('/bsb-web/sub-info/sub.do',
            {maskFacId: maskFacId},
            function (data) {
                var result = data.result;
                if (result === 'success') {
                    var queryResult = data.queryResult;
                    console.log(queryResult);
                    var optionString = "";
                    var optionString1 = "";
                    for (var i = 0; i < queryResult.length; i++) {
                        optionString += "<option value=\"" + queryResult[i] + "\">" + queryResult[i] + "</option>";
                        optionString1 += "<li data-value=\"" + queryResult[i] + "\" class=\"option\">" + queryResult[i] + "</li>"
                    }
                    var dataSub = $("#dataSubNo" );
                    dataSub.html("<option value=\"\">Please select<\/option>" + optionString);
                    dataSub.next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>" + optionString1);
                } else {

                }
            }
        )
    }
}

$(function (){
//notification page
    $("#doConfirm").click(function (){
        showWaiting();
        $("[name='action_type']").val("doConfirm");
        $("#mainForm").submit();
    });

    //InnerFooter.jsp & notification page
    $("#saveButton").click(function (){
        showWaiting();
        $("[name='action_type']").val("doSave");
        $("#mainForm").submit();
    });

    // $("#saveDraft").click(function (){
    //     showWaiting();
    //     $("[name='action_type']").val("saveDraft");
    //     $("#mainForm").submit();
    // });

    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });

    $("#next").click(function () {
        var optionVal = $("#facSelect option:selected").val();
        var dataSubVal = $("#dataSubNo option:selected").val();
        if (optionVal === "") {
            $("#facSelectError").html("This is Mandatory");
        }if(dataSubVal === ""){
            $("#dataSubNoError").html("This is Mandatory");
        } else {
            showWaiting();
            $("[name='action_type']").val("doNext");
            $("#facSelectError").html("");
            $("#facId").val(optionVal);
            $("#mainForm").submit();
        }
    });

})
