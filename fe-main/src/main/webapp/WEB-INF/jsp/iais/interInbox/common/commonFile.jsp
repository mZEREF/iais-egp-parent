<style>
    .table-info-display {
        margin: 20px 15px 25px 12px;
        background: #efefef;
        padding: 8px;
        border-radius: 8px;
        -moz-border-radius:8px;
        -webkit-border-radius:8px;

    }

    .table-count {
        float: left;
        margin-top: 5px;
    }
    .nav {background:#transparent;}
    .nav ul.pagination{
        padding: 10px;
    }

    .nav ul.pagination > li{
        padding-left: 3px;
    }

    .dashboard-gp .dashboard-tile-item .dashboard-tile h1.dashboard-count {
        margin-left: -5px;
    }
</style>
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
         window.location = "${pageContext.request.contextPath.concat(RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohDataSubmissionsInbox",request))}";
     }
</script>