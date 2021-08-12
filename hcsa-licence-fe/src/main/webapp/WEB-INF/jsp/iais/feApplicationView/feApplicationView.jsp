<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-blank"/>
<c:if test="${applicationDto.applicationType == 'APTY008'}">
    <%
        String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
    %>
    <div class="dashboard" id="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
        <div class="container">
            <div class="navigation-gp">
                <c:if test="${!isPopApplicationView}">
                <div class="row">
                    <%@ include file="../common/dashboardDropDown.jsp" %>
                </div>
                </c:if>
                <div class="row">
                    <div class="col-xs-12" style="padding-left: 95px;">
                        <h1>Cessation Form</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>

<c:if test="${applicationDto.applicationType == 'APTY008'}">
    <%@include file="../newApplication/cessationViewApp.jsp" %>
</c:if>
<c:if test="${applicationDto.applicationType != 'APTY008'}">
    <%@ include file="../newApplication/inboxView/dashboard.jsp" %>
</c:if>

<c:if test="${applicationDto.applicationType != 'APTY008'}">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_type_tab" value="">
        <div class="main-content">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="tab-gp steps-tab">
                            <div class="tab-content">
                                <div class="tab-pane active" id="previewTab" role="tabpanel">
                                    <div class="preview-gp">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel-group" id="accordion" role="tablist"
                                                     aria-multiselectable="true" style="margin-top: 40px" >
                                                    <c:if test="${applicationDto.applicationType != 'APTY008'}">
                                                        <%@include file="../newApplication/inboxView/inboxLicensee.jsp" %>
                                                        <%@include file="../newApplication/inboxView/inboxPremise.jsp" %>
                                                        <%@include file="../newApplication/inboxView/inboxPrimary.jsp" %>
                                                        <%@include file="../newApplication/inboxView/viewForm.jsp" %>
                                                        <%@include file="../common/declarations.jsp"%>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                        <c:if test="${!isPopApplicationView}">
                                            <div class="row">
                                                <div class="container">
                                                    <div class="col-xs-12 col-md-6 text-left">
                                                        <a href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em
                                                                class="fa fa-angle-left"></em> Back</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</c:if>

