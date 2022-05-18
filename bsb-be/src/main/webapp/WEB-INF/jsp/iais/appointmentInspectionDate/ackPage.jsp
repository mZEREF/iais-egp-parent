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
            <c:if test="${'APTY007' eq applicationViewDto.applicationDto.applicationType}">
              <h2><iais:message key="LOLEV_ACK041" escape="true"></iais:message></h2>
            </c:if>
            <c:if test="${'APTY007' ne applicationViewDto.applicationDto.applicationType}">
              <h2><iais:message key="LOLEV_ACK040" escape="true"></iais:message></h2>
            </c:if>
          </div>
        </div>
        <div style="text-align: left"><span><a href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a></span></div>
      </div>
    </div>
  </div>
</div>
