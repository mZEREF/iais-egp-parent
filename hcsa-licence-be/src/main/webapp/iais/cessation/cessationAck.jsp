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
    <div class="main-content">
        <div class="panel-heading"><h2><strong>Acknowledgement</strong></h2></div>
        <div class="row" style="margin-left: 1%;margin-right: 1%">
            <div class="col-xs-12">
                <div class="table-gp">
                    <c:forEach items="${appCessConDtos}" var="confirm" varStatus="num">
                        <p>Your cessation application has been successfully submitted, the following licences will be
                            ceased on <U><strong><fmt:formatDate value="${confirm.effectiveDate}" pattern="dd/MM/yyyy"/></strong></U></p>

                        <p>and the application number is<U><strong><c:out value="${confirm.appNo}"/></strong></U></p>
                        <table class="table" border="1" cellspacing="0" cellpadding="0">
                            <thead>
                            <tr style="padding: 1%">
                                <th style="text-align:center;padding: 1%">Licence No.</th>
                                <th style="text-align:center;padding: 1%">Service Name</th>
                                <th style="text-align:center;padding: 1%">HCI Name</th>
                                <th style="text-align:center;padding: 1%">HCI Address</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td class="col-xs-1" align="center">
                                    <p><c:out value="${confirm.licenceNo}"></c:out></p>
                                </td>
                                <td class="col-xs-1" align="center">
                                    <p><c:out value="${confirm.svcName}"></c:out></p>
                                </td>
                                <td class="col-xs-1" align="center">
                                    <p><c:out value="${confirm.hciName}"></c:out></p>
                                </td>
                                <td class="col-xs-1" align="center">
                                    <p><c:out value="${confirm.hciAddress}"></c:out></p>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </c:forEach>
                </div>
                <div align="right">
                    <button id="ackBack" type="button" class="btn btn-primary">Back</button>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    $('#ackBack').click(function () {
        location.href="https://egp.sit.inter.iais.com/main-web/eservice/INTERNET/MohInternetInbox";
    });
</script>