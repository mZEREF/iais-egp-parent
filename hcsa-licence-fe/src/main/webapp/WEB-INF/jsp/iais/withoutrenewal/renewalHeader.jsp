
<div class="col-xs-12">
    <div class="dashboard-page-title">
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
            <p class="center">You are ${prefixTitle} the <strong>${firstSvcName} (Licence No. ${renew_licence_no})</strong>
        </c:if>
    </c:if>
    </div>
</div>