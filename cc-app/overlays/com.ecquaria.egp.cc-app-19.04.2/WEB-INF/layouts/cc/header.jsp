<!-- start of /_themes/sop6/jsp/header.jsp -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ page import="com.ecquaria.cloud.client.menu.Menu" %>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="ecq.commons.helper.StringHelper" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ page import="sop.config.ConfigUtil" %>
<%@ page import="com.ecquaria.egov.core.svcreg.ServiceRegistry" %>
<%@ page import="com.ecquaria.cloud.entity.svcreg.ServiceRegistryService" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.egp.core.helper.HostHelper" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<link href="<%=EngineHelper.getResourcePath()%>/_statics/css/aboutstyle-new.css" rel="stylesheet" type="text/css" media="all" />

<%
    String cmsHomeUrl = ConfigUtil.getString("egp.cms.home.url", "/cc");
    String cmsCCUrl = ConfigUtil.getString("egp.cc.url", "#");
    String cmsNewsUrl = ConfigUtil.getString("egp.cms.news.url", "#");

%>
<script type="text/javascript">
    function mainmenu() {
        // $(" #nav ul ").addClass("dropdown-menu")
        $(" .dropdown-menu li ").css({listStyle:'none', listStylePosition:'inside', zoom:'1'})
//        $(" #nav ul ").css({display: "none"}); // Opera Fix
//        $(" #nav li").hover(function () {
//            $(this).find('ul:first').css({visibility: "visible", display: "none"}).show(100);
//        }, function () {
//            $(this).find('ul:first').css({visibility: "hidden"});
//        });
    }

    $(document).ready(function () {
        mainmenu();
    });

</script>
<nav class="nav-bar-default navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <%
                String lang=MultiLangUtil.getSiteLanguage();
                String picName="egp-navlogo.png";
//                if ("zh".equals(lang))
//                    picName="ecq-logo-zh.png";
            %>
            <span class="white logo-text" >
                <img class="navbar-brand" src="<%=EngineHelper.getResourcePath()%>/_themes/cc/img/<%=picName%>" alt="">
                <egov-smc:commonLabel>Ecquaria Government Platform</egov-smc:commonLabel>
            </span>
            <%
                String hostEnable = ConfigHelper.getString("cloud.layout.host.enable", "true");
                if("true".equals(hostEnable)){
                    String hostName = HostHelper.getHostName();
                    hostName = StringHelper.escapeHtmlChars(hostName);

            %>
            <a class="navbar-brand" style="float:right;line-height: 40px; font-size: 12px;">(Host: <%=hostName%>)</a>
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
                        <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" href="#">
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
                </c:if>
                </menu:iterate>

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
                        <ul class="dropdown-menu" >
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
</nav>

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
                        <%--<p><span><egov-smc:commonLabel><b>Build</b></egov-smc:commonLabel>:</span><egov-smc:commonLabel> 201801041106 </egov-smc:commonLabel></p>--%>
                        <hr>
                        <p class="footer-copyright"><egov-smc:commonLabel>Copyright</egov-smc:commonLabel> &copy; <%=Calendar.getInstance().get(Calendar.YEAR) %> <egov-smc:commonLabel>Ecquaria Technologies Pte Ltd.</egov-smc:commonLabel> <egov-smc:commonLabel>All Rights Reserved.</egov-smc:commonLabel></p>
                        <br>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="application/javascript">
    function confirmChangeLanguage(language){
        <egov-smc:messageTemplate var="confirmChangeLanguage" key="confirmChangeLanguage" default="Are you sure you want to change language?"></egov-smc:messageTemplate>
        EGOV.Common.confirm({message:'${confirmChangeLanguage}',func:function(){changeLanguageAction(language)}});
    }

    function changeLanguageAction(language) {
//        SOP.Common.showMask();

        $.ajax({
            type: "get",
            url: "<%=EngineHelper.getContextPath()%>/process/EGPCLOUD/SetLanguage?language="+language,
            success: function (data) {
                <%--if(location.pathname.indexOf("MyProfile")>=0){--%>
                    <%--location.href="<%=EngineHelper.getContextPath()%>/process/EGPCLOUD/MyProfile/?_csrf=<%=csrf_token%>"--%>
                <%--}--%>
                <%--else{--%>
                    <%--location.reload();--%>
                <%--}--%>
                location.href="<%=EngineHelper.getWorkspaceContextPath()%>";

            },
            error: function (msg) {
//                alert("set language fail.");
                alert("<egov-smc:message key="setLangFail">set language fail.</egov-smc:message>");
            }
        });
    }
</script>
