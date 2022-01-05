<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
  String webrootDS=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>

<c:set var="title" value="Data Submission" />

<%@ include file="assistedReproduction/common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
  <div class="row">
    <div class="container">
      <div class="col-xs-12">
        <h3>Please select the submission you wish to make</h3>
      </div>
    </div>
  </div>
  <div class="row" >
        <div class="container">
            <div class="col-xs-12" style="height: 60px">
              <div class="col-xs-8 col-md-4 text-left">
                <a class="btn btn-primary next premiseId" onclick="submit('AR')"  href="javascript:void(0);">Assisted Reproduction</a>
              </div>
            </div>
              <div class="col-xs-12" style="height: 60px">
              <div class="col-xs-8 col-md-4 text-left">
                <a class="btn btn-primary next premiseId" onclick="submit('DP')"  href="javascript:void(0);">Drug Practices</a>
              </div>
            </div>
          <div class="col-xs-12"  style="height: 60px">
            <div class="col-xs-8 col-md-4 text-left">
              <a class="btn btn-primary next premiseId" onclick="submit('LDT')"  href="javascript:void(0);">Laboratory Developed Test</a>
            </div>
          </div>
          <div class="col-xs-12" style="height: 60px">
            <div class="col-xs-8 col-md-4 text-left">
              <a class="btn btn-primary next premiseId" onclick="submit('TP')"  href="javascript:void(0);">Termination of Pregnancy</a>
            </div>
          </div>
          <div class="col-xs-12"  style="height: 60px">
            <div class="col-xs-8 col-md-4 text-left">
              <a class="btn btn-primary next premiseId" onclick="submit('VS')"  href="javascript:void(0);">Voluntary Sterilisation</a>
            </div>
          </div>
        </div>
  </div>
  <div class="row">
    <div class="container">
        <div class="col-xs-12">
        <div class="application-tab-footer">
          <div class="col-xs-12 col-sm-4 col-md-2 text-left">
            <a style="padding-left: 5px;" class="back" id="backBtn" href="/main-web">
              <em class="fa fa-angle-left">&nbsp;</em> Back
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>


</form>
<c:if test="${cannotCLT eq 'Y'}">
  <iais:confirm msg="CANNOT_SUBMIT" callBack="$('#cannotCltDiv').modal('hide');" popupOrder="cannotCltDiv" yesBtnDesc="Close"
                yesBtnCls="btn btn-secondary" needCancel="false" needFungDuoJi="false"/>
</c:if>
<link rel="stylesheet" href="<%=webrootDS%>css/data_submission.css">
<script type="text/javascript">
  $(document).ready(function () {
    if ($('#cannotCltDiv').length > 0){
      $('#cannotCltDiv').modal('show');
    }
  });
</script>