<%@page import="com.ecquaria.cloud.RedirectUtil" %>
<%@page import="ecq.commons.helper.StringHelper" %>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.security.MessageDigest" %>
<%@page import="java.security.NoSuchAlgorithmException" %>
<%@page import="java.util.*" %>
<%@ include file="/WEB-INF/jsp/inc/script.jsp" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<webui:setLayout name="none"/>
<%!
    static final String SECURE_SECRET = "6A92740F77EFF1C21DFF9281EC53C519";

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

    void appendQueryFields(StringBuffer buf, Map fields) {

        // create a list
        List fieldNames = new ArrayList(fields.keySet());
        Iterator itr = fieldNames.iterator();

        // move through the list and create a series of URL key/value pairs
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);

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

    boolean isValidRedirectUrl(String url) {
        return EngineHelper.isValidateRedirectUrl(url);
    }
%>
<%
    String merchant = request.getParameter("vpc_Merchant");
    String orderInfo = request.getParameter("vpc_OrderInfo");
    String amount = request.getParameter("vpc_Amount");
    String currency = request.getParameter("vpc_Currency");
    String returnUrl = request.getParameter("vpc_ReturnURL");
    String accessCode = request.getParameter("vpc_AccessCode");
    String locale = request.getParameter("vpc_Locale");

    String cardno = request.getParameter("cardno");
    String cardexpirymonth = request.getParameter("cardexpirymonth");
    String cardexpiryyear = request.getParameter("cardexpiryyear");
    String cardsecurecode = request.getParameter("cardsecurecode");

    merchant = StringHelper.escapeHtmlChars(merchant);
    orderInfo = StringHelper.escapeHtmlChars(orderInfo);
    currency = StringHelper.escapeHtmlChars(currency);
    returnUrl = StringHelper.escapeHtmlChars(returnUrl);
    locale = StringHelper.escapeHtmlChars(locale);

    Map fieldValus = new HashMap();
    fieldValus.put("vpc_Merchant", merchant);
    fieldValus.put("vpc_OrderInfo", orderInfo);
    fieldValus.put("vpc_Amount", amount);
    fieldValus.put("vpc_Currency", currency);
    fieldValus.put("vpc_CardNum", cardno);
    fieldValus.put("vpc_AcqResponseCode", "0");
    fieldValus.put("vpc_TxnResponseCode", "0");
    fieldValus.put("vpc_ReceiptNo", "1234567");
    fieldValus.put("vpc_TransactionNo", "97841");
    fieldValus.put("vpc_Card", "MC");
    fieldValus.put("vpc_Command", "pay");
    String hash = hashAllFields(fieldValus);
    fieldValus.put("vpc_SecureHash", hash);
    fieldValus.put("vpc_SecureHashType", "SHA256");

    StringBuffer buf = new StringBuffer();
    buf.append(returnUrl);
    if (!StringHelper.isEmpty(returnUrl) && !returnUrl.contains("?"))
        buf.append('?');
    else {
        buf.append("&");
    }
    appendQueryFields(buf, fieldValus);
    //request.getRequestDispatcher(buf.toString()).forward(request, response);
    String url = buf.toString();
//    if(!isValidRedirectUrl(url)){
//        throw new RuntimeException("The redirect url is invalid.");
//    }
    String url1 = RedirectUtil.appendCsrfGuardToken(buf.toString(), request);
    RedirectUtil.redirect(url1, request, response);

%>
