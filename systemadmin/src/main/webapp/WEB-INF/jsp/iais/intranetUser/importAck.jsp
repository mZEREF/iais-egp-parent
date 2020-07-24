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
                        <h2 class="component-title">Uploaded Result</h2>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>No.</th>
                                <th>User ID</th>
                                <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="result" items="${orgUserUpLoadDtos}" varStatus="status">
                                <tr>
                                    <td>
                                        <p>${status.count}</p>
                                    </td>
                                    <td>
                                        <p>${result.userId}</p>
                                    </td>
                                    <td>
                                        <c:forEach var="msg" items="${result.msg}">
                                            <p style="color: red">${msg}</p>
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
            <a style="margin-left: 0%" class="back" onclick="submit()"><em class="fa fa-angle-left"></em> Back</a>
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
    function submit() {
        $("#mainForm").submit();
    }
</script>