<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/24/2019
  Time: 6:54 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<webui:setLayout name="iais-intranet"/>


<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<style>
  .btn.btn-secondary{
    font-size: 10px;
    font-weight: 500;
    background: white;
    border: 1px solid #333333;
    color: black;
    padding: 10px 25px;
    text-transform: uppercase;
    border-radius: 30px;
  }

</style>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <%@ include file="/include/formHidden.jsp" %>


  <div class="center-content">
    <div class="intranet-content">
      <div class="bg-title">
        <h2>Moh Define Preferred Date Range Period</h2>
      </div>
      <br>
      <span id="error_numberError" name="iaisErrorMsg" class="error-msg"></span>
      <br><br><br>
      <div class="row">
        <div class="col-xs-10 col-md-12">
          <div class="components">
            <a class="btn btn-secondary collapsed" data-toggle="collapse" data-target="#advfilter" aria-expanded="false">Filter</a>
          </div>
        </div>
        <div id="advfilter" class="collapse" aria-expanded="false" style="height: 0px;">
          <div class="filter-box">
            <h3>Filter</h3>
            <form class="form-horizontal">

              <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label" >Service Code</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                  <iais:select name="svcName" firstOption = "Please Select" options ="svcNameSelect"></iais:select>
                </div>
              </div>

              <br><br><br>
              <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label" >Block-out Period after Application (*week)</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                  <input type="text" name="periodAfterApp">
                </div>
              </div>

              <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label" >Block-out Period before Expiry (*week)</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                  <input type="text" name="periodBeforeExp">
                </div>
              </div>

              <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label" >Non-reply Notification Window (*week)</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                  <input type="text" name="nonReplyWindow">
                </div>
              </div>

            </form>
              <div class="row">
                <div class="col-xs-12 col-md-12">
                  <div class="text-right"><a id="prefSearchBtnId" class="btn btn-primary" href="#">Search</a></div>
                </div>
              </div>
            </div>
        </div>
      </div>
      <!---------------->
      <!--- Pagination and display---->
      <div class="row table-info-display">
        <div class="col-xs-12 col-md-4 text-left">
          <p class="count table-count">1-3 out of 10 items</p>
          <div class="form-group">
            <div class="col-xs-12 col-md-3">
              <select class="table-select" id="contentSelect1" style="display: none;">
                <option>5</option>
                <option>10</option>
                <option>20</option>
                <option>30</option>
                <option>40</option>
              </select><div class="nice-select table-select" tabindex="0"><span class="current">5</span><ul class="list mCustomScrollbar _mCS_1 mCS_no_scrollbar"><div id="mCSB_1" class="mCustomScrollBox mCS-light mCSB_vertical mCSB_inside" style="max-height: none;" tabindex="0"><div id="mCSB_1_container" class="mCSB_container mCS_y_hidden mCS_no_scrollbar_y" style="position:relative; top:0; left:0;" dir="ltr"><li data-value="5" class="option selected">5</li><li data-value="10" class="option">10</li><li data-value="20" class="option">20</li><li data-value="30" class="option">30</li><li data-value="40" class="option">40</li></div><div id="mCSB_1_scrollbar_vertical" class="mCSB_scrollTools mCSB_1_scrollbar mCS-light mCSB_scrollTools_vertical" style="display: none;"><div class="mCSB_draggerContainer"><div id="mCSB_1_dragger_vertical" class="mCSB_dragger" style="position: absolute; min-height: 30px; height: 0px; top: 0px;"><div class="mCSB_dragger_bar" style="line-height: 30px;"></div></div><div class="mCSB_draggerRail"></div></div></div></div></ul></div>
            </div>
          </div>
        </div>

        <div class="col-xs-12 col-md-8 text-right">
          <div class="nav">
            <ul class="pagination">
              <li><a href="#" aria-label="Previous"><span aria-hidden="true"><em class="fa fa-chevron-left"></em></span></a></li>
              <li><a href="#">1</a></li>
              <li class="active"><a href="#">2</a></li>
              <li><a href="#">3</a></li>
              <li><a href="#" aria-label="Next"><span aria-hidden="true"><em class="fa fa-chevron-right"></em></span></a></li>
            </ul>
          </div>
        </div>
      </div>
      <!------------------------------>
      <div class="table-gp">
        <table class="table">
          <thead>
          <tr>
            <th>No.</th>
            <th>Service Name</th>
            <th>Service Type</th>
            <th>Block-out Period after Application</th>
            <th>Block-out Period before Expiry</th>
            <th>Non-reply Notification Window</th>
            <th>Action</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <c:forEach varStatus="status" items="${prefPeriodResult.rows}" var="prefItem">
              <tr>
                <td>
                  <p class="visible-xs visible-sm table-row-title">No.</p>
                  <p>${status.index + 1}</p>
                </td>

                <td>
                  <p class="visible-xs visible-sm table-row-title">Service Name</p>
                  <p>${prefItem.svcName}</p>
                </td>

                <td>
                  <p class="visible-xs visible-sm table-row-title">Service Type</p>
                  <p>${prefItem.svcType}</p>
                </td>

                <td>
                  <p class="visible-xs visible-sm table-row-title">Block-out Period after Application</p>
                  <p>${prefItem.periodAfterApp}</p>
                </td>

                <td>
                  <p class="visible-xs visible-sm table-row-title">Block-out Period before Expiry</p>
                  <p>${prefItem.periodBeforeExp}</p>
                </td>

                <td>
                  <p class="visible-xs visible-sm table-row-title">Non-reply Notification Window</p>
                  <p>${prefItem.nonReplyWindow}</p>
                </td>

                <td>
                  <p class="visible-xs visible-sm table-row-title">Action</p>
                  <%--<a class="btn btn-secondary" id="prefItemId" name = "prefItemId" onclick="preUpdateData(<iais:mask name="prefItemId" value="${prefItem.id}"/>)">Update</a>--%>
                  <a class="btn btn-secondary" id="prefItemId" name = "prefItemId" onclick="preUpdateData('${prefItem.id}')">Update</a>
                </td>

              </tr>


            </c:forEach>



          </tr>
          </tbody>
        </table>
        <div class="table-footnote">
          <div class="row">
            <div class="col-xs-6 col-md-4">
              <p class="count">1-3 out of 10 items</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="application-tab-footer">
      <%--<div class="row">
        <div class="col-xs-12 col-sm-12">
          <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#">PROCEED</a></div>
        </div>
      </div>--%>
    </div>
  </div>


</form>




<%@include file="/include/validation.jsp"%>
<script>

    prefSearchBtnId.onclick = function () {
        SOP.Crud.cfxSubmit("mainForm", "doSearch");
    }

    function preUpdateData(id){
        SOP.Crud.cfxSubmit("mainForm", "preUpdateData", id);
    }
</script>
