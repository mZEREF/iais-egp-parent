<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<!DOCTYPE html>
<html>
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
<input type="hidden" id="txnReq" name="txnReq" value='${txnReq}'>
<input type="hidden" id="keyId" name="keyId" value='${API_KEY}'>
<%--<input type="hidden" id="hmac" name="hmac" value='${newHMAC}'>--%>
<input type="hidden" id="failUrl" name="failUrl" value='${failUrl}'>
<input type="hidden" id="timeoutNets" value="${timeout}"/>
<form id="eNETSRedirectForm"
      name="eNETSRedirectForm" action='${listenerUrl}' method='POST'>
    <input type="hidden" id="payload" name="payload" value='${txnReq}'>
    <input type="hidden" id="apiKey" name="apiKey" value='${API_KEY}'>
    <input type="hidden" id="hmac" name="hmac" value='${newHMAC}'>
</form>
<%--<div id="anotherSection">--%>
<%--    <fieldset>--%>
<%--        <legend></legend>--%>
<%--        <div id="ajaxResponse"></div>--%>
<%--    </fieldset>--%>
<%--</div>--%>
<%--<input type="button" value="checkout" onclick="payLoad()">--%>
<script>
    // window.onload=function () {
    //     var txnReq = $('#txnReq').val();
    //     var keyId = $('#keyId').val();
    //     var hmac = $('#hmac').val();
    //     var timeout = $('#timeoutNets').val();
    //     sendPayLoad(txnReq,hmac ,keyId );
    //     window.setTimeout(goPyBack, timeout);
    // }
    window.onload = function () {
        $('#eNETSRedirectForm').submit();
        window.setTimeout(goPyBack, timeout);
    };
</script>
</body>
</html>
<script>
    function payLoad() {
        var txnReq = $('#txnReq').val();
        var keyId = $('#keyId').val();
        var hmac = $('#hmac').val();
        sendPayLoad(txnReq,hmac,keyId  );
    }
    function goPyBack(){
        location.href=$('#failUrl').val();
    }
</script>