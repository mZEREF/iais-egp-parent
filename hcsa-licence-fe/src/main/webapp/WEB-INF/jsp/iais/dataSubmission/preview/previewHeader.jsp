<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>

<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/ds_preview.js"></script>

<input type="hidden" name="ar_page" value="${currentPageStage}"/>
<input type="hidden" id="_contextPath" name="_contextPath" value="${pageContext.request.contextPath}"/>

<div class="dashboard" id="comDashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
    <div class="container" style="padding: 0 90px;">
        <div class="row d-flex">
            <c:if test="${ DashboardTitle != 'Withdrawal Form'}">
            <%@ include file="/WEB-INF/jsp/iais/common/dashboardDropDown.jsp" %>
            </c:if>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="dashboard-page-title">
                    <c:if test="${not empty title}">
                        <h1>${title}</h1>
                    </c:if>
                    <c:if test="${empty title}">
                        <h1>Data Submission</h1>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

