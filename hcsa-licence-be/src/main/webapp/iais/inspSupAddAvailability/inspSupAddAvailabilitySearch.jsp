<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/1/13
  Time: 13:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot= IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainSupForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="inspSupAddAvailabilityType" value="">
    <input type="hidden" id="avaId" name="avaId" value="">

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
                    <iais:section title="" id = "supPoolList">
                      <iais:row>
                        <iais:field value="Username"/>
                        <iais:value width="18">
                          <c:if test="${curRole eq 'INSPECTOR_LEAD'}">
                            <input type="text" name="userName" value="${avaSearchParam.filters['userName']}" />
                          </c:if>
                          <c:if test="${curRole ne 'INSPECTOR_LEAD'}">
                            <label><c:out value="${userName}"/></label>
                          </c:if>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Year"/>
                        <iais:value width="18">
                          <iais:select name="availabilityYear" options="yearOption" firstOption="Please select" value="${avaSearchParam.filters['availabilityYear']}" ></iais:select>
                        </iais:value>
                      </iais:row>
                      <iais:action style="text-align:center;">
                        <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doInspAvailabilitySearch()">Search</button>
                      </iais:action>
                    </iais:section>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <c:if test="${avaTaskSearchResult != null}">
        <button type="button" class="btn btn-default" style="float:left" onclick="javascript:doInspAvailabilityAdd();">Add</button>
        <button type="button" class="btn btn-default" style="float:right" onclick="javascript:doInspAvailabilityDelete();">Delete</button>
        <iais:pagination  param="avaSearchParam" result="avaTaskSearchResult"/>
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
                    <iais:sortableHeader needSort="false" field="" value="">
                      <input type="checkbox" name="availabilityCheck" id="availabilityCheck" onclick="javascript:doInspAvailabilityCheckAll();" value=""/>
                    </iais:sortableHeader>
                    <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="" value="User ID"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="" value="Year"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="" value="User Blockout Date Start"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="" value="User Blockout Date End"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="" value="User Blockout Date Description"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="" value="Recurrence"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="" value="Recurrence end date"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="" value="Expected Results"></iais:sortableHeader>
                    <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                  </tr>
                  </thead>
                  <tbody>
                  <c:choose>
                    <c:when test="${empty avaTaskSearchResult.rows}">
                      <tr>
                        <td colspan="12">
                          <iais:message key="ACK018" escape="true"></iais:message>
                          <!--No Record!!-->
                        </td>
                      </tr>
                    </c:when>
                    <c:otherwise>
                      <c:forEach var="pool" items="${avaTaskSearchResult.rows}" varStatus="status">
                        <tr>
                          <td>
                            <input type="checkbox" name="availabilityChCheck" onclick="javascript:doInspAvailabilityCheck();" value="<c:out value="${pool.value}"/>"/>
                          </td>
                          <td class="row_no"><c:out value="${(status.index + 1) + (avaSearchParam.pageNo - 1) * avaSearchParam.pageSize}"/></td>
                          <td><c:out value="${pool.applicationNo}"/></td>
                          <td><iais:code code="${pool.applicationType}"/></td>
                          <td><c:out value="${pool.hciCode}"/></td>
                          <td><c:out value="${pool.hciName}"/></td>
                          <td><c:out value="${pool.serviceName}"/></td>
                          <td><c:out value="${pool.hciCode}"/></td>
                          <td><c:out value="${pool.hciCode}"/></td>
                          <td><fmt:formatDate value='${pool.inspectionDate}' pattern='dd/MM/yyyy' /></td>
                          <td><button type="button"  class="btn btn-default" onclick="javascript:doInspAvailabilityEdit('<iais:mask name="avaId" value="${pool.avaId}"/>');">Edit</button></td>
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
      </c:if>
    </iais:body>
  </form>
</div>
<script type="text/javascript">

    function doInspAvailabilityCheck() {

    }

    function doInspAvailabilityCheckAll() {

    }

    function doInspAvailabilityEdit(avaId) {
        $("#avaId").val(avaId);
        inspAvailabilitySubmit('edit');
    }

    function doInspAvailabilityAdd() {
        inspAvailabilitySubmit('add');
    }

    function doInspAvailabilityDelete() {
        inspAvailabilitySubmit('delete');
    }

    function inspAvailabilitySubmit(action){
        $("[name='inspSupAddAvailabilityType']").val(action);
        var mainPoolForm = document.getElementById('mainSupForm');
        mainPoolForm.submit();
    }

    function doInspAvailabilitySearch() {
        inspAvailabilitySubmit('search');
    }

    function jumpToPagechangePage(){
        inspAvailabilitySubmit('page');
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        inspAvailabilitySubmit('sort');
    }
</script>
