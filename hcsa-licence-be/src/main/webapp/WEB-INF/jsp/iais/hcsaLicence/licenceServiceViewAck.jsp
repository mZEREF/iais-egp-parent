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
              <c:if test = "${not empty errorMsg}">
                <iais:error>
                <div class="error">
                    ${errorMap}
                </div>
                </iais:error>
              </c:if>

              <c:if test = "${not empty successMsg}">
                <iais:success>
                <div class="success">
                    ${successMsg}
                </div>
                </iais:success>
              </c:if>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  $('#selectDetail',window.opener.document).html('${parentMsg}');
  $('#rfiSelect',window.opener.document).show();
  window.close();
</script>

