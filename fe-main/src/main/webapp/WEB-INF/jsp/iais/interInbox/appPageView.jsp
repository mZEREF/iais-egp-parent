<%@include file="common/commonImport.jsp"%>
<c:set var="tabCode" value="${appTab == 1 ? 'app' : ''}"/>
<%@ include file="common/commonDashboard.jsp" %>
<%@ include file="common/mainContent.jsp" %>
<%@ include file="common/commonFile.jsp" %>
<script type="text/javascript">
    function submit(action) {
        $("[name='app_action_type']").val(action);
        $("#appForm").submit();
    }

    function appToMsgPage() {
        showWaiting();
        submit("appToMsg");
    }

    function appToLicPage() {
        showWaiting();
        submit("appToLic");
    }

    function scrollIntoAppView() {
        $("#appForm")[0].scrollIntoView(true);
    }

    function doSearchApp() {
        showWaiting();
        submit("appSearch");
    }

    function doAppClear(){
        $("[name='appNoPath']").val("");
        $("[name='eed']").val("");
        $("[name='esd']").val("");
        $("#appTypeSelect option:first").prop("selected", 'selected').val("");
        $("#appStatusSelect option:first").prop("selected", 'selected').val("");
        $("#appServiceType option:first").prop("selected", 'selected').val("");
        $("#clearBody .current").text("All");
        $(".error-msg").text("")
    }

    function jumpToPagechangePage() {
        submit('appPage');
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('appSort');
    }
    $(function () {
        $(".appDoSelectActive").find('ul li').each(function (k,v) {
            if($(this).data('value') == 'Inspection'){
                $(this).css('padding-bottom','18px');
            }

            if($(this).data('value') == 'Make Payment'){
                $(this).css('padding-bottom','18px');
            }
        });

        if ('${appIsAppealed}') {
            $('#isAppealModal').modal('show');
        }
        if('${appCannotRecall}'){
            $('#isAppealModal').modal('show');
        }
        if('${appIsWithdrawal}'){
            $('#isAppealModal').modal('show');
        }

    });

    function LimitDeadline(startDate) {
        $("#eed").attr("startDate",startDate);
        $("#eed").attr("data-date-start-date",startDate);
    }


    $(".appDoSelectActive").change(function () {
        var appNo = $(this).closest("tr").find(".appNo").html();
        var appId = $(this).closest("tr").find(".appId").html();
        var appStatus = $(this).closest("tr").find(".appStatus").html();
        var appGrpId = $(this).closest("tr").find(".appGroupId").html();
        var appSelfFlag = $(this).closest("tr").find(".appSelfFlag").html();
        var appType = $(this).closest("tr").find(".apptype").html();
        var action = $(this).val();
        if ("Continue" == action) {
            showWaiting();
            $("[name='action_no_value']").val($(this).closest("tr").find(".appdraftNo").html());
            $("[name='action_type_value']").val(appType);
            $("[name='action_status_value']").val(appStatus);
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
            $("[name='action_grp_value']").val(appGrpId);
            $("[name='action_id_value']").val(appId);
            submit("appDoRecall");
        }
        if ("Withdraw" == action) {
            showWaiting();
            $("[name='action_no_value']").val(appNo);
            $("[name='action_id_value']").val(appId);
            submit("appRenew");
        }

        if ("Delete" == action) {
            $("[name='action_no_value']").val($(this).closest("tr").find(".appdraftNo").html());
            $('#deleteDraftModal').modal('show');
        }

        if ("Assessment" == action){
            showWaiting();
            $("[name='action_no_value']").val(appNo);
            $("[name='action_grp_value']").val(appGrpId);
            $("[name='action_self_value']").val(appSelfFlag);
            submit("doSelfAssMt");
        }

        if ("Inspection" == action){
            showWaiting();
            $("[name='action_no_value']").val(appNo);
            $("[name='action_grp_value']").val(appGrpId);
            $("[name='action_self_value']").val(appSelfFlag);
            submit("doInspection");
        }

        if ("Make Payment" == action) {
            $("[name='action_no_value']").val(appNo);
            $("[name='action_id_value']").val(appId);
            $("[name='action_grp_value']").val(appGrpId);
            $("[name='action_self_value']").val('appMakePayment');
            submit("appDoRecall");
        }
    });

    function appAjax(){
        $.ajax({
            data:{
                appNoPath:$("[name='appNoPath']").val(),
                appServiceType:$("[name='appServiceType']").val()
            },
            type:"POST",
            dataType: 'json',
            url:"/main-web/inbox/appInbox.do",
            success:function(data){
            }
        });
    }
</script>