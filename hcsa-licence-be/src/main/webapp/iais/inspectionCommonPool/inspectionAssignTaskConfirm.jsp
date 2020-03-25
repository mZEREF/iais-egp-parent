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
  <form method="post" id="mainConfirmForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
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
                  <iais:row>
                    <iais:field value="Application Number"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspecTaskCreAndAssDto.applicationNo}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Application Type"/>
                    <iais:value width="7">
                      <p><label><iais:code code="${inspecTaskCreAndAssDto.applicationType}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Application Status"/>
                    <iais:value width="7">
                      <p><label><iais:code code="${inspecTaskCreAndAssDto.applicationStatus}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="HCI Code"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspecTaskCreAndAssDto.hciCode}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="HCI Name / Address"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspecTaskCreAndAssDto.hciName}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Service Name"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspecTaskCreAndAssDto.serviceName}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Submission Date"/>
                    <iais:value width="7">
                      <p><label><fmt:formatDate value='${inspecTaskCreAndAssDto.submitDt}' pattern='dd/MM/yyyy' /></label></p>
                    </iais:value>
                  </iais:row>
                  <c:if test="${inspecTaskCreAndAssDto.inspectionLeads != null}">
                    <iais:row>
                      <iais:field value="${groupRoleFieldDto.groupLeadName}"/>
                      <iais:value width="7">
                        <c:forEach var="lead" items="${inspecTaskCreAndAssDto.inspectionLeads}">
                          <p><label><c:out value="${lead}"/></label></p>
                        </c:forEach>
                      </iais:value>
                    </iais:row>
                  </c:if>
                  <iais:row>
                    <iais:field value="Inspection Type"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspecTaskCreAndAssDto.inspectionTypeName}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:action >
                    <a class="back" id="Back" onclick="javascript:doInspectionAssignTaskConfirmBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
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
        inspectionAssignTaskConfirmSubmit('assign');
    }

    function doInspectionAssignTaskConfirmSubmit() {
        inspectionAssignTaskConfirmSubmit('success');
    }
    function inspectionAssignTaskConfirmSubmit(action){
        $("[name='inspectionPoolType']").val(action);
        var mainPoolForm = document.getElementById('mainConfirmForm');
        mainPoolForm.submit();
    }
</script>

