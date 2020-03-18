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
    <%@ include file="/include/formHidden.jsp" %>
    <div class="bg-title"><h2>Define Non-Availability</h2></div>

    <form class="form-horizontal">


          <iais:section title="" id = "demoList">
             <iais:row>
                <iais:field value="Working Group:" required="true"/>
                <iais:value width="18">
                  <iais:select name="wrlGrpNameOpt" id="wrlGrpNameOpt"  options = "wrlGrpNameOpt" firstOption="Please Select" value="${shortName}" ></iais:select>
                  <span id="error_groupName" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
              </iais:row>

            <div class="clearRow">

              <c:if test="${isGroupLead == 'Y'}">
              <iais:row>
                <iais:field value="Inspector ID:"/>
                <iais:value width="18">
                  <input type="text" name="userName" value="${userName}" maxlength="255" />
                  <span id="error_userName" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
              </iais:row>
              </c:if>

            <iais:row>
              <iais:field value="Year:" required="true"/>
              <iais:value width="18">
                <iais:select name="dropYearOpt" id="dropYearOpt"
                             options = "dropYearOpt" firstOption="Please Select" value="${dropYear}" ></iais:select>
                <span id="error_year" name="iaisErrorMsg" class="error-msg"></span>
              </iais:value>
            </iais:row>

            <iais:row>
              <iais:field value="Non-Available Date Start:"/>
              <iais:value width="18">
                <iais:datePicker id = "userBlockDateStart" name = "userBlockDateStart"  value="${userBlockDateStart}"></iais:datePicker>
                <span id="error_userBlockDateStart" name="iaisErrorMsg" class="error-msg"></span>
              </iais:value>
            </iais:row>

            <iais:row>
              <iais:field value="Non-Available Date End:"/>
              <iais:value width="18">
                <iais:datePicker id = "userBlockDateEnd" name = "userBlockDateEnd"  value="${userBlockDateEnd}"></iais:datePicker>
                <span id="error_userBlockDateEnd" name="iaisErrorMsg" class="error-msg"></span>
              </iais:value>
            </iais:row>

            <iais:row>
              <iais:field value="Non-Available Date Description:"/>
              <iais:value width="18">
                <input type="text" name="userBlockDateDescription" maxlength="255" value="${userBlockDateDescription}" />
                <span id="error_description" name="iaisErrorMsg" class="error-msg"></span>
              </iais:value>
            </iais:row>

            <iais:row>
              <iais:field value="Recurrence:"/>
              <iais:value width="18">
                <iais:select name="recurrence" id="recurrence"
                             options = "recurrenceOpt" firstOption="Please Select" value="${recurrence}" ></iais:select>
                <%--<span id="error_year" name="iaisErrorMsg" class="error-msg"></span>--%>
              </iais:value>
            </iais:row>

            <iais:action style="text-align:center;">
              <div class="text-right">
                <a class="btn btn-secondary" onclick="javascript:doClear()" href="#">Clear</a>
                <a class="btn btn-primary" id="crud_search_button" value="doQuery" href="#">Search</a>
              </div>
            </iais:action>

            </div>
          </iais:section>
    </form>


    <br><br><br>
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
                      <iais:sortableHeader needSort="true"   field="YEAR" value="Year"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="BLOCK_OUT_START" value="User Block Date Start"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="BLOCK_OUT_END" value="User Block Date End"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="REMARKS" value="User Block Date Description"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty inspectorCalenDarResultAttr.rows}">
                        <tr>
                          <td colspan="6">
                            No Record!!
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="calendar" items="${inspectorCalenDarResultAttr.rows}" varStatus="status">
                          <tr>
                            <td>${status.index + 1}</td>
                              <%--<td>
                                 2020&lt;%&ndash;<fmt:formatDate value="${nonwkrDay.startDate}" pattern="yyyy"></fmt:formatDate>&ndash;%&gt;
                              </td>--%>
                            <td>${calendar.userName}</td>
                            <td>${calendar.year}</td>

                            <td><fmt:formatDate value="${calendar.userBlockDateStart}" pattern="MM/dd/yyyy"/></td>
                            <td><fmt:formatDate value="${calendar.userBlockDateEnd}" pattern="MM/dd/yyyy"/></td>

                            <td>${calendar.description}</td>
                            <td>
                              <input type="hidden" id="nonAvailId" name="nonAvailId" value="">
                                <button type="button"   onclick="doDelete('<iais:mask name="nonAvailId" value="${calendar.id}"/>')"  class="btn btn-default btn-sm" >Delete</button>
                                <button type="button"  onclick="doEdit('<iais:mask name="nonAvailId" value="${calendar.id}"/>')" class="btn btn-default btn-sm" >Update</button>
                            </td>
                          </tr>
                        </c:forEach>


                      </c:otherwise>
                    </c:choose>
                    </tbody>
                  </table>
                  <div class="table-footnote">
                    <div class="row">
                      <div class="col-xs-6 col-md-8 text-right">
                        <br><br>


                      </div>
                    </div>
                  </div>


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
<%@include file="/include/validation.jsp"%>
<%@include file="/include/utils.jsp"%>
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
      $("#userBlockDateDescription").val("");

      $("#recurrence option[text = 'N/A']").val("selected", "selected");
      $("#recurrence").val("N/A");

      $("#dropYearOpt option[text = 'Please Select']").val("selected", "selected");
      $("#dropYearOpt").val("");

      $(".form-horizontal .clearRow .current").text("Please Select");
    }
    
</script>
