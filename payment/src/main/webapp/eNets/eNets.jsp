<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
    <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
    <webui:setLayout name="none"/>
    <title>Merchant Page</title>
    <script src="https://uat2.enets.sg/GW2/js/jquery-3.1.1.min.js"
            type="text/javascript"></script>
    <script src="https://uat2.enets.sg/GW2/pluginpages/env.jsp"></script>
    <script type="text/javascript"
            src="https://uat2.enets.sg/GW2/js/apps.js"></script>
</head>
<body>
<input type="hidden" id="txnReq" name="txnReq" value='${txnReq}'>
<input type="hidden" id="keyId" name="keyId" value='${API_KEY}'>
<input type="hidden" id="hmac" name="hmac" value='${newHMAC}'>
<input type="hidden" id="failUrl" name="failUrl" value='${failUrl}'>

<div id="anotherSection">
    <fieldset>
        <legend></legend>
        <div id="ajaxResponse"></div>
    </fieldset>
</div>
<%--<input type="button" value="checkout" onclick="payLoad()">--%>
<script>
    window.onload=function () {
        var txnReq = $('#txnReq').val();
        var keyId = $('#keyId').val();
        var hmac = $('#hmac').val();
        sendPayLoad(txnReq,hmac ,keyId );
        window.setTimeout(goPyBack, 60000);
    }
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