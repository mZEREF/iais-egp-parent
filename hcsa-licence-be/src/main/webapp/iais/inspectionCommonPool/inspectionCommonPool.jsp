<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
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
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="inspectionPoolType" value="">
  <input type="hidden" id="applicationNo" name="applicationNo" value="">

  <iais:body >
    <div class="container">
      <div class="col-xs-12">
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <h3>
            <span>Search Criteria</span>
          </h3>
          <div class="panel panel-default">
            <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
              <div class="panel-body">
                <div class="panel-main-content">
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
                        <iais:select name="application_type" options="appTypeOption" firstOption="Please select" value="${cPoolSearchParam.filters['application_type']}" ></iais:select>
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
                        <input type="text" name="hci_address" value="${cPoolSearchParam.filters['blk_no']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Submission Date"/>
                      <iais:value width="18">
                        <iais:datePicker id = "sub_date" name = "sub_date" value="${cPoolSearchParam.filters['sub_date']}"></iais:datePicker>
                      </iais:value>
                    </iais:row>
                    <iais:action style="text-align:center;">
                      <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doInspectionCommonPoolSearch()">Search</button>
                      <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:doInspectionCommonPoolClear()">Clear</button>
                    </iais:action>
                  </iais:section>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <iais:pagination  param="cPoolSearchParam" result="cPoolSearchResult"/>
      <div class="container">
        <div class="col-xs-12">
          <div class="components">
            <h3>
              <span>Search Result</span>
            </h3>
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
                        <td colspan="6">
                          <iais:message key="Null!" escape="true"></iais:message>
                          <!--No Record!!-->
                        </td>
                      </tr>
                    </c:when>
                    <c:otherwise>
                      <c:forEach var="pool" items="${cPoolSearchResult.rows}" varStatus="status">
                        <tr>
                          <td class="row_no"><c:out value="${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"/></td>
                          <td><c:out value="${pool.applicationNo}"/></td>
                          <td><c:out value="${pool.applicationType}"/></td>
                          <td><c:out value="${pool.hciCode}"/></td>
                          <td><c:out value="${pool.hciName}"/></td>
                          <td><c:out value="${pool.submitDt}"/></td>
                          <td><button type="button"  class="btn btn-default" onclick="javascript:doInspectionCommonPoolAssign('<iais:mask name="applicationNo" value="${pool.applicationNo}"/>');">Assign</button></td>
                        </tr>
                      </c:forEach>
                    </c:otherwise>
                  </c:choose>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
  </iais:body>
</form>
</div>
<script type="text/javascript">

    function doInspectionCommonPoolAssign(applicationNo) {
        $("#applicationNo").val(applicationNo);
        submit('assign');
    }

    function doInspectionCommonPoolClear() {
        $('input[name="application_no"]').val("");
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $('input[name="hci_code"]').val("");
        $('input[name="hci_name"]').val("");
        $('input[name="service_name"]').val("");
        $('input[name="sub_date"]').val("");
    }
    function submit(action){
        $("[name='inspectionPoolType']").val(action);
        var mainPoolForm = document.getElementById('mainPoolForm');
        mainPoolForm.submit();
    }
    function doInspectionCommonPoolSearch() {
        submit('search');
    }
</script>