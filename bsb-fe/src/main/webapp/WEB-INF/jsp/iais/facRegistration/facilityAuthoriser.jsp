<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="facAuth" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto"--%>
<%--@elvariable id="DATA_HAS_ERROR" type="java.lang.Boolean"--%>
<%--@elvariable id="DATA_ERRORS" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.validation.ValidationListResultUnit>"--%>
<%--@elvariable id="VALID_FILE" type="java.lang.Boolean"--%>
<fac:facilityAuthoriser facAuth="${facAuth}" dataHasError="${DATA_HAS_ERROR}" dataErrors="${DATA_ERRORS}" validFile="${VALID_FILE}">
</fac:facilityAuthoriser>