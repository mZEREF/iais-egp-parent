<div class="alert alert-info" role="alert">
    <strong>
        <h4>Processing History</h4>
    </strong>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col">Username</th>
                    <th scope="col">Status Update</th>
                    <th scope="col">Remarks</th>
                    <th scope="col">Last Updated</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${routingHistoryList}" var="history">
                    <tr>
                        <td>
                            <p><iais:code code="${history.actionBy}"></iais:code></p>
                        </td>
                        <td>
                            <p><iais:code code="${history.appStatus}"></iais:code></p>
                        </td>
                        <td>
                            <p><c:out value="${history.internalRemarks}"></c:out></p>
                        </td>
                        <td>
                            <p><fmt:formatDate value='${history.modifiedAt}' pattern='dd/MM/yyyy HH:mm:ss'/></p>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
