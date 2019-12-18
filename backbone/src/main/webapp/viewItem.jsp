
<%@page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts"%>
<%@page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.dto.LoginContext" %>
<%@ page import="java.util.HashMap" %>

<%
    LoginContext loginContext = null;
    try {
        loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
    } catch (Exception e) {}
    if (loginContext == null) {
%>
<jsp:forward page="/sessionExpired.jsp"></jsp:forward>
<%        
    }
    String itemID = request.getParameter("itemID");
    String itemContainer = request.getParameter("itemContainer");
    if (itemID == null || "".equals(itemID) || itemContainer == null || "".equals(itemContainer)) {
%>
<jsp:forward page="/sessionExpired.jsp"></jsp:forward>
<%
    }
    HashMap itemURLMap = (HashMap) ParamUtil.getSessionAttr(request, itemContainer);
    if (itemURLMap == null || !itemURLMap.containsKey(itemID)) {
      //nothing defined
%>
<jsp:forward page="/sessionExpired.jsp"></jsp:forward>
<%
    }
    String url = (String)itemURLMap.get(itemID);

    if (url.startsWith("/sop/")) {
        url = url.substring(4);
    } else if (url.startsWith("sop/")) {
        url = url.substring(3);
    }

    if (!url.startsWith("/")) {
        url = "/" + url;
    }
    String viewEmsStoredTabId = ParamUtil.getString(request, "emsStoredTabId");
    if (!StringUtil.isEmpty(viewEmsStoredTabId)) {
        if (url.indexOf("?") >= 0) {
            url += "&emsStoredTabId=" + viewEmsStoredTabId;
        } else {
            url += "?emsStoredTabId=" + viewEmsStoredTabId;
        }
    }
%>
<jsp:forward page="<%=url%>"></jsp:forward>
