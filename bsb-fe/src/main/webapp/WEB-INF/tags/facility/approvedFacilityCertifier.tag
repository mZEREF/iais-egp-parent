<%@tag description="Approved facility certifier tag of facility registration" pageEncoding="UTF-8" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="afc" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto" %>
<%@attribute name="afcOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<%@include file="/WEB-INF/jsp/iais/facRegistration/dashboard.jsp" %>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="action_type" value="">
  <input type="hidden" name="action_value" value="">
  <input type="hidden" name="action_additional" value="">
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <%@include file="/WEB-INF/jsp/iais/mainAppCommon/facRegistration/InnerNavTab.jsp" %>
            <div class="tab-content">
              <div class="tab-pane fade in active">
                <div id="facInfoPanel" role="tabpanel">
                  <div class="form-horizontal">
                    <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Approved Facility Certifier</h3>
                    <div class="form-group ">
                      <div class="col-sm-5 control-label">
                        <label>Has the facility appointed an Approved Facility Certifier </label>
                        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>1. The facility shall inform MOH of the selected certifier at least 1 month in advance of the certification date. </p><br/><p>2.When selecting the certifier, the facility shall ensure that the company or member of the certifying team has not provided any design, construction, commissioning, or maintenance services to the facility in the 12 months preceding the certification date. </p>">i</a>
                        <span class="mandatory otherQualificationSpan">*</span>
                      </div>
                      <div class="col-sm-6 col-md-7">
                        <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                          <label for="hasAppointedCertifier">Yes</label>
                          <input type="radio" name="appointed" id="hasAppointedCertifier" value="Y" <c:if test="${afc.appointed eq 'Y'}">checked="checked"</c:if> />
                        </div>
                        <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                          <label for="notAppointedCertifier">No</label>
                          <input type="radio" name="appointed" id="notAppointedCertifier" value="N" <c:if test="${afc.appointed eq 'N'}">checked="checked"</c:if> />
                        </div>
                        <span data-err-ind="appointed" class="error-msg"></span>
                      </div>
                    </div>

                    <div id="appointedCertifierSection" <c:if test="${afc.appointed ne 'Y'}">style="display: none" </c:if>>
                      <div class="form-group">
                        <div class="col-sm-5 control-label">
                          <label for="certifierSelection">Select Approved Facility Certifier </label>
                          <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                          <select name="afc"  class="afcDropDown" id="certifierSelection">
                            <option value="">Please Select</option>
                            <c:forEach var="item" items="${afcOps}">
                              <option value="${item.value}" <c:if test="${afc.afc eq item.value}">selected="selected"</c:if>>${item.text}</option>
                            </c:forEach>
                          </select>
                          <span data-err-ind="afc" class="error-msg"></span>
                        </div>
                      </div>

                      <div class="form-group">
                        <div class="col-sm-5 control-label">
                          <label for="selectReason">Reasons for choosing this AFC </label>
                          <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                          <textarea maxLength="1000" class="col-xs-12" name="selectReason" id="selectReason" rows="5"><c:out value="${afc.selectReason}"/></textarea>
                          <span data-err-ind="selectReason" class="error-msg"></span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <%@include file="/WEB-INF/jsp/iais/facRegistration/InnerFooter.jsp" %>
              </div>
            </div>
            <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>