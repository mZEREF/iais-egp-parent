<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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
                    <th scope="col">Username</th>
                    <th scope="col">Status Update</th>
                    <th scope="col">Remarks</th>
                    <th scope="col">Last Updated</th>
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
<c:choose>
    <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
    <c:when test="${goBackUrl ne null}">
        <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:when>
    <c:otherwise>
        <a class="back" href="/bsb-be/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:otherwise>
</c:choose>