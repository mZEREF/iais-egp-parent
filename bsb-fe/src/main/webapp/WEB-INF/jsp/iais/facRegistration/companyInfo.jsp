<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="organizationAddress" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo"--%>
<fac:companyInfo organizationAddress="${organizationAddress}">
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <%--@elvariable id="isNewFacilityRegistration" type="java.lang.Boolean"--%>
        <fac:innerFooter canSaveDraftJudge="${isNewFacilityRegistration}"/>
    </jsp:attribute>
</fac:companyInfo>