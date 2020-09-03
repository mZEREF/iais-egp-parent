<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<div class="col-xs-12">
    <h1>Amendment</h1>
    <c:if test="${AppSubmissionDto.licenceNo ne null}">
    <p class="center">You are amending the <strong>${SvcName}  licence (Licence No. ${AppSubmissionDto.licenceNo}</strong>)</p>
    </c:if>
</div>