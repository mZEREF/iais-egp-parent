<%@ page import="ecq.commons.helper.StringHelper" %>
<%@ page import="com.ecquaria.egp.core.payment.api.config.GatewayConfig" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ page import="com.ecquaria.egov.core.common.constants.AppConstants" %>
<!-- saved from url=(0109)https://migs.mastercard.com.au/ssl?sessionid=PAY78E75168616325E467514A1EA62A0C97&paymentId=553304598644307772 -->
<html><head><meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<webui:setLayout name="none"/>
<title><egov-smc:commonLabel>MasterCard Payment Gateway</egov-smc:commonLabel></title>
<%--<link rel="stylesheet" type="text/css" media="screen" href="./source/3pp.css">--%>
<%--<link rel="stylesheet" type="text/css" media="screen" href="./source/3ppcust.css">--%>
<link rel="stylesheet" type="text/css" media="screen" href="./source/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="./source/responsivebank.css">
<link rel="stylesheet" type="text/css" media="screen" href="./source/bank.css">
<link rel="stylesheet" type="text/css" media="screen" href="./source/font-awesome.min.css">
<%
    response.setContentType("text/html;charset=UTF-8");
	String merchant = request.getParameter("vpc_Merchant");
	String orderInfo = request.getParameter("vpc_OrderInfo");
	String amountStr = request.getParameter("vpc_Amount");
	String currency = request.getParameter("vpc_Currency");
	String returnUrl = request.getParameter("vpc_ReturnURL");
	String accessCode = request.getParameter("vpc_AccessCode");
	String locale = request.getParameter("vpc_Locale");
	String card = request.getParameter("card");
	Integer amount = Integer.valueOf(amountStr)/100;

    merchant = StringHelper.escapeHtmlChars(merchant);
    orderInfo = StringHelper.escapeHtmlChars(orderInfo);
    currency = StringHelper.escapeHtmlChars(currency);
    returnUrl = StringHelper.escapeHtmlChars(returnUrl);
    locale = StringHelper.escapeHtmlChars(locale);
    card = StringHelper.escapeHtmlChars(card);
%>
<%@ include file="/WEB-INF/jsp/inc/script.jsp" %>
<script language="JavaScript">
<!--
    function checkCR(evt) {
        var evt  = (evt) ? evt : ((event) ? event : null);
        var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
        if ((evt.keyCode == 13) && ((node.type == "text") || (node.type == "radio"))) {
            return false;
        }
    }
    document.onkeypress = checkCR;
// -->
</script>
    <script src="https://js.stripe.com/v3/"></script>
</head>
<body class="3PP_body">
    <nav>
        <img src="./source/3PP_menu_left.gif" class="img-responsive logobank">
    </nav>
    <div class="col-md-12 merchant">
        <div class="pull-left mer-des"><egov-smc:commonLabel>Merchant name</egov-smc:commonLabel>:&nbsp;</div>
        <div class="pull-right mer-name"><egov-smc:commonLabel>Times Cineplex Sdn Bhd (Times Square)</egov-smc:commonLabel></div>
    </div>

    <script language="JavaScript">
    <!--
        var paid = false;
        function LockPayButton() {
            if (!paid) {
              paid=true;
              return true;
            }
            return false;
        }
    // -->
    </script>
    <div class="col-md-12 carddetail">
        <div style="min-height:46px"><img src="./source/detailHead.gif" class="pull-left"><span><egov-smc:message key="enterCardDetails">Enter your card details</egov-smc:message></span></div>
    </div>
    <div class="col-md-12 blockHeadSpace">
    </div>

<form accept-charset="iso-8859-1" acceptcharset="iso-8859-1" id="paymentDetail" action="./return.jsp" method="POST" autocomplete="OFF">

<input type="hidden" name="vpc_Merchant" value="<%=merchant%>"/>
<input type="hidden" name="vpc_OrderInfo" value="<%=orderInfo%>"/>
<input type="hidden" name="vpc_Amount" value="<%=amount%>"/>
<input type="hidden" name="vpc_Currency" value="<%=currency%>"/>
<input type="hidden" name="vpc_ReturnURL" value="<%=returnUrl%>"/>
<input type="hidden" name="vpc_Locale" value="<%=locale%>"/>
<input type="hidden" name="card"/>

<input type="hidden" name="paymentId" value="553304598644307772">
<table align="center" cellspacing="0" cellpadding="0">
    <tbody><tr><td><table align="center" cellspacing="0" cellpadding="0">
        <tbody><tr>
            <td colspan="3" class="topSeparator"></td>
        </tr>
        <tr>
            <td class="detailLeftCol" align="right" valign="top"><img src="./source/padlock.gif" align="absmiddle"><span class="detailCardName"><egov-smc:commonLabel><%=card %></egov-smc:commonLabel>:</span></td>
            <td class="detailSeparator" align="center"><img src="./source/clearpixel.gif" class="detailSeparatorSpace"></td>
            <td class="detailRightCol"><font face="Arial, Helvetica, sans-serif" size="2" color="black"><egov-smc:messageTemplate key="enterCardDetailsAndPay" paramArray="<%=new String[]{MultiLangUtil.translate(request,AppConstants.KEY_TRANSLATION_MODULE_LABEL,card)} %>" default="You have chosen <b>{0}</b> as your method of payment. Please enter your card details into the form below and click 'pay' to complete your purchase."></egov-smc:messageTemplate></font><br><br></td>
        </tr>
        <tr>
            <td class="detailLeftCol" align="right" valign="top"><egov-smc:commonLabel>Card Number</egov-smc:commonLabel>&nbsp;<span class="hidden-md hidden-lg">:&nbsp;</span><img src="./source/left_for_white.gif" class="img_lfw hidden-xs hidden-sm" align="absmiddle"></td>
            <td class="detailSeparator"></td>
            <td class="detailRightCol cardNo"><input id="CardNumber" type="Text" autocomplete="off" name="cardno" value="" align="CENTER" size="16" maxlength="23"></td>
        </tr>



      <tr>
	<td class="detailLeftCol" align="right"><egov-smc:commonLabel>Expiry&nbsp;Date</egov-smc:commonLabel>&nbsp;<span class="hidden-md hidden-lg">:&nbsp;</span><img src="./source/left_for_white.gif" class="img_lfw hidden-xs hidden-sm" align="absmiddle"></td>
	<td class="detailSeparator"></td>
	<td class="detailRightCol monthYear"><input id="CardMonth" type="Text" name="cardexpirymonth" align="CENTER" size="2" maxlength="2" value="">&nbsp;/&nbsp;<input id="CardYear" type="Text" name="cardexpiryyear" align="CENTER" size="2" maxlength="2" value="">
&nbsp;&nbsp;<egov-smc:commonLabel>month/year</egov-smc:commonLabel></td>
</tr>






        <tr>
	<td class="detailLeftCol" align="right" valign="top"><egov-smc:commonLabel>Security&nbsp;Code</egov-smc:commonLabel>&nbsp;<span class="hidden-md hidden-lg">:&nbsp;</span><img src="./source/left_for_white.gif" class="img_lfw hidden-xs hidden-sm" align="absmiddle"></td>
	<td class="detailSeparator"></td>
	<td class="detailRightCol">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tbody><tr>
				<td align="left" valign="top"  style="width:70px;">
					<input id="Securecode" type="Text" name="cardsecurecode" align="CENTER" size="4" maxlength="4" value="">
				</td>
				<td class="detailRightCol" align="left" valign="middle">
					<egov-smc:message key="ThreeDigitsAfterCardNumber">The 3 digits after the card number on the signature panel of your card.</egov-smc:message>
				</td>
			</tr>
			<tr>
			  <td colspan="2" align="left" valign="top"><img src="./source/card_sm_visa_csc.gif" align="absmiddle" class="img-responsive col-xs-10 col-sm-6 col-md-6"></td>
		  </tr>
		</tbody></table>
	</td>
</tr>






        <!-- Plan Payments row -->




        <tr>
            <td class="detailLeftCol" align="right" valign="top"><egov-smc:commonLabel>Purchase&nbsp;Amount</egov-smc:commonLabel>&nbsp;<span class="hidden-md hidden-lg">:&nbsp;</span><img src="./source/left_for_white.gif" class="img_lfw hidden-xs hidden-sm" align="absmiddle"></td>
            <td class="detailSeparator"></td>
            <td class="detailRightCol amount" id="purchase_amount"> <egov-smc:commonLabel><%=currency %></egov-smc:commonLabel> <%=amount %></td>
        </tr>
        <tr>
            <td align="right" width="180"></td>
            <td class="detailSeparator"></td>
            <td class="detailRightCol" align="right">
              <table width="100%" border="0" cellspacing="5" cellpadding="0">
                <tbody><tr>
                  <td><a href="https://migs.mastercard.com.au/ssl?op=cancel&paymentId=553304598644307772"><font size="1"><egov-smc:commonLabel>Cancel</egov-smc:commonLabel></font></a></td>
                    <%
                        String payPath = "";
                        String language = MultiLangUtil.getSiteLanguage();
                        if(language.equals("zh")){
                            payPath = "./source/pay_zh.gif";
                        }else{
                            payPath = "./source/pay.gif";
                        }
                    %>
                    <td align="right"><input id="Paybutton" alt="Submit card details for processing" type="image" name="payButtonImage" src="<%=payPath%>" class="img_pay"></td>
                </tr>
              </tbody></table>
            </td>
        </tr>
<!-- Authentiction Program Block row -->
        <!-- D:\Viewstore\RobD_MC_PS3.1.x\customer\Mastercard\ps\server\source\Branding\assets\svr\default\ssl-detail-block.jhtml-->
       <!-- D:\Viewstore\RobD_MC_PS3.1.x\customer\Mastercard\ps\server\source\Branding\assets\svr\default\ssl-detail-auth-program-block.jhtml -->

        <tr>
            <td class="detailLeftCol" align="right" valign="top"><img src="./source/authProgram_VBV.gif"></td>
            <td class="detailSeparator"></td>
            <td class="detailRightCol">&nbsp;</td>
        </tr>

        <tr>
            <td colspan="3" class="bottomSeparator"></td>
        </tr>
        <tr>
            <td colspan="3" class="agreementSeparator"></td>
        </tr>
    </tbody></table></td></tr>
    <tr></tr>
</tbody></table>
    <div class="text-center agreement"><p><egov-smc:message key="authoriseDebitToAccount">I hereby authorise the debit to my VISA Account in favour of Times Cineplex Sdn Bhd (Times Square)</egov-smc:message></p></div>
</form>




    <footer>
        <div class="footer text-center">
            <egov-smc:commonLabel>Copyright</egov-smc:commonLabel> &copy;<egov-smc:commonLabel>2007</egov-smc:commonLabel> <egov-smc:commonLabel>TNS Payment Technologies Pty Ltd. All Rights Reserved.</egov-smc:commonLabel>
        </div>
        <div class="carddetail2">
            <tbody>
            <tr>
                <td>
                    <table class="powered_by_inner" align="center" cellspacing="0" cellpadding="0">
                        <tbody>
                        <tr>
                            <td align="left" width="8"><img src="./source/powered_by_01.gif" width="8" height="16" border="0"></td>
                            <td align="left" class="bgexpand"><img src="./source/powered_by_02.gif"></td>
                            <td align="right" width="235"><img src="./source/powered_by_03.gif" width="235" height="16" border="0"></td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            <!--tr>
                <td colspan="0"><img src="img/clearpixel.gif" class="payHeadWidth"></td>
            </tr-->
            </tbody>
        </div>
    </footer>
</body>
<button id="checkout-button" onclick="checkoutButton()">checkout</button>
</html>
<c:set var="coutSessionId" value="${CHECKOUT_SESSION_ID}"></c:set>
<script src="https://js.stripe.com/v3/">
    var stripe = Stripe('sk_test_YGXYtjBWWLt6qhEqW34wu8Vg00iEFDMW4w');
    var cSessionId="${coutSessionId}";
     function checkoutButton() {
        stripe.redirectToCheckout({
            // Make the id field from the Checkout Session creation API response
            // available to this file, so you can provide it as argument here
            // instead of the {{CHECKOUT_SESSION_ID}} placeholder.
            sessionId: cSessionId
        }).then(function (result) {

            // If `redirectToCheckout` fails due to a browser or network
            // error, display the localized error message to your customer
            // using `result.error.message`.
        });
    }

</script>

