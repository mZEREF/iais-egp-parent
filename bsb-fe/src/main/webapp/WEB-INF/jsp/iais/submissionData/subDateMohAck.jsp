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
    String msg = "Thank you for your submission.";
%>
<webui:setLayout name="iais-internet"/>
<div class="dashboard" id="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1 class="font-weight 0"><iais:code code="${submissionType}"/> Submission</h1>
                        <h3>You are submitting for <iais:code code="${submissionType}"/></h3>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="main-content">
        <div class="container">
<%--            <p class="print"><div style="font-size: 16px;text-align: right;"><a href="javascript:void(0)" onclick="printWDPDF()"> <em class="fa fa-print"></em>Print</a></div></p>--%>
            <div class="row">
                <div class="col-xs-12">
                    <div class="center-content">
                        <div><span style="font-size:2rem;"><strong>Successful Submission</strong></span></div>
                        <div><span style="font-size:2rem;"><%=msg%></span></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="container">
            <div class="col-xs-12 col-md-6 text-left">
                <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxApp"><em class="fa fa-angle-left"></em> Back</a>
            </div>
        </div>
    </div>
<style>
    .mandatory {
        color: rgb(255, 0, 0);
    }

    .prelogin-title{
        padding-left: 90px;
    }

</style>
<script type="text/javascript">
    function printWDPDF(){
        window.open("<%=request.getContextPath() %>/eservice/INTERNET/MohFeMainPrint?whichPage=ldtMsg",'_blank');
    }

</script>