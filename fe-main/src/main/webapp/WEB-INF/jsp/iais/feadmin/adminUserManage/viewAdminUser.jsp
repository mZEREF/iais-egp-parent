<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<style>
    .backPadding{
        padding-left: 90px;
    }
    @media only screen and (max-width: 767px){
        .backPadding{
            padding-left: 20px;
        }
    }

    </style>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>Internet User Accounts</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_deactivate" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="table-responsive">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" >ID No.</th>
                                    <th scope="col" >ID Type</th>
                                    <th scope="col" >Salutation</th>
                                    <th scope="col" >Name</th>
                                    <th scope="col" >Designation</th>
                                    <th scope="col" >Is Administrator</th>
                                    <th scope="col" >Is Active</th>
                                    <th scope="col" >Action</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty feAdmin}">
                                        <tr>
                                            <td colspan="12">
                                                <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item"  items="${feAdmin}" varStatus="status">
                                            <tr style="display: table-row;">
                                                <%--<td>--%>
                                                    <%--<p><c:out  value="${(status.index + 1) + (feAdminSearchParam.pageNo - 1) * feAdminSearchParam.pageSize}"/></p>--%>
                                                <%--</td>--%>
                                                <td>
                                                    <p><c:out value="${item.idNo}"/></p>
                                                </td>
                                                <td>
                                                    <p><iais:code code="${item.idType}"/></p>
                                                </td>
                                                <td>
                                                    <p><iais:code code="${item.salutation}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.name}"/></p>
                                                </td>
                                                <td>
                                                    <c:if test="${item.designation != 'DES999'}">
                                                        <p><iais:code code="${item.designation}"/></p>
                                                    </c:if>
                                                    <c:if test="${item.designation == 'DES999'}">
                                                        <iais:code code="${item.designation}"/>
                                                        <br/><c:out value="${item.designationOther}"/>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.role == 'ORG_ADMIN'}">
                                                            <p>Yes</p>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <p>No</p>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td id="active${item.id}">
                                                    <c:choose>
                                                    <c:when test="${item.isActive eq '1'}">
                                                        <p>Yes</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>No</p>
                                                    </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <p><a onclick="edit('userIndex${status.index}')">Edit</a></p>
                                                    <input hidden name="userIndex${status.index}" value="<iais:mask name='userIndex${status.index}' value='${item.id}'></iais:mask>">
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-4 col-sm-2">
                                    <div><a id="back" class="backPadding"><em class="fa fa-angle-left"></em> Back</a></div>
                                </div>
                                <div class="col-xs-8 col-md-9">
                                    <div class="text-right">
                                        <button class="btn btn-primary" id="savebtn"
                                                onclick="javascript:create()">Create</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <input hidden name="userIndex" value="">
    </form>
</div>
<script>
    function create() {
        $("[name='crud_action_type']").val("create");
        var mainPoolForm = document.getElementById('mainForm');
        mainPoolForm.submit();
    }

    function edit(name) {
        $("[name='crud_action_type']").val("edit");
        $("[name='userIndex']").val(name);
        var mainPoolForm = document.getElementById('mainForm');
        mainPoolForm.submit();
    }
    $("#back").click(function () {
        $("[name='crud_action_type']").val("back");
        var mainPoolForm = document.getElementById('mainForm');
        mainPoolForm.submit();
    })
    function doActive(id,targetStatus){
        $.post(
            '/main-web/feAdmin/active.do',
            {
                userId: id,
                targetStatus:targetStatus
            },
            function (data, status) {
                var res = data.result;
                var status = data.active;
                if(res == "Success"){
                    if(status == "true"){
                        console.log("true")
                        // $("#active" + id).empty();
                        $("#active" + id).html("<p><a onclick=\"doActive('"+id+"','inActive')\">Active</a></p>");
                        // $("#inactive" + id).empty();
                        $("#inactive" + id).html("<p>De-active</p>");
                    }else{
                        console.log("false")
                        // $("#active" + id).empty();
                        $("#active" + id).html("<p>Active</p>");
                        // $("#inactive" + id).empty();
                        $("#inactive" + id).html("<p><a onclick=\"doActive('"+id+"','Active')\">De-activate</a></p>");
                    }

                }
            }
        )
    }


</script>