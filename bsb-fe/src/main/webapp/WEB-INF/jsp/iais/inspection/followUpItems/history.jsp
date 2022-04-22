<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--@elvariable id="routingHistoryList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto>"--%>
<div class="alert alert-info" role="alert">
    <strong>
        <h4>Remarks History</h4>
    </strong>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col" class="col-xs-2">User</th>
                    <th scope="col" class="col-xs-8">Remarks</th>
                    <th scope="col" class="col-xs-2">Last Update</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${routingHistoryList ne null}">
                    <c:forEach items="${routingHistoryList}" var="history">
                        <tr>
                            <td class="col-xs-2"><c:out value="${history.userName}"/></td>
                            <td class="col-xs-8"><c:out value="${history.remarks}"/></td>
                            <td class="col-xs-2"><c:out value='${history.lastUpdated}'/></td>
                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>