<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-internet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<form id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <%@ include file="../cessation/ackHead.jsp" %>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 col-xs-12 cesform-box">
                    <p><strong>Your cessation application has been successfully submitted,below are the summary
                        information</strong></p>
                    <table class="table">
                        <thead>
                        <tr>
                            <th style="text-align:center">Application No.</th>
                            <th style="text-align:center">Licence No.</th>
                            <th style="text-align:center">Service Name</th>
                            <th style="text-align:center">HCI Name</th>
                            <th style="text-align:center">HCI Address</th>
                            <th style="text-align:center">Cessation Date</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${appCessConDtos}" var="confirm" varStatus="num">
                        <tr style="text-align: center">
                            <td>
                                <p><c:out value="${confirm.appNo}"></c:out></p>
                            </td>
                            <td>
                                <p><c:out value="${confirm.licenceNo}"></c:out></p>
                            </td>
                            <td>
                                <p><c:out value="${confirm.svcName}"></c:out></p>
                            </td>
                            <td>
                                <p><c:out value="${confirm.hciName}"></c:out></p>
                            </td>
                            <td>
                                <p><c:out value="${confirm.hciAddress}"></c:out></p>
                            </td>
                            <td>
                                <p><fmt:formatDate value="${confirm.effectiveDate}" pattern="dd/MM/yyyy"/></p>
                            </td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-sm-6">
                        <a onclick="back('back')"><em class="fa fa-angle-left"></em> Back</a>
                    </div>
                    <br/>
                </div>
                <br/>
            </div>
        </div>
    </div>
</form>

<style>
    .col-md-5 {
        width: 26%;
    }

    .col-md-4 {
        width: 35%;
    }

    .main-content {
        margin-top: 1%;
        width: 90%;
        padding-left: 1%;
        padding-right: 1%;
    }
</style>

<script type="text/javascript">
    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }

    function back(action) {
        submit(action);
    }
</script>