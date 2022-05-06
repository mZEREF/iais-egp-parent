<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="configList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>"--%>
<%--@elvariable id="answerMap" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<%--@elvariable id="answerErrorMap" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<fac:otherAppInfo configList="${configList}" answerMap="${answerMap}" answerErrorMap="${answerErrorMap}">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-facility-register.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <%@include file="InnerFooter.jsp" %>
    </jsp:attribute>
</fac:otherAppInfo>