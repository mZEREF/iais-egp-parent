<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/arHeader.jsp" %>

<c:set var="dataSubmission" value="${arSuperDataSubmissionDto.dataSubmissionDto}" />

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <div class="main-content">
    <div class="container center-content">
      <div class="col-xs-12">
        <h3>Please key in the donor sample information below.</h3>
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h4  class="panel-title" >
                <a href="#arDonorSampleDetails" data-toggle="collapse" >
                  Donor Sample
                </a>
              </h4>
            </div>
            <div id="arDonorSampleDetails" class="panel-collapse collapse in">
              <div class="panel-body">
                <div class="panel-main-content form-horizontal">
                  <div class="donorSample">
                    <%@include file="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/new/section/donorSample.jsp"%>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <c:if test="${arSuperDataSubmissionDto.appType eq 'DSTY_005'}">
            <%@include file="common/dsAmendment.jsp" %>
          </c:if>
        </div>
        <%@include file="common/arFooter.jsp" %>
      </div>
    </div>
  </div>
</form>
