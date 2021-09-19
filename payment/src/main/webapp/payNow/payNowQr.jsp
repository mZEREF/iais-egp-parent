<%@page import="com.ecquaria.egp.core.payment.api.config.GatewayPayNowConfig" %>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
    <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
    <webui:setLayout name="none"/>
    <title>Merchant Page</title>
    <script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-3.5.1.min.js"></script>

</head>
<body>
<div class="main-content">
    <div id="payNowImgWm" onclick="payNowImgStringRefresh()" class="row" style=" padding: 8px;
        display: flex;
        justify-content: center;
        align-items:flex-end;">
        <img id="payNowImg" alt="Scan me!" src="data:image/png;base64,${imageStreamInBase64Format}" />
    </div>
    <div class="application-tab-footer">
        <div class="row" style=" padding: 8px;
        display: flex;
        justify-content: center;
        align-items:flex-end;">
            <a class="btn btn-secondary" align="center"
               href="javascript:void(0)" onclick="submit()">Cancel</a>
            <c:if test="${GatewayPayNowConfig.mockserverSwitch.equals('on')}">
                <span style="float:right">&nbsp;</span>
                <a class="btn btn-secondary" align="center" onclick="payNowMockServer()"
                   href="javascript:void(0)" >MockServer</a>
            </c:if>

        </div>
    </div>
    <form id="payNowRedirectForm" style="display: none"
          name="payNowRedirectForm" action='${payNowCallBackUrl}' method='POST'>
    </form>
</div>
<script  type="text/javascript">
    setInterval(function(){ payNowImgStringRefresh(); }, "${GatewayPayNowConfig.timeout}");
    setInterval(function(){ payNowPoll(); }, "${GatewayPayNowConfig.checkoutTime}");
    <c:if test="${GatewayPayNowConfig.mockserverSwitch.equals('on')}">

    function payNowMockServer(){
        $.ajax({
            type: "get",
            url:  "${pageContext.request.contextPath}/payNowMockServer",
            success: function (data) {

            },
            error: function (msg) {

            }
        });
    }
    </c:if>



    function payNowPoll(){
        $.ajax({
            type: "get",
            url:  "${pageContext.request.contextPath}/payNowPoll",
            success: function (data) {
                let result = data.result;
                console.log(result);
                if('Success' === result){
                    submit();
                }
            }
        });
    }
    function submit(){
        //showWaiting();
        $('#payNowRedirectForm').submit();
        //dismissWaiting();
    }

    function payNowImgStringRefresh(){
        if(${GatewayPayNowConfig.timeout=="0"}){
            return;
        }
        $.ajax({
            type: "get",
            url:  "${pageContext.request.contextPath}/payNowRefresh",
            success: function (data) {
                let result = data.result;
                if('Success' !== result){
                    $('#payNowImgWm').html('<img id="payNowImg" src="data:image/png;base64,' + data.QrString + '" />');
                }
                //console.log(data);
                //$("#payNowImg").attr("src",data);
            },
            error: function (msg) {
                //console.log(msg);
            }
        });
    }

</script>
</body>
</html>