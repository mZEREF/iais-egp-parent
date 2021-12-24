<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../assistedReproduction/section/previewPatientDetail.jsp" %>
<%@include file="../assistedReproduction/section/previewHusbandDetail.jsp" %>
<c:if test="${arSuperDataSubmissionDto.appType eq 'DSTY_005'}">
    <%@include file="../assistedReproduction/common/dsAmendment.jsp" %>
</c:if>
<c:if test="${arSuperDataSubmissionDto.appType ne 'DSTY_005'}">
    <%@include file="../assistedReproduction/common/arDeclaration.jsp" %>
</c:if>