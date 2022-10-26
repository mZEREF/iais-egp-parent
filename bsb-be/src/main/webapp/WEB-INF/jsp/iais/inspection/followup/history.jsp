<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="remarkHistoryList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.inspection.followup.RemarkHistoryItemDto>"--%>
<div>&nbsp;</div>
<div class="alert alert-info" role="alert">
    <strong><h4>Remarks History</h4></strong>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col" class="col-xs-3">User</th>
                    <th scope="col" class="col-xs-7">Remarks</th>
                    <th scope="col" class="col-xs-2">Last Update</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${remarkHistoryList ne null}">
                    <c:forEach items="${remarkHistoryList}" var="history">
                        <tr>
                            <td class="col-xs-3"><c:out value="${history.user}"/></td>
                            <td class="col-xs-7"><c:out value="${history.remark}"/></td>
                            <td class="col-xs-2"><c:out value='${history.lastUpdate}'/></td>
                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>