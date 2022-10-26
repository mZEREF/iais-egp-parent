<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="facProfile" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto"--%>
<%--@elvariable id="organizationAddress" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo"--%>
<%--@elvariable id="facTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="addressTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="opvSabinPIMRiskLevelOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="isSPFifthRegisteredFacility" type="java.lang.Boolean"--%>
<%--@elvariable id="isPolioVirusRegisteredFacility" type="java.lang.Boolean"--%>
<%--@elvariable id="findGazetteError" type="java.lang.Boolean"--%>
<%--@elvariable id="pageAppEditSelectDto" type="sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto"--%>
<fac:facilityProfile facProfile="${facProfile}" isFifthRf="${isSPFifthRegisteredFacility}" isPvRf="${isPolioVirusRegisteredFacility}"
                     organizationAddress="${organizationAddress}" facTypeOps="${facTypeOps}" opvSabinPIMRiskLevelOps="${opvSabinPIMRiskLevelOps}"
                     addressTypeOps="${addressTypeOps}" editJudge="${pageAppEditSelectDto.facSelect}" findGazetteError="${findGazetteError}">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-rfi-facility-register.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <%@include file="InnerFooter.jsp" %>
    </jsp:attribute>
</fac:facilityProfile>