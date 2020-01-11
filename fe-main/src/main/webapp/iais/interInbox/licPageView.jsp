<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
</style>
<script type="text/javascript">

    function submit(action){
        $("[name='lic_action_type']").val(action);
        $("#licForm").submit();
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

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('licSort');
    }
</script>