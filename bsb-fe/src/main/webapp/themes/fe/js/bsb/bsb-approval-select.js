$(function (){
    $("#facilityId").change(function (){
        var facilityId = $(this).val();
        if (facilityId){
            $.post(
                "/bsb-fe/bsbApprovalSelect/facilityIdSelect",
                {facilityId:facilityId},
                function (data){
                    var result = data.result;
                    if(result == 'success'){
                        var queryResult = data.queryResult;
                        console.log(queryResult);
                        var optionString = "";
                        var optionString1 = "";
                        for (var i = 0; i < queryResult.length; i++) {
                            optionString += "<option value=\""  + queryResult[i] + "\">" + queryResult[i] + "</option>";
                            optionString1+= "<li data-value=\""+queryResult[i].id+"\" class=\"option\">"+queryResult[i].activityType+"</li>"
                        }
                        $("select[name = 'activityType']").html("<option value=\"\">Please select<\/option>"+optionString);
                        $("select[name = 'activityType']").next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>"+optionString1);
                    }else{

                    }
                }
            )
        }
    })
})