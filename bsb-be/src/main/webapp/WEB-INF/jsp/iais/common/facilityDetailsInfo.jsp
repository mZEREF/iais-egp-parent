<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants" %>
<%--@elvariable id="facilityDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo"--%>
<common:facilityDetailsInfo facilityDetailsInfo="${facilityDetailsInfo}" firstTab="${ModuleCommonConstants.APPLICATION_RECOMMENDATIONS_FIRST_TAB_APPLICATION_DETAILS}" displayValidityEndDate="false">
</common:facilityDetailsInfo>