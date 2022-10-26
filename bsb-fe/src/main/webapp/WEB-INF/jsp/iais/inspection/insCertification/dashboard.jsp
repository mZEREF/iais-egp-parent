<%@taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<common:dashboard>
    <jsp:attribute name="titleFrag">
        <%--@elvariable id="dashboardMsg" type="java.lang.String"--%>
        <h1>${dashboardMsg}</h1>
    </jsp:attribute>
</common:dashboard>