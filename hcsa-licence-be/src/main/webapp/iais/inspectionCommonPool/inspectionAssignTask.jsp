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
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="inspectionPoolType" value="">
    <input type="hidden" name="actionValue" value="">
    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <h3>
              <span>Assign Task</span>
            </h3>
            <div class="panel panel-default">
              <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                <div class="panel-body">
                  <div class="panel-main-content">
                    <iais:section title="" id = "assign_Task">
                      <iais:row>
                        <iais:field value="Application Number"/>
                        <iais:value width="7">
                          <label><c:out value="${inspecTaskCreAndAssDto.applicationNo}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Application Type"/>
                        <iais:value width="7">
                          <label><iais:code code="${inspecTaskCreAndAssDto.applicationType}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="HCI Code"/>
                        <iais:value width="7">
                          <label><c:out value="${inspecTaskCreAndAssDto.hciCode}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="HCI Name / Address"/>
                        <iais:value width="7">
                          <label><c:out value="${inspecTaskCreAndAssDto.hciName}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Service Name"/>
                        <iais:value width="7">
                          <label><c:out value="${inspecTaskCreAndAssDto.serviceName}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Submission Date"/>
                        <iais:value width="7">
                          <label><fmt:formatDate value='${inspecTaskCreAndAssDto.submitDt}' pattern='dd/MM/yyyy' /></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Inspection Lead"/>
                        <iais:value width="7">
                          <label><c:out value="${inspecTaskCreAndAssDto.inspectionLead}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Inspection Type"/>
                        <iais:value width="7">
                          <label><c:out value="${inspecTaskCreAndAssDto.inspectionTypeName}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:action >
                        <button class="btn btn-lg btn-login-back" style="float:left" type="button" onclick="javascript:doInspectionAssignTaskBack()">Back</button>
                        <c:if test="${'true' eq isInspector}">
                          <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:doInspectionAssignTaskNext()">Next</button>
                        </c:if>
                      </iais:action>
                    </iais:section>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </iais:body>
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

