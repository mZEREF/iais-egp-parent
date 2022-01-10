<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/ldt_common.js"></script>
<input type="hidden" name="current_page" value="${ldt_current_page}"/>
<input type="hidden" name="_contextPath" id="_contextPath" value="${pageContext.request.contextPath}"/>
<input type="hidden" name="printflag" id="printflag" value="${printflag}">

<div class="dashboard" id="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <%@ include file="/WEB-INF/jsp/iais/common/dashboardDropDown.jsp" %>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1 class="font-weight 0">Laboratory Developed Test</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>