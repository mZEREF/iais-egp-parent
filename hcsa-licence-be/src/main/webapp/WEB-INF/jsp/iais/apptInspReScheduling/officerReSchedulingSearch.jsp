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
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>

<style type="text/css">
  .btn.btn-md {
    font-size: .986rem;
    font-weight: 600;
    padding: 10px 25px;
    text-transform: uppercase;
    border-radius: 30px;
  }
</style>

<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainSearchForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="mohOfficerReSchedulingType" value="">
    <input type="hidden" id="inspectorCheck" name="inspectorCheck" value="${reschedulingOfficerDto.inspectorCheck}">
    <input type="hidden" id="workGroupCheck" name="workGroupCheck" value="${reschedulingOfficerDto.workGroupCheck}">
    <input type="hidden" id="applicationNo" name="applicationNo" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Scheduled Appointments</span>
                </h2>
              </div>
              <iais:body>
                <iais:section title="" id = "demoList">
                  <iais:row>
                    <iais:field value="Working Group"/>
                    <iais:value width="18">
                      <iais:select name="reSchInspWorkGroup" onchange="chooseWorkGroup()" options="workGroupOption" value="${reschedulingOfficerDto.workGroupCheck}" ></iais:select>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Inspector Name"/>
                    <iais:value width="18">
                      <c:forEach var="workGroupNo" items="${workGroupNos}">
                        <div id="workGroupNo${workGroupNo}">
                          <iais:select name="inspectorName${workGroupNo}" options="inspectorOption${workGroupNo}" firstOption="Please Select" needSort="true"></iais:select>
                        </div>
                      </c:forEach>
                    </iais:value>
                  </iais:row>
                  <iais:action style="text-align:right;">
                    <button name="searchBtn" class="btn btn-primary" type="button" onclick="javascript:officerReSchedulingSearch()">Search</button>
                  </iais:action>
                </iais:section>
                <iais:pagination  param="inspReSchSearchParam" result="inspReSchSearchResult"/>
                <div class="table-gp">
                  <table aria-describedby="" class="table application-group">
                    <thead>
                      <tr >
                        <th scope="col" style="display: none"></th>
                                <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true" field="HCI_NAME" value="HCI Name"></iais:sortableHeader>
                        <iais:sortableHeader needSort="false" field="" value="Inspector(s)"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true" field="RECOM_IN_DATE" value="Date and Time of Inspection"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true" field="TASK_TYPE" value="Type of Task"></iais:sortableHeader>
                        <iais:sortableHeader needSort="false" field="" value="Service(s)"></iais:sortableHeader>
                        <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                      </tr>
                    </thead>
                    <tbody>
                      <c:choose>
                        <c:when test="${empty inspReSchSearchResult.rows}">
                          <tr>
                            <td colspan="7">
                              <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                            </td>
                          </tr>
                        </c:when>
                        <c:otherwise>
                          <c:forEach var="reschSearch" items="${inspReSchSearchResult.rows}" varStatus="status">
                            <tr>
                              <td class="row_no"><c:out value="${(status.index + 1) + (inspReSchSearchParam.pageNo - 1) * inspReSchSearchParam.pageSize}"/></td>
                              <td><c:out value="${reschSearch.hciName}"/></td>
                              <c:if test="${empty reschSearch.inspectors}">
                                <td><c:out value="-"/></td>
                              </c:if>
                              <c:if test="${not empty reschSearch.inspectors}">
                                <td>
                                  <c:forEach var="inspector" items="${reschSearch.inspectors}">
                                    <c:out value="${inspector}"/><br>
                                  </c:forEach>
                                </td>
                              </c:if>
                              <td><fmt:formatDate value = "${reschSearch.inspectionDate}" pattern = "dd/MM/yyyy HH:mm:ss"/></td>
                              <td><c:out value="${reschSearch.taskType}"/></td>
                              <c:if test="${empty reschSearch.serviceNames}">
                                <td><c:out value="-"/></td>
                              </c:if>
                              <c:if test="${not empty reschSearch.serviceNames}">
                                <td>
                                  <c:forEach var="service" items="${reschSearch.serviceNames}">
                                    <c:out value="${service}"/><br>
                                  </c:forEach>
                                </td>
                              </c:if>
                              <td>
                                <button class="btn btn-secondary btn-md" type="button" onclick="javascript:officerReSchedulingAssign('<iais:mask name="applicationNo" value="${reschSearch.appNo}"/>', '${reschSearch.taskType}')">Re-schedule</button>
                              </td>
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
    $(document).ready(function() {
        var inspectorCheck = $("#inspectorCheck").val();
        var workGroupCheck = $("#workGroupCheck").val();
        var numbers = $("#inspectorName" + workGroupCheck).find("option");
        for (var j = 1; j < numbers.length; j++) {
            if ($(numbers[j]).val() == inspectorCheck) {
                $(numbers[j]).attr("selected", "selected");
                var inspName = $(numbers[j]).text();
                $("#workGroupNo" + workGroupCheck + " .current").text(inspName);
            }
        }
        $("#reSchInspWorkGroup option").each(function(){
            var value = $(this).val();
            $("#workGroupNo" + value).addClass("hidden");
        });
        $("#workGroupNo" + workGroupCheck).removeClass("hidden");
    });

    $("#reSchInspWorkGroup").change(function reSchInspWorkGroupCheck() {
        var workGroupCheck = $("#reSchInspWorkGroup").val();
        $("#reSchInspWorkGroup option").each(function(){
            var value = $(this).val();
            $("#workGroupNo" + value).addClass("hidden");
        });
        $("#workGroupNo" + workGroupCheck).removeClass("hidden");
    });

    function officerReSchedulingSubmit(action){
        $("[name='mohOfficerReSchedulingType']").val(action);
        var mainPoolForm = document.getElementById('mainSearchForm');
        mainPoolForm.submit();
    }

    function officerReSchedulingAssign(applicationNo, taskType) {
        showWaiting();
        $("#applicationNo").val(applicationNo);
        if("Inspection" == taskType){
            $("#actionValue").val('assign');
            officerReSchedulingSubmit('assign');
        } else if ("Audit" == taskType){
            $("#actionValue").val('audit');
            officerReSchedulingSubmit('audit');
        }
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

    function chooseWorkGroup() {
      showWaiting();
      officerReSchedulingSubmit('search');

    }
</script>

