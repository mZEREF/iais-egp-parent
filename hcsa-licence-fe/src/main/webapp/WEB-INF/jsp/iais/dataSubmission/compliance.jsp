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

<c:set var="title" value="New Data Submission" />

<%@ include file="assistedReproduction/common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
  <div class="row">
    <div class="container center-content">
      <div class="col-xs-12">
        <h3>MODULES SELECTED</h3>
      </div>
      <div class="col-xs-12">
        <ul><li style="padding-bottom: 14px;"><span><Strong>${complianceDto.submissionTypeDisplay}</Strong></span></li></ul>
        <h3>Before You Begin</h3>
      </div>
      <div class="col-xs-12">
        <ul>
          <li style=""><span>This form will take approximately ${complianceDto.mins} mins to complete. You may save your progress at anytime and resume your submission later</span></li>
          <li style=""><span>* denotes mandatory field.</span></li>
        </ul>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="container center-content">
      <div class="col-xs-12">
        <div class="application-tab-footer">
          <div class="col-xs-12 col-sm-4 col-md-2 text-left">
            <c:if test="${selfAssessmentGuide!='true'}">
              <a style="padding-left: 5px;" class="back" id="back" onclick=" showWaiting();submit('back');" >
                <em class="fa fa-angle-left">&nbsp;</em> Back
              </a>
            </c:if>
            <c:if test="${selfAssessmentGuide=='true'}">
              <a style="padding-left: 5px;" class="back" id="back" href="/main-web/eservice/INTERNET/MohAccessmentGuide">
                <em class="fa fa-angle-left">&nbsp;</em> Back
              </a>
            </c:if>

          </div>
          <div class="col-xs-12 col-sm-8 col-md-10">
            <div class="button-group">
              <a class="btn btn-primary next premiseId" id="proceed"  onclick="submit('${complianceDto.submissionType}')">PROCEED</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <input type="hidden" name="crud_type" id="crud_type" />
</form>
<c:if test="${param.cannotCLT eq 'Y'}">
  <iais:confirm msg="CANNOT_SUBMIT" callBack="$('#cannotCltDiv').modal('hide');" popupOrder="cannotCltDiv"
                yesBtnDesc="Close"
                yesBtnCls="btn btn-secondary" needCancel="false" needFungDuoJi="false"/>
</c:if>
<script type="text/javascript">
  $(document).ready(function () {
    const cannotCltDiv = $('#cannotCltDiv');
    if (cannotCltDiv.length > 0) {
      cannotCltDiv.modal('show');
    }
  });

</script>


<link rel="stylesheet" href="<%=webrootDS%>css/data_submission.css">
