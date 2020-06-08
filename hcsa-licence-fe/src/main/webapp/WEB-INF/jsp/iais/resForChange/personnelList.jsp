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
        <input type="hidden" id="hiddenIndex" name="hiddenIndex" value=""/>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-xl-12" style="margin-left: 15%;margin-right: 10%">
            <div class="col-sm-5 col-md-1">
                <label class="control-label" for="psnType">Roles:</label>
            </div>
            <div class="col-sm-5 col-md-3">
                <iais:select name="psnTypes" id="psnType" value="${psnType}" options="PersonnelRoleList"
                             firstOption="All" onchange="doSearch()"></iais:select>
            </div>
            <div class="col-sm-5 col-md-5">
            </div>
            <div class="col-sm-5 col-md-3">
                <div class="col-md-11">
                    <input type="text" name="personName" value="${personName}" placeholder="Search Your Keywords"/>
                </div>
            </div>
        </div>
        <div class="row" style="margin-left: 15%;margin-right: 10%">
            <div class="col-xs-12">
                <div class="table-gp">
                    <iais:pagination param="PersonnelSearchParam" result="PersonnelSearchResult"/>
<%--                                        <div id="personPagDiv"></div>--%>
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Mobile</th>
                            <th>Licence</th>
                            <th>Roles</th>
                        </tr>
                        </thead>
<%--                                                <tbody id="personBodyDiv"></tbody>--%>
                        <tbody>
                        <c:forEach var="personne" items="${personnelListDtos}" varStatus="status">
                            <c:set var="map" value="${personne.licPsnTypeDtoMaps}"/>
                            <tr class="personnel">
                                <td>
                                    <p><a class="personnel"><c:out value="${personne.psnName}"/></a></p>
                                    <input type="hidden" class="statusIndex" name="statusIndex"
                                           value="${status.index}"/>
                                    <input type="hidden" class="personnelNo" name="personnelNo${status.index}"
                                           value="<iais:mask name="personnelNo${status.index}" value="${personne.idNo}" />"/>
                                </td>
                                <td><c:out value="${personne.emailAddr}"/></td>
                                <td><c:out value="${personne.mobileNo}"/></td>
                                <td>
                                    <c:forEach var="svcPsnTypes" items="${map}">
                                        <c:set var="dto" value="${svcPsnTypes.value}"/>
                                        <p><c:out value="${dto.licSvcName}"/></p>
                                        <c:forEach var="psnType" items="${dto.psnTypes}">
                                            <c:choose>
                                                <c:when test="${'CGO'==psnType}">
                                                    <p>&nbsp;</p>
                                                </c:when>
                                                <c:when test="${'PO'==psnType}">
                                                    <p>&nbsp;</p>
                                                </c:when>
                                                <c:when test="${'DPO'==psnType}">
                                                    <p>&nbsp;</p>
                                                </c:when>
                                                <c:when test="${'MAP'==psnType}">
                                                    <p>&nbsp;</p>
                                                </c:when>
                                            </c:choose>
                                        </c:forEach>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:forEach var="svcPsnTypes" items="${map}">
                                        <c:set var="dto" value="${svcPsnTypes.value}"/>
                                        <c:forEach var="psnType" items="${dto.psnTypes}">
                                            <c:choose>
                                                <c:when test="${'CGO'==psnType}">
                                                    <p>Clinical Governance Officer</p>
                                                </c:when>
                                                <c:when test="${'PO'==psnType}">
                                                    <p>Principal Officer</p>
                                                </c:when>
                                                <c:when test="${'DPO'==psnType}">
                                                    <p>Deputy Principal Officer</p>
                                                </c:when>
                                                <c:when test="${'MAP'==psnType}">
                                                    <p>MedAlert</p>
                                                </c:when>
                                            </c:choose>
                                        </c:forEach>
                                        <p>&nbsp;</p>
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em
                            class="fa fa-angle-left"></em> Back</a>
                </div>
            </div>
        </div>
    </form>
</div>
<script>
    $(document).ready(function () {

    });

    $('a.personnel').click(function () {
        $personnelEle = $(this).closest('tr.personnel');
        var index = $personnelEle.find('.statusIndex').val();
        $('#hiddenIndex').val(index);
        doSubmitForm('edit', '', '');

    });

    $('#Back').click(function () {
    });


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