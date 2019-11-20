<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>


<%@ include file="./dashboard.jsp" %>
<%@ include file="./mainContent.jsp" %>


<script type="text/javascript">

    function getAppList(){
        SOP.Crud.cfxSubmit("inboxForm","getAppList");
    }

    $("#inboxType").change(function() {
        var type = $("#inboxType").val();
        console.log(type)
        SOP.Crud.cfxSubmit("inboxForm","searchInbox");
    });

    // $("#inboxService").change(function() {
    //     var service = $("#inboxService").val();
    //     SOP.Crud.cfxSubmit("inboxForm","searchInboxStep",service);
    // });


</script>