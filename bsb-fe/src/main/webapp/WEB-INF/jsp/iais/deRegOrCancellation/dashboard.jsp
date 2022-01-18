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
                    <h1>Deregistration Form</h1>
                    <c:if test="${processType eq 'PROTYPE001' or processType eq 'PROTYPE005'}"><h1>Deregistration Form</h1></c:if>
                    <c:if test="${processType eq 'PROTYPE002' or processType eq 'PROTYPE003' or processType eq 'PROTYPE004'}"><h1>Cancellation Form</h1></c:if>
                </div>
            </div>
        </div>
    </div>
</div>