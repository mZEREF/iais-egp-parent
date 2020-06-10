<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>
<form class="form-horizontal" method="post" id="InstructionsForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="switch_value" value=""/>
    <div class="main-content">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="center-content">
                            <div class="licence-renewal-content">
                                <ul class="progress-tracker">
                                    <li class="tracker-item active">Instructions</li>
                                    <li class="tracker-item disabled">Licence Review</li>
                                    <li class="tracker-item disabled">Payment</li>
                                    <li class="tracker-item disabled">Acknowledgement</li>
                                </ul>
                                <c:if test="${isSingle == 'Y'}">
                                    <p>You are renewing the following licence:</p>
                                </c:if>
                                <c:if test="${isSingle == 'N'}">
                                    <p>Your licences to renew are listed below:</p>
                                </c:if>
                                <div class="table-gp">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>Licence No.</th>
                                            <th>Type</th>
                                            <c:if test="${isSingle == 'Y'}">
                                                <th>Licensee</th>
                                            </c:if>
                                            <th class="premises-info">Premises</th>
                                            <th>Start Date</th>
                                            <th>Expires On</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${renewDto.appSubmissionDtos}"
                                                   var="appSubmissionDtos">
                                            <tr>
                                                <td>${appSubmissionDtos.licenceNo}</td>
                                                <td>${appSubmissionDtos.serviceName}</td>
                                                <c:if test="${isSingle == 'Y'}">
                                                    <td>${licenseeName}</td>
                                                </c:if>
                                                <td><c:forEach items="${appSubmissionDtos.appGrpPremisesDtoList}" var="appGrpPremisesDtoList"><span>${appGrpPremisesDtoList.renewPremises}</span><br/></c:forEach></td>
                                                <td><fmt:formatDate value='${appSubmissionDtos.licStartDate}' pattern='dd/MM/yyyy'/></td>
                                                <td><fmt:formatDate value='${appSubmissionDtos.licExpiryDate}' pattern='dd/MM/yyyy'/></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <c:if test="${isSingle == 'Y'}">
                                <p>Click proceed to view your licence details and if necessary make amendment, before renewal. </p>
                            </c:if>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <a class="back" href="/main-web/eservice/INTERNET/MohInternetInbox?init_to_page=initLic"><em class="fa fa-angle-left"></em> Back</a>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <div class="text-right text-center-mobile"><a id="proceed" class="btn btn-primary">PROCEED</a></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
</form>
<script>
    $('#proceed').click(function () {
        $('[name="switch_value"]').val('doInstructions');
        $('#InstructionsForm').submit();
    });
</script>
