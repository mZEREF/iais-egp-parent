<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>

<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<%--<c:set var="title" value="New Assisted Reproduction Submission" />--%>
<c:set var="smallTitle" value="You are submitting for <strong>Termination Of Pregnancy</strong>" />

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/top_common.js"></script>

<input type="hidden" name="top_page" value="${currentPageStage}"/>
<input type="hidden" id="_contextPath" name="_contextPath" value="${pageContext.request.contextPath}"/>
<input type="hidden" name="printflag" id="printflag" value="${printflag}">

<c:if test="${empty title}">
    <div class="dashboard" id="comDashboard" style="padding: 30px 0 0;overflow:visible">
        <div class="container">
            <div class="navigation-gp">
                <div class="row d-flex">
                    <%@ include file="/WEB-INF/jsp/iais/common/dashboardDropDown.jsp" %>
                </div>
                <div class="row">
                    <div class="col-xs-12"></div>
                </div>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${not empty title}">
    <div class="dashboard" id="comDashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
        <div class="container">
            <div class="navigation-gp">
                <div class="row d-flex">
                    <%@ include file="/WEB-INF/jsp/iais/common/dashboardDropDown.jsp" %>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="dashboard-page-title">
                            <h1>${title}</h1>
                            <c:if test="${not empty smallTitle}">
                                <p style="font-size: 25px;">${smallTitle}</p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>
