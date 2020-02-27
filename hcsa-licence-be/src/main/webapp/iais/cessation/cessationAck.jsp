<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<form id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="panel-heading"><h2><strong>Acknowledgement</strong></h2></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">

                    <c:forEach items="${appCessConDtos}" var="confirm" varStatus="num">
                        <p>Your cessation application has been successfully submitted, the following licences will be
                            ceased on
                            <c:out value="${confirm.effectiveDate}"></c:out></p>
                        <p>and the application number is <c:out value="${confirm.appNo}"></c:out></p>
                        <table class="table" border="1" cellspacing="0" cellpadding="0">
                            <thead>
                            <tr>
                                <th>Licence No.</th>
                                <th>Service Name</th>
                                <th>HCI Name</th>
                                <th>HCI Address</th>
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
            </div>
        </div>
    </div>
</form>