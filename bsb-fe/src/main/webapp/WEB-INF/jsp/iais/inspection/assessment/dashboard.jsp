<%@taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.RfiType" %>
<%--@elvariable id="rfiType" type="sg.gov.moh.iais.egp.bsb.constant.RfiType"--%>
<common:dashboard>
    <jsp:attribute name="titleFrag">
        <c:choose>
            <c:when test="${RfiType.PRE_INSPECTION_CHECKLIST_APPOINTMENT eq rfiType}">
                <h1>Respond to Request for Information and Indicate Preferred Inspection Date</h1>
            </c:when>
            <c:otherwise>
                <h1>Pre-inspection Checklist Submission</h1>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</common:dashboard>