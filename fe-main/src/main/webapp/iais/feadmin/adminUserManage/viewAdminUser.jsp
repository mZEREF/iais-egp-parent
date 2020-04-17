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
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_deactivate" value="">
        <iais:pagination param="feAdminSearchParam" result="feAdmin"/>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>ID No.</th>
                                <th>ID Type</th>
                                <th>Salutation</th>
                                <th>Name</th>
                                <th>Designation</th>
                                <th>Is Administrator</th>
                                <th>Is Active</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty feAdmin.rows}">
                                    <tr>
                                        <td colspan="12">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item"  items="${feAdmin.rows}" varStatus="status">
                                        <tr style="display: table-row;">
                                            <%--<td>--%>
                                                <%--<p><c:out  value="${(status.index + 1) + (feAdminSearchParam.pageNo - 1) * feAdminSearchParam.pageSize}"/></p>--%>
                                            <%--</td>--%>
                                            <td>
                                                <p><c:out value="${item.idNo}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.idType}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.salutation}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.name}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.designation}"/></p>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.role eq 'ORG_ADMIN'}">
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
                                                <p><a onclick="edit('${item.id}')">Edit</a></p>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-11 col-md-11">
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
    </form>
</div>
<script>
    function create() {
        SOP.Crud.cfxSubmit("mainForm", "create");
    }

    function edit(id) {
        console.log(id);
        SOP.Crud.cfxSubmit("mainForm", "edit" , id);
    }
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