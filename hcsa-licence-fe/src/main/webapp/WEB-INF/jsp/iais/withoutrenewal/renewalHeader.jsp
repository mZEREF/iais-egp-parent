
<div class="col-xs-12">
    <h1>Licence Renewal</h1>
    <c:if test="${hasDetail == 'Y'}">
        <c:if test="${isSingle == 'N'}">
            <p class="center">You are renewing these licences: <strong>${firstSvcName}</strong>
                <c:forEach items="${serviceNameTitleList}"
                           var="svcName">
                    <strong> | ${svcName}</strong>
                </c:forEach>
            </p>
        </c:if>
        <c:if test="${isSingle == 'Y'}">
            <p class="center">You are renewing the <strong>${firstSvcName} (Licence No. ${renewDto.appSubmissionDtos.get(0).licenceNo})</strong>
        </c:if>
    </c:if>
</div>