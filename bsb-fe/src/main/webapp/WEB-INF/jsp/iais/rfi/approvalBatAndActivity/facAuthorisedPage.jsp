<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="appv" tagdir="/WEB-INF/tags/approvalApp" %>

<%--@elvariable id="authPersonList" type="sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto"--%>
<%--@elvariable id="authSelectionMap" type="java.util.Map<java.lang.String,sg.gov.moh.iais.egp.bsb.dto.register.approval.AuthorisedSelection>"--%>
<%--@elvariable id="pageAppEditSelectDto" type="sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto"--%>
<appv:facAuthorised authPersonList="${authPersonList}" authorisedSelectionMap="${authSelectionMap}" editJudge="${pageAppEditSelectDto.appSelect}">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-rfi-approval-bat-and-activity.js"></script>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-display-or-not.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <appv:innerFooter editApp="true"/>
    </jsp:attribute>
</appv:facAuthorised>