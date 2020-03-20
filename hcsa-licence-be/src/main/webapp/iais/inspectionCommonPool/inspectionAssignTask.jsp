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
    <input type="hidden" name="inspectionPoolType" value="">
    <input type="hidden" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Assign Task</span>
                </h2>
              </div>
              <iais:body >
                <iais:section title="" id = "assign_Task">
                  <iais:row>
                    <iais:field value="Application Number"/>
                    <iais:value width="7">
                      <p>
                        <a href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?appId=${inspecTaskCreAndAssDto.applicationId}" target="_blank">
                          <u><c:out value="${inspecTaskCreAndAssDto.applicationNo}"/></u>
                        </a>
                      </p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Application Type"/>
                    <iais:value width="7">
                      <p><label><iais:code code="${inspecTaskCreAndAssDto.applicationType}"/></label></p>
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
                      <iais:field value="Inspection Lead"/>
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
                    <a class="back" id="Back" onclick="javascript:doInspectionAssignTaskBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspectionAssignTaskNext()">Next</button>
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
    function doInspectionAssignTaskBack() {
        $("[name='actionValue']").val('back');
        inspectionAssignTaskSubmit('back');
    }

    function doInspectionAssignTaskNext() {
        doValidation();
        $("[name='actionValue']").val('confirm');
        inspectionAssignTaskSubmit('confirm');
    }
    function inspectionAssignTaskSubmit(action){
        $("[name='inspectionPoolType']").val(action);
        var mainPoolForm = document.getElementById('mainAssignForm');
        mainPoolForm.submit();
    }
</script>

