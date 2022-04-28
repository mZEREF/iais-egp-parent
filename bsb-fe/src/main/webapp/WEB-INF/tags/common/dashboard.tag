<%@tag description="Common dashboard" pageEncoding="UTF-8" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="titleFrag" fragment="true" %>

<%
    String webroot = com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant.CSS_ROOT + com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <%@include file="/WEB-INF/jsp/iais/inbox/dashboard/menuPage.jsp" %>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="dashboard-page-title">
                    <jsp:invoke fragment="titleFrag"/>
                </div>
            </div>
        </div>
    </div>
</div>