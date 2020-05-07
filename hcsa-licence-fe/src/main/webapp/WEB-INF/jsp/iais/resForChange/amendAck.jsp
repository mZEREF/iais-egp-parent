<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@include file="dashboard.jsp" %>
<%@include file="../common/dashboard.jsp" %>
<br/>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_form_value" value="">
    <input type="hidden" name="crud_action_type_value" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">

                <c:choose>
                    <c:when test="${AckMessage != null}">
                        ${AckMessage}
                    </c:when>
                    <c:otherwise>
                        Amend Success
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</form>


<script>


</script>