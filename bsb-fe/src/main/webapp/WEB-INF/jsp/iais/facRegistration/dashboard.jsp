<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <%@include file="../inbox/dashboard/menuPage.jsp" %>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="dashboard-page-title">
                    <h1>New Facility Registration</h1>
                    <p>You are applying for <strong><iais:code code="${SELECTED_CLASSIFICATION}"/></strong> with activity type:</p>
                    <p>
                    <c:forEach var="activity" items="${SELECTED_ACTIVITIES}" varStatus="status">
                        <c:if test="${status.index > 0}"> | </c:if>
                        <strong><iais:code code="${activity}"/></strong>
                    </c:forEach>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>