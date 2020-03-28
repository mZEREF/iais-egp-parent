<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <c:forEach items="${map}" var="error">
                            <c:out value="${error.key}"></c:out>
                            <c:out value="${error.value}"></c:out>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
        <iais:action>
            <a style="margin-left: 0%" class="back" onclick="submit()"><em class="fa fa-angle-left"></em> Back</a>
        </iais:action>
    </form>
</div>
<script>
function submit() {
    $("#mainForm").submit();
}
</script>