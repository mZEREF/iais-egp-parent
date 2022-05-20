<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="appv" tagdir="/WEB-INF/tags/approvalApp" %>

<appv:facAuthorised authPersonnelList="${authPersonnelList}" facilityAuthIdMap="${facilityAuthIdMap}" authPersonnelOps="${authPersonnelOps}">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-approval-bat-and-activity.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <%@include file="InnerFooter.jsp" %>
    </jsp:attribute>
</appv:facAuthorised>