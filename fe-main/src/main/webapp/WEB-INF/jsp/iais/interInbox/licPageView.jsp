<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MiscUtil" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>


<c:set var="tabCode" value="lic"/>
<%@ include file="common/commonDashboard.jsp" %>
<%@ include file="common/mainContent.jsp" %>
<%@ include file="common/commonFile.jsp" %>
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
<script type="text/javascript">

    $(function () {
        if ('${licIsRenewed}' || '${licIsAppealed}' || '${licIsAmend}') {
            $('#isRenewedModal').modal('show');
        }

        if ('${ceasedErrResult}') {
            $('#ceasedModal').modal('show');
        }

        if ('${!empty licence_err_list}') {
            licClick();
        }
    });

    function licClick() {
        var checkedNum = $("[name='licenceNo']:checked").length;
        if ($('.licenceCheck').is(':checked')){
            if (checkedNum == 1){
                $("#lic-print").removeClass('disabled');
                $("#lic-renew").removeClass('disabled');
                $("#lic-cease").removeClass('disabled');
                $("#lic-amend").removeClass('disabled');
                $("#lic-appeal").removeClass('disabled');
            } else{
                var statusDuo = [];
                $("[name='licenceNo']:checked").each(function (k,v) {
                    var $currentTr = $(this).closest('tr');
                    statusDuo.push($currentTr.find('td').eq(3).find('p').eq(1).html());
                });
                if (!statusDuo.includes('Ceased')){
                    $("#lic-renew").removeClass('disabled');
                    $("#lic-cease").removeClass('disabled');
                    $("#lic-print").removeClass('disabled');
                }else{
                    $("#lic-renew").addClass('disabled');
                    $("#lic-cease").addClass('disabled');
                    $("#lic-print").removeClass('disabled');
                }
                console.log(statusDuo);
                $("#lic-amend").addClass('disabled');
                $("#lic-appeal").addClass('disabled');
            }
        }else {
            $("#lic-amend").addClass('disabled');
            $("#lic-print").addClass('disabled');
            $("#lic-renew").addClass('disabled');
            $("#lic-cease").addClass('disabled');
            $("#lic-appeal").addClass('disabled');
        }
    }

    function submit(action){
        $("[name='lic_action_type']").val(action);
        $("#licForm").submit();
        $("input[name='licenceNo']").prop("checked",false);
    }

    function licToMsgPage(){
        showWaiting();
        submit("licToMsg");
    }

    function licToAppPage(){
        showWaiting();
        submit("licToApp");
    }

    function doSearchLic(){
        showWaiting();
        submit("licSearch");
    }

    function jumpToPagechangePage() {
        showWaiting();
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

    function doLicAppeal(){
        showWaiting();
        submit('licDoAppeal');
    }
    function doLicCease(){
        showWaiting();
        submit('licDoCease');
    }

    function doLicPrint(){
        if ($("input[type='checkbox']").is(':checked')) {
            var arr = [];
            var r = document.getElementsByName("licenceNo");
            for(var i=0;i<r.length;i++) {
                if (r[i].checked) {
                    var lic = document.getElementsByName(r[i].value);
                    var str = r[i].value + '@' + lic[0].value;
                    arr.push(str);
               }
            }
            $.ajax({
                'url': '${pageContext.request.contextPath}/internetInbox/licenceNo',
                'dataType': 'json',
                'data': {licenceNos:arr},
                'type': 'POST',
                'traditional':true,
                'success': function (data) {
                    window.location= "/main-web/eservice/INTERNET/PrintLicence";
                },
                'error': function () {
                    console.log("fail");
                }
            });
        }
    }


    function doLicAction(licNo){
        showWaiting();
        $("[name='crud_action_value']").val(licNo);
        submit('licDoAmend');
    }

    function toLicView(licId){

    }

    $(".licToView").click(function () {
        var licId = $(this).closest("tr").find(".licId").html();
        showWaiting();
        $("[name='action_id_value']").val(licId);
        submit('licToView');
    });
    
    $(".licActions").change(function () {

    });
    
    function scrollIntoLicView() {
        $("#licForm")[0].scrollIntoView(true);
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
        $("#licType option:first").prop("selected", 'selected').val("");
        $("#licStatus option:first").prop("selected", 'selected').val("");
        $("#clearBody .current").text("All");
        $(".error-msg").text("")
    }
</script>