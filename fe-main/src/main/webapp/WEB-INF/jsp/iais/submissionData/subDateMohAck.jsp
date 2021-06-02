<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
        <div class="container">
            <div class="navigation-gp">
                <div class="row">
                    <%@ include file="../common/dashboardDropDown.jsp" %>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="prelogin-title">
                    <h1>Laboratory Developed Test</h1>
                </div>
            </div>
        </div>
</div>
<div class="main-content">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <h2>Acknowledgement</h2>
                    <br>
                    <span style="font-size:2rem;">Thank you for your submission.</span>
                </div>
            </div>
            <div class="col-lg-12 col-xs-12">
                <div class="center-content " style="padding-top: 10px">
                    <a href="${backUrl}"><em
                            class="fa fa-angle-left"></em> Back</a>
                </div>
            </div>
        </div>
    </form>
</div>
<style>
    .mandatory {
        color: rgb(255, 0, 0);
    }

    .prelogin-title{
        padding-left: 90px;
    }

</style>