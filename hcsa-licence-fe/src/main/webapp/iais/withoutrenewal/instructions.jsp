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
                                <h2>YOUR LICENCES TO RENEW ARE LISTED BELOW</h2>
                                <div class="table-gp">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>Licence No.</th>
                                            <th>Type</th>
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
                                                <td><c:forEach items="${appSubmissionDtos.appGrpPremisesDtoList}" var="appGrpPremisesDtoList"><span>${appGrpPremisesDtoList.renewPremises}</span><br/></c:forEach></td>
                                                <td>${appSubmissionDtos.licStartDate}</td>
                                                <td>${appSubmissionDtos.licExpiryDate}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <p><a class="back" href="/main-web"><em class="fa fa-angle-left"></em> Back</a></p>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <div id="proceed" class="text-right text-center-mobile"><a class="btn btn-primary">PROCEED</a></div>
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
