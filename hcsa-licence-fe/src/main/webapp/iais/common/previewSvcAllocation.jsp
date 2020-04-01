<c:set var="appGrpPremisesDtoList" value="${AppSubmissionDto.appGrpPremisesDtoList}"></c:set>
<div class="amended-service-info-gp">
    <h2>DISCIPLINE ALLOCATION</h2>
    <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <table class="table discipline-table">
                        <thead>
                        <tr>
                            <th>Premises</th>
                            <th>Laboratory Disciplines</th>
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
                    </table>
                </div>

            </div>
        </div>
    </div>
</div>