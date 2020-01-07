<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="dashboard.jsp" %>
<%@ include file="mainContent.jsp" %>

<script type="text/javascript">

    function submit(pageTab,action){
        if(pageTab == "inbox"){
            $("[name='crud_action_type']").val(action);
            $("[name='form_pageTab']").val("inbox");
            $("#inboxForm").submit();
        }else if (pageTab == "app") {
            $("[name='crud_action_type']").val(action);
            $("[name='form_pageTab']").val("app");
            $("#appForm").submit();
        }else if (pageTab == "lic") {
            $("[name='crud_action_type']").val(action);
            $("[name='form_pageTab']").val("lic");
            $("#licenceForm").submit();
        }
    }

    function doSearchApp(){
        submit('app','doSearch');
    }

    function doSearchLic(){
        submit('lic','doSearch');
    }

    $("#inboxType").change(function() {
        submit('inbox','doSearch');
    });

    $("#inboxService").change(function() {
        submit('inbox','doSearch');
    });

    $("#appContentSelect").change(function () {
        $("[name='appFrom_pageNo']").val(1);
        $("[name='appFrom_pageSize']").val(this.value);
        submit('app','doPage');
    });

    $("#inboxContentSelect").change(function () {
        $("[name='inboxFrom_pageNo']").val(1);
        $("[name='inboxFrom_pageSize']").val(this.value);
        submit('inbox','doPage');
    });

    $("#licContentSelect").change(function () {
        $("[name='licFrom_pageNo']").val(1);
        $("[name='licFrom_pageSize']").val(this.value);
        submit('lic','doPage');
    });

    function doAddPageNo(fromName){
        if ('app' == fromName) {
            $("[name='appFrom_pageNo']").val('${appParam.pageNo + 1}');
            $("[name='appFrom_pageSize']").val(10);
            submit('app','doPage');
        }
        if ('inbox' == fromName){
            $("[name='inboxFrom_pageNo']").val('${appParam.pageNo + 1}');
            $("[name='inboxFrom_pageSize']").val(10);
            submit('inbox','doPage');
        }
        if ('lic' == fromName){
            $("[name='licFrom_pageNo']").val('${appParam.pageNo + 1}');
            $("[name='licFrom_pageSize']").val(10);
            submit('lic','doPage');
        }

    };

    function doSubPageNo(fromName){
        if ('app' == fromName) {
            $("[name='appFrom_pageNo']").val('${appParam.pageNo - 1}');
            $("[name='appFrom_pageSize']").val(10);
            submit('app','doPage');
        }
        if ('inbox' == fromName){
            $("[name='inboxFrom_pageNo']").val('${appParam.pageNo - 1}');
            $("[name='inboxFrom_pageSize']").val(10);
            submit('inbox','doPage');
        }
        if ('lic' == fromName){
            $("[name='licFrom_pageNo']").val('${appParam.pageNo - 1}');
            $("[name='licFrom_pageSize']").val(10);
            submit('lic','doPage');
        }
    };

    function doDraftAction(appNo,val){
        var action = val;
        if ("Reload" == action){
            $("[name='crud_action_value']").val(appNo);
            submit('app','doDraft');
        }
        if ("Delete" == action) {
            $("[name='crud_action_value']").val(appNo);
        }
    }

    $(function () {
        activeTab('${TAB_NO}');
    });

    function activeTab(tabNo){
        if(tabNo == 'inboxTab'){
            $('#'+tabNo+' a[href="#tabInbox"]').tab('show');
        }else if(tabNo == 'appTab'){
            $('#'+tabNo+' a[href="#tabApp"]').tab('show');
        }else if (tabNo == 'licTab') {
            $('#'+tabNo+' a[href="#tabLic"]').tab('show');
        }
    }

    function doRenew(){
        var licenceNo = $("input:checkbox:checked").val();
        $("[name='crud_action_value']").val(licenceNo);
        submit('lic','doRenew')
    }

    function doDraft(appNo){
        if (appNo.indexOf("DN") !== -1 ||appNo.indexOf("DR") !== -1) {
            $("[name='crud_action_value']").val(appNo);
            submit('app','doDraft');
        }
    }

    function searchLicenceNo(){
        submit('lic','doSearch');
    }

    function searchByAppNo() {
        submit('app','doSearch')
    }

</script>