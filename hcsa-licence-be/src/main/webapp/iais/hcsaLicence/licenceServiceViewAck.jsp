<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
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
            <iais:error>
              <c:if test = "${not empty errorMsg}">
                <div class="error">
                    ${errorMap}
                </div>
              </c:if>
            </iais:error>
            <iais:success>
              <c:if test = "${not empty successMsg}">
                <div class="success">
                    ${successMsg}
                </div>
              </c:if>
            </iais:success>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
