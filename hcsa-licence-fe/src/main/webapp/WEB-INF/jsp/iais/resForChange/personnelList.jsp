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
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<br/>
<div class="tab-pane" id="tabApp" role="tabpanel" style="margin-bottom: 4%;">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="personnel-list-gp">
                        <div class="personnel-list">
                            <h2>LICENCE-SPECIFIC ROLES</h2>
                            <form class="form-inline" method="post" id="menuListForm" style="justify-content:space-between" action=<%=process.runtime.continueURL()%>>
                                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                <input type="hidden" name="crud_action_type" value="">
                                <input type="hidden" id="personnelNo" name="personnelNo" value=""/>
                                <div class="form-group">
                                    <label class="control-label" for="psnType">Roles</label>
                                    <div class="col-xs-12 col-md-8 col-lg-9">
                                        <iais:select name="psnTypes" id="psnType" value="${psnType}" options="PersonnelRoleList"
                                                     firstOption="All" onchange="doSearch()"></iais:select>
                                    </div>
                                </div>
                                <div class="form-group large right-side">
                                    <div class="search-wrap">
                                        <div class="input-group">
                                            <input class="form-control" placeholder="Search Your Keywords" value="${personName}" type="text" name="personName">
                                            <span class="input-group-btn"><button class="btn btn-default buttonsearch"><em class="fa fa-search"></em></button></span>
                                        </div>
                                    </div>
                                </div>
                            </form>
                            <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div id="personPagDiv"></div>
                    <div class="col-sx-12">
                        <div class="table-responsive">
                            <table aria-describedby="" class="table">
                                <thead style="align-content: center">
                                <tr>
                                    <th scope="col" style="width: 15%">Name</th>
                                    <th scope="col" style="width: 15%">Email</th>
                                    <th scope="col" style="width: 15%">Mobile</th>
                                    <th scope="col" style="width: 30%">Licence</th>
                                    <th scope="col" style="width: 25%">Roles</th>
                                </tr>
                                </thead>
                                <c:choose>
                                    <c:when test="${noRecord == 'Y'}">
                                        <tbody>
                                        <td colspan="6">
                                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                        </td>
                                        </tbody>
                                    </c:when>
                                    <c:otherwise>
                                        <tbody id="personBodyDiv"></tbody>
                                    </c:otherwise>
                                </c:choose>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12">
                    <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"></em> Back</a>
                </div>
            </div>
        </div>
    </div>




</div>

<style>
    .col-md-4 {
        width: 23%;
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
        Utils.submit("menuListForm", "search");
    }


    function jumpToPagechangePage() {
        Utils.submit("menuListForm", "page");
    }

    function sortRecords(sortFieldName, sortType) {
        Utils.submit("menuListForm", "sort", sortFieldName, sortType);
    }
</script>