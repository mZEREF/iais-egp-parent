<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/6/24
  Time: 13:30
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
  <div class="row">
    <div class="col-lg-12 col-xs-12">
      <div class="center-content">
        <div class="intranet-content">
          <div class="bg-title">
            <c:if test="${'APTY007' eq applicationDto.applicationType}">
              <h2><iais:message key="LOLEV_ACK043" escape="true"></iais:message></h2>
            </c:if>
            <c:if test="${'APTY007' ne applicationDto.applicationType}">
              <c:if test="${'reScheduleSuccess' eq reScheduleSuccess}">
                <c:if test="${'SUCCESS' eq appStatusFlag}">
                  <h2><iais:message key="LOLEV_ACK044" escape="true"></iais:message></h2>
                </c:if>
                <c:if test="${'FAIL' eq appStatusFlag}">
                  <h2><iais:message key="Rescheduled appointment failed. The status of these applications has been changed." escape="true"></iais:message></h2>
                </c:if>
              </c:if>
              <c:if test="${'reScheduleSuccess' ne reScheduleSuccess}">
                <h2><iais:message key="LOLEV_ACK052" escape="true"></iais:message></h2>
              </c:if>
            </c:if>
          </div>
        </div>
        <div align="left"><span><a href="/hcsa-licence-web/eservice/INTRANET/MohOfficerReScheduling"><em class="fa fa-angle-left"></em> Back</a></span></div>
      </div>
    </div>
  </div>
</div>