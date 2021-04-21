<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/4/20
  Time: 15:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<%
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="beDashCommonSuccessForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="hcsaBeDashboardSwitchType" value="">
    <input type="hidden" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <c:choose>
                  <c:when test="${'true' eq taskHasBeenAssigned}">
                    <h2><iais:message key="LOLEV_ACK039" escape="true"></iais:message></h2>
                  </c:when>
                  <c:otherwise>
                    <h2><iais:message key="LOLEV_ACK029" escape="true"></iais:message></h2>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>
            <div align="left"><span><a href="/hcsa-licence-web/eservice/INTRANET/MohInspectionAllotTaskInspector"><em class="fa fa-angle-left"></em> Back</a></span></div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>