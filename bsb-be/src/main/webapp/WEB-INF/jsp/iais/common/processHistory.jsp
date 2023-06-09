<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="routingHistoryList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto>"--%>
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
                    <th scope="col">User</th>
                    <th scope="col">Working Group</th>
                    <th scope="col">Status Update</th>
                    <th scope="col">Remarks</th>
                    <th scope="col">Last Update</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${routingHistoryList ne null}">
                    <c:forEach items="${routingHistoryList}" var="history">
                        <tr>
                            <td>
                                <p><iais:code code="${history.userName}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${history.workingGroup}"/></p>
                            </td>
                            <td>
                                <p><iais:code code="${history.statusUpdate}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${history.remarks}"/></p>
                            </td>
                            <td>
                                <p><c:out value='${history.lastUpdated}'/></p>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>