<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="msg/msgDashboard.jsp" %>
<%@ include file="msg/msgMainContent.jsp" %>
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
        $("[name='msg_action_type']").val(action);
        $("#msgForm").submit();
    }

    function msgToAppPage(){
        submit("msgToApp");
    }

    function msgToLicPage(){
        submit("msgToLic");
    }

    $("#inboxType").change(function() {
        submit('msgSearch');
    });

    $("#inboxService").change(function() {
        submit('msgSearch');
    });

    function jumpToPagechangePage() {
        submit('msgPage');
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('msgSort');
    }

    function searchBySubject(){
        submit('msgSearch');
    }
</script>