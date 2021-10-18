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
<form class="form-horizontal table-responsive" method="post" id="InstructionsForm" action=<%=process.runtime.continueURL()%>>
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
                                <div class="table-responsive">
                                    <table aria-describedby="" class="table">
                                        <thead>
                                        <tr>
                                            <th scope="col" >Licence No.</th>
                                            <th scope="col" >Type</th>
                                            <c:if test="${isSingle == 'Y'}">
                                                <th scope="col" >Licensee</th>
                                            </c:if>
                                            <th scope="col" class="premises-info">Mode of Service Delivery</th>
                                            <th scope="col" >Start Date</th>
                                            <th scope="col" style="white-space: nowrap;">Expires On</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${renewDto.appSubmissionDtos}"
                                                   var="appSubmissionDtos">
                                            <tr>
                                                <td class="word-wrap">${appSubmissionDtos.licenceNo}</td>
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
                                <p>Click proceed to view your licence information and if necessary make amendment, before renewal.</p>
                            </c:if>
                            <c:if test="${isSingle == 'N'}">
                                <p>Please submit renewal application for each individual licence if you wish to amend the information.</p>
                            </c:if>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <a class="back" href="/main-web/eservice/INTERNET/MohInternetInbox?init_to_page=${backUrl}"><em class="fa fa-angle-left"></em> Back</a>
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
<iais:confirm msg="RFC_ACK001"  needCancel="false" callBack="tagConfirmCallbacksupportReport()" popupOrder="supportReport" ></iais:confirm>
<script>
    $('#proceed').click(function () {
        showWaiting();
        $('[name="switch_value"]').val('doInstructions');
        $('#InstructionsForm').submit();
    });
    function tagConfirmCallbacksupportReport(){
        $('#supportReport').modal('hide');
    }
    $(document).ready(function () {
        if('${isSingle}' == 'N'){
            $('#supportReport').modal('show');
        }
    });

</script>
