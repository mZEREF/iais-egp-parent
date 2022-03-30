$(function () {
    $("#next").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("next");
        $("#mainForm").submit();
    });

    $("#saveDraft").click(function () {
        showWaiting();
        $("input[name='action_type']").val("draft");
        $("#mainForm").submit();
    });

    $("#back").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("back");
        $("#mainForm").submit();
    });

    $("a[data-step-key]").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val($(this).attr("data-step-key"));
        $("#mainForm").submit();
    })

    // facility classification radio button changes
   $("input[name=sameAddress]").change(function (){
       var id = $(this).attr("id");
       if(id === 'sameAddressY'){
           $("#sameAddressSection").show();
           $("#notSameAddressSection").hide();
           $("#registeredLocal").attr("checked","checked");
           $("#registeredOverseas").removeAttr("checked").attr("disabled",true);
       }else if(id === 'sameAddressN'){
           $("#sameAddressSection").hide();
           $("#notSameAddressSection").show();
           $("#registeredOverseas").removeAttr("disabled");
       }
   })

    $("input[name=registered]").change(function (){
        var id = $(this).attr("id");
        if(id === 'registeredLocal'){
            $("#localRegistered").show();
            $("#overseasRegistered").hide();
        }else if(id === 'registeredOverseas'){
            $("#localRegistered").hide();
            $("#overseasRegistered").show();
        }
    })


    $("#addressType").change(function (){
        var addressType = $(this).val();
        if(addressType === 'ADDTY001'){
            $(".aptBlk").each(function (){
                $(this).show();
            })
        }else if(addressType === 'ADDTY002' || addressType === ''){
            $(".aptBlk").each(function (){
                $(this).hide();
            })
        }
    })


});