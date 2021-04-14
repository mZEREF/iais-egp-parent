<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>
<style>
    .margin-bottom-10{
        margin-bottom:10px;
    }
    .aMarginleft{
        margin-left: 9px;
    }
</style>
<form method="post" class="table-responsive" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type"  value=""/>
    <div class="main-content">
        <div class="container">
            <div class="row">

                <div class="col-xs-12">
                    <div class="center-content">
                        <div class="licence-renewal-content">
                            <div class="tab-pane"  role="tabpanel">
                                <div class="multiservice">
                            <ul class="progress-tracker  col-xs-12" ${isSingle == 'Y' ? 'style="margin-left:-8%;"' : ''} >
                                <li class="tracker-item active">Instructions</li>
                                <li class="tracker-item active">Licence Review</li>
                                <li class="tracker-item active">Payment</li>
                                <li class="tracker-item active">Acknowledgement</li>
                            </ul>
                                </div>
                            </div>
                            <c:choose>
                                <c:when test="${'error' != AckStatus}">
                                    <%@include file="../common/rennewAck.jsp"%>
                                </c:when>
                                <c:otherwise>
                                    <h3>${AckMessage}</h3>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>


            <div class="row margin-bottom-10 text-right">
                <div class="col-xs-12 col-md-1">
                    <p class="print"><a href="#" id="print-ack"> <em class="fa fa-print"></em>Print</a></p>
                </div>
                <div class="col-xs-11 col-md-11">

                    <c:if test="${requestInformationConfig == null}">
                        <a class="btn btn-primary aMarginleft col-md-2 pull-right"  id="Dashboard" >Go to <br>Dashboard</a>
                        <%--<a class="btn btn-secondary aMarginleft col-md-3 pull-right" href="/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu">Apply for <br>Another Licence</a>--%>
                        <%--<a class="btn btn-secondary aMarginleft col-md-3 pull-right" id="doSelfAssessment">Submit <br>Self-Assessment</a>--%>
                    </c:if>
                        <%--<a class="btn btn-secondary aMarginleft col-md-3 pull-right" id="doPrefInsDate">Indicate preferred<br>Inspection Date</a>--%>
                </div>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">

    $('#doSelfAssessment').click(function () {
        $("[name='crud_action_type']").val('MohAppPremSelfDecl');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });

    // $('#doPrefInsDate').click(function () {
    //     $("[name='crud_action_type']").val('MohSubmitInspectionDate');
    //     var mainForm = document.getElementById("mainForm");
    //     mainForm.submit();
    // });
    $('#Dashboard').click(function (){
        showWaiting();
        location.href="https://${pageContext.request.serverName}/main-web<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohInternetInbox",request)%>";
    });
    $("#print-ack").click(function () {
        window.print();
    })
</script>



