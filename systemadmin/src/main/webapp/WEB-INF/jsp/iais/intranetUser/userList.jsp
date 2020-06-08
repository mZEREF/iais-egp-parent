
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
            <input type="checkbox" class="form-check-input" name="userUids" id="checkboxAll" onchange="javascirpt:checkAll();" hidden/>
            <label class="form-check-label" for="checkboxAll"><span class="check-square"></span></label>
            </div>
        </th>
    </c:when>
    <c:otherwise>
        <th>
            <div class="form-check">
            <input type="checkbox" class="form-check-input" name="userUids" id="checkboxAll" onchange="javascirpt:checkAll();"/>
            <label class="form-check-label" for="checkboxAll"><span class="check-square"></span></label>
            </div>
        </th>
    </c:otherwise>
</c:choose>
        <iais:sortableHeader needSort="false" field="index" value="No."/>
        <iais:sortableHeader needSort="true" field="USER_ID" value="User ID"/>
        <iais:sortableHeader needSort="true" field="EMAIL_ADDR" value="Email address"/>
        <iais:sortableHeader needSort="true" field="STATUS" value="Account Status"/>
        <iais:sortableHeader needSort="false" field="action" value="Action"/>
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
                            <input class="form-check-input" type="checkbox" value="<iais:mask name="maskUserId" value="${user.id}"/>" name="userUid"
                                   id="userUid<iais:mask name="maskUserId" value="${user.id}"/>">
                            <label class="form-check-label" for="userUid<iais:mask name="maskUserId" value="${user.id}"/>"><span class="check-square"></span></label>
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
                                    onclick="doEdit('<iais:mask name="maskUserId" value="${user.id}"/>')">Edit
                            </button>
                            <button type="button" class="btn btn-default btn-sm"
                                    onclick="doDelete('<iais:mask name="maskUserId" value="${user.id}"/>')">Delete
                            </button>
                            <button type="button" class="btn btn-default btn-sm"
                                    onclick="doRole('<iais:mask name="maskUserId" value="${user.id}"/>')">role
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
        </table>
        </div>
    <div class="panel-heading" id="exportError" hidden><h4><strong>Please select user.</strong></h4></div>
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
        $("input[name='userUid']").attr("checked","true");
        } else {
        $("input[name='userUid']").removeAttr("checked");
        }
        }
        </script>