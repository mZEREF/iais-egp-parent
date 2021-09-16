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
<style>
    .okBtn{
        float: right;margin-left: 5px;
    }
    td p{
        word-wrap: break-word;
        word-break: break-all;
    }
</style>
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
                    <h3>
                        <span>Search Results</span>
                    </h3>
                    <iais:pagination param="blastSearchParam" result="blastSearchResult"/>

                    <div class="table-gp">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr >
                                <th scope="col" ></th>
                                <iais:sortableHeader needSort="false" field="" value="S/N" style="width:1%"/>
                                <iais:sortableHeader needSort="true" field="SUBJECT" value="Email Subject" style="width:14%"/>
                                <iais:sortableHeader needSort="true" field="MSG_NAME" value="Message Name" style="width:14%"/>
                                <iais:sortableHeader needSort="true" field="DISTRIBUTION_NAME" value="Distribution Name" style="width:15%"/>
                                <iais:sortableHeader needSort="true" field="DELIVERY_MODE" value="Mode of Delivery" style="width:10%"/>
                                <iais:sortableHeader needSort="true" field="SCHEDULE_SEND_DATE" value="Scheduled Send Date" style="width:10%"/>
                                <iais:sortableHeader needSort="true" field="ACTUAL_SEND_DATE" value="Actual Send Date" style="width:10%"/>
                                <iais:sortableHeader needSort="true" field="DOC_NAME" value="Attachment" style="width:10%"/>
                                <iais:sortableHeader needSort="true" field="STATUS" value="Status" style="width:8%"/>
                                <iais:sortableHeader needSort="false" field="subject" value="Action" style="width:8%"/>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty blastSearchResult.rows}">
                                    <tr>
                                        <td colspan="10">
                                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                            <!--No Record!!-->
                                        </td>
                                    </tr>
                                    <input hidden id="rows" value="0">
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${blastSearchResult.rows}" varStatus="status">
                                        <c:set var="massIndex" value="${(status.index + 1) + (blastSearchParam.pageNo - 1) * blastSearchParam.pageSize}"></c:set>
                                        <tr style="display: table-row;">
                                            <td>
                                                <p><input type="checkbox" id="edit${massIndex}" name="editBlast" value="<iais:mask name='editBlast' value='${item.id}'/>"
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
                                                <p><c:out  value="${massIndex}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.subject}"/>
                                                </p>
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
                                                <p style="word-break: normal"><c:out value="${item.schedule}"/></p>
                                            </td>
                                            <td>
                                                <p style="word-break: normal"><c:out value="${item.actual}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.docName}"/></p>
                                            </td>
                                            <td>
                                                <p><iais:code code="${item.status}"></iais:code></p>
                                            </td>
                                            <td>
                                                <p>
                                                    <c:choose>
                                                        <c:when test="${!empty item.actual}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="#" onclick="edit('${massIndex}')">Edit</a>
                                                        </c:otherwise>
                                                    </c:choose>

                                                    <a href="#" onclick="audit('${item.messageId}','${item.mode}','${item.createBy}','${item.createDt}','${item.modifiedBy}','${item.modifiedDt}')">Audit</a>
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
        <iais:confirm msg="Please select record for deletion."  needCancel="false" callBack="cancel()" popupOrder="support" ></iais:confirm>
        <iais:confirm msg="Are you sure you want to delete this item?" yesBtnCls="okBtn btn btn-primary"   needCancel="true" callBack="deleteDis()" popupOrder="deleteSupport" ></iais:confirm>
        <input hidden id="editBlast" name="editBlast" value="">
        <input hidden id="msgId" name="msgId" value="">
        <input hidden id="createby" name="createby" value="">
        <input hidden id="createDt" name="createDt" value="">
        <input hidden id="modifiedBy" name="modifiedBy" value="">
        <input hidden id="modifiedDt" name="modifiedDt" value="">
        <input hidden id="mode" name="mode" value="">
        <input hidden id="fieldName" name="fieldName" value="">
        <input hidden id="sortType" name="sortType" value="">
    </form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function sortRecords(sortFieldName, sortType) {
        $("[name='fieldName']").val(sortFieldName);
        $("[name='sortType']").val(sortType);
        SOP.Crud.cfxSubmit("mainForm", "search");
    }
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
        $("#editBlast").val();
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
                $('#support').find("span").eq(0).html("The message cannot be deleted as it has been sent out to recipients.");
                $('#support').modal('show');
            }
        } else {
            $('#support').find("span").eq(0).html("Please select record for deletion.");
            $('#support').modal('show');
        }
    }

    function edit(id) {
        var edit = $("#edit"+id).data("edit");
        var id = $("#edit"+id).val();
        if(edit == 1){
            $("#editBlast").val(id);
            SOP.Crud.cfxSubmit("mainForm", "edit");
        }else{
            $('#support').find("span").eq(0).html("The message cannot be amended as it has been sent out to recipients.");
            $('#support').modal('show');
        }

    }
    function preview(id) {
        $("#editBlast").val(id);
        SOP.Crud.cfxSubmit("mainForm", "preview");
    }
    function audit(id,mode,createby,createDt,modifiedBy,modifiedDt) {
        $("#msgId").val(id);
        $("#mode").val(mode);
        $("#createby").val(createby);
        $("#createDt").val(createDt);
        $("#modifiedBy").val(modifiedBy);
        $("#modifiedDt").val(modifiedDt);
        SOP.Crud.cfxSubmit("mainForm", "audit");
    }

    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("mainForm", "page");
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
        $("#error_errDate").hide();
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
                /*$("div.distributionList->ul").mCustomScrollbar({
                        advanced:{
                            updateOnContentResize: true
                        }
                    }
                );*/
            }
        });
    })
</script>