
<%@page import="com.ecquaria.cloud.mc.message.Message"%>
<%@page import="com.ecquaria.cloud.mc.api.MessageHelper"%>
<%@page import="com.ecquaria.cloud.mc.api.MessageConstants"%>
<%@page import="com.ecquaria.cloud.mc.api.MCPageTitleHelper"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="sop.rbac.user.User"%>
<%@page import="sop.iwe.SessionManager"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page import="sop.util.SysteminfoUtil"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@page import="java.util.Map"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.springframework.web.servlet.i18n.SessionLocaleResolver" %>
<%@ page import="com.ecquaria.cloud.mc.common.constants.AppConstants" %>
<%@ page import="com.ecquaria.cloud.mc.api.ConsistencyHelper" %>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<webui:setLayout name="printerfriendly"/>
<%
    response.setContentType("text/html;charset=UTF-8");
	String title = MCPageTitleHelper.getMCPageTitle();
	pageContext.setAttribute("transTitle", title);
%>
<webui:setAttribute name="title"><c:out value="${transTitle }"></c:out></webui:setAttribute>
<%
	Message message = (Message)request.getAttribute("entity");
	String content = message.getContent();
    content = content.replaceAll("\n","");
	String base64String = new String(Base64.encodeBase64(content.getBytes()));
	
	
%>
<script type="text/javascript">
	$(function(){
		
		$("#email-content").load(function() {
			
			$(this).height($(this).contents().height() + 20);
		});
		initEmailContent();
		
		window.print();
	});
	
	function initEmailContent() {
		var base64Content = '<%=base64String%>';
//		var content = Base64.decode(base64Content);
		var content = '<%=content%>';
		var iframe = document.getElementById("email-content");

		var doc = getIframeDocument(iframe);
		doc.designMode = "on";
		doc.open();
		doc.write(content);
		doc.close();
		doc.designMode ="off";
		
	}

	function getIframeDocument(element) {
		return element.contentDocument || element.contentWindow.document;
	}
</script>
<%
	String subject = message.getSubject();
	if(StringHelper.equals(message.getType(), MessageConstants.MODE_TYPE_SMS)){
		subject = MessageConstants.MODE_SUBJECT_LABEL_SMS;
	}
    Locale locale= MultiLangUtil.getSiteLocale();
    if (locale==null)
        locale= AppConstants.DEFAULT_LOCAL;
	if(message != null){
		Date dateTime = message.getReceivedDate();
		pageContext.setAttribute("dateTime", ConsistencyHelper.formatDateTime(dateTime));
	}
	String interval = MessageHelper.getDisplayIntervalTime(message.getReceivedDate());//msg.getDisplayIntervalTime();
%>

<div class="navbar navbar-default" role="navigation">
  <div class="container">
    <div class="navbar-header">
      <a class="navbar-brand" href="#"><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/img/bn-citilogo.png" alt="<egov-smc:commonLabel>Welcome</egov-smc:commonLabel>"><egov-smc:commonLabel>Citizen Centre</egov-smc:commonLabel></a>
    </div>

  </div>
</div>
<!-- // Fixed navbar -->

<div class="container">
<div>
       <!-- message box -->

          <!-- message top -->

          <div  class="message-boxtitle">
              <%
                  String displayLabel="";
                  String styleClass="";
                  switch (message.getImportance()){
                      case "L":
                          displayLabel="Low";
                          styleClass="label-success";
                          break;
                      case "H":
                          displayLabel="High";
                          styleClass="label-danger";
                          break;
                      case "N":
                          displayLabel="Normal";
                          styleClass="label-success";
                          break;
                      default:
                          break;
                  }
              %>
            <h4><%=subject%> <span class="label <%=styleClass%>"><egov-smc:commonLabel><%=displayLabel%></egov-smc:commonLabel></span></h4>
          </div>
          
          <!-- message content -->
            <div class="message-area">
              <div class="msg-avator"><span><%=MessageHelper.getSenderLabel(message) %></span></div>
              <div class="msg-datetime"><c:out value="${dateTime}"></c:out><br>
              <span><%=interval%></span>
              </div>

              <div class="textbox">
              <iframe id="email-content" width="100%" height="100%" class="mail-mesgbox" frameborder="0"></iframe>
              </div>
              <div class="clearfix"></div>
            </div>
       
        <!-- // table listing -->



  </div>
<!--  -->

</div>
<!-- // container -->


<!-- Footer -->
  <div id="footer">
      <div class="container">
          <%
              Calendar ca = Calendar.getInstance();
              int year = ca.get(Calendar.YEAR);
          %>
          <p class="copyright text-center"><egov-smc:commonLabel>Copyright</egov-smc:commonLabel> &copy; <%=year%> <egov-smc:commonLabel>Ecquaria Technologies Pte Ltd.</egov-smc:commonLabel> <egov-smc:commonLabel>All Rights Reserved.</egov-smc:commonLabel></p>
      </div>
  </div>
<!-- // Footer -->
