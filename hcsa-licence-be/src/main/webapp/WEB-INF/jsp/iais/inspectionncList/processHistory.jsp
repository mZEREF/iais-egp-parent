<div class="alert alert-info" role="alert">
    <strong>
        <h4>Processing History</h4>
    </strong>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table aria-describedby="" style="table-layout:fixed;" class="table application-group">
                <thead>
                <tr>
                    <th width="17%" scope="col" >Username</th>
                    <th width="17%" scope="col" >Working Group</th>
                    <th width="17%" scope="col" >Status Update</th>
                    <th width="32%" scope="col" >Remarks</th>
                    <th width="17%" scope="col" >Last Updated</th>
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
