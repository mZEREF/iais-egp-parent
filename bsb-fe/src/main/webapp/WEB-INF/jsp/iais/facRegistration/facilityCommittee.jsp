<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="facCommittee" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto"--%>
<%--@elvariable id="DATA_HAS_ERROR" type="java.lang.Boolean"--%>
<%--@elvariable id="DATA_ERRORS" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.validation.ValidationListResultUnit>"--%>
<%--@elvariable id="VALID_FILE" type="java.lang.Boolean"--%>
<fac:facilityCommittee facCommittee="${facCommittee}" dataHasError="${DATA_HAS_ERROR}" dataErrors="${DATA_ERRORS}" validFile="${VALID_FILE}">
</fac:facilityCommittee>