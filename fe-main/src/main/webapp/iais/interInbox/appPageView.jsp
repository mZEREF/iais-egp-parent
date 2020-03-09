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

<%@ include file="app/appDashboard.jsp" %>
<%@ include file="app/appMainContent.jsp" %>
<style>
    .table-info-display {
        margin: 20px 0px 5px 0px;
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

    .nav ul.pagination {
        padding-top: 7px;
    }

    .nav ul.pagination > li {
        padding-left: 3px;
    }

    .dashboard-gp .dashboard-tile-item .dashboard-tile h1.dashboard-count {
        margin-left: -12px;
    }
</style>
<script type="text/javascript">
    function submit(action) {
        $("[name='app_action_type']").val(action);
        $("#appForm").submit();
    }

    function appToMsgPage() {
        submit("appToMsg");
    }

    function appToLicPage() {
        submit("appToLic");
    }

    function doSearchApp() {
        submit("appSearch");
    }

    function jumpToPagechangePage() {
        submit('appPage');
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('appSort');
    }

    function doDraft(appNo,appType,appStatus) {
        if ('APST008' == appStatus) {
            showWaiting();
            $("[name='action_no_value']").val(appNo);
            $("[name='action_type_value']").val(appType);
            submit('appDraft');
        }
    }

    function doAppAction(appId,appNo,actionName) {
        if ("Withdraw" == actionName) {
            showWaiting();
            $("[name='action_no_value']").val(appNo);
            $("[name='action_id_value']").val(appId);
            submit("appRenew");
        }if ("Appeal" == actionName) {
            showWaiting();
            $("[name='action_no_value']").val(appNo);
            $("[name='action_id_value']").val(appId);
            submit("appDoAppeal");
        }
    };

    function doDraftAction(appNo, val) {
        var action = val;
        if ("Reload" == action) {
            $("[name='action_no_value']").val(appNo);
            submit('appDraft');
        }

        if ("Delete" == action) {
            $("[name='action_no_value']").val(appNo);
        }
    }
</script>