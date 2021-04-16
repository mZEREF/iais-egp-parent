<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>

<%

    String sessionId= request.getParameter("reqNo");
    String url= AppConsts.REQUEST_TYPE_HTTPS + request.getServerName()+"/egov/eservice/INTERNET/Payment";
    StringBuilder bud = new StringBuilder();
    bud.append(url).append("?reqNo=").append(sessionId);
    String header =  request.getParameter("hmac");
    System.out.println("MerchantApp:b2sTxnEndUrl : sessionId: " + session.getId());
    System.out.println("MerchantApp:b2sTxnEndUrl : hmac: " + header);
    String message =  request.getParameter("message");//contains TxnRes message
    System.out.println("MerchantApp:b2sTxnEndUrl : data, message: " + message);
    bud.append("&message=").append(message);
    bud.append("&hmac=").append(header);
    RedirectUtil.redirect(bud.toString(), request, response);

%>

