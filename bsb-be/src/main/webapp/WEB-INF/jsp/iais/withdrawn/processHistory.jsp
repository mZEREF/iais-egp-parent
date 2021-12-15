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
                <%--@elvariable id="processData" type="sg.gov.moh.iais.egp.bsb.dto.audit.OfficerProcessAuditDto"--%>
                <c:if test="${processData.historyDtos ne null}">
                    <c:forEach items="${processData.historyDtos}" var="history">
                        <tr>
                            <td>
                                <p><iais:code code="${history.userName}"/></p>
                            </td>
                            <td>
                                <p><iais:code code="${history.statusUpdate}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${history.remarks}"/></p>
                            </td>
                            <td>
                                <p><fmt:formatDate value='${history.lastUpdated}' pattern='dd/MM/yyyy HH:mm:ss'/></p>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
