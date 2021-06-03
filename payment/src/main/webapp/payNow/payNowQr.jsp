<%--
  Created by IntelliJ IDEA.
  User: hyr
  Date: 2021/5/19
  Time: 15:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Merchant Page</title>
    <%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
    <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
    <webui:setLayout name="none"/>
</head>
<body>
<div class="row">
<img id="payNowImg" alt="Scan me!" src="data:image/png;base64,${imageStreamInBase64Format}" style="text-align: center;">
</div>
<div class="application-tab-footer">
    <div class="row" style="padding-top: 10px;">
        <a class="btn btn-primary" align="center"
           href="${payNowCallBackUrl}">Submit</a>
        <span style="float:right">&nbsp;</span>
        <a class="btn btn-secondary" align="center"
           href=${payNowCallBackUrl}>Cancel</a>
        <button class="btn btn-secondary" align="center"  onclick="payNowImgStringRefresh()">Refresh</button>
    </div>
</div>
<script>
    setInterval(function(){ payNowImgStringRefresh(); }, 60000);
</script>
</body>
</html>
<script>
    var xmlHttpRequest;

    function createXmlHttpRequest(){
        if(window.ActiveXObject){
            return new ActiveXObject("Microsoft.XMLHTTP");
        }else if(window.XMLHttpRequest){
            return new XMLHttpRequest();
        }
    }
    function payNowImgStringRefresh(){
        var url = "${pageContext.request.contextPath}/payNowRefresh";

        xmlHttpRequest = createXmlHttpRequest();

        xmlHttpRequest.onreadystatechange = zswFun;

        xmlHttpRequest.open("POST",url,true);

        xmlHttpRequest.send(null);
    }
    function zswFun(){
        var b = xmlHttpRequest.responseText;
        $('#payNowImg').src("data:image/png;base64,"+b);
    }

</script>
