<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<c:if test="${cookie['service_bsb'].value eq 'Y'}">
    <script>
        $(function () {
            document.location ="/bsb-fe/eservice/INTRANET/MohBSBInboxMsg"
        })
    </script>
</c:if>
<webui:setLayout name="iais-internet"/>

<c:set var="tabCode" value="msg"/>
<%@ include file="common/commonDashboard.jsp" %>
<%@ include file="common/mainContent.jsp" %>
<%@ include file="common/commonFile.jsp" %>
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
    $(function () {
        if ('${archiveResult}') {
            $('#isArchivedModal').modal('show');
        }
    });

    function pageAction() {
        if ('${msgPage}' == "msgView") {
            $("[name='msg_page_action']").val("msg_view");
        }else {
            $("[name='msg_page_action']").val("archive_view");
        }
    }
    function submit(action) {
        showWaiting();
        $("[name='msg_action_type']").val(action);
        $("#msgForm").submit();
    }

    function msgToAppPage() {
        submit("msgToApp");
    }

    function msgToLicPage() {
        submit("msgToLic");
    }

    $("#inboxType").change(function () {
        pageAction();
        submit('msgSearch');
    });

    $("#inboxService").change(function () {
        pageAction();
        submit('msgSearch');
    });

    function jumpToPagechangePage() {
        pageAction();
        submit('msgPage');
    }

    function sortRecords(sortFieldName, sortType) {
        pageAction();
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('msgSort');
    }

    function searchBySubject() {
        pageAction();
        submit('msgSearch');
    }
    
    function toArchiveView() {
        doClearMsg();
        submit('msgToArchive');
    }

    $('#doArchive').click(function () {
        if ($('.msgCheck').is(':checked')){
            $('#doArchiveModal').modal('show')
        }else{
            $('#archiveModal').modal('show');
        }
    });

    $('#confirmArchive').click(function () {
        $('#doArchiveModal').modal('hide');
        setTimeout(function (){
            submit('msgDoArchive');
        }, 1000);

    });

    function toMsgPage() {
        doClearMsg();
        submit('toMsgPage');
    }

    function toMsgView(msgContent,msgId,msgType) {
        pageAction();
        $("[name='crud_action_value']").val(msgContent);
        $("[name='msg_action_id']").val(msgId);
        $("[name='msg_page_type']").val(msgType);
        submit('msgToView');
    }

    function doClearMsg(){
        $("[name='inboxAdvancedSearch']").val("");
        $("#inboxType option:first").prop("selected", 'selected').val("All");
        $("#inboxService option:first").prop("selected", 'selected').val("All");
        $("#inboxType .current").text("Select a type");
        $("#inboxService .current").text("Select a service");
    }

</script>