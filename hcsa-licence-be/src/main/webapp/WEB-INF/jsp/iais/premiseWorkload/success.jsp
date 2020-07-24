<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
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
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <div class="col-xs-12">
    <div class="col-lg-12 col-xs-12">
      <div class="center-content">
        <div class="intranet-content">
          <div class="bg-title">
            <h2>
              <span>The record has been updated successfully.</span>
            </h2>
          </div>
          <div class="row">
            <div class="col-xs-12">
              <div align="left">
                <a class="back" href="#" onclick="back()"><em class="fa fa-angle-left"></em> Back</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>
</div>

<script type="text/javascript">
  function back() {
    SOP.Crud.cfxSubmit("mainForm");
  }
</script>
