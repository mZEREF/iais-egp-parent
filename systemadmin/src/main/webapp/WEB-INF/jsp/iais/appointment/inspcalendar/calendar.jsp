<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/4/2020
  Time: 11:00 AM
  To change this template use File | Settings | File Templates.
--%>
--%><%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-intranet"/>


<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" id="nonAvailId" name="nonAvailId" value="">
    <div class="bg-title"><h2>Define Non-Availability</h2></div>


      <form class="form-horizontal">
        <c:if test="${isGroupLead == 'Y'}">
        <iais:section title="" id = "demoList">
          <iais:row>
            <iais:field value="Working Group:" required="true"/>
            <iais:value width="18">
              <iais:select name="wrlGrpNameOpt" id="wrlGrpNameOpt"  options = "wrlGrpNameOpt"  value="${param.shortName}" ></iais:select>
              <span id="error_groupName" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </iais:row>

          <div class="clearRow">

              <iais:row>
                <iais:field value="Inspector ID:" required="true"/>
                <iais:value width="18">
                  <input type="text" id="userName" name="userName" value="${param.userName}" maxlength="255" />
                  <span id="error_userName" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
              </iais:row>

            <iais:row>
              <iais:field value="Year:" required="true"/>
              <iais:value width="18">
                <iais:select name="dropYearOpt" id="dropYearOpt"
                             options = "dropYearOpt" firstOption="Please Select" value="${param.dropYearOpt}" ></iais:select>
                <span id="error_year" name="iaisErrorMsg" class="error-msg"></span>
              </iais:value>
            </iais:row>

            <iais:row>
              <iais:field value="Non-Available Date Start:"/>
              <iais:value width="18">
                <iais:datePicker id = "userBlockDateStart" name = "userBlockDateStart"  value="${param.userBlockDateStart}"></iais:datePicker>
                <span id="error_userBlockDateStart" name="iaisErrorMsg" class="error-msg"></span>
              </iais:value>
            </iais:row>

            <iais:row>
              <iais:field value="Non-Available Date End:"/>
              <iais:value width="18">
                <iais:datePicker id = "userBlockDateEnd" name = "userBlockDateEnd"  value="${param.userBlockDateEnd}"></iais:datePicker>
                <span id="error_userBlockDateEnd" name="iaisErrorMsg" class="error-msg"></span>
              </iais:value>
            </iais:row>

            <iais:row>
              <iais:field value="Non-Available Date Description:"/>
              <iais:value width="18">
                <input type="text" name="userBlockDateDescription" maxlength="255" value="${param.userBlockDateDescription}" />
                <span id="error_description" name="iaisErrorMsg" class="error-msg"></span>
              </iais:value>
            </iais:row>

            <iais:row>
              <iais:field value="Recurrence:"/>
              <iais:value width="18">
                <iais:select name="recurrence" id="recurrence"
                             codeCategory="CATE_ID_DATE_TYPE" value="${param.recurrence}" ></iais:select>
              </iais:value>
            </iais:row>

            <iais:row>
              <iais:field value="Recurrence End Date:"/>
              <iais:value width="18">
                <iais:datePicker id = "recurrenceEndDate" name = "recurrenceEndDate"  value="${param.recurrenceEndDate}"></iais:datePicker>
                <span id="error_year" name="iaisErrorMsg" class="error-msg"></span>
              </iais:value>
            </iais:row>

            <iais:action style="text-align:center;">
              <div class="text-right">
                <a class="btn btn-secondary <c:if test="${isGroupLead != 'Y'}">disabled</c:if>"  onclick="javascript:doClear()" href="#">Clear</a>
                <a class="btn btn-primary <c:if test="${isGroupLead != 'Y'}">disabled</c:if>" id="crud_search_button" value="doQuery" href="#">Search</a>
              </div>
            </iais:action>



          </div>
        </iais:section>
      </form>
      <br><br><br>
    </c:if>

    <div>
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
          <div class="row">
            <br><br>
            <div class="col-xs-12">
              <div class="components">
                <iais:pagination  param="inspectorCalenDarQueryAttr" result="inspectorCalenDarResultAttr"/>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="false"   field="index" value="No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="NAME" value="Inspector ID"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="BLOCK_OUT_START" value="Year"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="BLOCK_OUT_START" value="Non-Available Date Start"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="BLOCK_OUT_END" value="Non-Available Date End"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="REMARKS" value="Non-Available Date Description"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="RECURRENCE" value="Recurrence"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="RECURRENCE_END_DATE" value="Recurrence End Date"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty inspectorCalenDarResultAttr.rows}">
                        <tr>
                          <td colspan="6">
                            <iais:message key="ACK018" escape="true"></iais:message>
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="calendar" items="${inspectorCalenDarResultAttr.rows}" varStatus="status">
                          <tr>
                            <td>${status.index + 1}</td>
                            <td><c:out value="${calendar.userName}"></c:out></td>
                            <td><fmt:formatDate value="${calendar.userBlockDateStart}" pattern="yyyy"/></td>
                            <td><fmt:formatDate value="${calendar.userBlockDateStart}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatDate value="${calendar.userBlockDateEnd}" pattern="dd/MMyyyy"/></td>

                            <td><c:out value="${calendar.description}"></c:out></td>
                            <td><iais:code code="${calendar.recurrence}"></iais:code></td>
                            <td><fmt:formatDate value="${calendar.recurrenceEndate}" pattern="dd/MM/yyyy"/></td>
                            <td>

                                <button type="button"   onclick="doDelete('<iais:mask name="nonAvailId" value="${calendar.id}"/>')"  class="btn btn-default btn-sm" >Delete</button>
                                <button type="button"  onclick="doEdit('<iais:mask name="nonAvailId" value="${calendar.id}"/>')" class="btn btn-default btn-sm" >Update</button>
                            </td>
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
        </div>
      </div>

    </div>


      <div class="text-right text-center-mobile">
        <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doAdd();">Create</a>
      </div>


  </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script>

    function doAdd() {
        SOP.Crud.cfxSubmit("mainForm", "add");
    }

    function doEdit(val) {
        $('#nonAvailId').val(val);
        SOP.Crud.cfxSubmit("mainForm", "edit", val);
    }

    function doDelete(val) {
        $('#nonAvailId').val(val);
        SOP.Crud.cfxSubmit("mainForm", "delete", val);
    }
    
    function doClear() {
      $("#userName").val("");
      $("#userBlockDateStart").val("");
      $("#userBlockDateEnd").val("");
      $("#recurrenceEndDate").val("");
      $("#userBlockDateDescription").val("");

      $("#recurrence option[text = 'N/A']").val("selected", "selected");
      $("#recurrence").val("N/A");

      $("#dropYearOpt option[text = 'Please Select']").val("selected", "selected");
      $("#dropYearOpt").val("");

      $(".form-horizontal .clearRow .current").text("Please Select");
    }
    
</script>
