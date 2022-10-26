<%@taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="instructionInfo" type="sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityInstructionDto"--%>
<common:dashboard>
    <jsp:attribute name="titleFrag">
        <h1>Renewal of Facility Registration</h1>
        <p>You are applying to <strong>Renew the Registration for Facility No. ${instructionInfo.facilityNo})</strong></p>
    </jsp:attribute>
</common:dashboard>