<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input type="hidden" name="currentValidateId" value="">
        <div class="bg-title"><h2>Acknowledgement</h2></div>
        <p>You have uploaded Master code on <fmt:formatDate value="${UPLOAD_DATE}" pattern="dd/MM/yyyy"/> at <fmt:formatDate value="${UPLOAD_DATE}" pattern="HH:mm"/></p>
        <div class="text-right text-center-mobile">
            <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doCancel();">Done</a>
        </div>
    </form>
</div>
<script type="text/javascript">
    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm","doCancel");
    }
</script>