
<div class="col-xs-12">
    <h1>Licence Renewal</h1>
    <p class="center">You are renewing these licences: <strong>${firstSvcName}</strong>
        <c:forEach items="${serviceNameTitleList}"
                   var="svcName">
        <strong> | ${svcName}</strong>
        </c:forEach>
    </p>
</div>