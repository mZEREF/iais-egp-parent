<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>


<c:set var="tabCode" value="app"/>
<%@ include file="common/commonDashboard.jsp" %>
<%@ include file="common/mainContent.jsp" %>
<%@ include file="commonFile.jsp" %>
<style>
    .table-info-display {
        margin: 20px 15px 25px 12px;
        background: #efefef;
        padding: 8px;
        border-radius: 8px;
        -moz-border-radius: 8px;
        -webkit-border-radius: 8px;
    }

    .table-count {
        float: left;
        margin-top: 5px;
    }
    .nav {background:#transparent;}
    .nav ul.pagination {
        padding: 10px;
    }

    .nav ul.pagination > li {
        padding-left: 3px;
    }

    .dashboard-gp .dashboard-tile-item .dashboard-tile h1.dashboard-count {
        margin-left: -5px;
    }
</style>
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