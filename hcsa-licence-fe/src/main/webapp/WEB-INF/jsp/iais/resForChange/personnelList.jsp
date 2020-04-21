<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp"%>
<br/>
<div class="tab-pane" id="tabApp" role="tabpanel">
    <form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_type_form_value" value="">
        <input type="hidden" id="hiddenIndex" name="hiddenIndex" value="" />

        <div class="row col-xs-12">
            <div class="col-xs-12">
                <label>${PersonnelListMap.size()} outof ${PersonnelListMap.size()}</label>
            </div>
        </div>
        <div class="col-xl-12">
            <div class="col-sm-5 col-md-1">
                <label class="control-label" for="psnType">Roles: </label>
            </div>
            <div class="col-sm-5 col-md-2">
                <iais:select name="" id="psnType" options="PersonnelRoleList" firstOption="All"></iais:select>
            </div>
            <div class="col-sm-5 col-md-6">
            </div>
            <div class="col-sm-5 col-md-3">
                <div class="col-md-11">
                    <input type="text" placeholder="Search Your Keywords"/>
                </div>
            </div>
        </div>
        <div class="row col-xs-12 ">
            <div class="col-xs-12">
                <div class="table-gp">
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
                    <tbody>
                    <c:forEach var="personnelist" items="${PersonnelListMap}" varStatus="status">
                        <c:set var="onePersonnel" value="${personnelist.value.get(0)}"></c:set>
                        <tr class="personnel">
                            <td>
                                <p><a  class="personnel"><c:out value="${onePersonnel.psnName}"/></a></p>
                                <input type="hidden" class="statusIndex" name="statusIndex" value="${status.index}" />
                                <input type="hidden" class="personnelNo" name="personnelNo${status.index}" value="<iais:mask name="personnelNo${status.index}" value="${onePersonnel.idNo}" />"/>
                            </td>
                            <td><c:out value="${onePersonnel.emailAddr}"/></td>
                            <td><c:out value="${onePersonnel.mobileNo}"/></td>
                            <td>
                                <c:forEach var="personnel" items="${personnelist.value}">
                                    <p><c:out value="${personnel.svcId}"/></p>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="personnel" items="${personnelist.value}">
                                    <c:choose>
                                        <c:when test="${'CGO'==personnel.psnType}">
                                            <p>Clinical Governance Officer</p>
                                        </c:when>
                                        <c:when test="${'PO'==personnel.psnType}">
                                            <p>Principal Officer</p>
                                        </c:when>
                                        <c:when test="${'DPO'==personnel.psnType}">
                                            <p>Deputy Principal Officer</p>
                                        </c:when>
                                    </c:choose>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    </table>
                </div>
            </div>
            <div class="col-xs-12 text-center">
                <a class="btn btn-secondary" id="" >Download the List</a></div>
            </div>
        </div>

        <div class="row col-xs-11 ">
            <div class="col-xs-12">
                <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"></em> Back</a>
            </div>
        </div>

        <div class="row">
        </div>
    </form>
</div>
<script>
    $(document).ready(function () {


    });

    $('a.personnel').click(function () {
        $personnelEle = $(this).closest('tr.personnel');

        var index =  $personnelEle.find('.statusIndex').val();
        $('#hiddenIndex').val(index);
        doSubmitForm('preparePersonnelEdit','', '');

    });

    $('#Back').click(function() {
    });


    function doSubmitForm(action,value,additional){
        $("[name='crud_action_type_form_value']").val(action);


        var mainForm = document.getElementById('menuListForm');
        mainForm.submit();
    }

    function doBack(action ) {
        $("[name='crud_action_type_value']").val('back');
        $("[name='crud_action_type_form_value']").val(action);
        var mainForm = document.getElementById('menuListForm');
        mainForm.submit();

    }
</script>