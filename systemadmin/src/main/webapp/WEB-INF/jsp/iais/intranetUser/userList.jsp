<div class="row">
    <div class="col-lg-12 col-xs-12">
        <div class="center-content">
            <div class="intranet-content">
                <h3>
                    <span>Search Results</span>
                </h3>
                <iais:pagination param="IntranetUserSearchParam" result="IntranetUserSearchResult"/>
                <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <c:choose>
                            <c:when test="${empty IntranetUserSearchResult.rows}">
                                <th scope="col" >
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input" name="userUids" id="checkboxAll"
                                               onchange="javascirpt:checkAll();" hidden/>
                                        <label class="form-check-label" for="checkboxAll"><span
                                                class="check-square"></span></label>
                                    </div>
                                </th>
                            </c:when>
                            <c:otherwise>
                                <th scope="col" >
                                    <style>
                                        .table-gp .form-check {
                                            margin-top: 0px;
                                            margin-bottom: 14px;
                                            display: revert;
                                        }
                                    </style>
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input" name="userUids" id="checkboxAll"
                                               onchange="javascirpt:checkAll();"/>
                                        <label class="form-check-label" for="checkboxAll"><span
                                                class="check-square"></span></label>
                                    </div>
                                </th>
                            </c:otherwise>
                        </c:choose>
                        <th scope="col" style="display: none"></th>
                        <iais:sortableHeader needSort="false" field="" value="No."  />
                        <iais:sortableHeader needSort="true" field="USER_ID" value="User ID"/>
                        <iais:sortableHeader needSort="true" field="EMAIL_ADDR" value="Email address"/>
                        <iais:sortableHeader needSort="true" field="STATUS" value="Account Status"/>
                        <iais:sortableHeader needSort="false" field="" value="Action"  />
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty IntranetUserSearchResult.rows}">
                            <tr>
                                <td colspan="12">
                                    <iais:message key="GENERAL_ACK018" escape="true"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="user" items="${IntranetUserSearchResult.rows}" varStatus="status">
                                <tr>
                                    <td>
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" value="<iais:mask name="maskUserId" value="${user.id}"/>" name="userUid"
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
                </div>
                <!--delete  Modal -->
                <div class="modal fade" id="deleteModal" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
<%--                            <div class="modal-header">--%>
<%--                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                            </div>--%>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-12"><span style="font-size: 2rem">This account has been used for log in before and hence is not eligible for deletion.</span></div>
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
                <div class="modal fade" id="importUser" tabindex="-1" role="dialog" aria-labelledby="importUser">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
<%--                            <div class="modal-header">--%>
<%--                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span--%>
<%--                                        aria-hidden="true">&times;</span></button>--%>
<%--                                <div class="modal-title" id="gridSystemModalLabel" style="font-size: 2rem;">Confirmation Box</div>--%>
<%--                            </div>--%>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-12"><span style="font-size: 2rem">Do you confirm the modification ?</span>
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
            <div class="panel-heading" id="exportError" style="display: none; font-size: 1.6rem; color: #D22727; padding-left: 20px"><iais:message key="USER_ERR021"
                                                                                                escape="flase"></iais:message></div>
            <iais:action style="text-align:center;">
                <div class="text-right">
                    <a class="btn btn-primary" onclick="doCreate()">Create</a>
                    <a class="btn btn-primary" onclick="doExport()">Export</a>
                    <input type="file" id="inputFile" name="xmlFile" style="display:none" onchange="javascript:doImport();"/>
                    <input type="button" class="btn btn-primary" onclick="document.getElementById('inputFile').click()" value="Import"/>
                    <a class="btn btn-primary" onclick="doExportRole()">Export Role</a>
                    <input class="selectedFile premDoc" id="userRoleUpload" name = "userRoleUpload" type="file" onchange="javascript:doUserRoleUploadFile()" style="display: none;" aria-label="selectedFile1"/>
                    <button type="button" class="btn btn-file-upload btn-primary" onclick="javascript:doUserRoleUploadDo()">Import Role</button>
                </div>
                <p></p>
                <div class="text-right"><span class="error-msg" name="iaisErrorMsg" id="error_userUploadFile"></span></div>
                <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
            </iais:action>
        </div>
    </div>
</div>

</div>
<script>
    function doUserRoleUploadDo() {
        showWaiting();
        $("#userRoleUpload").trigger('click');
        dismissWaiting();
    }

    function doUserRoleUploadFile() {
        showWaiting();
        const userFileSize = $("#userFileSize").val();
        const error = validateUploadSizeMaxOrEmpty(userFileSize, "userRoleUpload");
        if (error == "N"){
            $('#error_userUploadFile').html($("#fileMaxMBMessage").val());
            dismissWaiting();
        } else {
            const file = $("#userRoleUpload").val();
            const ext = file.slice(file.lastIndexOf(".") + 1).toLowerCase();
            if ("xml" != ext) {
                $('#error_userUploadFile').html('Only files with the following extensions are allowed: XML. Please re-upload the file.');
                dismissWaiting();
            } else {
                submitUser('importUserRole');
            }
        }
    }

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