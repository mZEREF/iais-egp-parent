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
    <input type="hidden" name="InspectionSupSearchSwitchType" value="">
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
                      <span style="font-size: 16px"><c:out value="${inspectionTaskPoolListDto.applicationNo}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Application Status</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${inspectionTaskPoolListDto.applicationStatus}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">HCI Code</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${inspectionTaskPoolListDto.hciCode}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">HCI Name / Address</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px">${inspectionTaskPoolListDto.hciName}</span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Service Name</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${inspectionTaskPoolListDto.serviceName}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <c:if test="${'INSPECTOR' eq iais_Login_User_Info_Attr.curRoleId || 'INSPECTOR_LEAD' eq iais_Login_User_Info_Attr.curRoleId}">
                    <c:if test="${'common' eq inspectionTaskPoolListDto.editHoursFlag}">
                      <div class="row">
                        <div class="col-md-2">
                          <label style="font-size: 16px">Estimated Effort for Inspection (Man Hours)</label>
                        </div>
                        <div class="col-md-6">
                          <c:if test="${empty inspectionTaskPoolListDto.inspManHours}">
                            <span style="font-size: 16px"><c:out value="-"/></span>
                          </c:if>
                          <c:if test="${!empty inspectionTaskPoolListDto.inspManHours}">
                            <span style="font-size: 16px"><c:out value="${inspectionTaskPoolListDto.inspManHours}"/></span>
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
                      <span style="font-size: 16px"><fmt:formatDate value='${inspectionTaskPoolListDto.submitDt}' pattern='dd/MM/yyyy' /></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px"><c:out value="${groupRoleFieldDto.groupLeadName}"/></label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${inspectionTaskPoolListDto.groupLeadersShow}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px"><c:out value="${groupRoleFieldDto.groupMemBerName}"/></label>
                    </div>
                    <div class="col-md-6">
                      <c:forEach items="${inspectionTaskPoolListDto.inspectorCheck}" var="name">
                        <span style="font-size: 16px"><c:out value="${name.text}"/></span>
                      </c:forEach>
                    </div>
                  </div>
                  <p></p>
                  <c:if test="${'INSPECTOR' eq iais_Login_User_Info_Attr.curRoleId || 'INSPECTOR_LEAD' eq iais_Login_User_Info_Attr.curRoleId}">
                    <div class="row">
                      <div class="col-md-2">
                        <label style="font-size: 16px">Inspection Type</label>
                      </div>
                      <div class="col-md-6">
                        <span style="font-size: 16px"><c:out value="${inspectionTaskPoolListDto.inspectionTypeName}"/></span>
                      </div>
                    </div>
                    <p></p>
                  </c:if>
                  <iais:action>
                    <a href="#" class="back" id="Back" onclick="javascript:doInspectionSupAssignTaskConfirmBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspectionSupAssignTaskConfirmSubmit()">Submit</button>
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
    function doInspectionSupAssignTaskConfirmBack() {
        showWaiting();
        inspectionSupAssignTaskConfirmSubmit('assign');
    }

    function doInspectionSupAssignTaskConfirmSubmit() {
        showWaiting();
        inspectionSupAssignTaskConfirmSubmit('success');
    }
    function inspectionSupAssignTaskConfirmSubmit(action){
        $("[name='InspectionSupSearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainConfirmForm');
        mainPoolForm.submit();
    }
</script>

