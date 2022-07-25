<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<c:choose>
    <c:when test="${cookie['service_bsb'].value eq 'Y'}">
        <webui:setLayout name="iais-blank"/>
        <script>
            $(function () {
                document.location ="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"
            })
        </script>
    </c:when>
    <c:when test="${cookie['service_bsb_afc'].value eq 'Y'}">
        <webui:setLayout name="iais-blank"/>
        <script>
            $(function () {
                document.location ="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"
            })
        </script>
    </c:when>
    <c:otherwise>
        <%@include file="/WEB-INF/jsp/iais/interInbox/msgPageView.jsp"%>
    </c:otherwise>
</c:choose>