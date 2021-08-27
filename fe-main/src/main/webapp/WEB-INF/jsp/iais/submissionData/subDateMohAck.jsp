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
    String noContainCLB = ParamUtil.getRequestString(request,"noContainCLB");
    String msg = "Thank you for your submission.";
    if(!StringUtil.isEmpty(noContainCLB)){
        msg = noContainCLB;
    }

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
            <p class="print"><div style="font-size: 16px;text-align: right;"><a href="javascript:void(0)" onclick="printWDPDF();"> <em class="fa fa-print"></em>Print</a></div></p>
            <div class="col-lg-12 col-xs-12">
                <div id = "print" class="center-content">
                    <span style="font-size:2rem;"><%=msg%></span>
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
<script type="text/javascript">
    function printWDPDF(){
        window.open("<%=request.getContextPath() %>/eservice/INTERNET/MohFeMainPrint?whichPage=ldtMsg",'_blank');
    }

</script>