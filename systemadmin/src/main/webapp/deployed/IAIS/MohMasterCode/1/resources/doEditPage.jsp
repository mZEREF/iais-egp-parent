<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setAttribute name="header-ext">
<%
/* You can add additional content (SCRIPT, STYLE elements)
 * which need to be placed inside HEAD element here.
 */
%>
</webui:setAttribute>
<webui:setAttribute name="title">
<%
/* You can set your page title here. */
%>
<%=process.runtime.getCurrentComponentName()%>
</webui:setAttribute>

<form method="post" action=<%=process.runtime.continueURL()%>>
<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
<input type="hidden" name="crud_action_type" value="">
<input type="hidden" name="crud_action_value" value="">
<input type="hidden" name="crud_action_additional" value="">

    <iais:body >
        <br/>
        <c:choose>
            <c:when test="${empty MasterCodeDto}">
                <h2>Master Code Create</h2>
            </c:when>
            <c:otherwise>
                <h2>Master Code Edit</h2>
            </c:otherwise>
        </c:choose>
        <iais:error>
            <c:if test = "${not empty errorMap}">
                <div class="error">
                    <c:forEach items="${errorMap}" var="map">
                        ${map.key}  ${map.value} <br/>
                    </c:forEach>
                </div>
            </c:if>
        </iais:error>
    </iais:body>
</form>
