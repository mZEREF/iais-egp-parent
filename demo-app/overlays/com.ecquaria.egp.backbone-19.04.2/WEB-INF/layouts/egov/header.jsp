<!-- start of /_themes/sop6/jsp/header.jsp -->
<%@page import="com.ecquaria.cloud.client.menu.Menu"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="ecq.commons.helper.ArrayHelper" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="ecq.commons.helper.StringHelper" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.ecquaria.cloud.client.task.Task" %>
<%@ page import="ecq.commons.util.EgpcloudPortFactory" %>
<%@ page import="com.ecquaria.cloud.jobschedule.util.SLAAlgorithm" %>
<%@ page import="com.ecquaria.cloud.jobschedule.util.ISLAAlgorithm" %>
<%@ page import="com.ecquaria.cloud.client.task.TaskService" %>
<%@ page import="sop.rbac.user.User" %>
<%@ page import="sop.iwe.SessionManager" %>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%
	TaskService mgr = (TaskService) TaskService.getInstance();
	User loginUser = SessionManager.getInstance(request).getCurrentUser();
	Map task = null;
	if (loginUser != null) {
		task = mgr.getTotalTaskBySLA(loginUser.getUserDomain(), loginUser.getId());
	}
	SessionManager manager = SessionManager.getInstance((HttpServletRequest) pageContext.getRequest());
	User user = manager.getCurrentUser();
// Announcement[] annous = null;
	int taskCount=0;
	int alertCount = 0;
	if (user != null) {
//    annous = AnnouncementManager.getInstance().retrieveAllActive(UserUtil.convert(user.getUserIdentifier()));
		Map taskCountMap = mgr.getTotalTaskCountByStatus(loginUser.getUserDomain(),loginUser.getId());
		int completedCount = getCountBy(taskCountMap, Task.STATUS_COMPLETED);
		taskCount =(int)(mgr.getTaskCountByUserDomainAndUserId(user.getUserDomain(),user.getId()) - completedCount);
		alertCount = mgr.getAlertCount(task);
	}
// if (annous == null) annous = new Announcement[0];
%>



<%!
	public int getCountBy(Map taskCountMap, String status) {
		Object obj = taskCountMap.get(status);
		return (obj == null) ? 0 : ((Integer) obj).intValue();
	}

	public String generateTask(Task task) {
		String elementId = null;
		Date dateCreated = null;
		int slaInDays = -1;
		int slaAlertInDays = -1;
		Integer remainInDays = task.getSlaRemainInDays();
		Date slaDateCompleted = null;
		long daysDifference = -1;
		long daysDifferenceRatio = -1;
		double progressBarValue = -1;
		ISLAAlgorithm strategy = SLAAlgorithm.getInstance().getStrategy();
		StringBuffer sb = new StringBuffer();

		slaInDays = task.getSlaInDays()==null?0:task.getSlaInDays();
		String cssClass = "";
		if (slaInDays != 0 && remainInDays != null) {
			slaAlertInDays = task.getSlaAlertInDays()==null?0:task.getSlaAlertInDays();
			dateCreated = task.getDateCreated();
			slaAlertInDays = task.getSlaAlertInDays()==null?0:task.getSlaAlertInDays();
			slaDateCompleted = task.getSlaDateCompleted();
			slaDateCompleted = slaDateCompleted == null ? new Date() : slaDateCompleted;
			daysDifference = slaInDays - remainInDays;

			if (remainInDays >= 0) {
				if(remainInDays > slaAlertInDays){
					cssClass = "icon-sla-dot-green";
				}else{
					cssClass = "icon-sla-dot-yellow";
				}
			}else{
				cssClass = "icon-sla-dot-red";
			}

			sb.append("<li class=\"" + cssClass + "\">");
			sb.append("<a href='" + EngineHelper.getWorkspaceContextPath() + "/process/EGPCLOUD/MyTask/1/GetAction?crud_action_type=callback&crud_action_value=" + task.getId() + "'>");
			sb.append(task.getSubject());
			sb.append("<br/><span class=\"status\">");
			sb.append("Status: " + task.getStatus());
			sb.append("&nbsp;&nbsp;&nbsp;l&nbsp;&nbsp;&nbsp;");
			sb.append("SLA:");
			dateCreated = task.getDateCreated();
			slaAlertInDays = task.getSlaAlertInDays();
			slaDateCompleted = task.getSlaDateCompleted();
			slaDateCompleted = slaDateCompleted == null ? new Date() : slaDateCompleted;

			daysDifferenceRatio = 0;
			if (slaInDays > 0) {
				daysDifferenceRatio = daysDifference * 100 / slaInDays;
				daysDifferenceRatio = (daysDifferenceRatio > 100) ? 100 : daysDifferenceRatio;
			}

			double _a = new Double(daysDifference).doubleValue();
			double _b = new Double(slaInDays).doubleValue();
			progressBarValue = Math.round(_a / _b * 1000);
			progressBarValue /= 10;

			sb.append(daysDifference).append("/").append(slaInDays).append(" (").append(progressBarValue).append("%)</span></a></li>");
		} else {
			sb.append("<li class='icon-sla-dot-green'>");
			sb.append("<a href='" + EngineHelper.getWorkspaceContextPath() + "/process/EGPCLOUD/MyTask/1/GetAction?crud_action_type=callback&crud_action_value=" + task.getId() + "'>");
			sb.append(task.getSubject());
			sb.append("<br/><span class=\"status\">");
			sb.append("Status: " + task.getStatus());
			sb.append("&nbsp;&nbsp;&nbsp;l&nbsp;&nbsp;&nbsp;");
			sb.append("SLA: N/A");
			sb.append("</span></a>");
			sb.append("</li>");
		}

		return sb.toString();
	}

	public String generateListTask(Map tasks, Integer maxNo) {
		if (tasks == null || tasks.size() == 0) return "";
		StringBuffer sb = new StringBuffer();
		List outstandingList = (List) tasks.get(Task.SLA_TASK_OUTSTANDING);
		sb.append("<div class=\"shortcut-popup-menu\">");
		if (outstandingList != null && outstandingList.size() > 0) {
			sb.append("<ul class=\"popup-menu\" style=\"display:none;\">");
			int maxValue = outstandingList.size();
			if (maxNo < outstandingList.size()) maxValue = maxNo;
			for (int i = 0; i < maxValue; i++) {
				Task task = (Task) outstandingList.get(i);
				sb.append(generateTask(task));
				if (i < outstandingList.size() - 1)
					sb.append("<li class=\"sep-dotted\"></li>");
				else sb.append("<li class=\"sep\"></li>");
			}
			sb.append("<li class=\"icon-trayinbox\"><a href=\"" + EngineHelper.getWorkspaceContextPath() + "/process/EGPCLOUD/MyTask/\" class=\"viewmore\">View All Tasks</a></li>");
			sb.append("</ul>");
		}
		sb.append("</div>");
		return sb.toString();
	}

	public String generateAlertTask(Map tasks, Integer maxNo) {
		if (tasks == null || tasks.size() == 0) return "";
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"shortcut-popup-menu\">");
		List overduedList = (List) tasks.get(Task.SLA_TASK_OVERDUED);
		List approachingList = (List) tasks.get(Task.SLA_TASK_APPROACHING);
		if ((overduedList != null && overduedList.size() > 0) || (approachingList != null && approachingList.size() > 0)) {
			sb.append("<ul class=\"popup-menu\" style=\"display:none;\">");
			int maxValue = overduedList.size();
			if (maxNo < overduedList.size()) {
				maxValue = maxNo;
			}
			for (int i = 0; i < maxValue; i++) {
				Task task = (Task) overduedList.get(i);
				sb.append(generateTask(task));
				if (i < overduedList.size() - 1 || approachingList.size() > 0) {
					sb.append("<li class=\"sep-dotted\"></li>");
				}
				else{
					sb.append("<li class=\"sep\"></li>");
				}
			}

			if (maxNo < approachingList.size()) {
				maxValue = maxNo;
			}else{
				maxValue = approachingList.size();
			}

			for (int i = 0; i < maxValue; i++) {
				Task task = (Task) approachingList.get(i);
				sb.append(generateTask(task));
				if (i < approachingList.size() - 1) {
					sb.append("<li class=\"sep-dotted\"></li>");
				}
				else{
					sb.append("<li class=\"sep\"></li>");
				}
			}
			sb.append("<li class=\"icon-alert\"><a href=\"" + EngineHelper.getWorkspaceContextPath() + "/process/EGPCLOUD/MyTask/\" class=\"viewmore\">View All Alerts</a></li>");
			sb.append("</ul>");
		}
		sb.append("</div>");
		return sb.toString();
	}
%>
<%Logger logger = LoggerFactory.getLogger(this.getClass());

	response.setContentType("text/html;charset=UTF-8");
	String langCode = MultiLangUtil.getSiteLanguage();

	String logPath = "";
	if(langCode.equals("zh")){
		logPath="/_statics/egov-login/img/ecq-logo_zh.png";
	}else{
		logPath = "/_statics/egov-login/img/ecq-logo.png";
	}
%>
<link href="<%=EngineHelper.getResourcePath()%>/_statics/css/aboutstyle.css" rel="stylesheet" type="text/css" media="all" />


<div id="logo">
	<a title="Back to Homepage" href="#" style="line-height: 20px; font-size: 14px; color: white;"><img border="0"
																										title="Ecquaria SOP&trade;"
																										alt="Ecquaria SOP&trade;"
																										src="<%=EngineHelper.getResourcePath() %><%=logPath%>" style="vertical-align: middle;"/><egov-smc:commonLabel>Ecquaria Government Platform</egov-smc:commonLabel></a>
</div>
<div class="alert-notification">
	<!-- Begin Inbox Blurb  -->
		<%if(taskCount == 0){%>
	<div class="top-shortcut shortcut-container disabled-shortcut" id="shortcut-listing-task">
		<%}else if(taskCount > 0){ %>
		<div class="top-shortcut shortcut-container" id="shortcut-listing-task">
			<span class="shortcut-icon-inboxtray"><%=taskCount %></span>
			<%} %>
			<div class="tray-icon">Tray Inbox</div>
			<!-- Begin Shortcut content  -->
			<%=generateListTask(task, 5) %>
			<!-- End Shortcut content  -->
		</div>
		<!-- End Inbox Blurb  -->

		<!-- Begin Alert Blurb  -->
		<%if(alertCount == 0){%>
		<div class="top-shortcut shortcut-container disabled-shortcut" id="shortcut-listing-alert">
			<%}else if(alertCount > 0){ %>
			<div class="top-shortcut shortcut-container" id="shortcut-listing-alert">
				<span class="shortcut-icon-alert"><%=alertCount %></span>
				<%} %>

				<div class="alert-icon">Alert Icon</div>
				<!-- Begin Shortcut content  -->
				<%=generateAlertTask(task, 5) %>
				<!-- End Shortcut content  -->
			</div>
			<!-- End Alert Blurb  -->

			<!-- Begin Announcement Blurb  -->
			<%--<%if(annous.length == 0){%>--%>
			<%--<div class="top-shortcut shortcut-container disabled-shortcut" id="shortcut-listing-announce">--%>
			<%--<%} else if (annous.length > 0) { %>--%>
			<%--<div class="top-shortcut shortcut-container" id="shortcut-listing-announce">--%>
			<%--<span class="shortcut-icon-notification"><%=annous.length %></span>--%>
			<%--<%} %>--%>

			<%--<div class="system-icon">System Announcement</div>--%>
			<%--<!-- Begin Shortcut content  -->--%>
			<%--<%=generateAnnouncement(annous) %>--%>
			<%--<!-- End Shortcut content  -->--%>
			<%--</div>--%>
			<%--<!-- End Announcement Blurb  -->--%>


		</div>
			<div class="float-rgh" id="nav-secondary">
				<menu:load id="top-menus">
					<menu:include name="EGOV_CLOUD_TOP" />
				</menu:load>
				<ul>
					<menu:iterate id="top-menus" var="item" varStatus="status">
						<c:if test="${item.depth == 1}">
							<li>
								<%
									Menu temp = (Menu)pageContext.getAttribute("item");
									if ( temp != null && temp.getMenuStyle() != null && !temp.getMenuStyle().equals("")){
										if (temp.getMenuStyle().equals("default")){
								%>
								<span class="menu_icon default"></span>
								<%
								} else if (temp.getMenuStyle().startsWith("url:")){
									String menuStyleDis = temp.getMenuStyle().substring(4);
									pageContext.setAttribute("menuStyleDis", menuStyleDis);
								%>
								<c:if test="${!empty menuStyleDis}">
						<span class="menu_icon">
							<img src="<c:out value="${menuStyleDis}" />" width="16px" height="16px" />
						</span>
								</c:if>
								<%
								} else if (temp.getMenuStyle().startsWith("css:")){
									String menuStyleCssDis = temp.getMenuStyle().substring(4);
									pageContext.setAttribute("menuStyleCssDis", menuStyleCssDis);
								%>
								<span class="menu_icon <c:out value="${menuStyleCssDis}" />"></span>
								<%
										}
									}
								%>

								<c:set var="url" value="${item.url}" scope="page" />
								<%
									String url = (String) pageContext.getAttribute("url");
									if (url == null || "".equals(url) || "#".equals(url)) {
								%>
								<a href="javascript:void(0);" class="drp-aro"><span>
								<egov-smc:commonLabel><c:out value="${item.displayLabel}" /></egov-smc:commonLabel>
					</span></a>
								<%
								} else if (url.startsWith("/")) {
								%>
								<a href="<c:out value="${item.url}" />">
									<egov-smc:commonLabel><c:out value="${item.displayLabel}" /></egov-smc:commonLabel>
								</a>
								<%
								} else if (url.startsWith("ctx:")) {
									String u = url.substring(4);
									String[] args = u.split("\\@\\|\\@");
									String path = "";
									if(!ArrayHelper.isEmpty(args) && args.length == 2){
										path = ConfigHelper.getString(args[0]) + args[1];
										logger.debug("path: "+path);
									}
								%>
								<a href="<%=path%>">
									<label><egov-smc:commonLabel><c:out value="${item.displayLabel}" /></egov-smc:commonLabel></label>
								</a>
								<%
								}else {
								%>
								<a href="<c:out value="${item.url}" />">
									<egov-smc:commonLabel><c:out value="${item.displayLabel}" /></egov-smc:commonLabel>
								</a>
								<%
									}
								%>

							</li>
						</c:if>
					</menu:iterate>
					<li id="language" onmouseover="show()" onmouseout="hide()">
						<a class="drp-aro" onmouseover="this.style.cssText='color:#ffc600; text-decoration:none;'" onmouseout="this.style.cssText='color:white;text-decoration:none'">
							<%
								String siteLanguage = MultiLangUtil.getSiteLanguage();
								if(StringHelper.equals(siteLanguage, "en")){
							%>
							<img src="<%=EgpcloudPortFactory.webContext%>/_themes/egov/images/US_flag.png" style="vertical-align:middle;"/>&nbsp;
							<%
							}else if(StringHelper.equals(siteLanguage, "zh")){
							%>
							<img src="<%=EgpcloudPortFactory.webContext%>/_themes/egov/images/CN_flag.png" style="vertical-align:middle;"/>&nbsp;
							<%
								}
							%>
						</a>
						<div style="clear:both;"></div>


						<ul class="language-menu"
							style="display: none;
							background: #444 none repeat scroll 0 0;
							border-radius: 0;
							border-top: 3px solid #6195b4;
							box-shadow: 0 2px 3px rgba(0, 0, 0, 0.2);
							margin: 0;
							opacity: 0.95;
							/*padding: 7px;*/
							position: absolute;
							z-index: 91000;
							white-space: nowrap;
							width: 100px;"
						>
							<li>
								<a href="javascript:void(0);"  onclick="confirmChangeLanguage('en')"><img src="<%=EgpcloudPortFactory.webContext%>/_themes/egov/images/US_flag.png" style="vertical-align:middle;"/>&nbsp;<span>English</span></a>
							</li>
							<div style="clear:both;"></div>
							<li>
								<a href="javascript:void(0);" onclick="confirmChangeLanguage('zh')"><img src="<%=EgpcloudPortFactory.webContext%>/_themes/egov/images/CN_flag.png" style="vertical-align:middle;"/>&nbsp;<span>简体中文</span></a>
							</li>
						</ul>
					</li>&nbsp; &nbsp; &nbsp; &nbsp;
				</ul>
			</div></div>

<div id="fullbg" style="height: 1181px; display: none;"></div>
<div id="layer" style="display: none;">
	<div id="title_about"><b><egov-smc:commonLabel>About </egov-smc:commonLabel><egov-smc:commonLabel><%=ConfigHelper.getString("egp.name","Ecquaria Government Platform")%></egov-smc:commonLabel></b></div>
	<br>
	<div id="logo_container">
		<div class="col-md-12">
			<div class="row">
				<div class="col-xs-3 col-sm-2 col-md-2 col-lg-offset-1 col-lg-2" align="right">
					<img id="logo_about" src="<%=EngineHelper.getResourcePath() %>/_statics/egov-login/img/ecq-logo-bigger.png" class="img-responsive">
				</div>
				<div class="col-xs-9 col-sm-10 col-md-10 col-lg-6" align="left">
					<h3><egov-smc:commonLabel><%=ConfigHelper.getString("egp.name","Ecquaria Government Platform")%></egov-smc:commonLabel></h3>
				</div>
			</div>

		</div>
	</div>
	<div class="clearfix"></div>
	<hr>
	<div id="content_about">
		<p><span><egov-smc:commonLabel>Product Name</egov-smc:commonLabel>:</span> <egov-smc:commonLabel><%=ConfigHelper.getString("egp.name","Ecquaria Government Platform")%></egov-smc:commonLabel></p>
		<p><span><egov-smc:commonLabel>Version</egov-smc:commonLabel>:</span> <egov-smc:message key="egp.version"><%=ConfigHelper.getString("egp.version")%></egov-smc:message></p>
		<p><span><egov-smc:commonLabel>Build</egov-smc:commonLabel>:</span> <%=ConfigHelper.getString("egp.version.build")%></p>

		<hr>
		<p class="footer-copyright"><egov-smc:commonLabel>Copyright</egov-smc:commonLabel> &copy; <%=Calendar.getInstance().get(Calendar.YEAR) %> <egov-smc:commonLabel>Ecquaria Technologies Pte Ltd.</egov-smc:commonLabel> <egov-smc:commonLabel>All Rights Reserved.</egov-smc:commonLabel></p>
		<br>
	</div>
	<div id="close_ct">
		<a href="javascript:void(0)" title="close" id="close">&times;</a>
	</div>
</div>

<script type="application/javascript">
	function show() {
		$('.language-menu').css('display','block');

	}
	function hide() {
		$('.language-menu').css('display','none');

	}


	function confirmChangeLanguage(language){
		<egov-smc:messageTemplate var="confirmChangeLanguage" key="confirmChangeLanguage" default="Are you sure you want to change language?"></egov-smc:messageTemplate>
		SOP.Common.confirm({message:'${confirmChangeLanguage}',func:function(){changeLanguageAction(language)}});
	}


	function changeLanguageAction(language) {
		$.ajax({
			type: "get",
			url: "<%=EngineHelper.getWorkspaceContextPath()%>/process/EGPCLOUD/SetLanguage?language="+language,
			success: function (data) {
				location.href="<%=EngineHelper.getWorkspaceContextPath()%>";
			},
			error: function (msg) {
				alert("set language fail.");
			}
		});
	}

	$( function() {

		var abt=$("span:contains('<egov-smc:commonLabel>About</egov-smc:commonLabel>')");
		abt.click(function(){
            $('#layer').fadeIn();
            $("#fullbg").height($(document).height());
            $("#fullbg").fadeIn()

		});
        $('#close').click(function() {
            $('#layer').fadeOut();
            $("#fullbg").fadeOut();
        });
        $("#fullbg").click(function() {
            $('#layer').fadeOut();
            $("#fullbg").fadeOut();
        });
        $("#close_ct").click(function() {
            $('#layer').fadeOut();
            $("#fullbg").fadeOut();
        });
	} );

</script>
	<script>
		var params = {
			left: window.innerWidth/2,
			top: window.innerHeight/2,
			currentX: 0,
			currentY: 0,
			flag: false
		};

		var getCss = function(o,key){
			return o.currentStyle? o.currentStyle[key] : document.defaultView.getComputedStyle(o,false)[key];
		};


		var startDrag = function(bar, target, callback){
			if(getCss(target, "left") !== "auto"&&getCss(target, "left") !== "50%"){
				params.left = getCss(target, "left");

			}
			if(getCss(target, "top") !== "auto"&&getCss(target, "top") !== "50%"){
				params.top = getCss(target, "top");

			}

			bar.onmousedown = function(event){
				params.flag = true;
				if(!event){
					event = window.event;
					bar.onselectstart = function(){
						return false;
					}
				}
				var e = event;
				params.currentX = e.clientX;
				params.currentY = e.clientY;

				var ro = target.getBoundingClientRect();

				if(getCss(target, "left") !== "auto"&&getCss(target, "left") !== "50%"){
					params.left = ro.left+200;

				}
				if(getCss(target, "top") !== "auto"&&getCss(target, "top") !== "50%"){
					params.top = getCss(target, "top");

				}
			};
			document.onmouseup = function(){
				params.flag = false;
				if(getCss(target, "left") !== "auto"&&getCss(target, "left") !== "50%"){
					params.left = getCss(target, "left");

				}
				if(getCss(target, "top") !== "auto"&&getCss(target, "top") !== "50%"){
					params.top = getCss(target, "top");

				}
			};
			document.onmousemove = function(event){
				var e = event ? event: window.event;
				if(params.flag){
					var nowX = e.clientX, nowY = e.clientY;
					var disX = nowX - params.currentX, disY = nowY - params.currentY;
					target.style.left = parseInt(params.left) + disX + "px";
					target.style.top = parseInt(params.top) + disY + "px";
					if (event.preventDefault) {
						event.preventDefault();
					}
					return false;
				}

				if (typeof callback == "function") {
					callback(parseInt(params.left) + disX, parseInt(params.top) + disY);
				}
			}
		};

		var layer=document.getElementById("layer");
		startDrag(layer,layer);
	</script>
</div>