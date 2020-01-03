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
            $("#inboxForm").submit();
        }else if (pageTab == "app") {
            $("[name='crud_action_type']").val(action);
            $("#appForm").submit();
        }else if (pageTab == "lic") {
            $("[name='crud_action_type']").val(action);
            $("#licenceForm").submit();
        }

    }
    doDraft
    function doSearchApp(){
        $("[name='crud_action_value']").val("app");
        submit('app','doSearch');
    }

    function doSearchLic(){
        $("[name='crud_action_value']").val("lic");
        submit('lic','doSearch');
    }

    $("#inboxType").change(function() {
        submit('inbox','doSearch');
    });

    $("#inboxService").change(function() {
        submit('inbox','doSearch');
    });

    $(function () {
        console.log('${TAB_NO}');
        activeTab('${TAB_NO}');
    });

    function doRenew(){
        var licenceNo = $("input:checkbox:checked").val();
        $("[name='crud_action_type']").val(licenceNo);
        submit('lic','doRenew')
    }

    function activeTab(tabNo){
        if(tabNo == 'inboxTab'){
            $('#'+tabNo+' a[href="#tabInbox"]').tab('show');
        }else if(tabNo == 'appTab'){
            $('#'+tabNo+' a[href="#tabApp"]').tab('show');
        }else if (tabNo == 'licTab') {
            console.log("Lic Tab")
            $('#'+tabNo+' a[href="#tabLic"]').tab('show');
        }

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