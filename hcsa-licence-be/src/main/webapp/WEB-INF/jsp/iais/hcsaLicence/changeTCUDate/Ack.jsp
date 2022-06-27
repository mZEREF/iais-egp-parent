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
            <h2><iais:message key="AUDIT_ACK005" /></h2>
          </div>
        </div>
        <div align="left"><span><a href="/hcsa-licence-web/eservicecontinue/INTRANET/MohChangeTCUDate/1/PremiseList"><em class="fa fa-angle-left"></em> Back</a></span></div>
      </div>
    </div>
  </div>
</div>
