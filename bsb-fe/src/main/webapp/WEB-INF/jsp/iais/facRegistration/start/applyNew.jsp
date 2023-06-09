<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="action_type" value="">
  <input type="hidden" name="action_value" value="">
  <div class="container">
    <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
      <div class="col-xs-12 col-sm-12" style="padding-top: 30px">
        <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Before You Begin</p>
        <div>
          <ul>
            <li>In the next page, you will select the classification of the facility which you intend to register and
              the corresponding facility activity type. Before proceeding, you are advised
              to refer to the information that is available on the <a href="https://www.moh.gov.sg">MOH Biosafety website</a>
              to understand the different options, to ensure selection of the correct facility classification. Please note that
              selection of an incorrect facility classification may result in rejection of the application.
            </li>
            <li>This form will take approximately 15 minutes to complete. You may save your progress at any time and
              resume your application later.
            </li>
          </ul>
        </div>
      </div>
      <div class="row">
        <div class="col-xs-12">
          <div class="text-right text-center-mobile">
            <a class="btn btn-primary next" id="next" href="javascript:void(0);">START APPLICATION</a>
          </div>
        </div>
      </div>

    </div>
  </div>
</form>

