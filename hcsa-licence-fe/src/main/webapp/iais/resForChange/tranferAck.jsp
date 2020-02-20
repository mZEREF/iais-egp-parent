<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_form_value" value="">
    <input type="hidden" name="crud_action_type_value" value="">

    <%@include file="dashboard.jsp" %>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <br>
                <br>
                <br>
                <h2>Tranfer Success!</h2>
                <br>
                <br>
                <br>
            </div>
        </div>
    </div>
</form>


<script>


</script>