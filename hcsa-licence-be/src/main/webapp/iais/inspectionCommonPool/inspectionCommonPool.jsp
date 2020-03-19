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


<form method="post" id="mainPoolForm" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="inspectionPoolType" value="">
  <input type="hidden" id="appCorrelationId" name="appCorrelationId" value="">
  <div class="main-content">
    <div class="row">
      <div class="col-lg-12 col-xs-12">
        <div class="center-content">
          <div class="intranet-content">
            <div class="bg-title">
              <h2>
                <span>Common Pool Search Criteria</span>
              </h2>
            </div>
            <iais:body >
              <iais:section title="" id = "demoList">
                  <iais:row>
                    <iais:field value="Application No."/>
                    <iais:value width="18">
                      <input type="text" name="application_no" value="${cPoolSearchParam.filters['application_no']}" />
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Application Type"/>
                    <iais:value width="18">
                      <div id = "appComPoolSelect">
                        <iais:select name="application_type" options="appTypeOption" firstOption="Please Select" value="${cPoolSearchParam.filters['application_type']}" ></iais:select>
                      </div>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="HCI Code"/>
                    <iais:value width="18">
                      <input type="text" name="hci_code" value="${cPoolSearchParam.filters['hci_code']}" />
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="HCI Name"/>
                    <iais:value width="18">
                      <input type="text" name="hci_name" value="${cPoolSearchParam.filters['hci_name']}" />
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="HCI Address"/>
                    <iais:value width="18">
                      <input type="text" name="hci_address" value="${cPoolSearchParam.filters['hci_address']}" />
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Submission Date"/>
                    <iais:value width="18">
                      <iais:datePicker id = "sub_date" name = "sub_date" value="${sub_date2}"></iais:datePicker>
                    </iais:value>
                  </iais:row>
                  <iais:action style="text-align:right;">
                    <button class="btn btn-primary" type="button" onclick="javascript:doInspectionCommonPoolSearch()">Search</button>
                    <button class="btn btn-secondary" type="button" onclick="javascript:doInspectionCommonPoolClear()">Clear</button>
                  </iais:action>
                </iais:section>
              <iais:pagination  param="cPoolSearchParam" result="cPoolSearchResult"/>
              <div class="table-gp">
                <table class="table">
                  <thead>
                    <tr align="center">
                      <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"  field="APPLICATION_NO" value="Application No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"  field="APP_TYPE" value="Application Type"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"  field="HCI_CODE" value="HCI Code"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"  field="HCI_NAME" value="HCI Name / Address"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"  field="SUBMIT_DT" value="Submission Date"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                    </tr>
                  </thead>
                  <tbody>
                    <c:choose>
                      <c:when test="${empty cPoolSearchResult.rows}">
                        <tr>
                          <td colspan="7">
                            <iais:message key="ACK018" escape="true"></iais:message>
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="pool" items="${cPoolSearchResult.rows}" varStatus="status">
                          <tr>
                            <td class="row_no"><c:out value="${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"/></td>
                            <td><c:out value="${pool.applicationNo}"/></td>
                            <td><iais:code code="${pool.applicationType}"/></td>
                            <td><c:out value="${pool.hciCode}"/></td>
                            <td><c:out value="${pool.hciName}"/></td>
                            <td><fmt:formatDate value='${pool.submitDt}' pattern='dd/MM/yyyy' /></td>
                            <td><button type="button"  class="btn btn-default" onclick="javascript:doInspectionCommonPoolAssign('<iais:mask name="appCorrelationId" value="${pool.id}"/>');">Assign</button></td>
                          </tr>
                        </c:forEach>
                      </c:otherwise>
                    </c:choose>
                  </tbody>
                </table>
              </div>
            </iais:body>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>
</div>
<script type="text/javascript">

    function doInspectionCommonPoolAssign(appCorrelationId) {
        $("#appCorrelationId").val(appCorrelationId);
        inspectionCommonPoolSubmit('assign');
    }

    function doInspectionCommonPoolClear() {
        $('input[name="application_no"]').val("");
        $("#application_type option[text = 'Please Select']").val("selected", "selected");
        $("#appComPoolSelect .current").text("Please Select");
        $('input[name="hci_code"]').val("");
        $('input[name="hci_name"]').val("");
        $('input[name="hci_address"]').val("");
        $('input[name="service_name"]').val("");
        $('input[name="sub_date"]').val("");
    }
    function inspectionCommonPoolSubmit(action){
        $("[name='inspectionPoolType']").val(action);
        var mainPoolForm = document.getElementById('mainPoolForm');
        mainPoolForm.submit();
    }
    function doInspectionCommonPoolSearch() {
        inspectionCommonPoolSubmit('search');
    }
    function jumpToPagechangePage(){
        inspectionCommonPoolSubmit('page');
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        inspectionCommonPoolSubmit('sort');
    }
</script>

