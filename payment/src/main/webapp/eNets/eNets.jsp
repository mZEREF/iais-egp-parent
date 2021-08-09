<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@page import="com.ecquaria.egp.core.payment.api.config.GatewayConfig" %>
<%@page import="com.ecquaria.egp.core.payment.api.config.GatewayNetsConfig" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
    <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
    <webui:setLayout name="none"/>
    <title>Merchant Page</title>
    <script src="${jquery_js}"
            type="text/javascript"></script>
    <script src="${env_jsp}"></script>
    <script type="text/javascript"
            src="${apps_js}"></script>
</head>
<body>
<input type="hidden" id="failUrl" name="failUrl" value='${failUrl}'>
<form id="eNETSRedirectForm"
      name="eNETSRedirectForm" action='${listenerUrl}' method='POST'>
    <input type="hidden" id="payload" name="payload" value='${txnReq}'>
    <input type="hidden" id="apiKey" name="apiKey" value='${GatewayConfig.eNetsKeyId}'>
    <input type="hidden" id="hmac" name="hmac" value='${newHMAC}'>
</form>
<div id="anotherSection">
    <fieldset>
        <legend></legend>
        <div id="ajaxResponse"></div>
    </fieldset>
</div>
<%--<input type="button" value="checkout" onclick="payLoad()">--%>
<script>
     window.onload=function () {
         var txnReq = $('#payload').val();
         var keyId = "${GatewayConfig.eNetsKeyId}";
         var hmac = $('#hmac').val();
        var timeout = "${GatewayNetsConfig.timeout}";
         sendPayLoad(txnReq,hmac ,keyId );
        window.setTimeout(goPyBack, timeout);
     }
    <%--window.onload = function () {--%>
    <%--    var timeout = "${GatewayNetsConfig.timeout}";--%>
    <%--    $('#eNETSRedirectForm').submit();--%>
    <%--    window.setTimeout(goPyBack, timeout);--%>
    <%--};--%>
</script>
</body>
</html>
<script>
    function payLoad() {
        var txnReq = $('#txnReq').val();
        var keyId = "${GatewayConfig.eNetsKeyId}";
        var hmac = $('#hmac').val();
        sendPayLoad(txnReq,hmac,keyId  );
    }
    function goPyBack(){
        location.href=$('#failUrl').val();
    }
</script>