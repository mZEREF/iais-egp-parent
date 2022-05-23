<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="facCommittee" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto"--%>
<%--@elvariable id="DATA_HAS_ERROR" type="java.lang.Boolean"--%>
<%--@elvariable id="DATA_ERRORS" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.validation.ValidationListResultUnit>"--%>
<%--@elvariable id="VALID_FILE" type="java.lang.Boolean"--%>
<%--@elvariable id="committeeSampleFile" type="sg.gov.moh.iais.egp.bsb.dto.entity.SampleFileDto"--%>
<fac:facilityCommittee facCommittee="${facCommittee}" dataHasError="${DATA_HAS_ERROR}" dataErrors="${DATA_ERRORS}" validFile="${VALID_FILE}" committeeSampleFile="${committeeSampleFile}" editJudge="true">
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
</fac:facilityCommittee>