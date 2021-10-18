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
  <form method="post" id="mainAssignForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="InspectionSupSearchSwitchType" value="">
    <input type="hidden" name="actionValue" value="">
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
                      <a style="font-size: 16px" href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService" target="_blank">
                        <u><c:out value="${inspectionTaskPoolListDto.applicationNo}"/></u>
                      </a>
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
                        <div class="col-md-2">
                          <input type="text" maxlength="3" style="margin-bottom: 0px;" name="inspManHours" value="${inspectionTaskPoolListDto.inspManHours}"/>
                          <span class="error-msg" name="iaisErrorMsg" id="error_inspManHours"></span><p></p>
                        </div>
                      </div>
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
                      <label style="font-size: 16px"><c:out value="${groupRoleFieldDto.groupMemBerName}"/><span style="color: red"> *</span></label>
                    </div>
                    <div class="col-md-6">
                      <c:if test="${'APST029' eq applicationViewDto.applicationDto.status}">
                        <c:if test="${'true' == inspectionTaskPoolListDto.inspectorFlag}">
                          <c:if test="${inspectionTaskPoolListDto.inspectorCheck == null}">
                            <c:forEach items="${inspectionTaskPoolListDto.inspectorOption}" var="name">
                              <input type="checkbox" name="inspectorCheck" id="inspectorCheck" value="<c:out value="${name.value}"/>"/>
                              <span style="font-size: 16px"><c:out value="${name.text}"/></span><p></p>
                            </c:forEach>
                          </c:if>
                          <c:if test="${inspectionTaskPoolListDto.inspectorCheck != null}">
                            <c:forEach items="${inspectionTaskPoolListDto.inspectorOption}" var="name">
                              <input type="checkbox" name="inspectorCheck" id="inspectorCheck" value="<c:out value="${name.value}"/>"
                                      <c:forEach items="${inspectionTaskPoolListDto.inspectorCheck}" var="checkName">
                                        <c:if test="${name.value eq checkName.value}">checked="checked"</c:if>
                                      </c:forEach>
                              /><span style="font-size: 16px"><c:out value="${name.text}"/></span><p></p>
                            </c:forEach>
                          </c:if>
                          <span class="error-msg" name="iaisErrorMsg" id="error_inspectorCheck"></span><p></p>
                        </c:if>
                      </c:if>
                      <c:if test="${'APST029' ne applicationViewDto.applicationDto.status}">
                        <c:if test="${'true' == inspectionTaskPoolListDto.inspectorFlag}">
                          <c:if test="${inspectionTaskPoolListDto.inspectorCheck == null}">
                            <c:forEach items="${inspectionTaskPoolListDto.inspectorOption}" var="name">
                              <input type="radio" name="inspectorCheck" id="inspectorCheck" value="<c:out value="${name.value}"/>"/>
                              <span style="font-size: 16px"><c:out value="${name.text}"/></span><p></p>
                            </c:forEach>
                          </c:if>
                          <c:if test="${inspectionTaskPoolListDto.inspectorCheck != null}">
                            <c:forEach items="${inspectionTaskPoolListDto.inspectorOption}" var="name">
                              <input type="radio" name="inspectorCheck" id="inspectorCheck" value="<c:out value="${name.value}"/>"
                                      <c:forEach items="${inspectionTaskPoolListDto.inspectorCheck}" var="checkName">
                                        <c:if test="${name.value eq checkName.value}">checked="checked"</c:if>
                                      </c:forEach>
                              /><span style="font-size: 16px"><c:out value="${name.text}"/></span><p></p>
                            </c:forEach>
                          </c:if>
                          <span class="error-msg" name="iaisErrorMsg" id="error_inspectorCheck"></span><p></p>
                        </c:if>
                      </c:if>
                      <c:if test="${'false' == inspectionTaskPoolListDto.inspectorFlag}">
                        <span style="font-size: 16px">-</span><p></p>
                      </c:if>
                    </div>
                  </div>
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
                  <iais:action >
                    <a href="#" class="back" id="Back" onclick="javascript:doInspectionSupAssignTaskBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                    <c:if test="${'true' == inspectionTaskPoolListDto.inspectorFlag}">
                      <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspectionSupAssignTaskNext()">Next</button>
                    </c:if>
                    <c:if test="${'true' != inspectionTaskPoolListDto.inspectorFlag}">
                      <button class="btn btn-primary disabled" style="float:right" type="button" disabled>Next</button>
                    </c:if>
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

<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script>
    function doInspectionSupAssignTaskBack() {
        clearErrorMsg();
        showWaiting();
        $("[name='actionValue']").val('back');
        inspectionSupAssignTaskSubmit('back');
    }

    function doInspectionSupAssignTaskNext() {
        clearErrorMsg();
        showWaiting();
        $("[name='actionValue']").val('confirm');
        inspectionSupAssignTaskSubmit('confirm');
    }

    function inspectionSupAssignTaskSubmit(action){
        $("[name='InspectionSupSearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainAssignForm');
        mainPoolForm.submit();
    }
</script>

