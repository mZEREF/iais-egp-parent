<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <br/><br/> <br/><br/>
        <input type="hidden" name="crud_action_type" value="">
        <div class="container" style="margin-left: 23%">
            <div class="tab-pane active" id="tabInbox" role="tabpanel">
                <div class="form-horizontal">
                    <div class="tab-content">
                        <h2 class="component-title">Import Users</h2>
                        <h3>You have existing records which conflict with records you have imported</h3>
                        <c:forEach var="oldUser" items="${existUsersOld}" varStatus="status">
                            <span style="font-size: 2rem;">User${status.count} - ${oldUser.firstName}&nbsp;${oldUser.lastName}</span>
                        <table aria-describedby="" style="width: 50%;border-collapse:separate;border-spacing:10px;border: 1px solid #151515">
                            <thead>
                            <tr>
                                <th scope="col" style="width: 20%"></th>
                                <th scope="col" style="width: 10%"></th>
                                <th scope="col" style="width: 1%"></th>
                            </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <strong><p>User ID</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.userId}</p>
                                    </td>

                                    <c:if test="${oldUser.userId != existUsersNew[status.index].userId}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].userId}</p>
                                        </td>
                                    </c:if>

                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>User Domain</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.userDomain}</p>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Display Name</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.displayName}</p>
                                    </td>
                                    <c:if test="${oldUser.displayName != existUsersNew[status.index].displayName}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].displayName}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Account Activation Start</p></strong>
                                    </td>
                                    <td>
                                        <p><fmt:formatDate value='${oldUser.accountActivateDatetime}' pattern='dd/MM/yyyy'/></p>
                                    </td>
                                    <c:if test="${oldUser.accountActivateDatetime != existUsersNew[status.index].accountActivateDatetime}">
                                        <td class="compareTdStyle">
                                            <p><fmt:formatDate value='${existUsersNew[status.index].accountActivateDatetime}' pattern='dd/MM/yyyy'/></p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Account Activation End</p></strong>
                                    </td>
                                    <td>
                                        <p><fmt:formatDate value='${oldUser.accountDeactivateDatetime}' pattern='dd/MM/yyyy'/></p>
                                    </td>
                                    <c:if test="${oldUser.accountDeactivateDatetime != existUsersNew[status.index].accountDeactivateDatetime}">
                                        <td class="compareTdStyle">
                                            <p><fmt:formatDate value='${existUsersNew[status.index].accountDeactivateDatetime}' pattern='dd/MM/yyyy'/></p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Salutation</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.salutation}</p>
                                    </td>
                                    <c:if test="${oldUser.salutation != existUsersNew[status.index].salutation}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].salutation}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>First Name</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.firstName}</p>
                                    </td>
                                    <c:if test="${oldUser.firstName != existUsersNew[status.index].firstName}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].firstName}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Last Name</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.lastName}</p>
                                    </td>
                                    <c:if test="${oldUser.lastName != existUsersNew[status.index].lastName}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].lastName}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Organization</p></strong>
                                    </td>
                                    <td>
                                        <strong><p>${oldUser.organization}</p></strong>
                                    </td>
                                    <c:if test="${oldUser.organization ne existUsersNew[status.index].organization}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].organization}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Division</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.division}</p>
                                    </td>
                                    <c:if test="${oldUser.division != existUsersNew[status.index].division}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].division}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Branch / Unit</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.branchUnit}</p>
                                    </td>
                                    <c:if test="${oldUser.branchUnit != existUsersNew[status.index].branchUnit}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].branchUnit}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Email</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.email}</p>
                                    </td>
                                    <c:if test="${oldUser.email != existUsersNew[status.index].email}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].email}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Mobile No</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.mobileNo}</p>
                                    </td>
                                    <c:if test="${oldUser.mobileNo != existUsersNew[status.index].mobileNo}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].mobileNo}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Office No</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.officeTelNo}</p>
                                    </td>
                                    <c:if test="${oldUser.officeTelNo != existUsersNew[status.index].officeTelNo}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].officeTelNo}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <strong><p>Remarks</p></strong>
                                    </td>
                                    <td>
                                        <p>${oldUser.remarks}</p>
                                    </td>
                                    <c:if test="${oldUser.remarks != existUsersNew[status.index].remarks}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].remarks}</p>
                                        </td>
                                    </c:if>
                                </tr>
                            </tbody>
                        </table>
                            <br/> <br/> <br/>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
            <div class="row">
                <div style="margin-left: 50%">
                    <span class="components">
                        <a class="btn  btn-secondary"data-toggle="modal" data-target= "#cancel" onclick="submit('back')">Cancel</a>
                    </span>
                    <span class="components">
                        <a class="btn btn-primary" onclick="submit()">Continue</a>
                    </span>
                </div>
            </div>
    </form>
</div>

<style>
    .form-horizontal p {
        line-height: 10px;
    }

    .compareTdStyle {
        display: block;
        padding: 5px 18px 5px 43px;
        line-height: 1;
        color: #53ab27;
        text-align: left;
        vertical-align: middle;
        border-radius: 50px;
        font-size: 100%;
        background-image: url('../../../../img/signs.png');
        background-repeat: no-repeat;
        background-position: 15px 50%;
        background-color: #e4fbdb;
    }

</style>

<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script>
    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }
</script>