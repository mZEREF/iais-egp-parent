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
</script>