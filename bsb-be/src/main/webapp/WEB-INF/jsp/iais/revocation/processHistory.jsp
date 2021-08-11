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
                <c:forEach items="${sessionScope.processingHistory}" var="history">
                    <tr>
                        <td>
                            <p><iais:code code="${history.actionBy}"></iais:code></p>
                        </td>
                        <td>
                            <p><c:out value="null"></c:out></p>
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
