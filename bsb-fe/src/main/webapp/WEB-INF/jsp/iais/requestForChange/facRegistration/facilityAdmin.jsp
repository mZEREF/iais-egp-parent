<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="facAdminOfficer" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto"--%>
<%--@elvariable id="salutationOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="nationalityOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="editableFieldSet" type="java.util.Set<java.lang.String>"--%>
<%--@elvariable id="errorMsg" type="java.lang.String"--%>
<fac:facilityAdminOfficer facAdminOfficer="${facAdminOfficer}" salutationOps="${salutationOps}" nationalityOps="${nationalityOps}" editJudge="${true}" editableFieldSet="${editableFieldSet}"
                          hasError="${not empty errorMsg}">
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
</fac:facilityAdminOfficer>