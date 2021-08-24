<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainConfirmForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="inspectionPoolType" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Task Details</span>
                </h2>
              </div>
              <iais:body >
                <iais:section title="" id = "assign_Task">
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Application Number</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px">
                        <c:out value="${inspecTaskCreAndAssDto.applicationNo}"/>
                      </span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Application Type</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${inspecTaskCreAndAssDto.applicationType}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Application Status</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="Pending Task Assignment"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">HCI Code</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${inspecTaskCreAndAssDto.hciCode}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">HCI Name / Address</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px">${inspecTaskCreAndAssDto.hciName}</span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Service Name</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${inspecTaskCreAndAssDto.serviceName}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <c:if test="${inspecTaskCreAndAssDto.fastTrackFlag}">
                    <div class="row">
                      <div class="col-md-2">
                        <label style="font-size: 16px">Fast Tracking?</label>
                      </div>
                      <div class="col-md-6">
                        <c:if test="${inspecTaskCreAndAssDto.fastTrackCheckFlag}">
                          <input type="checkbox" disabled value="true" name="fastTrackCommon" checked="checked"/>
                        </c:if>
                        <c:if test="${!inspecTaskCreAndAssDto.fastTrackCheckFlag}">
                          <input disabled type="checkbox" value="true" name="fastTrackCommon"
                                 <c:if test="${'true' eq inspecTaskCreAndAssDto.fastTrackCheck}">checked="checked"</c:if>/>
                        </c:if>
                      </div>
                    </div>
                    <p></p>
                  </c:if>
                  <c:if test="${'INSPECTOR' eq iais_Login_User_Info_Attr.curRoleId || 'INSPECTOR_LEAD' eq iais_Login_User_Info_Attr.curRoleId}">
                    <c:if test="${'common' eq inspecTaskCreAndAssDto.editHoursFlag}">
                      <div class="row">
                        <div class="col-md-2">
                          <label style="font-size: 16px">Estimated Effort for Inspection (Man Hours)</label>
                        </div>
                        <div class="col-md-6">
                          <c:if test="${!empty inspecTaskCreAndAssDto.inspManHours}">
                            <span style="font-size: 16px"><c:out value="${inspecTaskCreAndAssDto.inspManHours}"/></span>
                          </c:if>
                          <c:if test="${empty inspecTaskCreAndAssDto.inspManHours}">
                            <span style="font-size: 16px"><c:out value="-"/></span>
                          </c:if>
                        </div>
                      </div>
                      <p></p>
                    </c:if>
                  </c:if>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Submission Date</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><fmt:formatDate value='${inspecTaskCreAndAssDto.submitDt}' pattern='dd/MM/yyyy' /></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px"><c:out value="${groupRoleFieldDto.groupLeadName}"/></label>
                    </div>
                    <div class="col-md-6">
                      <c:if test="${!empty inspecTaskCreAndAssDto.inspectionLeads}">
                        <span style="font-size: 16px"><c:out value="${inspecTaskCreAndAssDto.groupLeadersShow}"/></span>
                      </c:if>
                      <c:if test="${empty inspecTaskCreAndAssDto.inspectionLeads}">
                        <span style="font-size: 16px"><c:out value="-"/></span>
                      </c:if>
                    </div>
                  </div>
                  <p></p>
                  <c:if test="${'INSPECTOR' eq iais_Login_User_Info_Attr.curRoleId || 'INSPECTOR_LEAD' eq iais_Login_User_Info_Attr.curRoleId}">
                    <div class="row">
                      <div class="col-md-2">
                        <label style="font-size: 16px">Inspection Type</label>
                      </div>
                      <div class="col-md-6">
                        <span style="font-size: 16px"><c:out value="${inspecTaskCreAndAssDto.inspectionTypeName}"/></span>
                      </div>
                    </div>
                    <p></p>
                  </c:if>
                  <iais:action >
                    <a href="#" class="back" id="Back" onclick="javascript:doInspectionAssignTaskConfirmBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspectionAssignTaskConfirmSubmit()">Submit</button>
                  </iais:action>
                </iais:section>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<script type="text/javascript">
    function doInspectionAssignTaskConfirmBack() {
        showWaiting();
        inspectionAssignTaskConfirmSubmit('assign');
    }

    function doInspectionAssignTaskConfirmSubmit() {
        showWaiting();
        inspectionAssignTaskConfirmSubmit('success');
    }
    function inspectionAssignTaskConfirmSubmit(action){
        $("[name='inspectionPoolType']").val(action);
        var mainPoolForm = document.getElementById('mainConfirmForm');
        mainPoolForm.submit();
    }
</script>

