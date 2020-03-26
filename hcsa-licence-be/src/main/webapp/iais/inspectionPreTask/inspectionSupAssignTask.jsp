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
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainAssignForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
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
                  <iais:row>
                    <iais:field value="Application Number"/>
                    <iais:value width="7">
                      <p>
                        <a href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService" target="_blank">
                          <u><c:out value="${inspectionTaskPoolListDto.applicationNo}"/></u>
                        </a>
                      </p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Application Type"/>
                    <iais:value width="7">
                      <p><label><iais:code code="${inspectionTaskPoolListDto.applicationType}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Application Status"/>
                    <iais:value width="7">
                      <p><label><iais:code code="${inspectionTaskPoolListDto.applicationStatus}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="HCI Code"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspectionTaskPoolListDto.hciCode}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="HCI Name / Address"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspectionTaskPoolListDto.hciName}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Service Name"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspectionTaskPoolListDto.serviceName}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Submission Date"/>
                    <iais:value width="7">
                      <p><label><fmt:formatDate value='${inspectionTaskPoolListDto.submitDt}' pattern='dd/MM/yyyy' /></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="${groupRoleFieldDto.groupLeadName}"/>
                    <iais:value width="7">
                      <c:forEach var="lead" items="${inspectionTaskPoolListDto.inspectorLeads}">
                        <p><label><c:out value="${lead}"/></label></p>
                      </c:forEach>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Inspector" required="true"/>
                    <iais:value width="10">
                      <c:if test="${'true' == inspectionTaskPoolListDto.inspectorFlag}">
                        <c:if test="${inspectionTaskPoolListDto.inspectorCheck == null}">
                          <c:forEach items="${inspectionTaskPoolListDto.inspectorOption}" var="name">
                            <p><input type="checkbox" name="inspectorCheck" id="inspectorCheck" value="<c:out value="${name.value}"/>"/>
                              <label><c:out value="${name.text}"/></label></p>
                          </c:forEach>
                        </c:if>
                        <c:if test="${inspectionTaskPoolListDto.inspectorCheck != null}">
                          <c:forEach items="${inspectionTaskPoolListDto.inspectorOption}" var="name">
                            <p><input type="checkbox" name="inspectorCheck" id="inspectorCheck" value="<c:out value="${name.value}"/>"
                                    <c:forEach items="${inspectionTaskPoolListDto.inspectorCheck}" var="checkName">
                                      <c:if test="${name.value eq checkName.value}">checked="checked"</c:if>
                                    </c:forEach>
                            /><label><c:out value="${name.text}"/></label></p>
                          </c:forEach>
                        </c:if>
                        <br><span class="error-msg" name="iaisErrorMsg" id="error_inspectorCheck"></span>
                      </c:if>
                      <c:if test="${'false' == inspectionTaskPoolListDto.inspectorFlag}">
                        <p><label>-</label></p>
                      </c:if>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Inspection Type"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspectionTaskPoolListDto.inspectionTypeName}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:action >
                    <a class="back" id="Back" onclick="javascript:doInspectionSupAssignTaskBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
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

<%@ include file="/include/validation.jsp" %>
<script>
    function doInspectionSupAssignTaskBack() {
        clearErrorMsg();
        $("[name='actionValue']").val('back');
        inspectionSupAssignTaskSubmit('back');
    }

    function doInspectionSupAssignTaskNext() {
        clearErrorMsg();
        doValidation();
        if (getErrorMsg()) {
            dismissWaiting();
        } else {
            $("[name='actionValue']").val('confirm');
            inspectionSupAssignTaskSubmit('confirm');
        }
    }
    function inspectionSupAssignTaskSubmit(action){
        $("[name='InspectionSupSearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainAssignForm');
        mainPoolForm.submit();
    }
</script>

