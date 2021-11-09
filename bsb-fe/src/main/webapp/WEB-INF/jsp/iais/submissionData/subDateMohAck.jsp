<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
//    String noContainCLB = ParamUtil.getRequestString(request,"noContainCLB");
    String msg = "Thank you for your submission.";
//    if(!StringUtil.isEmpty(noContainCLB)){
//        msg = noContainCLB;
//    }

%>
<webui:setLayout name="iais-internet"/>
<div class="dashboard" id="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
<%--            <div class="row">--%>
<%--                <%@include file="common/dashboard.jsp" %>--%>
<%--            </div>--%>
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1 class="font-weight 0">xxx Submission</h1>
                        <h3>You are submitting for xxx</h3>
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
            <p class="print"><div style="font-size: 16px;text-align: right;"><a href="javascript:void(0)" onclick="printWDPDF()"> <em class="fa fa-print"></em>Print</a></div></p>
            <div class="row">
                <div class="col-xs-12">
                    <div class="center-content">
                        <span style="font-size:2rem;"><%=msg%></span>
<%--                        <span style="font-size:2rem;">You have successfully submit</span>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="container">
            <div class="col-xs-12 col-md-6 text-left">
                <a class="back" href="/bsb-fe/eservice/INTERNET/BsbDataSubmission"><em class="fa fa-angle-left"></em> Back</a>
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