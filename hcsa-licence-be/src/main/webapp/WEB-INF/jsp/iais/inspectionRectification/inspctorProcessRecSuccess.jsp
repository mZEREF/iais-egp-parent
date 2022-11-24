<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<%--@elvariable id="isRollBack" type="java.lang.String"--%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
  <div class="row">
    <div class="col-lg-12 col-xs-12">
      <div class="center-content">
        <div class="intranet-content">
          <div class="bg-title">
            <h2>
              <c:choose>
                <c:when test="${'true' eq isRollBack}"><iais:message key="INSPE_ACK002" escape="true"/></c:when>
                <c:when test="${'laterally' eq isRollBack}"><iais:message key="LOLEV_ACK057" escape="true"/></c:when>
                <c:otherwise><iais:message key="LOLEV_ACK029" escape="true"/></c:otherwise>
              </c:choose>
            </h2>
          </div>
        </div>
        <div align="left"><span><a href="/main-web/eservice/INTRANET/MohHcsaBeDashboard"><em class="fa fa-angle-left"></em> Back</a></span></div>
      </div>
    </div>
  </div>
</div>
