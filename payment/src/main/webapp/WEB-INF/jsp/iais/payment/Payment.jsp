<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>

<%

    String sessionId= (String) ParamUtil.getSessionAttr(request,"sessionNetsId");
    String url= AppConsts.REQUEST_TYPE_HTTPS + request.getServerName()+"/egov/process/EGPCLOUD/PaymentCallBack";
    StringBuilder bud = new StringBuilder();
    bud.append(url).append("?sessionId=").append(sessionId);
    String header =  ParamUtil.getRequestString(request,"hmac");
    System.out.println("MerchantApp:b2sTxnEndUrl : hmac: " + header);
    String message =  ParamUtil.getRequestString(request,"message");//contains TxnRes message
    System.out.println("MerchantApp:b2sTxnEndUrl : data, message: " + message);
    ParamUtil.setSessionAttr(request,"message",message);
    ParamUtil.setSessionAttr(request,"header",header);
    RedirectUtil.redirect(bud.toString(), request, response);

%>

