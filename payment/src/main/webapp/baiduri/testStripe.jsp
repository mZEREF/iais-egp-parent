
<!-- saved from url=(0109)https://migs.mastercard.com.au/vpcpay?o=pt&DOID=36549BC89B9FF524607FAE2A0ED5485E&paymentId=553304598644307772 -->
<%@page import="com.ecquaria.egp.core.payment.api.config.GatewayConfig" %>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.util.Enumeration"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<!DOCTYPE html>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
    <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
    <webui:setLayout name="none"/>

<title><egov-smc:commonLabel>MasterCard Payment Gateway</egov-smc:commonLabel></title>
<link rel="stylesheet" type="text/css" media="screen" href="./source/3pp.css">
<link rel="stylesheet" type="text/css" media="screen" href="./source/3ppcust.css">
    <%
        response.setContentType("text/html;charset=UTF-8");
    %>
<%!
	static final String SECURE_SECRET = "6A92740F77EFF1C21DFF9281EC53C519";

	static final char[] HEX_TABLE = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public String hashAllFields(Map fields) {
        
        // create a list and sort it
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        
        // create a buffer for the md5 input and add the secure secret first
        StringBuffer buf = new StringBuffer();
        buf.append(SECURE_SECRET);
        
        // iterate through the list and add the remaining field values
        Iterator itr = fieldNames.iterator();
        
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                buf.append(fieldValue);
            }
        }
             
        return encrypt(buf.toString(), "SHA-256");
    
    } 
	
	public static String encrypt(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "SHA-256";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }
	
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	private static String hex(byte[] input) {
		// create a StringBuffer 2x the size of the hash array
		StringBuilder sb = new StringBuilder(input.length * 2);

		// retrieve the byte array data, convert it to hex
		// and add it to the StringBuilder
		for (int i = 0; i < input.length; i++) {
			sb.append(HEX_TABLE[(input[i] >> 4) & 0xf]);
			sb.append(HEX_TABLE[input[i] & 0xf]);
		}
		return sb.toString();
	}
	
	void appendQueryFields(StringBuffer buf, Map fields) {
        
        // create a list
        List fieldNames = new ArrayList(fields.keySet());
        Iterator itr = fieldNames.iterator();
        
        // move through the list and create a series of URL key/value pairs
        while (itr.hasNext()) {
            String fieldName = (String)itr.next();
            String fieldValue = (String)fields.get(fieldName);
            
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // append the URL parameters
                buf.append(URLEncoder.encode(fieldName));
                buf.append('=');
                buf.append(URLEncoder.encode(fieldValue));
            }

            // add a '&' to the end if we have more fields coming.
            if (itr.hasNext()) {
                buf.append('&');
            }
        }
    
    }
	
	static String null2unknown(String in) {
        if (in == null || in.length() == 0) {
            return "No Value Returned";
        } else {
            return in;
        }
    }
%>

<%
	
	String merchant = request.getParameter("vpc_Merchant");
	String orderInfo = request.getParameter("vpc_OrderInfo");
	String amount = request.getParameter("vpc_Amount");
	String currency = request.getParameter("vpc_Currency");
	String returnUrl = request.getParameter("vpc_ReturnURL");
	String secureHashType = request.getParameter("vpc_SecureHashType");
	String accessCode = request.getParameter("vpc_AccessCode");
	String locale = request.getParameter("vpc_Locale");
	String command = request.getParameter("vpc_Command");

    merchant = StringHelper.escapeHtmlChars(merchant);
    orderInfo = StringHelper.escapeHtmlChars(orderInfo);
    amount = StringHelper.escapeHtmlChars(amount);
    currency = StringHelper.escapeHtmlChars(currency);
    returnUrl = StringHelper.escapeHtmlChars(returnUrl);
    locale = StringHelper.escapeHtmlChars(locale);

	Map fields = new HashMap();
    for (Enumeration enum3 = request.getParameterNames(); enum3.hasMoreElements();) {
        String fieldName = (String) enum3.nextElement();
        String fieldValue = request.getParameter(fieldName);
        if ((fieldValue != null) && (fieldValue.length() > 0)) {
            fields.put(fieldName, fieldValue);
        }
    }
    
    String vpc_Txn_Secure_Hash = null2unknown((String) fields.remove("vpc_SecureHash"));
    String vpc_Txn_Secure_Type = null2unknown((String) fields.remove("vpc_SecureHashType"));
    String hashValidated = null;
    
    boolean errorExists = false;
    
    if (SECURE_SECRET != null && SECURE_SECRET.length() > 0 && 
         (fields.get("vpc_TxnResponseCode") != null || fields.get("vpc_TxnResponseCode") != "No Value Returned")) {
         
         // create secure hash and append it to the hash map if it was created
         // remember if SECURE_SECRET = "" it wil not be created
         String secureHash = hashAllFields(fields);
     
         // Validate the Secure Hash (remember MD5 hashes are not case sensitive)
         //System.out.println("vpc_Txn_Secure_Hash: "+vpc_Txn_Secure_Hash);
         //System.out.println("secureHash: "+secureHash);
         if (vpc_Txn_Secure_Hash.equalsIgnoreCase(secureHash)) {
             // Secure Hash validation succeeded, add a data field to be 
             // displayed later.
             hashValidated = "<font color='#00AA00'><strong>CORRECT</strong></font>";
         } else {
             // Secure Hash validation failed, add a data field to be
             // displayed later.
             errorExists = true;
             hashValidated = "<font color='#FF0066'><strong>INVALID HASH</strong></font>";
         }
     } else {
         // Secure Hash was not validated, 
         //errorExists = true;
         hashValidated = "<font color='orange'><strong>Not Calculated - No 'SECURE_SECRET' present.</strong></font>";
     }
    
    if(StringHelper.isEmpty(returnUrl)){
    	errorExists = true;
    	hashValidated = "<font color='#FF0066'><strong>INVALID Return URL</strong></font>";
    }
    
    
    //System.out.println("hashValidated: "+hashValidated);
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

<table width="100%" cellpadding="0" cellspacing="0" border="0">
  <tbody><tr class="menu_header">
    <td>
      <table cellpadding="0" cellspacing="0" border="0" class="menu_header_table">
        <tbody><tr>
          <td align="left" valign="top" class="menu_header_left_left"><img alt="" src="./source/3PP_menu_left_left.gif" vspace="5" class="img_menu_l_l"></td>
<%--          <td align="left" valign="top" class="menu_header_left"><img alt="" src="./source/3PP_menu_left.gif" alt="" vspace="5" class="img_menu_l"></td>--%>
          <td align="left" valign="top" class="menu_header_centre"><img  src="./source/3PP_menu_centre.gif" alt="" vspace="5" class="img_menu_c"></td>
          <td align="right">
            <table align="right" cellpadding="0" cellspacing="0" border="0">
              <tbody><tr>
                      <td align="right" valign="top" class="menu_header_right"><img  src="./source/3PP_menu_right.gif" alt="" vspace="5" class="img_menu_r"></td>
                      <td align="left" valign="top" class="menu_header_right_right"><img alt="" src="./source/3PP_menu_right_right.gif" vspace="5" class="img_menu_r_r"></td>
              </tr>
            </tbody></table>
            </td>
        </tr>
      </tbody></table>
    </td>
  </tr>
  <tr><td><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr>
</tbody></table>

<table class="bodyHeadSpace" align="center" cellpadding="0" cellspacing="0">
    <tbody><tr><td align="right"><strong></strong><img alt="" src="./source/clearpixel.gif" class="bodyHeadSpace"></td></tr>
  <tr><td><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr>
</tbody></table>

<table class="merchTable" align="center" cellspacing="0" cellpadding="0">
<tbody><tr><td><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr><tr><td>
<table class="merchTable" align="center" cellspacing="0" cellpadding="0">
  <tbody><tr>
    <td class="merchCol1"><img alt="" src="./source/clearpixel.gif" class="merchSpace1"></td>
    <td align="right" valign="middle" class="merchCol2" nowrap="nowrap"><span class="merchSpace2"><egov-smc:commonLabel>Merchant name</egov-smc:commonLabel>:&nbsp;</span></td>
    <td class="merchCol3"><img alt="" src="./source/clearpixel.gif" class="merchSpace3"></td>
    <td align="left" valign="middle" class="merchCol4" nowrap="nowrap"><span class="merchSpace4"><egov-smc:commonLabel><%=merchant %></egov-smc:commonLabel></span></td>
    <td class="merchCol5"><img alt="" src="./source/clearpixel.gif" class="merchSpace5"></td>
  </tr>
</tbody></table>
</td></tr></tbody></table>
<%if(!errorExists) {%>
<table width="100%" align="center" cellspacing="0" cellpadding="0">
<tbody><tr><td>
<table class="payHeadTable" align="center" cellspacing="0" cellpadding="0">
	<tbody><tr class="payHeadRow">
		<td class="payHeadImage"><img alt="" src="./source/payHead.gif"></td>
		<td class="payHeadLabel"><egov-smc:message key="selectPreferredPaymentMethod">Select your preferred payment method</egov-smc:message></td>
    </tr>
</tbody></table></td></tr><tr><td><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr>
</tbody></table>

<table class="blockHeadSpace" align="center" cellspacing="0" cellpadding="0">
    <tbody><tr><td><img alt="" src="./source/clearpixel.gif" class="blockHeadSpace"></td></tr>
    <tr><td colspan="0"><img alt="" src="./source/clearpixel.gif" height="0" class="payHeadWidth"></td></tr>
</tbody></table>

<table width="100%" align="center" cellspacing="0" cellpadding="0">
<tbody><tr><td><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr>
<tr class="label"><td>
<egov-smc:message key="payByClickCardBelow">Pay securely using SSL+ by clicking on the card logo below</egov-smc:message>:<br><br>
</td></tr><tr><td>
<script type="text/javascript">
	function doPayment(type){
		if(!type)
			alert("You must select one card type to complete your purchase.");
		else{
			$('[name="card"]').val(type);
			$('#paymentform').submit();
		}
		
	}
</script>
<form action="../../../../overlays/com.ecquaria.egp.egov-app-20.04.0/baiduri/doPayment.jsp" id="paymentform">

<input type="hidden" name="vpc_Merchant" value="<%=merchant%>"/>
<input type="hidden" name="vpc_OrderInfo" value="<%=orderInfo%>"/>
<input type="hidden" name="vpc_Amount" value="<%=amount%>"/>
<input type="hidden" name="vpc_Currency" value="<%=currency%>"/>
<input type="hidden" name="vpc_ReturnURL" value="<%=returnUrl%>"/>
<input type="hidden" name="vpc_Locale" value="<%=locale%>"/>
<input type="hidden" name="card"/>

<table align="center" border="0" cellspacing="0" cellpadding="3"
							style="margin-top: 20px">
							<tbody>
								<tr valign="top">
									<td width="86" align="center" valign="middle"><a
										href="javascript:void(0);" onclick="checkoutButton();"><img
                                            src="./source/card_sm_visa.gif" width="86" height="45"
                                            border="0" alt="VISA" name="Visa"></a></td>
									<td></td>
									<td width="86" align="center" valign="middle"><a
										href="javascript:void(0);" onclick="checkoutButton();"><img
                                            src="./source/card_sm_amex.gif" width="86" height="45"
                                            border="0" alt="American Express" name="Amex"></a></td>
									<td></td>
									<td width="86" align="center" valign="middle"><a
										href="javascript:void(0);" onclick="checkoutButton();"><img
                                            src="./source/card_sm_masterc.gif" width="86" height="45"
                                            border="0" alt="MasterCard" name="MasterCard"></a></td>
									<td></td>
								</tr>
							</tbody>
						</table></form>
					</td></tr></tbody></table>



<br>
<table class="cancelTable" align="center" cellspacing="0" cellpadding="0">
<tbody><tr><td>
<table align="center" cellspacing="0" cellpadding="0">
	<tbody><tr>
		<td><a href="javascript:void(0);"><span
                style="font-size: xx-small; "><egov-smc:commonLabel>Cancel</egov-smc:commonLabel></span></a></td>
	</tr>
</tbody></table></td></tr><tr><td><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr>
</tbody></table>
<%}else{ %>
<%=hashValidated %>
<%} %>
<table class="bodyFootSpace" align="center" cellspacing="0" cellpadding="0" border="0">
    <tbody><tr><td><img alt="" src="./source/clearpixel.gif" class="bodyFootSpace"></td></tr>
  <tr><td><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr>
    <tr class="bodyFootSeparator"><td><img alt="" src="./source/clearpixel.gif"></td></tr>
</tbody></table>
<table class="Copyright" align="center" cellspacing="0" cellpadding="0" border="0">
  <tbody><tr>
    <td align="center" valign="middle" class="legal"><egov-smc:commonLabel>Copyright</egov-smc:commonLabel> &copy;<egov-smc:commonLabel>2007</egov-smc:commonLabel> <egov-smc:commonLabel>TNS Payment Technologies Pty Ltd. All Rights Reserved.</egov-smc:commonLabel></td>
  </tr>
  <tr><td><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr>
</tbody></table>
<table class="pbHeadSpace" align="center" cellspacing="0" cellpadding="0" border="0">
    <tbody><tr><td><img alt="" src="./source/clearpixel.gif" class="pbHeadSpace"></td></tr>
  <tr><td><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr>
</tbody></table>
<table class="powered_by" align="center" cellspacing="0" cellpadding="0" border="0">
    <tbody><tr><td><table class="powered_by_inner" align="center" cellspacing="0" cellpadding="0">
      <tbody><tr>
        <td align="left" width="8"><img alt="" src="./source/powered_by_01.gif" width="8" height="16" border="0"></td>
        <td align="left" height="0">
          <table width="100%" align="center" cellspacing="0" cellpadding="0" background="./source/powered_by_02.gif">
            <tbody><tr><td><img alt="" src="./source/clearpixel.gif" width="8" height="16" border="0"></td></tr></tbody></table>
        </td>
        <td align="right" width="235"><img alt="" src="./source/powered_by_03.gif" width="235" height="16" border="0"></td>
      </tr>
    </tbody></table></td></tr>
    <tr><td colspan="0"><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr>
</tbody></table>
<table class="pbFootSpace" align="center" cellspacing="0" cellpadding="0" border="0">
    <tbody><tr><td><img alt="" src="./source/clearpixel.gif" class="pbFootSpace"></td></tr>
    <tr><td><img alt="" src="./source/clearpixel.gif" class="payHeadWidth"></td></tr>
</tbody></table>

</center>


</body></html>
<c:set var="coutSessionId" value="${CHECKOUT_SESSION_ID}"></c:set>
<script >
    var stripe = Stripe("${GatewayConfig.stripePKey}");
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