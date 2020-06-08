<script type="application/javascript">

    $(function () {
       var page = $.cookie("menuToPage");
       if ("licPage" == page) {
           scrollIntoLicView();
       }else if ("appPage" == page) {
           scrollIntoAppView();
       }
        $.cookie('menuToPage', '');
    });

    function clickMenu(menuName,pageMenu){
        if ("msgPageMenu" == pageMenu){
            if ("Renew a Licence" == menuName
                || "Request to Cease a Licence" == menuName
                || "Amend a Licence" == menuName){
                msgToLicPage();
                $.cookie('menuToPage', 'licPage');
            }else if("Edit a Drafted Application" == menuName
                || "Withdraw an Application" == menuName){
                msgToAppPage();
                $.cookie('menuToPage', 'appPage');
            }
        }else if ("appPageMenu" == pageMenu) {
            if ("Renew a Licence" == menuName
                || "Request to Cease a Licence" == menuName
                || "Amend a Licence" == menuName){
                appToLicPage();
                $.cookie('menuToPage', 'licPage');
            }
        }else if ("licPageMenu" == pageMenu) {
            if ("Renew a Licence" == menuName
                || "Request to Cease a Licence" == menuName
                || "Amend a Licence" == menuName){
            }
            else if("Edit a Drafted Application" == menuName
                || "Withdraw an Application" == menuName){
                licToAppPage();
                $.cookie('menuToPage', 'appPage');
            }
        }
    }

    $("#appDoActive").change(function () {
        var appNo = $(this).closest("tr").find(".appNo").html();
        var appId = $(this).closest("tr").find(".appId").html();
        var appType = $(this).closest("tr").find(".apptype").html();
        var action = $(this).val();
        if ("Continue" == action) {
            showWaiting();
            $("[name='action_no_value']").val($(this).closest("tr").find(".appdraftNo").html());
            $("[name='action_type_value']").val(appType);
            submit('appDraft');
        }
        if ("Appeal" == action) {
            showWaiting();
            $("[name='action_no_value']").val(appNo);
            $("[name='action_id_value']").val(appId);
            submit("appDoAppeal");
        }if ("Recall" == action) {
            showWaiting();
            $("[name='action_no_value']").val(appNo);
            $("[name='action_id_value']").val(appId);
            submit("appDoRecall");
        }
        if ("Withdraw" == action) {
            showWaiting();
            $("[name='action_no_value']").val(appNo);
            $("[name='action_id_value']").val(appId);
            submit("appRenew");
        }
    });


    $(".appdraftNo").click(function () {
        var appNo = $(this).closest("tr").find(".appdraftNo").html();
        var appType = $(this).closest("tr").find(".apptype").html();
        showWaiting();
        $("[name='action_no_value']").val(appNo);
        $("[name='action_type_value']").val(appType);
        submit('appDraft');
    });

    $(".appNo").click(function () {
        var appNo = $(this).closest("tr").find(".appNo").html();
        $("[name='action_no_value']").val(appNo);
        submit("appToAppView");
    })
</script>