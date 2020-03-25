<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="bg-title">
                        <h2>Blast Management</h2>
                    </div>
                    <div class="row">
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">Message Name</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input type="text" name="msgName" maxlength="500" id="msgName" value="${msgName}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">Scheduled Send Date From</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="start" name="start" value="${start}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">Scheduled Send Date To</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="end" name="end" value="${end}"/>
                                </div>
                            </div>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-11 col-md-11">
                                    <div class="text-right">
                                        <a class="btn btn-secondary" onclick="javascript:clearSearch()">Clear</a>
                                        <a class="btn btn-primary" onclick="javascript:search()">Search</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <iais:pagination param="blastSearchParam" result="blastSearchResult"/>

                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <th></th>
                                <th>Message ID</th>
                                <th>Message Name</th>
                                <th>Distribution Name</th>
                                <th>Mode of Delivery</th>
                                <th>Scheduled Send date</th>
                                <th>Actual send date</th>
                                <th>Attachment</th>
                                <th>Status</th>
                                <th>Edit</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty blastSearchResult.rows}">
                                    <tr>
                                        <td colspan="10">
                                            <iais:message key="No Result!" escape="true"></iais:message>
                                            <!--No Record!!-->
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${blastSearchResult.rows}" varStatus="status">
                                        <tr style="display: table-row;">
                                            <td>
                                                <p><input type="checkbox" name="checkboxlist" value="${item.id}"></p>
                                            </td>
                                            <td>
                                                <p><c:out
                                                        value="${(status.index + 1) + (blastSearchParam.pageNo - 1) * blastSearchParam.pageSize}"/></p>
                                            </td>
                                            <td>
                                                <p><a onclick="audit('${item.id}')"><c:out value="${item.msgName}"/></a>
                                                </p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.distributionName}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.mode}"/></p>
                                            </td>
                                            <td>
                                                <p><fmt:formatDate value="${item.schedule}"
                                                                   pattern="dd/MM/yyyy HH:mm"/></p>
                                            </td>
                                            <td>
                                                <p><fmt:formatDate value="${item.actual}"
                                                                   pattern="dd/MM/yyyy HH:mm"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.docName}"/></p>
                                            </td>
                                            <td>
                                                <p><iais:code code="${item.status}"></iais:code></p>
                                            </td>
                                            <td>
                                                <p><a onclick="edit('${item.id}')">edit</a></p>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="application-tab-footer">
            <div class="row">
                <div class="col-xs-11 col-md-11">
                    <div class="text-right">
                        <a class="btn btn-primary" id="addlist" onclick="addList()">Add Blast Management</a>
                        <a class="btn btn-primary" id="delete" onclick="deleteList()">Delete</a>
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/file-repo" title="Download">Download Excel</a>
                    </div>
                </div>
            </div>
        </div>
        <input hidden id="editBlast" name="editBlast" value="">
    </form>
</div>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    function addList() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "create");
    }

    function deleteList() {
        if ($("input:checkbox:checked").length > 0) {
            if(confirm('Are you sure you want to delete this item?')){
                SOP.Crud.cfxSubmit("mainForm", "delete")
            }
        } else {
            alert('Please select record for deletion.');
        }
    }

    function edit(id) {
        $("#editBlast").val(id);
        SOP.Crud.cfxSubmit("mainForm", "edit");
    }

    function audit(id) {
        $("#editBlast").val(id);
        SOP.Crud.cfxSubmit("mainForm", "audit");
    }

    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("mainForm", "search");
    }

    function search() {
        SOP.Crud.cfxSubmit("mainForm", "search");
    }

    function clearSearch() {
        $('input[name="descriptionSwitch"]').val("");
        $('input[name="msgName"]').val("");
        $('input[name="start"]').val("");
        $('input[name="end"]').val("");
    }
</script>