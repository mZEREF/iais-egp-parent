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
                        <h1>HCSA Internet User Account</h1>
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
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Admin User</h2>
                        </div>
                        <iais:pagination  param="feUserSearchParam" result="feuser"/>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Salutation</th>
                                <th>ID Type</th>
                                <th>ID No</th>
                                <th>Designation</th>
                                <th>Mobile No</th>
                                <th>Office/Telephone No</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Edit</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty feuser.rows}">
                                    <tr>
                                        <td colspan="12">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${feuser.rows}" varStatus="status">
                                        <tr >
                                            <td>
                                                <p><c:out value="${item.name}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.salutation}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.idType}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.idNo}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.designation}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.mobileNo}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.officeNo}"/></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.emailAddr}"/></p>
                                            </td>
                                            <td>
                                                <p>internet user</p>
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

                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script>
    function edit(id) {
        console.log(id);
        SOP.Crud.cfxSubmit("mainForm", "edit" , id);
    }
</script>