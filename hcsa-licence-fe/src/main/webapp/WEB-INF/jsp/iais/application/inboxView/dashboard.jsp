<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot
            =IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" id="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <c:if test="${!isPopApplicationView}">
                    <%@ include file="../../common/dashboardDropDown.jsp" %>
                </c:if>
                <c:if test="${applicationDto.applicationType == 'APTY008'}">
                    <div class="row">
                        <div class="col-xs-12" style="padding-left: 95px;">
                            <h1>Cessation Form</h1>
                        </div>
                    </div>
                </c:if>
                <c:if test="${applicationDto.applicationType != 'APTY008'}">
                    <div class="col-xs-12">
                        <div class="col-xs-12">
                            <div class="tab-gp steps-tab">
                                <div class="tab-content">
                                    <div class="tab-pane active" id="previewTab" role="tabpanel">
                                        <h1 class="font-weight 0"><c:out value="${cessationForm}"></c:out></h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
