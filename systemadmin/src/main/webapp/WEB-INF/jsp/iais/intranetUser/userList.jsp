<div class="row">
    <div class="col-lg-12 col-xs-12">
        <div class="center-content">
            <div class="intranet-content">
                <iais:pagination param="IntranetUserSearchParam" result="IntranetUserSearchResult"/>
                <table class="table">
                    <thead>
                    <tr>
                        <c:choose>
                            <c:when test="${empty IntranetUserSearchResult.rows}">
                                <th>
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input" name="userUids" id="checkboxAll"
                                               onchange="javascirpt:checkAll();" hidden/>
                                        <label class="form-check-label" for="checkboxAll"><span
                                                class="check-square"></span></label>
                                    </div>
                                </th>
                            </c:when>
                            <c:otherwise>
                                <th>
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input" name="userUids" id="checkboxAll"
                                               onchange="javascirpt:checkAll();"/>
                                        <label class="form-check-label" for="checkboxAll"><span
                                                class="check-square"></span></label>
                                    </div>
                                </th>
                            </c:otherwise>
                        </c:choose>
                        <th style="padding-bottom:16px">No.</th>
                        <iais:sortableHeader needSort="true" field="USER_ID" value="User ID"/>
                        <iais:sortableHeader needSort="true" field="EMAIL_ADDR" value="Email address"/>
                        <iais:sortableHeader needSort="true" field="STATUS" value="Account Status"/>
                        <th style="padding-bottom:16px">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty IntranetUserSearchResult.rows}">
                            <tr>
                                <td colspan="12">
                                    <iais:message key="ACK018" escape="true"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="user" items="${IntranetUserSearchResult.rows}" varStatus="status">
                                <tr>
                                    <td>
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   value="<iais:mask name="maskUserId" value="${user.id}"/>"
                                                   name="userUid"
                                                   id="userUid<iais:mask name="maskUserId" value="${user.id}"/>">
                                            <label class="form-check-label"
                                                   for="userUid<iais:mask name="maskUserId" value="${user.id}"/>"><span
                                                    class="check-square"></span></label>
                                        </div>
                                    </td>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Code Category</p>
                                        <p><c:out
                                                value="${(status.index + 1) + (IntranetUserSearchParam.pageNo - 1) * IntranetUserSearchParam.pageSize}"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Code Category</p>
                                        <p>${user.userId}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Code Category</p>
                                        <p>${user.emailAddr}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Code Category</p>
                                        <p><iais:code code="${user.status}"></iais:code></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Action</p>
                                        <button type="button" class="btn btn-default btn-sm"
                                                onclick="doEdit('<iais:mask name="maskUserId" value="${user.id}"/>')">
                                            Edit
                                        </button>
                                        <button type="button" class="btn btn-default btn-sm"
                                                onclick="doDelete('<iais:mask name="maskUserId" value="${user.id}"/>')">
                                            Delete
                                        </button>
                                        <button type="button" class="btn btn-default btn-sm"
                                                onclick="doRole('<iais:mask name="maskUserId" value="${user.id}"/>')">
                                            role
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <!--delete  Modal -->
                <div class="modal fade" id="deleteModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            </div>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem">This account has been used for log in before and hence is not eligible for deletion.</span></div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--delete end -->

                <!-- import Modal -->
                <div class="modal fade" id="importUser" tabindex="-1" role="dialog" aria-labelledby="importUser"
                     style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                        aria-hidden="true">&times;</span></button>
                                <h5 class="modal-title" id="gridSystemModalLabel">Confirmation Box</h5>
                            </div>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem">Do you confirm the modification ?</span>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" onclick="submitUser('doImport')">Confirm</button>
                                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="submitUser('importCancel')">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--Modal End-->

            </div>
            <div class="panel-heading" id="exportError" hidden style="color: red"><h4>Please select user.</h4></div>
            <iais:action style="text-align:center;">
                <div class="text-right">
                    <a class="btn btn-primary" onclick="doCreate()">Create</a>
                    <a class="btn btn-primary" onclick="doStatus()">Change</a>
                    <a class="btn btn-primary" onclick="doExport()">Export</a>
                    <input type="file" id="inputFile" name="xmlFile" style="display:none"
                           onchange="javascript:doImport();"/>
                    <input type="button" class="btn btn-primary" onclick="document.getElementById('inputFile').click()"
                           value="Import"/>
                </div>
            </iais:action>
        </div>
    </div>
</div>

</div>
<script>
    function checkAll() {
        if ($('#checkboxAll').is(':checked')) {
            $("input[name='userUid']").attr("checked", "true");
        } else {
            $("input[name='userUid']").removeAttr("checked");
        }
    }
    $(document).ready(function () {
       var value = '${deleteMod}';
        var value1 = '${importSelect}';
        if(value=='no'){
            $('#deleteModal').modal('show')
        }
        if(value1=='show'){
            $('#importUser').modal('show')
        }
    });
</script>