<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>

<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/ar_common.js"></script>
<c:set value="${arSuperDataSubmissionDto != null && arSuperDataSubmissionDto.getDataSubmissionDto() != null && 'DSTY_005'.equalsIgnoreCase(arSuperDataSubmissionDto.getDataSubmissionDto().getAppType())}"
       var="isRfc"/>
<input type="hidden" name="isRfc" value="${isRfc}"/>
<input type="hidden" name="ar_page" value="${currentPageStage}"/>
<input type="hidden" name="_contextPath" id="_contextPath" value="${pageContext.request.contextPath}"/>
<input type="hidden" name="printflag" id="printflag" value="${printflag}">

<c:if test="${empty title}">
    <div class="dashboard title-only" id="comDashboard" style="padding: 30px 0 0;overflow:visible">
        <div class="container">
            <div class="navigation-gp">
                <div class="row">
                    <%@ include file="/WEB-INF/jsp/iais/common/dashboardDropDown.jsp" %>
                    <div class="col-xs-12"></div>
                </div>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${not empty title}">
    <div class="dashboard title-only" id="comDashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
        <div class="container">
            <div class="navigation-gp">
                <div class="row">
                    <%@ include file="/WEB-INF/jsp/iais/common/dashboardDropDown.jsp" %>
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
