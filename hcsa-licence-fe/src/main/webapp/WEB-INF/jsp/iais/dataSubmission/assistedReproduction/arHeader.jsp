<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<c:if test="${empty title}">
    <div class="dashboard" id="comDashboard">
        <div class="container" style="padding: 0 90px;">
            <div class="row d-flex">
                <%@ include file="/WEB-INF/jsp/iais/common/dashboardDropDown.jsp" %>
            </div>
            <div class="row">
                <div class="col-xs-12"></div>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${not empty title}">
    <div class="dashboard" id="comDashboard" style="background-image:url('<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT%>img/Masthead-banner.jpg')" >
        <div class="container" style="padding: 0 90px;">
            <div class="row d-flex">
                <%@ include file="/WEB-INF/jsp/iais/common/dashboardDropDown.jsp" %>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>${title}</h1>
                        <c:if test="${not empty smallTitle}">
                            <p>${smallTitle}</p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>
