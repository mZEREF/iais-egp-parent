<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
sop.webflow.rt.api.BaseProcessClass process =
    (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="../dashboard2.jsp" %>

<h1>Premises List</h1>
<div class="tab-pane" id="tabApp" role="tabpanel">
  <form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type_form_value" value="">
  <input type="hidden" id="hiddenIndex" name="hiddenIndex" value="" />
  <div class="row col-xs-11 ">
    <ul>
      <li>
      Premises Information
      </li>
      <li>
      MedAlert Personnel
      </li>
      <li>
      Principal Officer
      </li>
      <li>
      Deputy Principal Officer
      </li>
      <li>
      Service-Related Information
      </li>
      <li>
      Supporting Document(s)
      </li>
    </ul>
  </div>

  <div class="row col-xs-11 ">
    <div class="col-xs-12">
      <a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a>
    </div>
    <div class="col-xs-12 col-sm-6">
      <div class="button-group">
        <a class="next btn btn-primary" id = "Next">Next</a>
      </div>
    </div>
  </div>

  <div class="row">
  </div>
  </form>
</div>
<script>
  $(document).ready(function () {

  });

</script>