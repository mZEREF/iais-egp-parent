<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <%@ include file="../common/dashboardDropDown.jsp" %>
                <div style="margin-top: 20px;margin-left: 4%" >
                    <h1>Appeal Form</h1>
                </div>
            </div>
        </div>
    </div>
    <c:if test="${rfiServiceName!=null }">
        <div style="margin-left: auto;margin-right: auto;width: 1280px">
            <div class="navigation-gp">

                <div style="margin-top: 20px;margin-left: 4%">
                    <p style="font-size: 2.2rem">You are appealing for <strong>${rfiServiceName }</strong></p>
                </div>

            </div>
        </div>
    </c:if>

</div>

<style>
    .mandatory{
        color: rgb(255,0,0);
    }

</style>