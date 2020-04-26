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
<%@ include file="commonFile.jsp" %>
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
        margin-left: -5px;
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

    function scrollIntoAppView() {
        $("#appForm")[0].scrollIntoView(true);
    }

    function doSearchApp() {
        submit("appSearch");
    }

    function doAppClear(){
        $("[name='appNoPath']").val("");
        $("[name='eed']").val("");
        $("[name='esd']").val("");
        $("#appTypeSelect option:first").prop("selected", 'selected').val("All");
        $("#appStatusSelect option:first").prop("selected", 'selected').val("All");
        $("#appServiceType option:first").prop("selected", 'selected').val("All");
        $("#clearBody .current").text("All");
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
        if ('${appIsAppealed}' || '${appCannotRecall}') {
            $('#isAppealModal').modal('show');
            window.setTimeout(function(){
                $('#isAppealModal').modal('hide');
            },5000);
        }
    });

    function LimitDeadline(startDate) {
        $("#eed").attr("startDate",startDate);
        $("#eed").attr("data-date-start-date",startDate);
    }

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