<%@ page import="com.ecquaria.cloud.RedirectUtil" %><%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2020/10/27
  Time: 14:53
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../common/dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container">
            <div class="row center">
                <c:choose>
                    <c:when test="${empty AckMessage}">
                        <c:choose>
                            <c:when test="${'APTY005' ==AppSubmissionDto.appType || 'APTY002' ==AppSubmissionDto.appType}">
                                <%@include file="../common/newAppAck.jsp"%>
                            </c:when>
                            <c:when test="${'APTY004' ==AppSubmissionDto.appType}">
                                <%@include file="../common/rennewAck.jsp"%>
                            </c:when>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <h3>${AckMessage}</h3>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="row margin-bottom-10 text-right">
                <div class="col-xs-12 col-md-1">
                    <c:if test="${empty AckMessage}">
                        <p class="print"><a href="javascript:void(0);" id="ackPrint"> <em class="fa fa-print"></em>Print</a></p>
                    </c:if>
                </div>
                <c:if test="${empty AckMessage}">
                    <div class="col-xs-11 col-md-11">
                        <a class="btn btn-primary  col-md-2 pull-right" id="toDashBoard" href="/main-web/eservice/INTERNET/MohInternetInbox">Go to <br>Dashboard</a>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">
    $(document).ready(function () {
        $('.tab-gp').removeClass('tab-gp');
        $('.dashboard').css('padding-bottom','0px');

        $("#ackPrint").click(function () {
            var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohFeAckPrintView/1/",request)%>';
            var suffix = "action=retrigger";
            if(url.indexOf('MohFeAckPrintView/1/?') != -1){
                url = url + '&' + suffix;
            }else{
                url = url + '?' + suffix;
            }
            window.open(url,'_blank');
        })
    });

</script>