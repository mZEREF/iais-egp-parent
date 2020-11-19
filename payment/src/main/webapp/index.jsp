<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>

<%

    String sessionId= (String) session.getAttribute("sessionNetsId");
    String url= AppConsts.REQUEST_TYPE_HTTPS + request.getServerName()+"/payment-web/process/EGPCLOUD/PaymentCallBack";
    StringBuilder bud = new StringBuilder();
    bud.append(url).append("?sessionId=").append(sessionId);
    String header =  request.getParameter("hmac");
    System.out.println("MerchantApp:b2sTxnEndUrl : sessionId: " + session.getId());
    System.out.println("MerchantApp:b2sTxnEndUrl : hmac: " + header);
    String message =  request.getParameter("message");//contains TxnRes message
    System.out.println("MerchantApp:b2sTxnEndUrl : data, message: " + message);
    ParamUtil.setSessionAttr(request,"message",message);
    ParamUtil.setSessionAttr(request,"header",header);
    RedirectUtil.redirect(bud.toString(), request, response);

%>

