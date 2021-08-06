<div class="row">
    <div class="col-xs-12">
        <c:forEach var = "item" items="${inspectionChecklistAttr}" varStatus="status">
            <c:if test="${item.common eq true}">
                <div class="bg-panel-heading">
                    <h2>General Regulation</h2>
                </div>
            </c:if>
            <c:if test="${item.common eq false}">
                <c:choose>
                    <c:when test="${not empty item.inspectionEntity}">
                        <h2>${item.svcName} (<iais:code code="${item.inspectionEntity}"/>)</h2>
                    </c:when>
                    <c:when test="${empty item.svcSubType}">
                        <h2>${item.svcName}</h2>
                    </c:when>
                    <c:otherwise>
                        <h2>${item.svcName} | ${item.svcSubType}</h2>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:forEach var = "sec" items="${item.sectionDtos}">
                <div class="panel panel-default">
                    <div class="panel-heading" id="headingPremise" role="tab">
                        <h4 class="panel-title">${sec.section}</h4>
                    </div>
                    <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
                        <div class="panel-body">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" width="15%">Regulation Clause Number</th>
                                    <th scope="col" width="40%">Regulations</th>
                                    <th scope="col" width="40%">Checklist Item</th>
                                    <th scope="col" width="10%">Risk Level</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var = "chklitem" items = "${sec.checklistItemDtos}" varStatus="status">
                                    <tr>
                                        <td>
                                            <p>${chklitem.regulationClauseNo}</p>
                                        </td>
                                        <td>
                                            <p>${chklitem.regulationClause}</p>
                                        </td>
                                        <td>
                                            <p>${chklitem.checklistItem}</p>
                                        </td>
                                        <td>
                                            <p><iais:code code="${chklitem.riskLevel}"></iais:code></p>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:forEach>
        <c:if test="${!empty adhocCheckListAttr}">
            <div class="panel panel-default">
                <div class="panel-heading" role="tab">
                    <h4 class="panel-title">Adhoc Item</h4>
                </div>
                <div class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingPremise">
                    <div class="panel-body">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" width="50%">Checklist Item</th>
                                <th scope="col" width="40%">Answer Type</th>
                                <th scope="col" width="10%">Risk Level</th>
                                <c:if test="${adhocActionFlag == 'Y'}">
                                    <th scope="col" width="10%">Action</th>
                                </c:if>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var = "adhocItem" items = "${adhocCheckListAttr.allAdhocItem}" varStatus="status">
                                <tr>
                                    <td>
                                        <p><c:out value="${adhocItem.question}"></c:out></p>
                                    </td>
                                    <td>
                                        <p><iais:code code="${adhocItem.answerType}"></iais:code></p>
                                    </td>
                                    <td>
                                        <p><iais:code code="${adhocItem.riskLvl}"></iais:code></p>
                                    </td>

                                    <td>
                                        <c:if test="${adhocActionFlag == 'Y'}">
                                            <button onclick="removeAdhocItem('<iais:mask name="crud_action_value" value="${status.index}"/>')" type="button"> Remove </button>
                                        </c:if>
                                    </td>

                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>