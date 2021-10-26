<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
  String continueURL = "";
  if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
    continueURL = process.runtime.continueURL();
  }
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=continueURL%>>
  <div class="main-content">
    <div class="container center-content">
      <div class="col-xs-12">
        <h3 style="font-size: 36px">New Assisted Reproduction Submission</h3>
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <%@include file="section/arSubmissionSection.jsp" %>
        </div>
        <%@include file="common/arFooter.jsp" %>
      </div>
    </div>
  </div>
</form>
<script type="text/javascript">
  function toggleOnCheck(sel, elem, hide) {
    if (isEmpty(sel)) {
      return;
    }
    console.log("-----------------------------");
    var $selector = $(sel);
    if ($selector.length == 0) {
      $selector = $('#' + sel);
    } else if ($selector.length == 0) {
      $selector = $('.' + sel);
    }
    var $target = $(elem);
    if ($target.length == 0) {
      $target = $('#' + elem);
    } else if ($target.length == 0) {
      $target = $('.' + sel);
    }
    if ($selector.length == 0 || $target.length == 0) {
      return;
    }
    if ($selector.is(':checked')) {
      if (hide) {
        $target.hide();
        clearFields($target);
      } else {
        $target.show();
      }
    } else {
      if (hide) {
        $target.show();
      } else {
        $target.hide();
        clearFields($target);
      }
    }
  }
</script>