<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%--<%@ include file="./dashboard.jsp" %>--%>
<style>
    .margin-bottom-10 {
        margin-bottom: 10px;
    }

    .margin-top-10 {
        margin-top: 100px;
    }

    .aMarginleft {
        margin-left: 9px;
    }
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container">
            <div class="dashboard-page-title">
                <h1>New Application</h1>
            </div>
            <div class="row center" style="margin-left: 8px">
                <c:choose>
                    <c:when test="${'error' != AckStatus}">
                        <%@include file="common/newAppAck.jsp" %>
                    </c:when>
                    <c:otherwise>
                        <h3>${AckMessage}</h3>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="row margin-bottom-10 text-right">
                <div class="col-xs-11 col-md-11">
                    <a class="btn btn-secondary aMarginleft col-md-2 pull-right" href="javascript:void(0);" id="toDashBoard">Home</a>
                    <a class="btn btn-secondary aMarginleft col-md-2 pull-right" href="javascript:void(0);" id="ackPrint">Print</a>
                </div>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">

    $(document).ready(function () {
        $('#newSvc').before('<br/><br/>');
        $('#dashboard').css('padding-bottom', '0px');

    });

    $('#toDashBoard').click(function () {
        showWaiting();
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });

    $("#ackPrint").click(function () {
        var url = '${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohFeAckPrintView/1/",request)%>';
        var appType = $('input[name="appType"]').val();
        var suffix = "appType=" + appType;
        if (url.indexOf('MohFeAckPrintView/1/?') != -1) {
            url = url + '&' + suffix;
        } else {
            url = url + '?' + suffix;
        }
        window.open(url, '_blank');
    })
</script>



