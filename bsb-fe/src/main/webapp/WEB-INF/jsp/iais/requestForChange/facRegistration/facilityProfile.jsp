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
<%--@elvariable id="editableFieldSet" type="java.util.Set<java.lang.String>"--%>
<%--@elvariable id="errorMsg" type="java.lang.String"--%>
<fac:facilityProfile facProfile="${facProfile}" isFifthRf="${isSPFifthRegisteredFacility}" isPvRf="${isPolioVirusRegisteredFacility}"
                     organizationAddress="${organizationAddress}" facTypeOps="${facTypeOps}" addressTypeOps="${addressTypeOps}" opvSabinPIMRiskLevelOps="${opvSabinPIMRiskLevelOps}"
                     findGazetteError="${findGazetteError}" editJudge="${true}" editableFieldSet="${editableFieldSet}" hasError="${not empty errorMsg}">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-facility-register.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="../common/dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <%--@elvariable id="isAllowToSaveDraft" type="java.lang.Boolean"--%>
        <fac:innerFooter canSaveDraftJudge="${isAllowToSaveDraft}"/>
    </jsp:attribute>
</fac:facilityProfile>