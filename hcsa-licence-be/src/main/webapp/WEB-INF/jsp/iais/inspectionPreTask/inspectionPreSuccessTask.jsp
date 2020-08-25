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
            <c:choose>
              <c:when test="${'routeBack' eq successPage}">
                <h2><iais:message key="LOLEV_ACK033" escape="true"></iais:message></h2>
              </c:when>
              <c:when test="${'requestForInfo' eq successPage}">
                <h2><c:out value="${successInfo}"></c:out></h2>
              </c:when>
              <c:otherwise>
                <h2><iais:message key="LOLEV_ACK029" escape="true"></iais:message></h2>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
        <div align="left"><span><a href="/main-web/eservice/INTRANET/MohBackendInbox"><em class="fa fa-angle-left"></em> Back</a></span></div>
      </div>
    </div>
  </div>
</div>
