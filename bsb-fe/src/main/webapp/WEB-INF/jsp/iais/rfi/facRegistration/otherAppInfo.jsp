<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="configList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>"--%>
<%--@elvariable id="answerMap" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<%--@elvariable id="answerErrorMap" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<%--@elvariable id="SELECTED_CLASSIFICATION" type="java.lang.String"--%>
<%--@elvariable id="SELECTED_ACTIVITIES" type="java.util.List<java.lang.String>"--%>
<fac:otherAppInfo configList="${configList}" answerMap="${answerMap}" answerErrorMap="${answerErrorMap}" classification="${SELECTED_CLASSIFICATION}" activities="${SELECTED_ACTIVITIES}">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-rfi-facility-register.js"></script>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-display-or-not.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <%@include file="InnerFooter.jsp" %>
    </jsp:attribute>
</fac:otherAppInfo>