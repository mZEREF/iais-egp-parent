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

  <form method="post" id="mainPoolForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="inspectionPoolType" value="">
    <input type="hidden" id="appCorrelationId" name="appCorrelationId" value="">
    <div class="main-content">
      <div class="row">
      <div class="col-lg-12 col-xs-12">
        <div class="center-content">
          <div class="intranet-content">
            <div class="bg-title">
              <h2>Page Title Here</h2>
            </div>
            <!-- Filter Box button-->
            <div class="row">
                  <div class="form-horizontal">
                    <div class="form-group">
                      <label class="col-xs-12 col-md-4 control-label" for="mobileNo">Mobile No.</label>
                      <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="formfill1" type="text">
                      </div>
                    </div>
                    <div class="form-group">
                      <label class="col-xs-12 col-md-4 control-label" for="emailAddress">Email Address.</label>
                      <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="formfill1" type="text">
                      </div>
                    </div>
                  </div>
                  <div class="application-tab-footer">
                    <div class="row">
                      <div class="col-xs-12 col-md-12">
                        <div class="text-center">
                          <a class="btn btn-secondary" href="#">Clear</a>
                          <a class="btn btn-primary" href="#">Search</a>
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
                    <select class="table-select" id="contentSelect1">
                      <option>5</option>
                      <option>10</option>
                      <option>20</option>
                      <option>30</option>
                      <option>40</option>
                    </select>
                  </div>
                </div>
              </div>

              <div class="col-xs-12 col-md-8 text-right">
                <div class="nav">
                  <ul class="pagination">
                    <li><a href="#" aria-label="Previous"><span aria-hidden="true"><i class="fa fa-chevron-left"></i></span></a></li>
                    <li><a href="#">1</a></li>
                    <li class="active"><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li><a href="#" aria-label="Next"><span aria-hidden="true"><i class="fa fa-chevron-right"></i></span></a></li>
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
                  <th>RN No</th>
                  <th>Status</th>
                  <th>HCI Code</th>
                  <th>HCI Name/ Address</th>
                  <th>Service Name</th>
                  <th>Submission Date </th>
                  <th>Licence End Date </th>
                  <th>Application Status</th>
                  <th>Payment Status</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">No.</p>
                    <p>1</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">RN No.</p>
                    <p><a href="#">RH-20181234-00001</a></p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Status</p>
                    <p>Renewal</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">HCI Code</p>
                    <p>16C0001</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">HCI Name/ Address</p>
                    <p>16 Raffles Quay # 01-03, Hong Leong Building, 048581​​​​​​​</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Service Name</p>
                    <p>Blood Banking</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Submission Date</p>
                    <p>4/12/2019</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Licence End Date</p>
                    <p>1/1/2020</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Application Status</p>
                    <p class="warning">Pending Admin Screening</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Payment Status</p>
                    <p>Paid</p>
                  </td>
                </tr>
                <tr>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">No.</p>
                    <p>2</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">RN No.</p>
                    <p><a href="#">RH-20181234-00001</a></p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Status</p>
                    <p>Renewal</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">HCI Code</p>
                    <p>16C0001</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">HCI Name/ Address</p>
                    <p>16 Raffles Quay # 01-03, Hong Leong Building, 048581​​​​​​​</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Service Name</p>
                    <p>Blood Banking</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Submission Date</p>
                    <p>4/12/2019</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Licence End Date</p>
                    <p>1/1/2020</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Application Status</p>
                    <p class="warning">Pending Admin Screening</p>
                  </td>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">Payment Status</p>
                    <p>Paid</p>
                  </td>
                </tr>

                </tbody>
              </table>

            </div>
          </div>

        </div>
      </div>
      </div>
    </div>
  </form>

<script type="text/javascript">

</script>




