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

    function clickMenu(menuName,pageMenu) {
        if ("msgPageMenu" == pageMenu) {
            if ("Renew a Licence" == menuName
                || "Request to Cease a Licence" == menuName
                || "Amend a Licence" == menuName) {
                msgToLicPage();
                $.cookie('menuToPage', 'licPage');
            } else if ("Edit a Draft Application" == menuName
                || "Withdraw an Application" == menuName) {
                msgToAppPage();
                $.cookie('menuToPage', 'appPage');
            }
        } else if ("appPageMenu" == pageMenu) {
            if ("Renew a Licence" == menuName
                || "Request to Cease a Licence" == menuName
                || "Amend a Licence" == menuName) {
                appToLicPage();
                $.cookie('menuToPage', 'licPage');
            }
        } else if ("licPageMenu" == pageMenu) {
            if ("Renew a Licence" == menuName
                || "Request to Cease a Licence" == menuName
                || "Amend a Licence" == menuName) {
            }
            else if ("Edit a Draft Application" == menuName
                || "Withdraw an Application" == menuName) {
                licToAppPage();
                $.cookie('menuToPage', 'appPage');
            }
        }
    }

    $(".appdraftNo").click(function () {
        var appNo = $(this).closest("tr").find(".appdraftNo").html();
        var appType = $(this).closest("tr").find(".apptype").html();
        var appStatus = $(this).closest("tr").find(".appStatus").html();
        showWaiting();
        $("[name='action_no_value']").val(appNo);
        $("[name='action_type_value']").val(appType);
        if ("Recalled" == appStatus){
            submit("appToAppView");
        }else{
            submit('appDraft');
        }
    });

    $(".appNo").click(function () {
        var appNo = $(this).closest("tr").find(".appNo").html();
        var appType = $(this).closest("tr").find(".apptype").html();
        var appGrpId = $(this).closest("tr").find(".appGroupId").html();
        var appPmtStatus = $(this).closest("tr").find(".appPmtStatus").html();
        showWaiting();
        $("[name='action_no_value']").val(appNo);
        $("[name='action_type_value']").val(appType);
        if ('PMT06' == appPmtStatus){
            $("[name='action_grp_value']").val(appGrpId);
            $("[name='action_self_value']").val('appMakePayment');
        }
        submit("appToAppView");
    })

     function goToSubmission(){
         window.location = "${pageContext.request.contextPath}/main-web/eservice/INTERNET/MohDataSubmissionsInbox";
     }
</script>