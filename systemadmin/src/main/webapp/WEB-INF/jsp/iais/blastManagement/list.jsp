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
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
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
                        <div class="form-horizontal" id="searchCondition">
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">Message Name</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input type="text" name="msgName" maxlength="500" id="msgName" value="${msgName}"/>
                                </div>
                            </div>
                            <div class="form-group" id="distributiondiv">
                                <label class="col-xs-12 col-md-4 control-label">Distribution List</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select id="distributionList" name="distributionList" options="distribution" firstOption="Please Select" value="${distributionList}"></iais:select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">Mode of Delivery</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select id="modeDelivery" name="modeDelivery" options="mode" value="${modeDelivery}" firstOption="Please Select" ></iais:select>
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
                                    <span class="error-msg" name="iaisErrorMsg" id="error_errDate" ></span>
                                </div>
                            </div>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-11 col-md-11">
                                    <div class="text-right">
                                        <a class="btn btn-secondary" onclick="javascript:clearSearch()">Clear</a>
                                        <a class="btn btn-primary" onclick="javascript:searchCondition()">Search</a>
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
                                <th>Created By</th>
                                <th>Created Date</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty blastSearchResult.rows}">
                                    <tr>
                                        <td colspan="10">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                            <!--No Record!!-->
                                        </td>
                                    </tr>
                                    <input hidden id="rows" value="0">
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${blastSearchResult.rows}" varStatus="status">
                                        <tr style="display: table-row;">
                                            <td>
                                                <p><input type="checkbox" name="checkboxlist" value="<iais:mask name="checkboxlist" value="${item.id}"/>"
                                                    <c:choose>
                                                    <c:when test="${!empty item.actual}">
                                                        data-edit = "0"
                                                    </c:when>
                                                    <c:otherwise>
                                                        data-edit = "1"
                                                    </c:otherwise>
                                                    </c:choose>
                                                ></p>
                                            </td>
                                            <td>
                                                <p><c:out
                                                        value="${(status.index + 1) + (blastSearchParam.pageNo - 1) * blastSearchParam.pageSize}"/></p>
                                            </td>
                                            <td>
                                                <p><a onclick="preview('${item.id}')"><c:out value="${item.msgName}"/></a>
                                                </p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.distributionName}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.mode}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.createBy}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.createDt}"/></p>
                                            </td>
                                            <td>
                                                <p><iais:code code="${item.status}"></iais:code></p>
                                            </td>
                                            <td>
                                                <p>
                                                    <a onclick="edit('${item.id}')">Edit</a>
                                                    <a onclick="audit('${item.messageId}','${item.mode}')">Audit</a>
                                                </p>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <input hidden id="rows" value="1">
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
                        <a class="btn btn-primary" id="addlist" onclick="addList()">Add New Blast Management</a>
                        <a class="btn btn-primary" id="delete" onclick="deleteList()">Delete</a>
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/file-repo" title="Download">Download</a>
                    </div>
                </div>
            </div>
        </div>
        <iais:confirm msg="The message cannot be amended as it has been sent out to recipients."  needCancel="false" callBack="cancel()" popupOrder="support" ></iais:confirm>
        <iais:confirm msg="Are you sure you want to delete this item?"  needCancel="true" callBack="deleteDis()" popupOrder="deleteSupport" ></iais:confirm>
        <input hidden id="editBlast" name="editBlast" value="">
        <input hidden id="mode" name="mode" value="">
    </form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function addList() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "create");
    }
    function deleteDis() {
        SOP.Crud.cfxSubmit("mainForm", "delete");
    }
    function cancel() {
        $('#support').modal('hide');
    }
    function tagConfirmCallbacksupport() {
        $('#support').modal('hide');
    }
    function deleteList() {
        if ($("input:checkbox:checked").length > 0) {
            var canedit = 1;
            $("input:checkbox:checked").each(function(i){
               var edit = $(this).data("edit");
               if(edit == 0){
                   canedit = 0;
               }
            })

            if(canedit == 1 ){
                $('#deleteSupport').modal('show');
            }else{
                $('#support').find("span").eq(1).html("The message cannot be deleted as it has been sent out to recipients.");
                $('#support').modal('show');
            }
        } else {
            $('#support').find("span").eq(1).html("Please select record for deletion.");
            $('#support').modal('show');
        }
    }

    function edit(id) {
        var edit = $("#edit"+id).val();
        if(edit == 1){
            $("#editBlast").val(id);
            SOP.Crud.cfxSubmit("mainForm", "edit");
        }else{
            $('#support').find("span").eq(1).html("The message cannot be amended as it has been sent out to recipients.");
            $('#support').modal('show');
        }

    }
    function preview(id) {
        $("#editBlast").val(id);
        SOP.Crud.cfxSubmit("mainForm", "preview");
    }
    function audit(id,mode) {
        $("#editBlast").val(id);
        $("#mode").val(mode);
        SOP.Crud.cfxSubmit("mainForm", "audit");
    }

    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("mainForm", "search");
    }

    function searchCondition() {
        SOP.Crud.cfxSubmit("mainForm", "search");
    }

    function clearSearch() {
        $('input[name="descriptionSwitch"]').val("");
        $('input[name="msgName"]').val("");
        $('input[name="start"]').val("");
        $('input[name="end"]').val("");
        $("#modeDelivery option:first").prop("selected", 'selected');
        $("#distributionList option:first").prop("selected", 'selected');
        $("#searchCondition .current").text("Please Select");
    }

    $("#modeDelivery").change(function () {
        $.ajax({
            data:{
                modeDelivery:$(this).children('option:selected').val()
            },
            type:"POST",
            dataType: 'json',
            url:'/system-admin-web/emailAjax/distributionList.do',
            error:function(data){

            },
            success:function(data){
                var html = '<label class="col-xs-12 col-md-4 control-label">Distribution List</label><div class="col-xs-8 col-sm-6 col-md-5">';
                html += data.distributionSelect;
                html += ' </div>';
                $("#distributiondiv").html(html);

            }
        });
    })
</script>