<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
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
              <h2>Inspection date for Audit task has been set.</h2>
            </c:if>
            <c:if test="${'APTY007' ne applicationViewDto.applicationDto.applicationType}">
              <h2>The appointment date has been sent to the applicant for confirmation.</h2>
            </c:if>
          </div>
        </div>
        <div align="left"><span><a href="/main-web/eservice/INTRANET/MohBackendInbox"><em class="fa fa-angle-left"></em> Back</a></span></div>
      </div>
    </div>
  </div>
</div>
