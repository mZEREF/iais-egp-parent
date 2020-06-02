<div class="main-content">
    <form method="post" id="MasterCodeForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_deactivate" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Master Code Management</h2>
                        </div>
                        <%@ include file="doSearchBody.jsp" %>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>SN</th>
                                <th>Master Code Category</th>
                                <th>Code Value</th>
                                <th>Code Description</th>
                                <th>Filter Value</th>
                                <th>Sequence</th>
                                <th>Version</th>
                                <th>Effective Start Date</th>
                                <th>Effective End Date</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty MasterCodeSearchResult.rows}">
                                    <tr>
                                        <td colspan="12">
                                            <iais:message key="ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="masterCodeResult" items="${MasterCodeSearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">No.</p>
                                                <p>#${(MasterCodeSearchParam.pageNo - 1) * MasterCodeSearchParam.pageSize + status.index + 1}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Code Category</p>
                                                <p>${masterCodeResult.codeCategory}</p>
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
                                                <p class="visible-xs visible-sm table-row-title">Filter Value</p>
                                                <p>${masterCodeResult.filterValue}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Sequence</p>
                                                <p>${masterCodeResult.sequence}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Version</p>
                                                <p>${masterCodeResult.version}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Effective Start
                                                    Date</p>
                                                <p><fmt:formatDate value="${masterCodeResult.effectiveStartDate}"
                                                                   pattern="dd/MM/yyyy"/></p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Effective End Date</p>
                                                <p><fmt:formatDate value="${masterCodeResult.effectiveEndDate}"
                                                                   pattern="dd/MM/yyyy"/></p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                                <p>${masterCodeResult.status}</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Action</p>
                                                <c:if test="${masterCodeResult.level != 5}">
                                                    <button type="button" class="btn btn-default btn-sm" onclick="doCreateCategory('${masterCodeResult.masterCodeId}')">Create</button>
                                                </c:if>
                                                <button type="button" class="btn btn-default btn-sm" onclick="doEdit('${masterCodeResult.masterCodeId}')">Edit</button>
                                                <button type="button" class="btn btn-default btn-sm" data-toggle="modal" data-target="#deleteModal${status.index}">Delete</button>
                                            </td>
                                        </tr>
                                        <!-- Modal -->
                                        <div class="modal fade" id="deleteModal${status.index}" tabindex="-1" role="dialog" aria-labelledby="deleteModal" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                            <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                        <h5 class="modal-title" id="deleteModalLabel">Confirmation Box</h5>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="row">
                                                            <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem">Do you confirm the Delete ?</span></div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                        <button type="button" class="btn btn-primary" onclick="doDeleteOrDeactivate('${masterCodeResult.masterCodeId}')">Confirm</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <!--Modal End-->
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                        <div class="row">
                            <div class="col-xs-12 col-md-12 text-right">
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/master-code-file">Download</a>
                                <a class="btn btn-file-upload btn-primary" href="#" id="MCUploadFile">Upload</a>
                                <a class="btn btn-primary" onclick="doCreate()">Create Master Code Category</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>