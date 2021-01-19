<div class="amended-service-info-gp">
    <h2>CLINICAL GOVERNANCE OFFICER</h2>
    <div class="amend-preview-info">
        <c:forEach var="cgo" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">
        <p><strong>Clinical Governance Officer ${status.index+1}</strong>: ${cgo.name },${cgo.idNo}(${cgo.idType })</p>
        </c:forEach>
    </div>

</div>

