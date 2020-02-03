<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/12/2019
  Time: 1:16 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>


<style>
  .form-horizontal label.control-label, .form-inline label.control-label {
    color: #333333;
    font-size: 1.3rem;
    text-align: left !important;
    line-height: 50px;
    padding-top: 0 !important;
  }


</style>
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">



  <div class="main-content">
    <div class="container">
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="form-horizontal">
          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Regulation Clause Number</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="regulationClauseNo" value="" />
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Regulation Clause Number</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="regulationClauseNo" value="" />
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Regulation</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="regulationClause" value="" />
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Checklist Item</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="checklistItem" value="" />
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Risk Level</label>
            <div class="col-xs-12 col-md-8 col-lg-9">
              <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL" firstOption="Select Risk Level"></iais:select>
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Risk Level</label>
            <div class="col-xs-12 col-md-8 col-lg-9">
              <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS" firstOption="Select Status"></iais:select>
            </div>
          </div>


        </div>
        <div class="tab-content">
          <div class="row">
            <div class="col-xs-12">
              <div class="components">
                <h2 class="component-title">Search &amp; Result</h2>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <th>Regulation Clause Number</th>
                      <th>Regulations</th>
                      <th>Checklist Item</th>
                      <th>Service</th>
                      <th>Risk Level</th>
                      <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                      <td>
                        <p class="visible-xs visible-sm table-row-title">Subject</p>
                        <p><a href="#">Hyperlink 1</a></p>
                      </td>
                      <td>
                        <p class="visible-xs visible-sm table-row-title">Message Type</p>
                        <p>Roll 1</p>
                      </td>
                      <td>
                        <p class="visible-xs visible-sm table-row-title">Ref. No.</p>
                        <p>Reference No. 1</p>
                      </td>
                      <td>
                        <p class="visible-xs visible-sm table-row-title">Service</p>
                        <p>Body Content</p>
                      </td>
                      <td>
                        <p class="visible-xs visible-sm table-row-title">Date</p>
                        <p>DD MM YY, Time</p>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                  <div class="table-footnote">
                    <div class="row">
                      <div class="col-xs-6 col-md-4">
                        <p class="count">5 out of 25</p>
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
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </div>



      </div>

      <div class="application-tab-footer">
        <div class="row">
          <div class="col-xs-12 col-sm-6">
            <p><a class="back" href="#"><em class="fa fa-angle-left"></em> Back</a></p>
          </div>
          <div class="col-xs-12 col-sm-6">
            <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: addChecklistItemNextAction();">Config Item</a></div>
          </div>
        </div>
      </div>
    </div>
    </div>




    </div>




</form>


<script type="text/javascript">
    function addChecklistItemNextAction() {
        SOP.Crud.cfxSubmit("mainForm","addChecklistItemNextAction");
    }

    function doNext() {
        SOP.Crud.cfxSubmit("mainForm","nextPage");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backLastPage");
    }
</script>