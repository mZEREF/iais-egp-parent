<%@page import="com.ecquaria.egp.core.application.controller.ApplicationViewController"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="com.ecquaria.egov.core.common.constants.AppConstants"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.ecquaria.egp.core.application.MessageHistory"%>
<%@ page import="com.ecquaria.cloud.entity.application.MessageHistoryService" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ page import="com.ecquaria.egp.core.helper.ConsistencyHelper" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<webui:setLayout name="NONE"/>
[<%
	response.setContentType("text/html;charset=UTF-8");
	String dateFormat = ConsistencyHelper.getDateFormat();
	DateFormat df = new SimpleDateFormat(dateFormat, MultiLangUtil.getSiteLocale());
	MessageHistory[] historys = (MessageHistory[])request.getAttribute("appMessages");
	for(int i = 0;i<historys.length;i++){
		MessageHistory his = historys[i];
		String receivedDate = df.format(his.getCreatedDate());
		String typeLabel = MessageHistoryService.getInstance().getSenderTypeLabel(his.getType());
		String s = "{dateSent:'"+receivedDate+"', officer:'"+his.getOfficer()+"',type:'"+ MultiLangUtil.translate(request,AppConstants.KEY_TRANSLATION_MODULE_LABEL,typeLabel)+"', content:'"+ new String(Base64.encodeBase64(his.getContent().getBytes()), ApplicationViewController.DEFAULT_BASE64_ENCODING) +"'}";
		String sep = "";
		if(i<historys.length-1){
			sep = ",";
		}
%>
<%=s %><%=sep %>
<%}%>]