<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/28/2019
  Time: 2:21 PM
  To change this template use File | Settings | File Templates.
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
    <input type="hidden" name="currentValidateId" value="">
    <div class="bg-title"><h2>Blacked Out Dates Management</h2></div>
<c:choose>
  <c:when test="${empty wrlGrpNameOpt}">
    Your current group cannot be found or you are not an inspection lead !
  </c:when>
  <c:otherwise>


    <iais:section title="" id = "demoList">
      <iais:row>
        <iais:field value="Working Group" required="true"/>
        <iais:value width="18">
          <iais:select name="wrlGrpNameOpt" id="wrlGrpNameOpt"  options = "wrlGrpNameOpt" value="${wrlGrpNameOpt}" ></iais:select>
        </iais:value>
      </iais:row>
      <div class="clearRow">
        <iais:row>
          <iais:field value="Year"/>
          <iais:value width="18">
            <iais:select name="dropYearOpt" id="dropYearOpt"  options = "dropYearOpt" firstOption="Please Select" value="${param.dropYearOpt}" ></iais:select>
          </iais:value>
        </iais:row>

        <iais:row>
          <iais:field value="Blacked Out Date From"/>
          <iais:value width="18">
            <iais:datePicker id = "startDate" name = "startDate"  value="${startDate}"></iais:datePicker>
            <span class="error-msg" id="error_inspectionStartDate" name="iaisErrorMsg"></span>
          </iais:value>
        </iais:row>

        <iais:row>
          <iais:field value="Blacked Out Date To"/>
          <iais:value width="18">
            <iais:datePicker id = "endDate" name = "endDate"  value="${endDate}"></iais:datePicker>
            <span class="error-msg" id="error_inspectionEndDate" name="iaisErrorMsg"></span>
          </iais:value>
        </iais:row>

        <iais:row>
          <iais:field value="Blacked Out Date Description"/>
          <iais:value width="18">
            <input type="text" id="blacked_out_desc" maxlength="255" name="desc"  value="${desc}">
            <span class="error-msg" id="error_desc" name="iaisErrorMsg"></span>
          </iais:value>
        </iais:row>

        <iais:row>
          <iais:field value="Status"/>
          <iais:value width="18">
            <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                         firstOption="Please Select" filterValue="CMSTAT002,CMSTAT004" value="${status}"></iais:select>
          </iais:value>
        </iais:row>
      </div>

    </iais:section>







    <div class="application-tab-footer">
      <div class="row">
        <div class="col-xs-12 col-md-11">
          <div class="text-right">
            <a class="btn btn-secondary" onclick="doClear()"  href="#">Clear</a>
            <a class="btn btn-primary" id="crud_search_button" value="doSearch" href="#">Search</a>
          </div>
        </div>
      </div>
    </div>

    <div>
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
          <div class="row">
            <br><br>
            <div class="col-xs-12">
              <div class="components">
                <iais:pagination  param="blackedOutDateQueryAttr" result="blackedOutDateResultAttr"/>
                <div class="table-gp">
                  <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                      <th scope="col" style="display: none"></th>
                      <iais:sortableHeader needSort="false"    field="index" value="No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"    field="year" value="Year"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="START_DATE" value="Blacked Out Date Start"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="END_DATE" value="Blacked Out Date End"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="[DESC]" value="Blacked Out Date Description"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"    field="status" value="Status"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"    field="action" value="Action"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty blackedOutDateResultAttr.rows}">
                          <td colspan="6">
                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                          </td>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="blackDateAttr" items="${blackedOutDateResultAttr.rows}" varStatus="status">
                          <tr>
                            <td>${status.index + 1}</td>
                            <td>
                              <fmt:formatDate value="${blackDateAttr.startDate}" pattern="yyyy"></fmt:formatDate>
                            </td>

                            <td><fmt:formatDate value="${blackDateAttr.startDate}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatDate value="${blackDateAttr.endDate}" pattern="dd/MM/yyyy"/></td>

                            <td>${blackDateAttr.desc}</td>
                            <td><iais:code code="${blackDateAttr.status}"></iais:code></td>
                            <td>
                              <input type="hidden" id="blackDateId" name="blackDateId" value="">
                              <div class="modal fade" id="DeleteTemplateModal${status.index}" tabindex="-1" role="dialog" aria-labelledby="DeleteTemplateModal">
                                <div class="modal-dialog modal-dialog-centered" role="document">
                                  <div class="modal-content">
<%--                                    <div class="modal-header">--%>
<%--                                      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                                      <div class="modal-title" id="gridSystemModalLabel" style="font-size:2rem;">Confirmation Box</div>--%>
<%--                                    </div>--%>
                                    <div class="modal-body">
                                      <div class="row">
                                        <div class="col-md-12"><span style="font-size: 2rem">Are you sure you want to delete?</span></div>
                                      </div>
                                    </div>
                                    <div class="modal-footer">
                                      <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>

                                      <button type="button" class="btn btn-primary" onclick="doDelete('<iais:mask name="blackDateId" value="${blackDateAttr.id}"/>')" >Confirm</button>
                                    </div>
                                  </div>
                                </div>
                              </div>

                              <button type="button"  id="deleteBtnId" name="blackDateId" class="btn btn-default btn-sm" data-toggle="modal" data-target="#DeleteTemplateModal${status.index}" >Delete</button>
                              <button type="button" id="updateBtnId" name="blackDateId"  onclick="doUpdate('<iais:mask name="blackDateId" value="${blackDateAttr.id}"/>')" class="btn btn-default btn-sm" >Update</button>
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

      <div class="application-tab-footer">
          <div class="col-xs-12 col-md-11">
            <div class="text-right">
              <a class="btn btn-primary" id="addBtnId" value="doSearch" href="#">Create</a>
            </div>
          </div>
        </div>
      </div>


    </div>

  </c:otherwise>
</c:choose>

  </form>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script>
  addBtnId.onclick = function(){
      SOP.Crud.cfxSubmit("mainForm", "preCreate");
  }

  function doDelete(val){
    $('#blackDateId').val(val);
    SOP.Crud.cfxSubmit("mainForm", "doDelete");
  }

  function doUpdate(val){
      $('#blackDateId').val(val);
      SOP.Crud.cfxSubmit("mainForm", "preUpdate");
  }

  function cutOutDate(date) {
      if (date != null){
          var str = date.toString();
          return str.substring(str.length - 4);
      }
  }

  function doClear() {
    $("#startDate").val("");
    $("#endDate").val("");
    $("#blacked_out_desc").val("");

    $("#dropYearOpt option[text = 'Please Select']").val("selected", "selected");
    $("#dropYearOpt").val("");
    $("#status option[text = 'Please Select']").val("selected", "selected");
    $("#status").val("");

    $(".form-horizontal .clearRow .current").text("Please Select");
  }


  function jumpToPagechangePage(){
      SOP.Crud.cfxSubmit("mainForm", "doPage");
  }

  function sortRecords(sortFieldName,sortType){
      SOP.Crud.cfxSubmit("mainForm","doFilter",sortFieldName,sortType);
  }

</script>

