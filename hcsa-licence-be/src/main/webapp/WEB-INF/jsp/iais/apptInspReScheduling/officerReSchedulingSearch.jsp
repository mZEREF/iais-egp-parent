<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/6/24
  Time: 10:33
  To change this template use File | Settings | File Templates.
--%>
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


  <form method="post" id="mainSearchForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="mohOfficerReSchedulingType" value="">
    <input type="hidden" id="appCorrelationId" name="appCorrelationId" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <iais:body>
                <iais:section title="" id = "demoList">
                  <div id="reScheduling" class="collapse">
                    <iais:row>
                      <iais:field value="Working Group"/>
                      <iais:value width="18">
                        <div id = "appComPoolSelect">
                          <iais:select name="inspWorkGroup" options="appTypeOption" firstOption="Please Select" value="${cPoolSearchParam.filters['application_type']}" ></iais:select>
                        </div>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Inspector Name"/>
                      <iais:value width="18">
                        <div id = "appComPoolSelect">
                          <iais:select name="inspectorName" options="appTypeOption" firstOption="Please Select" value="${cPoolSearchParam.filters['application_type']}" ></iais:select>
                        </div>
                      </iais:value>
                    </iais:row>
                    <iais:action style="text-align:right;">
                      <button name="clearBtn" class="btn btn-secondary" type="button" onclick="javascript:officerReSchedulingClear()">Clear</button>
                      <button name="searchBtn" class="btn btn-primary" type="button" onclick="javascript:officerReSchedulingSearch()">Search</button>
                    </iais:action>
                  </div>
                </iais:section>
                <iais:pagination  param="cPoolSearchParam" result="cPoolSearchResult"/>
                <div class="table-gp">
                  <table class="table application-group">
                    <thead>
                    <tr align="center">
                      <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="GROUP_NO" value="HCI NAME"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="" value="Inspector(s)"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="COU" value="Date and Time of Inspection"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="COU" value="Type of Task"></iais:sortableHeader>
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
                            <td class="row_no"><c:out value="${(status.index + 1) + (cPoolSearchParam.pageNo - 1) * cPoolSearchParam.pageSize}"/></td>
                            <td><iais:code code="${pool.applicationType}"/></td>
                            <td><c:out value="${pool.submissionType}"/></td>
                            <td><fmt:formatDate value='${pool.submitDt}' pattern='dd/MM/yyyy' /></td>
                            <td><iais:code code="${pool.paymentStatus}"/></td>
                            <td><iais:code code="${pool.paymentStatus}"/></td>
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

    function officerReSchedulingDo(appCorrelationId) {
        showWaiting();
        $("#appCorrelationId").val(appCorrelationId);
        officerReSchedulingSubmit('assign');
    }

    function officerReSchedulingClear() {
        $('#inspWorkGroup option:first').prop('selected', 'selected');
        $('#inspectorName option:first').prop('selected', 'selected');
        $("#appComPoolSelect .current").text("Please Select");
    }

    function officerReSchedulingSubmit(action){
        $("[name='mohOfficerReSchedulingType']").val(action);
        var mainPoolForm = document.getElementById('mainSearchForm');
        mainPoolForm.submit();
    }

    function officerReSchedulingSearch() {
        showWaiting();
        officerReSchedulingSubmit('search');
    }

    function jumpToPagechangePage(){
        showWaiting();
        officerReSchedulingSubmit('page');
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        officerReSchedulingSubmit('sort');
    }
</script>

