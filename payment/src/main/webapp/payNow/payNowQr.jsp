<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<!DOCTYPE html>
<html>
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
            <a class="btn btn-primary" align="center"
               href="${payNowCallBackUrl}">Submit</a>
            <span style="float:right">&nbsp;</span>
            <a class="btn btn-secondary" align="center"
               href=${payNowCallBackUrl}>Cancel</a>
<%--            <span style="float:right">&nbsp;</span>--%>
<%--            <a class="btn btn-secondary" align="center" href="#" onclick="payNowImgStringRefresh()">Refresh</a>--%>
        </div>
    </div>
</div>
<script  type="text/javascript">
    setInterval(function(){ payNowImgStringRefresh(); }, 60000);

    function payNowImgStringRefresh(){
        $.ajax({
            type: "get",
            url:  "${pageContext.request.contextPath}/payNowRefresh",
            success: function (data) {
                console.log(data);
                $('#payNowImgWm').html('<img id="payNowImg" src="data:image/png;base64,' + data + '" />');
                //$("#payNowImg").attr("src",data);
            },
            error: function (msg) {
                console.log(msg);
            }
        });
    }

</script>
</body>
</html>