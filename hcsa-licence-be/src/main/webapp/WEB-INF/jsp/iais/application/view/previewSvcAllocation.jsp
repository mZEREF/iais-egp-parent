<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<c:forEach var="stepSchem" items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}">
    <c:if test="${stepSchem.stepCode == 'SVST001'}">
        <c:set var="svcScopePageName" value="${stepSchem.stepName}"/>
    </c:if>
</c:forEach>
<c:set var="appGrpPremisesDtoList" value="${AppSubmissionDto.appGrpPremisesDtoList}"></c:set>
<div class="amended-service-info-gp">
    <label class="svc-title">${currStepName}</label>
    <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:if test="${!empty reloadDisciplineAllocationMap}">
                        <div class="table-responsive">
                            <table aria-describedby="" class="table discipline-table">
                                <thead style="text-decoration: none">
                                <tr>
                                    <th scope="col" >Mode of Service Delivery</th>
                                    <th scope="col" >${svcScopePageName}</th>
                                    <th scope="col" >Clinical Governance Officers</th>
                                    <th scope="col" > Section Leader</th>
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
                                    <td>
                                        <p>${disciplineAllocation.sectionLeaderName}</p>
                                    </td>
                                </tr>
                                </c:forEach>
                                </c:forEach>
                            </table>
                        </div>
                    </c:if>
                </div>

            </div>
        </div>
    </div>
</div>