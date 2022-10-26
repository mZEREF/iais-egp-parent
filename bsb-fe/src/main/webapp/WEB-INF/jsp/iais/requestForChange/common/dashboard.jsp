<%@taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="facClassification" type="java.lang.String"--%>
<%--@elvariable id="activityTypesString" type="java.lang.String"--%>
<common:dashboard>
    <jsp:attribute name="titleFrag">
        <h1>Request for Change</h1>
        <h3>You are amending for <strong><iais:code code="${facClassification}"/></strong> with activitytype:</h3>
        <h3><strong>${activityTypesString}</strong></h3>
    </jsp:attribute>
</common:dashboard>