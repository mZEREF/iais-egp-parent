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
        <div class="container">
            <div class="tab-pane active" id="tabInbox" role="tabpanel">
                <div class="form-horizontal">
                    <div class="tab-content">
                        <h2 class="component-title">Import Users</h2>
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" >No.</th>
                                <th scope="col" >User ID</th>
                                <th scope="col" >Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="oldUser" items="${orgUserUpLoadDtos}" varStatus="status">
                                <tr>
                                    <td>
                                        <p>${status.count}</p>
                                    </td>
                                    <td>
                                        <p>${oldUser.userId}</p>
                                    </td>
                                    <td>
                                        <c:forEach var="msg" items="${oldUser.msg}">
                                            <p style="color: red">${msg}</p><br/>
                                        </c:forEach>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <iais:action>
            <a href="#" style="margin-left: 0%" class="back" onclick="submit('back')"><em class="fa fa-angle-left"></em> Back</a>
        </iais:action>
    </form>
</div>

<style>
    .form-horizontal p {
        line-height: 10px;
    }
</style>

<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script>
    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }
</script>