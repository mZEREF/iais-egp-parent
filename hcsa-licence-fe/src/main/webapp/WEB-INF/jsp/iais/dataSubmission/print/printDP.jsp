<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="submissionType" value="${dpSuperDataSubmissionDto.submissionType}"/>

<c:choose>
    <c:when test="${submissionType == 'DP_TP001'}">
        <%-- Patient Information --%>
        <%@include file="../drugPractices/section/previewPatientInformationDetail.jsp" %>
        <%@include file="../drugPractices/common/previewDpDsAmendment.jsp" %>
        <%@include file="../drugPractices/common/dpDeclaration.jsp" %>
    </c:when>
    <c:when test="${submissionType == 'DP_TP002'}">
        <%-- Drug Prescribed --%>
        <%@include file="../drugPractices/section/previewDrugSubmissionSection.jsp" %>
        <%@include file="../drugPractices/section/previewDrugMedicationSection.jsp" %>
        <%@include file="../drugPractices/common/previewDpDsAmendment.jsp" %>
        <%@include file="../drugPractices/common/drugDeclarations.jsp" %>
    </c:when>
    <c:when test="${submissionType == 'DP_TP003'}">
        <%-- Sovenor Inventory --%>
        <%@include file="../drugPractices/section/previewSovenorInventory.jsp" %>
        <%--        <%@include file="../drugPractices/common/previewDpDsAmendment.jsp" %>--%>
        <%@include file="../drugPractices/common/dpDeclaration.jsp" %>
    </c:when>
</c:choose>
<style>
    .panel-group .panel.panel-default > .panel-heading h4 a {
        text-decoration: none;
    }
</style>
<script type="text/javascript">
    // textarea
    $('textarea').each(function(index, ele){
        $(ele).parent().append('<div style="border-radius:8px;border: 1px solid #000;padding: 5px;min-height: 100px;width: 100%">'
            + $(ele).val() + '</div>');
        $(ele).remove();
    });
    $('#ar-declaration').find('input').prop("disabled", true);
</script>