<div class="main-content" style="min-height: 73vh;">
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
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <iais:pagination param="MasterCodeSearchParam" result="MasterCodeSearchResult"/>
                        <div class="table-gp">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" style="display: none"></th>
                                <iais:sortableHeader needSort="false" field="SN" value="SN" style="width:1%" />
                                <iais:sortableHeader needSort="true" field="code_category" value="Master Code Category" style="width:15%" />
                                <iais:sortableHeader needSort="true" field="code_value" value="Code Value" style="width:10%"/>
                                <iais:sortableHeader needSort="true" field="code_category" value="Code Description" style="width:10%"/>
                                <iais:sortableHeader needSort="true" field="filter_value" value="Filter Value" style="width:10%"/>
                                <iais:sortableHeader needSort="true" field="sequence" value="Sequence" style="width:7%" />
                                <iais:sortableHeader needSort="true" field="Version" value="Version" style="width:7%"/>
                                <iais:sortableHeader needSort="true" field="effective_from" value="Effective Start Date"  style="width:13%"/>
                                <iais:sortableHeader needSort="true" field="effective_to" value="Effective End Date"  style="width:13%"/>
                                <iais:sortableHeader needSort="true" field="status" value="Status" style="width:6%"/>
                                <iais:sortableHeader needSort="false" field="Action" value="Action" />
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty MasterCodeSearchResult.rows}">
                                    <tr>
                                        <td colspan="12">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
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
                                                <p><fmt:formatNumber type="number" value="${masterCodeResult.sequence  / 1000}" maxFractionDigits="2" pattern="0"/></p>
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
                                                <c:if test="${masterCodeResult.level != 5 && masterCodeResult.isCentrallyManage == 1}">
                                                    <button type="button" class="btn btn-default btn-sm" onclick="doCreateCategory('${masterCodeResult.masterCodeId}')">Create</button>
                                                </c:if>
                                                <c:if test="${masterCodeResult.isCentrallyManage == 1}">
                                                    <button type="button" class="btn btn-default btn-sm" onclick="doEdit('${masterCodeResult.masterCodeId}')">Edit</button>
                                                </c:if>
                                                <c:if test="${masterCodeResult.isCentrallyManage == 1}">
                                                    <c:set var="nowDate" value="<%=System.currentTimeMillis()%>"/>
                                                    <button type="button" class="btn btn-default btn-sm" data-toggle="modal" data-target="#deleteModal${status.index}">
                                                        <c:choose>
                                                            <c:when test="${nowDate - masterCodeResult.effectiveStartDate.getTime() < 0}">
                                                                Delete
                                                            </c:when>
                                                            <c:otherwise>
                                                                Deactivate
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </button>
                                                </c:if>
                                            </td>
                                        </tr>
                                        <!-- Modal -->
                                        <div class="modal fade" id="deleteModal${status.index}" tabindex="-1" role="dialog" aria-labelledby="deleteModal">
                                            <div class="modal-dialog modal-dialog-centered" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-body">
                                                        <div class="row">
                                                            <c:set var="nowDate" value="<%=System.currentTimeMillis()%>"/>
                                                            <div class="col-md-12"><span style="font-size: 2rem">Do you confirm the <c:choose>
                                                                    <c:when test="${nowDate - masterCodeResult.effectiveStartDate.getTime() < 0}">Delete</c:when>
                                                                    <c:otherwise>Deactivate</c:otherwise>
                                                                </c:choose> ?</span></div>
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
                        </div>
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