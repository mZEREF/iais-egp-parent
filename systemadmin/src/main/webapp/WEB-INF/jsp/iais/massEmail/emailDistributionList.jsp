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
                        <h2>Distribution List</h2>
                    </div>
                    <div class="row">
                        <div class="form-horizontal" id="searchCondition">
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label" >Distribution Name</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input type="text" name="distributionName" id="distributionName" maxlength="500" value="${distributionName}"/>
                                </div>
                            </div>
                            <div class="form-group" >
                                <iais:field value="Service"/>
                                <iais:value>
                                    <iais:value width="10">
                                        <iais:select name="service" id="service" options="serviceSelection"
                                                     firstOption="Please Select"  value="${service}"></iais:select>
                                    </iais:value>
                                </iais:value>
                                <span id="error_service" name="iaisErrorMsg" class="error-msg"></span>
                            </div>

                            <div class="form-group" id="serviceDivByrole">
                                <iais:field value="Distribution List" />
                                <iais:value>
                                    <div class="col-xs-8 col-sm-6 col-md-5">
                                        <iais:select name="role" options="roleSelection" value="${role}" firstOption="Please Select" disabled=""></iais:select>
                                        <span id="error_role" name="iaisErrorMsg" class="error-msg"></span>
                                    </div>
                                </iais:value>
                            </div>

                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-11 col-md-11">
                                    <div class="text-right">
                                        <a class="btn btn-secondary" onclick="clearSearch()">Clear</a>
                                        <a class="btn btn-primary" onclick="searchResult()">Search</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <iais:pagination param="distributionSearchParam" result="distributionSearchResult"/>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <th></th>
                                <th>Distribution List ID</th>
                                <th>Distribution Name</th>
                                <th>Service</th>
                                <th>Recipients Role</th>
                                <th>Mode of Delivery</th>
                                <th>Created Date</th>
                                <th>Created By</th>
                                <th>Edit</th>
                            </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty distributionSearchResult.rows}">
                                        <tr>
                                            <td  colspan="9" >
                                                <iais:message key="No Result!" escape="true"></iais:message>
                                                <!--No Record!!-->
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${distributionSearchResult.rows}" varStatus="status">
                                            <tr style="display: table-row;">
                                                <td>
                                                    <p><input type="checkbox" name="checkboxlist" value='<iais:mask name="checkboxlist" value="${item.id}"/>'></p>
                                                </td>
                                                <td>
                                                    <p><c:out
                                                            value="${(status.index + 1) + (distributionSearchParam.pageNo - 1) * distributionSearchParam.pageSize}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.disname}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.service}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.role}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.mode}"/></p>
                                                </td>
                                                <td>
                                                    <p><fmt:formatDate value="${item.createDt}" pattern="MM/dd/yyyy"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.createBy}"/></p>
                                                </td>
                                                <td>
                                                    <p><a onclick="edit('<iais:mask name="editDistribution" value="${item.id}}"/>')">Edit</a></p>
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
                        <a class="btn btn-primary" oid="addlist" onclick="addList()">Add new distribution list</a>
                        <a class="btn btn-primary" id="delete" onclick="deleteList()">Delete</a>
                    </div>
                </div>
            </div>
        </div>
        <input hidden id="editDistribution" name="editDistribution" value="">
    </form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
function addList() {
    showWaiting();
    SOP.Crud.cfxSubmit("mainForm","create");
}
function deleteList() {
    checkUse();
}
function checkUse() {
    var deleteDis=new Array();
    $.each($('input:checkbox:checked'),function(){
        deleteDis.push($(this).val());
    });
    $.ajax({
        data:{
            checkboxlist:deleteDis
        },
        traditional: true,
        type:"POST",
        dataType: 'json',
        url:'/system-admin-web/emailAjax/checkUse.do',
        error:function(data){

        },
        success:function(data){
            if(data.res == 'true'){
                alert('The distribution list cannot be deleted as it is still in used by other mass email or sms blast.')
            }else{
                if ($("input:checkbox:checked").length > 0) {
                    if(confirm('Are you sure you want to delete this item?')){
                        SOP.Crud.cfxSubmit("mainForm", "delete")
                    }
                } else {
                    alert('Please select record for deletion.');
                }
            }
        }
    });
}
function edit(id) {
    $("#editDistribution").val(id);
    SOP.Crud.cfxSubmit("mainForm","edit");
}
function jumpToPagechangePage() {
    SOP.Crud.cfxSubmit("mainForm","search");
}
function searchResult() {
    SOP.Crud.cfxSubmit("mainForm","search");
}
function clearSearch(){
    $('input[name="distributionName"]').val("");
    $("#role option:first").prop("selected", 'selected');
    $("#service option:first").prop("selected", 'selected');
    $("#searchCondition .current").text("Please Select");
}

$("#service").change(function () {
    $.ajax({
        data:{
            serviceCode:$(this).children('option:selected').val()
        },
        type:"POST",
        dataType: 'json',
        url:'/system-admin-web/emailAjax/recipientsRoles.do',
        error:function(data){

        },
        success:function(data){
            var html = '<label class="col-xs-4 col-md-4 control-label" >Distribution List</label><div class="col-xs-8 col-sm-6 col-md-5">';
            html += data.roleSelect;
            html += ' <span id="error_role" name="iaisErrorMsg" class="error-msg"></span></div>';
            $("#serviceDivByrole").html(html);
        }
    });
})

</script>