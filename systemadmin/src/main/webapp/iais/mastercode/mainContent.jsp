<div class="main-content">
    <form class="form-horizontal" method="post" id="MasterCodeForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_deactivate" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Master Code View</h2>
                        </div>
                        <%@ include file="doSearchBody.jsp" %>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>No.</th>
                                <th>Code Category</th>
                                <th>Code Value</th>
                                <th>Code Description</th>
                                <th>Sequence</th>
                                <th>Status</th>
                                <th>Effective Start Date</th>
                                <th>Effective End Date</th>
                                <th>Remarks</th>
                                <th>Version</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty MasterCodeSearchResult.rows}">
                                    <tr>
                                        <td colspan="12">
                                            No Record!!
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="masterCodeResult" items="${MasterCodeSearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">No.</p>
                                                <p>#${(MasterCodeSearchParam.pageNo - 1) * 10 + status.index + 1}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Code Category</p>
                                                <p><a href="#">${masterCodeResult.codeCategory}</a></p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Code Value</p>
                                                <p>${masterCodeResult.codeValue}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Code Description</p>
                                                <p>${masterCodeResult.codeDescription}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Sequence</p>
                                                <p>${masterCodeResult.codeDescription}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                                <p>${masterCodeResult.status}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Effective Start
                                                    Date</p>
                                                <p><fmt:formatDate value="${masterCodeResult.effectiveStartDate}"
                                                                   pattern="MM/dd/yyyy HH:mm:ss"/></p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Effective End Date</p>
                                                <p><fmt:formatDate value="${masterCodeResult.effectiveEndDate}"
                                                                   pattern="MM/dd/yyyy HH:mm:ss"/></p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Remarks</p>
                                                <p>${masterCodeResult.remarks}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Version</p>
                                                <p>${masterCodeResult.version}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Action</p>
                                                <button type="button" class="btn btn-default btn-sm" onclick="doEdit('${masterCodeResult.masterCodeId}')">Edit</button>
                                                <button type="button" class="btn btn-default btn-sm" onclick="doDelete('${masterCodeResult.masterCodeId}')">Delete</button>
                                                <button type="button" class="btn btn-default btn-sm" onclick="doDeactivate('${masterCodeResult.masterCodeId}')">Deactivate</button>
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
                                    <p class="count">${MasterCodeSearchResult.rowCount} out of ${MasterCodeSearchParam.pageNo}</p>
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