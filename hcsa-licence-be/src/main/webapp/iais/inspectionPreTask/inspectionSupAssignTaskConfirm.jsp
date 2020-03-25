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
    <input type="hidden" name="InspectionSupSearchSwitchType" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Supervisor Confirm Task</span>
                </h2>
              </div>
              <iais:body >
                <iais:section title="" id = "assign_Task">
                  <iais:row>
                    <iais:field value="Application Number"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspectionTaskPoolListDto.applicationNo}"/></label></p>
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
                    <iais:field value="Inspection Lead"/>
                    <iais:value width="7">
                      <c:forEach var="lead" items="${inspectionTaskPoolListDto.inspectorLeads}">
                        <p><label><c:out value="${lead}"/></label></p>
                      </c:forEach>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Inspector"/>
                    <iais:value width="10">
                      <c:forEach items="${inspectionTaskPoolListDto.inspectorCheck}" var="name">
                        <p><label><c:out value="${name.text}"/></label></p>
                      </c:forEach>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Inspection Type"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspectionTaskPoolListDto.inspectionTypeName}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:action>
                    <a class="back" id="Back" onclick="javascript:doInspectionSupAssignTaskConfirmBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
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
        inspectionSupAssignTaskConfirmSubmit('assign');
    }

    function doInspectionSupAssignTaskConfirmSubmit() {
        inspectionSupAssignTaskConfirmSubmit('success');
    }
    function inspectionSupAssignTaskConfirmSubmit(action){
        $("[name='InspectionSupSearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainConfirmForm');
        mainPoolForm.submit();
    }
</script>

