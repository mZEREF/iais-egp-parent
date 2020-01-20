<div class="main-content">
    <form class="form-horizontal" method="post" id="IntranetUserForm" action=<%=process.runtime.continueURL()%>>
        <%--        <%@ include file="/include/formHidden.jsp" %>--%>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_deactivate" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Intranet User View</h2>
                        </div>
                        <table class="table">
                            <thead>
                            <tr>
                                <th></th>
                                <th>No.</th>
                                <th>User ID</th>
                                <th>Email address</th>
                                <th>Account Status</th>
                                <%--                                <th>Roles Assigned</th>--%>
                                <%--                                <th>Privileges Assigned</th>--%>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty IntranetUserSearchResult.rows}">
                                    <tr>
                                        <td colspan="12">
                                            No Record!!
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="user" items="${IntranetUserSearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td>
                                            <td><input type="checkbox" value="<c:out value='${user.id}'/>"
                                                       name="userUid" id="userUid<c:out value='${user.id}'/>"></td>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Code Category</p>
                                                <p>${status.count}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Code Category</p>
                                                <p>${user.displayName}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Code Category</p>
                                                <p>${user.emailAddr}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Code
                                                    Category</p>
                                                <p>${user.status}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Action</p>
                                                <button type="button" class="btn btn-default btn-sm"
                                                        onclick="doEdit('${user.id}')">Edit
                                                </button>
                                                <button type="button" class="btn btn-default btn-sm"
                                                        onclick="doDelete('${user.id}')">Delete
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                        <div class="table-footnote">
                            <div class="row">
                                <div class="col-xs-6 col-md-4">
                                    <p class="count">${IntranetUserSearchResult.rowCount} out
                                        of ${IntranetUserSearchParam.pageNo}</p>
                                </div>
                                <div class="col-xs-6 col-md-8 text-right">
                                    <div class="nav">
                                        <ul class="pagination">
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>