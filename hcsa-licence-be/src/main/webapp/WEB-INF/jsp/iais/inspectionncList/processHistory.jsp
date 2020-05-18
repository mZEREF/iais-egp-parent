<div class="alert alert-info" role="alert">
    <strong>
        <h4>Processing History</h4>
    </strong>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table class="table">
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Working Group</th>
                    <th>Status Update</th>
                    <th>Remarks</th>
                    <th>Last Updated</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach
                        items="${applicationViewDto.appPremisesRoutingHistoryDtoList}"
                        var="appPremisesRoutingHistoryDto">
                    <tr>
                        <td>
                            <p><c:out
                                    value="${appPremisesRoutingHistoryDto.actionby}"></c:out></p>
                        </td>
                        <td>
                            <p><c:out
                                    value="${appPremisesRoutingHistoryDto.workingGroup}"></c:out></p>
                        </td>
                        <td>
                            <p><c:out
                                    value="${appPremisesRoutingHistoryDto.processDecision}"></c:out></p>
                        </td>
                        <td>
                            <p><c:out
                                    value="${appPremisesRoutingHistoryDto.internalRemarks}"></c:out></p>
                        </td>
                        <td>
                            <p><c:out
                                    value="${appPremisesRoutingHistoryDto.updatedDt}"></c:out></p>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>