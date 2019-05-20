<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--
	start of /WEB-INF/layouts/egp-blank/layout.jsp
	this layout is used by egp form and file upload component, didn't include the stage indicate.
-->
<%@page import="com.ecquaria.cloud.client.menu.Menu"%>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%-- BEGIN imports --%>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper"%>
<%@ page import="com.ecquaria.egp.core.helper.HostHelper" %>
<%@ page import="ecq.commons.helper.StringHelper" %>
<%@ page import="sop.config.ConfigUtil" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.ecquaria.cloud.entity.svcreg.ServiceRegistryService" %>
<%@ page import="com.ecquaria.egov.core.svcreg.ServiceRegistry" %>
<%@ page import="java.util.Date" %>
<%-- END imports --%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%@ page import="sop.webflow.rt.api.BaseProcessClass" %>
<%-- BEGIN taglib --%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>

<%-- END taglib --%>
<%
	String cmsHomeUrl = ConfigUtil.getString("egp.cms.home.url", "/cc");
	String cmsCCUrl = ConfigUtil.getString("egp.cc.url", "#");
	String cmsNewsUrl = ConfigUtil.getString("egp.cms.news.url", "#");

    response.setContentType("text/html;charset=UTF-8");

    String picName = "egp-navlogo.png";
%>
<%
    pageContext.setAttribute("productName", ConfigHelper.getString("egp.name","Ecquaria Government Platform"));
    pageContext.setAttribute("productVersion",ConfigHelper.getString("egp.version", "Enterprise Edition Version 1.0.0"));
%>
<html>
<head>
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<%@ include file="/WEB-INF/jsp/inc/egp-saas-include.jsp" %>

<%-- BEGIN additional header --%>
<tiles:insertAttribute name="header-ext" ignore="true" />

    <link href="<%=EngineHelper.getResourcePath()%>/_statics/css/aboutstyle-new.css" rel="stylesheet" type="text/css" media="all" />
    <style type="text/css">
        <%
            Object obj = request.getAttribute("process");
            String processName=null;
            if (obj!=null){
                BaseProcessClass bpc=(BaseProcessClass)obj;
                processName=bpc.sopReq.getProcessName();
            }
            if (!StringHelper.isEmpty(processName)){
        %>
        #nav .dropdown-menu a[href*="<%=processName %>"]{
            color: #f8ca2c;
        }
        <%
            }
        %>
    </style>
<%-- END additional header --%>
</head>
<script type="text/javascript">
    function mainmenu() {
        // $(" #nav ul ").addClass("dropdown-menu")
        $(" .dropdown-menu li ").css({listStyle:'none', listStylePosition:'inside', zoom:'1'})
        // $(" #nav ul ").css({display: "none"}); // Opera Fix
        // $(" #nav li").hover(function () {
        //     $(this).find('ul:first').css({visibility: "visible", display: "none"}).show(100);
        // }, function () {
        //     $(this).find('ul:first').css({visibility: "hidden"});
        // });
        $("#nav .dropdown-menu a[href*='<%=processName %>']").parent().parent().prev().css("color","#f8ca2c")
    }

    $(document).ready(function () {
        mainmenu();
    });


    function confirmChangeLanguage(language){
        <egov-smc:messageTemplate var="confirmChangeLanguage" key="confirmChangeLanguage" default="Are you sure you want to change language?"></egov-smc:messageTemplate>
        SOP.Common.confirm({message:'<egov-core:escapeJavaScript value="${confirmChangeLanguage}"/>',func:function(){changeLanguageAction(language)}});
    }

    function changeLanguageAction(language) {
        SOP.Common.showMask();
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
</script>
<body class="nobg">
	<div class="nav-bar-default navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only"><egov-smc:commonLabel>Toggle navigation</egov-smc:commonLabel></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <span class="white logo-text" href="<%=cmsHomeUrl%>">
              <img class="navbar-brand" src="<%=EngineHelper.getResourcePath() %>/_themes/cc/img/<%=picName%>" alt="">
              <egov-smc:commonLabel>Ecquaria Government Platform</egov-smc:commonLabel>
          </span>
        <%--</div>--%>
          <%
              String hostEnable = ConfigHelper.getString("cloud.layout.host.enable", "true");
              if("true".equals(hostEnable)){
                  String hostName = HostHelper.getHostName();
                  hostName = StringHelper.escapeHtmlChars(hostName);

          %>
          <a class="white navbar-brand hidden-md hidden-sm hidden-xs" style="float:right;line-height: 32px; font-size: 12px; margin-left:0px;">(Host: <%=hostName%>)</a>
            <%
                }
            %>
        </div>

        <div id="navbar" class="collapse navbar-collapse ">
            <menu:load id="cc-top-menus">
                <menu:include name="CLOUD_CC"/>
            </menu:load>
            <ul id="nav" class="nav navbar-nav navbar-right">
                <menu:iterate id="cc-top-menus" var="item" varStatus="status">
                <c:choose>
                    <c:when test="${!status.last and status.next.depth > 1}">
                        <c:set var="nextDepth" value="${status.next.depth}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="nextDepth" value="1"/>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${item.depth >= 1}">
                        <c:set var="currDepth" value="${item.depth}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="currDepth" value="1"/>
                    </c:otherwise>
                </c:choose>

                <c:if test="${item.depth > 0}">
                <li>
                    <%
                        Menu temp = (Menu) pageContext.getAttribute("item");
                        if (temp != null && temp.getMenuStyle() != null && !temp.getMenuStyle().equals("")) {
                            if (temp.getMenuStyle().equals("default")) {
                    %>
                    <span class="menu_icon default"></span>
                    <%
                    } else if (temp.getMenuStyle().startsWith("url:")) {
                        String menuStyleDis = temp.getMenuStyle().substring(4);
                        pageContext.setAttribute("menuStyleDis", menuStyleDis);
                    %>
                    <c:if test="${!empty menuStyleDis}">
						<span class="menu_icon">
							<img src="<c:out value="${menuStyleDis}" />" width="16px" height="16px"/>
						</span>
                    </c:if>
                    <%
                    } else if (temp.getMenuStyle().startsWith("css:")) {
                        String menuStyleCssDis = temp.getMenuStyle().substring(4);
                        pageContext.setAttribute("menuStyleCssDis", menuStyleCssDis);
                    %>
                    <span class="menu_icon <c:out value="${menuStyleCssDis}" />"></span>
                    <%
                            }
                        }
                    %>

                    <c:set var="url" value="${item.url}" scope="page"/>
                    <%
                        String url = (String) pageContext.getAttribute("url");
                        if (url == null || "".equals(url) || "#".equals(url)) {
                    %>
                    <c:if test="${ 'About' eq item.displayLabel }">
                    <a class="drp-aro" href="#" data-toggle="modal" data-target="#myModal">
                        </c:if>
                        <c:if test="${ 'About' ne item.displayLabel }">
                            <a class="drp-aro dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" href="#">

                            </c:if>
                            <span>
                                    <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                                </span>
                            <c:if test="${ 'About' ne item.displayLabel }">
                                <span class="caret"/>
                            </c:if>
                    </a>
                    <%
                    } else if (url.startsWith("/")) {
                    %>
                    <%
                        String parts[] = StringHelper.tokenize(url, "/");
                        if(parts.length>3){
                            String project = parts[2];
                            String process = parts[3];
                            ServiceRegistry serviceRegistry = ServiceRegistryService.getInstance().getRegistryByProcess(project, process);
                            if(serviceRegistry!=null){
                                Date activateDate = serviceRegistry.getActivateDate();
                                Date deActivateDate = serviceRegistry.getDeactivateDate();
                                if(activateDate!=null){
                                    if(deActivateDate!=null){
                                        if(new Date().after(activateDate)&&new Date().before(deActivateDate)){
                                            %>
                                            <a href="<c:out value="${item.url}" />">
                                                <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                                            </a>
                                            <%
                                        }
                                    }else{
                                        if(new Date().after(activateDate)){
                                            %>
                                            <a href="<c:out value="${item.url}" />">
                                                <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                                            </a>
                                            <%
                                        }
                                    }
                                }
                                else {
                                    if(deActivateDate!=null){
                                        if(new Date().before(deActivateDate)){
                                            %>
                                            <a href="<c:out value="${item.url}" />">
                                                <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                                            </a>
                                            <%
                                         }
                                    }else{
                                        %>
                                        <a href="<c:out value="${item.url}" />">
                                            <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                                        </a>
                                        <%
                                    }
                                }
                            }else{
                                %>
                                <a href="<c:out value="${item.url}" />">
                                    <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                                </a>
                                <%
                            }
                         }else{
                            %>
                            <a href="<c:out value="${item.url}" />">
                                <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                            </a>
                            <%
                        }
                    %>

                    <%
                    } else {
                    %>
                    <a href="<c:out value="${item.url}" />">
                        <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                    </a>
                    <%
                        }
                    %>

                    <c:choose>
                    <c:when test="${currDepth == nextDepth}">
                </li>
                </c:when>
                <c:when test="${currDepth > nextDepth}">
                <c:forEach begin="0" end="${currDepth - nextDepth - 1}">
                </li>
            </ul>
            </c:forEach>
            </li>
            </c:when>
            <c:otherwise>
            <ul class="dropdown-menu">
                </c:otherwise>
                </c:choose>
                </c:if> </menu:iterate>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        <%
                                String siteLanguage = MultiLangUtil.getSiteLanguage();
                                if(StringHelper.equals(siteLanguage, "en")){
                            %>
                            <img src="<%=EgpcloudPortFactory.webContext%>/_themes/egov/images/US_flag.png"/>&nbsp;
                            <%
                                }else if(StringHelper.equals(siteLanguage, "zh")){
                            %>
                            <img src="<%=EgpcloudPortFactory.webContext%>/_themes/egov/images/CN_flag.png" />&nbsp;
                            <%
                                }
                            %>
                            <span class="caret"/>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a href="javascript:void(0);" onclick="confirmChangeLanguage('en')"><img src="<%=EgpcloudPortFactory.webContext%>/_themes/egov/images/US_flag.png" />&nbsp;<span>English</span></a>
                            </li>
                            <li>
                                <a href="javascript:void(0);" onclick="confirmChangeLanguage('zh')"><img src="<%=EgpcloudPortFactory.webContext%>/_themes/egov/images/CN_flag.png" />&nbsp;<span>简体中文</span></a>
                            </li>
                        </ul>
                    </li>
            </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
    <div class="row-fluid nav-second">
    </div>
    <div class="egp-sys-page-wrapper">
    	<div class="container" style="padding-top: 80px">
            <div class="breadcumb">
                <ul>
                <li><a class="black" href="<%=cmsHomeUrl%>"><egov-smc:commonLabel>Home</egov-smc:commonLabel></a> </li>
                <li><span class="black">${eServiceTitle}</span></li>
                </ul>
            </div>
            <div class="wrapper-content">
            <%--<egov-core:eServiceTitleTrail />--%>
                <h4><b> ${eServiceTitle}</b></h4>
            <%@ include file="../stage-indicator.jsp" %>
                <div class="content">
                    <tiles:insertAttribute name="body" ignore="true" />
                </div>
            </div>
    	</div>
    </div>
	<div id="footer"  class="footerlogin">
      <div class="container">
        <p class="white text-muted text-center copyright"><egov-smc:commonLabel>Copyright</egov-smc:commonLabel> &copy; <%=Calendar.getInstance().get(Calendar.YEAR) %> <egov-smc:commonLabel>Ecquaria Technologies Pte Ltd.</egov-smc:commonLabel> <egov-smc:commonLabel>All Rights Reserved.</egov-smc:commonLabel> </p>
      </div>
    </div>
    <div class="modal fade" id="myModal" role="dialog">
        <div class="vertical-alignment-helper">
            <div class="modal-dialog vertical-align-center">
                <div class="modal-content">
                    <div class="modal-header  text-center">
                        <button type="button" id="close" class="close" data-dismiss="modal">&times;</button>
                        <div class="row" id="content_about">
                            <img id="logo_about" src="<%=EngineHelper.getResourcePath() %>/_statics/egov-login-new/img/egp_about.png">
                            <h3><b> <egov-smc:commonLabel><%=ConfigHelper.getString("egp.name","Ecquaria Government Platform")%></egov-smc:commonLabel></b></h3>
                        </div>
                        <div id="content_about">
                            <hr>
                            <p><span><b><egov-smc:commonLabel>Product Name</egov-smc:commonLabel>:</b></span> <egov-smc:commonLabel><%=ConfigHelper.getString("egp.name","Ecquaria Government Platform")%></egov-smc:commonLabel></p>
                            <p><span><b><egov-smc:commonLabel>Version</egov-smc:commonLabel>:</b></span> <egov-smc:message key="egp.version"><%=ConfigHelper.getString("egp.version")%></egov-smc:message></p>
                            <p><span><b><egov-smc:commonLabel>Build</egov-smc:commonLabel>:</b></span><egov-smc:commonLabel> <%=ConfigHelper.getString("egp.version.build","201801041106")%></egov-smc:commonLabel></p>
                            <hr>
                            <p class="footer-copyright"><egov-smc:commonLabel>Copyright</egov-smc:commonLabel> &copy; <%=Calendar.getInstance().get(Calendar.YEAR) %> <egov-smc:commonLabel>Ecquaria Technologies Pte Ltd.</egov-smc:commonLabel> <egov-smc:commonLabel>All Rights Reserved.</egov-smc:commonLabel></p>
                            <br>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%--<script type="application/javascript">--%>
        <%--$( function() {--%>

            <%--var abt=$("span:contains('<egov-smc:commonLabel>About</egov-smc:commonLabel>')");--%>
            <%--abt.click(function(){--%>
                <%--$('#layer').fadeIn();--%>
                <%--$("#fullbg").height($(document).height());--%>
                <%--$("#fullbg").fadeIn()--%>

            <%--});--%>
            <%--$('#close').click(function() {--%>
                <%--$('#layer').fadeOut();--%>
                <%--$("#fullbg").fadeOut();--%>
            <%--});--%>
            <%--$("#fullbg").click(function() {--%>
                <%--$('#layer').fadeOut();--%>
                <%--$("#fullbg").fadeOut();--%>
            <%--});--%>
            <%--$("#close_ct").click(function() {--%>
                <%--$('#layer').fadeOut();--%>
                <%--$("#fullbg").fadeOut();--%>
            <%--});--%>
        <%--} );--%>

    <%--</script>--%>

    <%--<script>--%>
        <%--var params = {--%>
            <%--left: window.innerWidth/2,--%>
            <%--top: window.innerHeight/2,--%>
            <%--currentX: 0,--%>
            <%--currentY: 0,--%>
            <%--flag: false--%>
        <%--};--%>

        <%--var getCss = function(o,key){--%>
            <%--return o.currentStyle? o.currentStyle[key] : document.defaultView.getComputedStyle(o,false)[key];--%>
        <%--};--%>


        <%--var startDrag = function(bar, target, callback){--%>
            <%--if(getCss(target, "left") !== "auto"&&getCss(target, "left") !== "50%"){--%>
                <%--params.left = getCss(target, "left");--%>

            <%--}--%>
            <%--if(getCss(target, "top") !== "auto"&&getCss(target, "top") !== "50%"){--%>
                <%--params.top = getCss(target, "top");--%>

            <%--}--%>

            <%--bar.onmousedown = function(event){--%>
                <%--params.flag = true;--%>
                <%--if(!event){--%>
                    <%--event = window.event;--%>
                    <%--bar.onselectstart = function(){--%>
                        <%--return false;--%>
                    <%--}--%>
                <%--}--%>
                <%--var e = event;--%>
                <%--params.currentX = e.clientX;--%>
                <%--params.currentY = e.clientY;--%>

                <%--var ro = target.getBoundingClientRect();--%>

                <%--if(getCss(target, "left") !== "auto"&&getCss(target, "left") !== "50%"){--%>
                    <%--params.left = ro.left+200;--%>

                <%--}--%>
                <%--if(getCss(target, "top") !== "auto"&&getCss(target, "top") !== "50%"){--%>
                    <%--params.top = getCss(target, "top");--%>

                <%--}--%>
            <%--};--%>
            <%--document.onmouseup = function(){--%>
                <%--params.flag = false;--%>
                <%--if(getCss(target, "left") !== "auto"&&getCss(target, "left") !== "50%"){--%>
                    <%--params.left = getCss(target, "left");--%>

                <%--}--%>
                <%--if(getCss(target, "top") !== "auto"&&getCss(target, "top") !== "50%"){--%>
                    <%--params.top = getCss(target, "top");--%>

                <%--}--%>
            <%--};--%>
            <%--document.onmousemove = function(event){--%>
                <%--var e = event ? event: window.event;--%>
                <%--if(params.flag){--%>
                    <%--var nowX = e.clientX, nowY = e.clientY;--%>
                    <%--var disX = nowX - params.currentX, disY = nowY - params.currentY;--%>
                    <%--target.style.left = parseInt(params.left) + disX + "px";--%>
                    <%--target.style.top = parseInt(params.top) + disY + "px";--%>
                    <%--if (event.preventDefault) {--%>
                        <%--event.preventDefault();--%>
                    <%--}--%>
                    <%--return false;--%>
                <%--}--%>

                <%--if (typeof callback == "function") {--%>
                    <%--callback(parseInt(params.left) + disX, parseInt(params.top) + disY);--%>
                <%--}--%>
            <%--}--%>
        <%--};--%>

        <%--var layer=document.getElementById("layer");--%>
        <%--startDrag(layer,layer);--%>
    <%--</script>--%>
</body>
</html>
<!-- end of /WEB-INF/layouts/egp-blank/layout.jsp -->