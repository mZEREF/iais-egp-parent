<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>

<%

    String sessionId= (String) ParamUtil.getSessionAttr(request,"sessionNetsId");
    String url= AppConsts.REQUEST_TYPE_HTTPS + request.getServerName()+"/payment-web/process/EGPCLOUD/PaymentCallBack";
    Map<String, String> fields = IaisCommonUtils.genNewHashMap();
    fields.put("sessionId",sessionId);
    StringBuilder bud = new StringBuilder();
    bud.append(url).append("?sessionId=").append(sessionId);
    String header = request.getParameter("hmac");
    System.out.println("MerchantApp:b2sTxnEndUrl : hmac: " + header);
    Object message = request.getParameter("message");//contains TxnRes message
    System.out.println("MerchantApp:b2sTxnEndUrl : data, message: " + message);
    String txnRes = String.valueOf(message);
    ParamUtil.setSessionAttr(request,"message",txnRes);
    ParamUtil.setSessionAttr(request,"header",header);
    RedirectUtil.redirect(bud.toString(), request, response);

%>

