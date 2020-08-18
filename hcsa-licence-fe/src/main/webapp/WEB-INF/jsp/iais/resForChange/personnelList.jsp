<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp" %>
<br/>
<div class="tab-pane" id="tabApp" role="tabpanel">
    <form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" id="personnelNo" name="personnelNo" value=""/>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="row" style="margin-left: 15%;margin-right: 10%">
            <div class="col-xs-12" style="margin-bottom: 1%">
                <div class="col-sm-12 col-md-1">
                    <label style="margin-left: -31%" class="control-label" for="psnType">Roles:</label>
                </div>
                <div class="col-sm-12 col-md-4" style="margin-right: 2%;width: 20%">
                    <iais:select name="psnTypes" id="psnType" value="${psnType}" options="PersonnelRoleList"
                                 firstOption="All" onchange="doSearch()"></iais:select>
                </div>
                <div class="col-sm-12 col-md-4" style="width: 35%">
                </div>
                <div class="col-sm-12 col-md-3" style="margin-right: 2%">
                    <div class="search-wrap">
                        <div class="input-group">
                            <input class="form-control" placeholder="Search Your Keywords" value="${personName}" type="text" name="personName">
                            <span class="input-group-btn"><button class="btn btn-default buttonsearch"><i class="fa fa-search"></i></button></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="table-gp">
                <div id="personPagDiv"></div>
                <table class="table">
                    <thead style="align-content: center">
                    <tr>
                        <th style="width: 12%">Name</th>
                        <th style="width: 12%">Email</th>
                        <th style="width: 12%">Mobile</th>
                        <th style="width: 12%">Licence</th>
                        <th style="width: 12%">Roles</th>
                    </tr>
                    </thead>
                    <c:choose>
                        <c:when test="${noRecord == 'Y'}">
                            <tbody>
                            <tr>
                                <td>No record found.</td>
                            </tr>
                            </tbody>
                        </c:when>
                        <c:otherwise>
                            <tbody id="personBodyDiv"></tbody>
                        </c:otherwise>
                    </c:choose>
                </table>
                <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <br/>
        </div>
    </form>
</div>

<style>
    .col-md-4 {
        width: 23%;
    }
    .table {
        width: 88%;
        max-width: 100%;
        margin-bottom: 20px;
    }
</style>
<script>
    function doPersonnel(obj) {
        $('#personnelNo').val(obj);
        doSubmitForm('edit', '', '');
    }

    function doSubmitForm(action, value, additional) {
        $("[name='crud_action_type']").val(action);
        $("[name='crud_action_additional']").val(additional);
        var mainForm = document.getElementById('menuListForm');
        mainForm.submit();
    }

    function doBack(action) {
        $("[name='crud_action_type']").val('back');
        $("[name='crud_action_type']").val(action);
        var mainForm = document.getElementById('menuListForm');
        mainForm.submit();
    }

    function doSearch() {
        SOP.Crud.cfxSubmit("menuListForm", "search");
    }


    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("menuListForm", "page");
    }

    function sortRecords(sortFieldName, sortType) {
        SOP.Crud.cfxSubmit("menuListForm", "sort", sortFieldName, sortType);
    }
</script>