<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="lic/licDashboard.jsp" %>
<%@ include file="lic/licMainContent.jsp" %>
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
    .nav ul.pagination{
        padding-top: 7px;
    }

    .nav ul.pagination > li{
        padding-left: 3px;
    }

    .dashboard-gp .dashboard-tile-item .dashboard-tile h1.dashboard-count {
        margin-left: -12px;
    }
</style>
<script type="text/javascript">

    function submit(action){
        $("[name='lic_action_type']").val(action);
        $("#licForm").submit();
        $("input[name='licenceNo']").prop("checked",false);
    }

    function licToMsgPage(){
        submit("licToMsg");
    }

    function licToAppPage(){
        submit("licToApp");
    }

    function doSearchLic(){
        submit("licSearch");
    }

    function jumpToPagechangePage() {
        submit('licPage');
    }

    function doLicAmend() {
        showWaiting();
        submit('licDoAmend');
    }

    function doLicRenew() {
        showWaiting();
        submit('licDoRenew');
    }

    function doLicCease(){
        showWaiting();
        submit('licDoCease');
    }

    function doLicAction(licNo){
        showWaiting();
        $("[name='crud_action_value']").val(licNo);
        submit('licDoAmend');
    }

    function toLicView(licId){
        showWaiting();
        $("[name='action_id_value']").val(licId);
        submit('licToView');
    }
    
    function doLicAppeal(licNo) {
        showWaiting();
        $("[name='crud_action_value']").val(licNo);
        submit('licDoAppeal');
    }
    
    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('licSort');
    }

    function doClearLic(){
        $("[name='licNoPath']").val("");
        $("[name='fExpiryDate']").val("");
        $("[name='eExpiryDate']").val("");
        $("[name='eStartDate']").val("");
        $("[name='fStartDate']").val("");
        $("#licType option:first").prop("selected", 'selected').val("All");
        $("#licStatus option:first").prop("selected", 'selected').val("All");
        $("#clearBody .current").text("All");
    }
</script>