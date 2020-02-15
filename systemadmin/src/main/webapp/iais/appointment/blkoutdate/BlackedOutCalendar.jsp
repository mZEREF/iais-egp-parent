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
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-intranet"/>
<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="currentValidateId" value="">
    <div class="bg-title"><h2>Blacked Out Dates Management</h2></div>
<c:choose>
  <c:when test="${empty wrlGrpNameOpt}">
    Your current group cannot be found or you are not an inspection lead !
  </c:when>
  <c:otherwise>


    <iais:section title="" id = "demoList">
      <iais:row>
        <iais:field value="Working Group"/>
        <iais:value width="18">
          <iais:select name="wrlGrpNameOpt" id="wrlGrpNameOpt"  onchange="doSearch()" options = "wrlGrpNameOpt" firstOption="Please Select" value="${shortName}" ></iais:select>
        </iais:value>
      </iais:row>


      <iais:row>
        <iais:field value="Year."/>
        <iais:value width="18">
          <iais:select name="dropYearOpt" id="dropYearOpt"  onchange="doSearch()" options = "dropYearOpt" firstOption="Please Select" value="${dropYear}" ></iais:select>
        </iais:value>
      </iais:row>

      <iais:row>
        <iais:field value="Blacked Out Date Start"/>
        <iais:value width="18">
          <iais:datePicker id = "startDate" name = "startDate"  value="${startDate}"></iais:datePicker>
        </iais:value>
      </iais:row>

      <iais:row>
        <iais:field value="Blacked Out Date End"/>
        <iais:value width="18">
          <iais:datePicker id = "endDate" name = "endDate"  value="${endDate}"></iais:datePicker>
        </iais:value>
      </iais:row>

      <iais:row>
        <iais:field value="Status"/>
        <iais:value width="18">
          <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                       firstOption="Select Status" filterValue="CMSTAT002" value="${status}"></iais:select>
        </iais:value>
      </iais:row>

      <iais:action style="text-align:center;">
        <button class="btn btn-lg btn-login-search" type="button" value="doSearch" style="background:#2199E8; color: white ">Search</button>
        <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white">Clear</button>
      </iais:action>
    </iais:section>


    <div>
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
          <div class="row">
            <br><br>
            <div class="col-xs-12">
              <div class="components">
                <iais:pagination  param="blackedOutDateQueryAttr" result="blackedOutDateResultAttr"/>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="false"   field="index" value="No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="year" value="Year"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="START_DATE" value="Blacked Out Date Start"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="END_DATE" value="Blacked Out Date End"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="desc" value="Blacked Out Date Description"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="status" value="Status"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty blackedOutDateResultAttr.rows}">
                        <tr>
                          <td colspan="6">
                            No Record!!
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="blackDateAttr" items="${blackedOutDateResultAttr.rows}" varStatus="status">
                          <tr>
                            <td>${status.index + 1}</td>
                            <td>
                              <fmt:formatDate value="${blackDateAttr.startDate}" pattern="yyyy"></fmt:formatDate>
                            </td>

                            <td><fmt:formatDate value="${blackDateAttr.startDate}" pattern="MM/dd/yyyy"/></td>
                            <td><fmt:formatDate value="${blackDateAttr.endDate}" pattern="MM/dd/yyyy"/></td>

                            <td>${blackDateAttr.desc}</td>
                            <td><iais:code code="${blackDateAttr.status}"></iais:code></td>
                            <td>
                              <input type="hidden" id="blackDateId" name="blackDateId" value="">
                              <button type="button"  id="deleteBtnId" name="blackDateId"  onclick="doDelete('<iais:mask name="blackDateId" value="${blackDateAttr.id}"/>')"  class="btn btn-default btn-sm" >Delete</button>
                              <button type="button" id="updateBtnId" name="blackDateId"  onclick="doUpdate('<iais:mask name="blackDateId" value="${blackDateAttr.id}"/>')" class="btn btn-default btn-sm" >Update</button>
                            </td>
                          </tr>
                        </c:forEach>


                      </c:otherwise>
                    </c:choose>
                    </tbody>
                  </table>
                  <div class="table-footnote">
                    <div class="row">
                      <div class="col-xs-6 col-md-4">

                      </div>
                      <div class="col-xs-6 col-md-8 text-right">
                        <div class="nav">

                        </div>
                        <br><br>



                        <div class="text-right text-center-mobile">
                          <a class="btn btn-primary next" id="addBtnId" >Create</a>
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


    </div>

  </c:otherwise>
</c:choose>

  </form>
</div>

<%@include file="/include/validation.jsp"%>
<%@include file="/include/utils.jsp"%>
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

  $(document).ready(function () {
      setYears();
  });

  function cutOutDate(date) {
      if (date != null){
          var str = date.toString();
          return str.substring(str.length - 4);
      }
  }
/*
  function setYears(){
    var currentDate = new Date();
    var startYear= currentDate.getFullYear() - 10 ;
    var endYear = currentDate.getFullYear();
    console.log(endYear);
      $("#dropYear").append("<option value=" + "</option>")
    for (var i = endYear; i > startYear; i--){
     $("#dropYear").append("<option value=" + i + ">" + i +"</option>")
    }
  }
  */


  function jumpToPagechangePage(){
      SOP.Crud.cfxSubmit("mainForm", "doPage");
  }

  function sortRecords(sortFieldName,sortType){
      SOP.Crud.cfxSubmit("mainForm","doFilter",sortFieldName,sortType);
  }

</script>

