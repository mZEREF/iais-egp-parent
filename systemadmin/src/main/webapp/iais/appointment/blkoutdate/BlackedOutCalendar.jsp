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

<style>
  .nice-select{
    width: 30%;
  }
</style>

<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="currentValidateId" value="">
    <div class="bg-title"><h2>Blacked Out Dates</h2></div>

    <iais:select name="wrlGrpNameOpt" id="wrlGrpNameOpt"  onchange="doSearch()" options = "wrlGrpNameOpt" firstOption="Please Select" value="${shortName}" ></iais:select>

    <td>
      <iais:select name="dropYearOpt" id="dropYearOpt"  onchange="doSearch()" options = "dropYearOpt" firstOption="Please Select" value="${dropYear}" ></iais:select>
    </td>

    <div>
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
          <div class="row">
              <br><br>
              <div class="col-xs-12">
                <div class="components">
                  <div class="table-gp">
                    <table class="table">
                      <thead>
                      <tr>
                        <iais:sortableHeader needSort="false"   field="index" value="No."></iais:sortableHeader>
                        <iais:sortableHeader needSort="true"   field="year" value="Year"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true"   field="startDate" value="Blacked Out Date Start"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true"   field="endDate" value="Blacked Out Date End"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true"   field="desc" value="Blacked Out Date Description"></iais:sortableHeader>
                        <iais:sortableHeader needSort="false"   field="status" value="Status"></iais:sortableHeader>
                        <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
                      </tr>
                      </thead>
                      <tbody>
                      <c:choose>
                        <c:when test="${empty blackedOutDateResultAttr}">
                          <tr>
                            <td colspan="6">
                              No Record!!
                            </td>
                          </tr>
                        </c:when>
                        <c:otherwise>
                          <c:forEach var="blackDateAttr" items="${blackedOutDateResultAttr}" varStatus="status">
                            <tr>
                              <td>${status.index + 1}</td>
                              <td>
                                <fmt:formatDate value="${blackDateAttr.startDate}" pattern="yyyy"></fmt:formatDate>
                              </td>
                              <td>${blackDateAttr.startDate}</td>
                              <td>${blackDateAttr.endDate}</td>
                              <td>${blackDateAttr.desc}</td>
                              <td>${blackDateAttr.status}</td>
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
                          <td class="row_no">${(status.index + 1) + (blackedOutDateQueryAttr.pageNo - 1) * blackedOutDateQueryAttr.pageSize}</td>
                        </div>
                        <div class="col-xs-6 col-md-8 text-right">
                          <div class="nav">
                            <ul class="pagination">
                              <li class="hidden"><a href="#" aria-label="Previous"><span aria-hidden="true"><em class="fa fa-chevron-left"></em></span></a></li>
                              <li class="active"><a href="#">1</a></li>
                              <li><a href="#">2</a></li>
                              <li><a href="#">3</a></li>
                              <li><a href="#" aria-label="Next"><span aria-hidden="true"><em class="fa fa-chevron-right"></em></span></a></li>

                            </ul>


                          </div>
                          <br><br>



                          <div class="text-right text-center-mobile">
                            <a class="btn btn-primary next" id="cancelBtnId">Cancel</a>
                            <a class="btn btn-primary next" id="addBtnId" >Add</a>
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

  </form>
</div>

<%@include file="/include/validation.jsp"%>
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
  function doSearch() {
      SOP.Crud.cfxSubmit("mainForm", "doSearch");
  }


</script>

