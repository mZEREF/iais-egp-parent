<c:forEach var="stepSchem" items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}">
    <c:if test="${stepSchem.stepCode == 'SVST003'}">
        <c:set var="currStepName" value="${stepSchem.stepName}"/>
    </c:if>
    <c:if test="${stepSchem.stepCode == 'SVST001'}">
        <c:set var="svcScopePageName" value="${stepSchem.stepName}"/>
    </c:if>
</c:forEach>
<c:set var="appGrpPremisesDtoList" value="${AppSubmissionDto.appGrpPremisesDtoList}"></c:set>
<div class="amended-service-info-gp">
    <label style="font-size: 2.2rem">${currStepName}</label>
    <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:if test="${!empty reloadDisciplineAllocationMap}">
                        <table class="table discipline-table">
                            <thead style="text-decoration: none">
                            <tr>
                                <th>Premises</th>
                                <th>${svcScopePageName}</th>
                                <th>Clinical Governance Officers</th>
                            </tr>
                            </thead>
                            <c:forEach var="appGrpPrem" items="${appGrpPremisesDtoList}" varStatus="status">
                            <c:set var="reloadMapValue" value="${appGrpPrem.premisesIndexNo}"/>
                            <tbody>
                            <c:forEach var="disciplineAllocation" items="${reloadDisciplineAllocationMap[reloadMapValue]}" varStatus="stat">
                            <tr>
                                <c:if test="${stat.first}">
                                    <td rowspan="${reloadDisciplineAllocationMap[reloadMapValue].size()}">
                                        <p class="">${appGrpPrem.address}</p>
                                    </td>
                                </c:if>
                                <td>
                                    <p>${disciplineAllocation.chkLstName}</p>
                                </td>
                                <td>
                                    <p>${disciplineAllocation.cgoSelName}</p>
                                </td>
                            </tr>
                            </c:forEach>
                            </c:forEach>
                        </table>
                    </c:if>
                </div>

            </div>
        </div>
    </div>
</div>