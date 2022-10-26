<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="DATA_LIST" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto>"--%>
<fac:previewAuthoriserInfo dataList="${DATA_LIST}" srcNodePath="view">
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
</fac:previewAuthoriserInfo>