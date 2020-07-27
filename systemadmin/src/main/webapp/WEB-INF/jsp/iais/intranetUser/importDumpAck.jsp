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
        <div class="container">
            <div class="tab-pane active" id="tabInbox" role="tabpanel">
                <div class="form-horizontal">
                    <div class="tab-content">
                        <h2 class="component-title">Import Users</h2>
                        <table style="width: 100%">
                            <thead>
                            <tr>
                                <th style="width: 30%"></th>
                                <th style="width: 30%"></th>
                                <th style="width: 40%"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="oldUser" items="${existUsersOld}" varStatus="status">
                                <tr>
                                    <td>
                                        <p>User ID</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.userId}</p>
                                    </td>

                                    <c:if test="${oldUser.userId ne existUsersNew[status.index].userId}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].userId}</p>
                                        </td>
                                    </c:if>

                                </tr>
                                <tr>
                                    <td>
                                        <p>Display Name</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.displayName}</p>
                                    </td>
                                    <c:if test="${oldUser.displayName ne existUsersNew[status.index].displayName}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].displayName}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Account Activation Start</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.accountActivateDatetime}</p>
                                    </td>
                                    <c:if test="${oldUser.accountActivateDatetime ne existUsersNew[status.index].accountActivateDatetime}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].accountActivateDatetime}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Account Activation End</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.accountDeactivateDatetime}</p>
                                    </td>
                                    <c:if test="${oldUser.accountDeactivateDatetime ne existUsersNew[status.index].accountDeactivateDatetime}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].accountDeactivateDatetime}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Salutation</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.salutation}</p>
                                    </td>
                                    <c:if test="${oldUser.accountDeactivateDatetime ne existUsersNew[status.index].accountDeactivateDatetime}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].salutation}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>First Name</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.firstName}</p>
                                    </td>
                                    <c:if test="${oldUser.userId ne existUsersNew[status.index].firstName}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].firstName}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Last Name</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.lastName}</p>
                                    </td>
                                    <c:if test="${oldUser.userId ne existUsersNew[status.index].lastName}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].lastName}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Organization</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.organization}</p>
                                    </td>
                                    <c:if test="${oldUser.userId ne existUsersNew[status.index].organization}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].organization}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Division</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.division}</p>
                                    </td>
                                    <c:if test="${oldUser.userId ne existUsersNew[status.index].division}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].division}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Branch / Unit</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.branchUnit}</p>
                                    </td>
                                    <c:if test="${oldUser.userId ne existUsersNew[status.index].branchUnit}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].branchUnit}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Email</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.email}</p>
                                    </td>
                                    <c:if test="${oldUser.userId ne existUsersNew[status.index].email}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].email}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Mobile No</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.mobileNo}</p>
                                    </td>
                                    <c:if test="${oldUser.userId ne existUsersNew[status.index].mobileNo}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].mobileNo}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Office No</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.officeTelNo}</p>
                                    </td>
                                    <c:if test="${oldUser.userId ne existUsersNew[status.index].officeTelNo}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].officeTelNo}</p>
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Remarks</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.remarks}</p>
                                    </td>
                                    <c:if test="${oldUser.userId ne existUsersNew[status.index].remarks}">
                                        <td class="compareTdStyle">
                                            <p>${existUsersNew[status.index].remarks}</p>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <iais:action>
            <a style="margin-left: 0%" class="back" onclick="submit('back')"><em class="fa fa-angle-left"></em> Back</a>
        </iais:action>
    </form>
</div>

<style>
    .form-horizontal p {
        line-height: 10px;
    }

    .compareTdStyle {
        padding: 5px 18px 5px 43px;
        line-height: 1;
        color: #53ab27;
        text-align: left;
        vertical-align: middle;
        border-radius: 50px;
        font-size: 100%;
        background-image: url(../intranetUser/signs.png);
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