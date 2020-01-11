<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
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
        -moz-border-radius:8px;
        -webkit-border-radius:8px;

    }

    .table-count {
        float: left;
        margin-top: 5px;
    }
</style>
<script type="text/javascript">
    function submit(action){
        $("[name='app_action_type']").val(action);
        $("#appForm").submit();
    }

    function appToMsgPage(){
        submit("appToMsg");
    }

    function appToLicPage(){
        submit("appToLic");
    }

    function doSearchApp(){
        submit("appSearch");
    }

    function jumpToPagechangePage() {
        submit('appPage');
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('appSort');
    }

    function doDraft(appNo){
        if (appNo.indexOf("DN") !== -1 ||appNo.indexOf("DR") !== -1) {
            $("[name='crud_action_value']").val(appNo);
            submit('appDraft');
        }
    }

    function doDraftAction(appNo,val){
        var action = val;
        if ("Reload" == action){
            $("[name='crud_action_value']").val(appNo);
            submit('appDraft');
        }

        if ("Delete" == action) {
            $("[name='crud_action_value']").val(appNo);
        }
    }
</script>