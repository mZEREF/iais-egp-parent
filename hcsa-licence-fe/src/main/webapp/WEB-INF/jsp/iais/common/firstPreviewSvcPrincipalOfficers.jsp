
<div class="amended-service-info-gp">
    <h2>PRINCIPAL OFFICERS</h2>
    <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:forEach items="${ReloadPrincipalOfficers}" var="po" varStatus="status">
                        <p><strong>Clinical Governance Officer ${status.index+1}</strong>: ${po.name },${po.idNo}(${po.idType })</p>
                    </c:forEach>


                    <c:choose>
                        <c:when test="${ReloadDeputyPrincipalOfficers.size() == 0}">
                            <p><strong>Deputy Principal Officer (Optional)</strong>: You have not assigned anyone for this role</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${ReloadDeputyPrincipalOfficers}" var="dpo" varStatus="stat">
                                <p><strong>Deputy Principal Officer ${stat.index+1} (Optional)</strong>: ${dpo.name },${dpo.idNo}(${dpo.idType })</p>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
