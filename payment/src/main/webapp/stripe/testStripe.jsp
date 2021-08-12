
<!-- saved from url=(0109)https://migs.mastercard.com.au/vpcpay?o=pt&DOID=36549BC89B9FF524607FAE2A0ED5485E&paymentId=553304598644307772 -->
<%@page import="com.ecquaria.egp.core.payment.api.config.GatewayConfig" %>

<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<!DOCTYPE html>
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<meta http-equiv="refresh" content="${GatewayConfig.refreshTime};url=${failUrl}">

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
    <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
    <webui:setLayout name="none"/>

<title><egov-smc:commonLabel>MasterCard Payment Gateway</egov-smc:commonLabel></title>
    <script src="https://js.stripe.com/v3/"></script>

</head>

<body class="3PP_body">

<c:set var="coutSessionId" value="${CHECKOUT_SESSION_ID}"></c:set>

<script >
    var stripe = Stripe("${GatewayConfig.stripePKey}");
    var cSessionId="${coutSessionId}";

    stripe.redirectToCheckout({
        // Make the id field from the Checkout Session creation API response
        // available to this file, so you can provide it as argument here
        // instead of the {{CHECKOUT_SESSION_ID}} placeholder.
        sessionId: cSessionId
    }).then(function (result) {
        console.error(result.error.message);
        // If `redirectToCheckout` fails due to a browser or network
        // error, display the localized error message to your customer
        // using `result.error.message`.
    });

</script>

</body></html>