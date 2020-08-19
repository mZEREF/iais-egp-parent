<div class="form-check-gp">
    <div class="row">
        <p class="form-check-title">Choose the personnel who's information you wish to amend</p>
    </div>
    <div class="row">
        <div class="col-xs-12 col-md-5">
            <iais:select name="personnelOptions" options="personnelOptions" id="personnelId" value="${param.personnelOptions}"/>
        </div>
    </div>
    <div class="row">
        <p class="form-check-title">The following licences will be affected by the change of personal Info</p>
    </div>

    <iais:pagination  param="amendUpdateContactSearchParam" result="amendUpdateContactSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr align="center">
                <iais:sortableHeader needSort="true"  field="SVC_NAME" value="Type"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No."/>
                <iais:sortableHeader needSort="true"  field="ROLE" value="Role"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty amendUpdateContactSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${amendUpdateContactSearchResult.rows}" varStatus="status">
                        <c:forEach var="assessMap" items="${pool.licPsnTypeDtoMaps}" varStatus="status">
                            <tr>
                                <td>${pool.svcName}</td>
                                <td>${assessMap.key}</td>
                                <td>
                                    <c:forEach var="assessList" items="${assessMap.value.psnTypes}" varStatus="assessStatus">
                                        <c:choose>
                                            <c:when test="${assessMap.value.psnTypes.size() == 1}">
                                                <c:choose>
                                                    <c:when test="${assessList == 'CGO'}">
                                                        Clinical Governance Officer
                                                    </c:when>
                                                    <c:when test="${assessList == 'PO'}">
                                                        Principal Officer
                                                    </c:when>
                                                    <c:when test="${assessList == 'DPO'}">
                                                        Deputy Principal Officer
                                                    </c:when>
                                                    <c:when test="${assessList == 'MP'}">
                                                        MedAlert Person
                                                    </c:when>
                                                    <c:otherwise>
                                                        Service Personnel
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${assessMap.value.psnTypes.size() == assessStatus.index + 1}">
                                                        <c:choose>
                                                            <c:when test="${assessList == 'CGO'}">
                                                                Clinical Governance Officer
                                                            </c:when>
                                                            <c:when test="${assessList == 'PO'}">
                                                                Principal Officer
                                                            </c:when>
                                                            <c:when test="${assessList == 'DPO'}">
                                                                Deputy Principal Officer
                                                            </c:when>
                                                            <c:when test="${assessList == 'MP'}">
                                                                MedAlert Person
                                                            </c:when>
                                                            <c:otherwise>
                                                                Service Personnel
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${assessList == 'CGO'}">
                                                                Clinical Governance Officer,
                                                            </c:when>
                                                            <c:when test="${assessList == 'PO'}">
                                                                Principal Officer,
                                                            </c:when>
                                                            <c:when test="${assessList == 'DPO'}">
                                                                Deputy Principal Officer,
                                                            </c:when>
                                                            <c:when test="${assessList == 'MP'}">
                                                                MedAlert Person,
                                                            </c:when>
                                                            <c:otherwise>
                                                                Service Personnel,
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <c:choose>
        <c:when test="${!empty amendUpdateContactSearchResult.rows}">
            <a class="btn btn-primary " onclick="guideSubmit('amendLic7','second')">NEXT</a>
        </c:when>
    </c:choose>
</div>
