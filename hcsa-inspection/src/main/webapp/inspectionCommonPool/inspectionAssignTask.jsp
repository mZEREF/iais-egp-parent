<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  String webroot=IaisEGPConstant.FE_CSS_ROOT;
%>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">

  <iais:body >
    <iais:section title="Assign Task" id = "assign_Task">
      <iais:row>
        <iais:field value="Application Number"/>
        <iais:value width="7">
          <label><c:out value="${inspecTaskCreAndAssQueryDto.applicationNo}"/></label>
        </iais:value>
      </iais:row>
      <iais:row>
        <iais:field value="Application Type"/>
        <iais:value width="7">
          <label><c:out value="${inspecTaskCreAndAssQueryDto.applicationType}"/></label>
        </iais:value>
      </iais:row>
      <iais:row>
        <iais:field value="Application Status"/>
        <iais:value width="7">
          <label><c:out value="${inspecTaskCreAndAssQueryDto.applicationStatus}"/></label>
        </iais:value>
      </iais:row>
      <iais:row>
        <iais:field value="HCI Code"/>
        <iais:value width="7">
          <label><c:out value="${inspecTaskCreAndAssQueryDto.hciCode}"/></label>
        </iais:value>
      </iais:row>
      <iais:row>
        <iais:field value="HCI Name / Address"/>
        <iais:value width="7">
          <label><c:out value="${inspecTaskCreAndAssQueryDto.hciName}"/></label>
        </iais:value>
      </iais:row>
      <iais:row>
        <iais:field value="Service Name"/>
        <iais:value width="7">
          <label><c:out value="${inspecTaskCreAndAssQueryDto.serviceName}"/></label>
        </iais:value>
      </iais:row>
      <iais:row>
        <iais:field value="Submission Date"/>
        <iais:value width="7">
          <iais:datePicker id = "submitDt" name = "submitDt" value="${inspecTaskCreAndAssQueryDto.submitDt}"></iais:datePicker>
        </iais:value>
      </iais:row>
      <iais:row>
        <iais:field value="Inspection Lead"/>
        <iais:value width="7">
          <label><c:out value="${inspecTaskCreAndAssQueryDto.inspectionLead}"/></label>
        </iais:value>
      </iais:row>
      <iais:row>
        <iais:field value="Inspector"/>
        <iais:value width="10">
          <input type="checkbox" name="status" value="yc">YiChen</input>
          <input type="checkbox" name="status" value="hc">HuaChong</input>
        </iais:value>
      </iais:row>
      <iais:row>
        <iais:field value="Inspection Type"/>
        <iais:value width="7">
          <label><c:out value="${inspecTaskCreAndAssQueryDto.inspectionType}"/></label>
        </iais:value>
      </iais:row>
      <iais:action >
        <button class="btn btn-lg btn-login-back" type="button" onclick="javascript:doBack()">Back</button>
        <button class="btn btn-lg btn-login-next" type="button" onclick="javascript:doNext()">Next</button>
      </iais:action>
    </iais:section>
  </iais:body>
</form>
<script type="text/javascript">
    function doBack() {
        SOP.Crud.cfxSubmit("inspectionPoolType","back");
    }

    function doNext() {
        SOP.Crud.cfxSubmit("inspectionPoolType","confirm");
    }
</script>

