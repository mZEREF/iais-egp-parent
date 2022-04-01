<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
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
                    <h1>Application for Approval</h1>
                </div>
                <div class="col-xs-12 col-sm-12" style="padding-top: 30px">
                    <div>
                        <c:if test="${processType ne null}">
                            <p>You are applying for <strong><iais:code code="${processType}"/></strong></p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>