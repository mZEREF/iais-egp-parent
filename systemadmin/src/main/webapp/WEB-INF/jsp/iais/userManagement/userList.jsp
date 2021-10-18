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
                        <h2>Licensee User Management</h2>
                    </div>
                    <div class="row search-pannel">
                        <div class="form-horizontal" id="searchCondition">
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">ID No.</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input type="text" name="idNo" maxlength="500" id="idNo" value="${idNo}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">Designation</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select id="designation" name="designation" codeCategory="CATE_ID_DESIGNATION" cssClass="designationOption" firstOption="Please Select"
                                    value="${designation}"></iais:select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">UEN</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input type="text" name="uenNo" maxlength="500" id="licenseeName" value="${uenNo}"/>
                                </div>
                            </div>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-11 col-md-11">
                                    <div class="text-right">
                                        <a class="btn btn-secondary" onclick="javascript:clearSearch()">Clear</a>
                                        <a class="btn btn-primary" onclick="javascript:searchUser()">Search</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <h3>
                        <span>Search Results</span>
                    </h3>
                    <iais:pagination param="feAdminSearchParam" result="feAdmin"/>

                    <div class="table-gp">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" ></th>
                                <iais:sortableHeader needSort="false" field="subject" value="S/N" style="width:5%"/>
                                <iais:sortableHeader needSort="true" field="ID_NO" value="ID No." style="width:12%"/>
                                <iais:sortableHeader needSort="true" field="ID_TYPE" value="ID Type" style="width:12%"/>
                                <iais:sortableHeader needSort="true" field="SALUTATION" value="Salutation" style="width:13%"/>
                                <iais:sortableHeader needSort="true" field="DISPLAY_NAME" value="Name" style="width:13%"/>
                                <iais:sortableHeader needSort="true" field="DESIGNATION" value="Designation" style="width:15%"/>
                                <iais:sortableHeader needSort="true" field="ROLE_ID" value="Is Administrator" style="width:15%"/>
                                <iais:sortableHeader needSort="true" field="STATUS" value="Is Active" style="width:10%"/>
                                <iais:sortableHeader needSort="false" field="subject" value="Action" style="width:5%"/>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty feAdmin.rows}">
                                    <tr>
                                        <td colspan="10">
                                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                            <!--No Record!!-->
                                        </td>
                                    </tr>
                                    <input hidden id="rows" value="0">
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${feAdmin.rows}" varStatus="status">
                                        <c:set var="userIndex" value="${(status.index + 1) + (feAdminSearchParam.pageNo - 1) * feAdminSearchParam.pageSize}"></c:set>
                                        <tr style="display: table-row;">
                                            <td>
                                                <p><input type="checkbox" id="userId${userIndex}" name="userId" value="<iais:mask name='userId' value='${item.id}'/>" ></p>
                                            </td>
                                            <td>
                                                <p><c:out  value="${status.index + 1}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out  value="${item.idNo}"/></p>
                                            </td>
                                            <td>
                                                <p><iais:code code="${item.idType}"></iais:code></p>
                                            </td>
                                            <td>
                                                <p><iais:code code="${item.salutation}"></iais:code></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.name}"/></p>
                                            </td>
                                            <td>
                                                <p>
                                                    <iais:code code="${item.designation}"/>
                                                    <c:if test="${item.designation == 'DES999'}">
                                                        <br/><c:out value="${item.designationOther}"/>
                                                    </c:if>
                                                </p>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${'ORG_ADMIN' == item.role }">
                                                        <p>Yes</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>No</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <p><c:choose>
                                                    <c:when test="${item.isActive eq 'CMSTAT001'}">
                                                <p>Yes</p>
                                                </c:when>
                                                <c:otherwise>
                                                    <p>No</p>
                                                </c:otherwise>
                                                </c:choose></p>
                                            </td>
                                            <td>
                                                <p><a href="javascript:void(0);" onclick="edit('${userIndex}')">Edit</a></p>
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
                        <a class="btn btn-primary" id="addlist" onclick="create()">Create</a>
                        <a class="btn btn-primary" id="delete" onclick="deleteUser()">Delete</a>
                    </div>
                </div>
            </div>
        </div>
        <input hidden value="" id="userId" name="userId">
        <input hidden id="fieldName" name="fieldName" value="">
        <input hidden id="sortType" name="sortType" value="">
        <iais:confirm msg="Please select record for deletion."  needCancel="false" callBack="cancel()" popupOrder="support" ></iais:confirm>
        <iais:confirm msg="Do you confirm the delete?"  needCancel="true" callBack="submitFrom()" popupOrder="deletePopup" ></iais:confirm>
    </form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function edit(index) {
        $('#userId').val($('#userId' + index).val());
        SOP.Crud.cfxSubmit("mainForm", "edit");
    }

    function deleteUser() {
        if ($("input:checkbox:checked").length > 0) {
            $('#deletePopup').modal('show');
        }else{
            $('#support').modal('show');
        }
    }

    function submitFrom(){
        SOP.Crud.cfxSubmit("mainForm", "delete");
    }

    function searchUser() {
        SOP.Crud.cfxSubmit("mainForm", "search");
    }
    function cancel() {
        $('#support').modal('hide');
    }
    function tagConfirmCallbacksupport() {
        $('#support').modal('hide');
    }
    function create() {
        SOP.Crud.cfxSubmit("mainForm", "create");
    }
    
    function clearSearch() {
        clearFields('.search-pannel');
    }

    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("mainForm", "searchPage");
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='fieldName']").val(sortFieldName);
        $("[name='sortType']").val(sortType);
        SOP.Crud.cfxSubmit("mainForm", "search");
    }

</script>