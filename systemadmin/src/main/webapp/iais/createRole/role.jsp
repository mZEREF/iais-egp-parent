<%--
  Created by IntelliJ IDEA.
  User: yaoxia
  Date: 2019/12/23
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>


<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="row">
      <div class="col-lg-12 col-xs-12">
        <div class="center-content">
          <div class="intranet-content">
            <div class="bg-title">
              <h2>Roles</h2>
            </div>
            <!-- Filter Box button-->
            <div class="row">
              <div class="col-xs-10 col-md-12">
                <div class="components">
                  <a class="btn btn-secondary" data-toggle="collapse" data-target="#advfilter">Filter</a>
                </div>
              </div>
              <div id="advfilter" class="collapse">
                <div class="filter-box">
                  <h3>Filter</h3>
                  <form class="form-horizontal">
                    <div class="form-group">
                      <label class="col-xs-12 col-md-4 control-label">Role Name</label>
                      <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="roleName" type="text">
                      </div>
                    </div>
                    <div class="form-group">
                      <label class="col-xs-12 col-md-4 control-label">Role ID</label>
                      <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="roleId" type="text">
                      </div>
                    </div>
                    <div class="form-group">
                      <label class="col-xs-12 col-md-4 control-label">tag</label>
                      <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="roleTag" type="text">
                      </div>
                    </div>
                  </form>
                  <div class="application-tab-footer">
                    <div class="row">
                      <div class="col-xs-12 col-md-12">
                        <div class="text-right"><a class="btn btn-primary" href="#">Search</a></div>
                      </div>
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
                    </select>
                    <div class="nice-select table-select" tabindex="0"><span class="current">5</span>
                      <ul class="list mCustomScrollbar _mCS_1 mCS_no_scrollbar">
                        <div id="mCSB_1" class="mCustomScrollBox mCS-light mCSB_vertical mCSB_inside"
                             style="max-height: none;" tabindex="0">
                          <div id="mCSB_1_container" class="mCSB_container mCS_y_hidden mCS_no_scrollbar_y"
                               style="position:relative; top:0; left:0;" dir="ltr">
                            <li data-value="5" class="option selected">5</li>
                            <li data-value="10" class="option">10</li>
                            <li data-value="20" class="option">20</li>
                            <li data-value="30" class="option">30</li>
                            <li data-value="40" class="option">40</li>
                          </div>
                          <div id="mCSB_1_scrollbar_vertical"
                               class="mCSB_scrollTools mCSB_1_scrollbar mCS-light mCSB_scrollTools_vertical"
                               style="display: none;">
                            <div class="mCSB_draggerContainer">
                              <div id="mCSB_1_dragger_vertical" class="mCSB_dragger"
                                   style="position: absolute; min-height: 30px; height: 0px; top: 0px;">
                                <div class="mCSB_dragger_bar" style="line-height: 30px;"></div>
                              </div>
                              <div class="mCSB_draggerRail"></div>
                            </div>
                          </div>
                        </div>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-xs-12 col-md-8 text-right">
                <div class="nav">
                  <ul class="pagination">
                    <li><a href="#" aria-label="Previous"><span aria-hidden="true"><i
                            class="fa fa-chevron-left"></i></span></a></li>
                    <li><a href="#">1</a></li>
                    <li class="active"><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li><a href="#" aria-label="Next"><span aria-hidden="true"><i
                            class="fa fa-chevron-right"></i></span></a></li>
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
                  <th>Role No</th>
                  <th>Role Name</th>
                  <th>remakes</th>
                  <th>tag</th>
                  <th>option</th>
                </tr>
                </thead>
                <tbody>

                <tr>
                  <td>
                    <p class="visible-xs visible-sm table-row-title">No.</p>
                    <p>3</p>
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
                    <p class="visible-xs visible-sm table-row-title">HCI Code</p>
                    <p>16C0001sasasasassadasdq</p>
                  </td>
                  <td>
                    <button id="editRole" >edit</button>
                    <button id="viewRole" >view</button>
                    <button id="delete" >delete</button>
                  </td>

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
            <div class="row">
              <div class="col-xs-12 col-sm-12">
                <div class="text-right text-center-mobile">
                  <a class="btn btn-primary" href="#" id="createRole">create Role</a>
                  <a class="btn btn-primary" href="#">import</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>

<script type="text/javascript">

    $('#createRole').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "createRole");
    });
    $('#editRole').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "editRole");
    });

  $('#viewRole').click(function () {
      SOP.Crud.cfxSubmit("mainForm", "viewRole");

  });
  $('#delete').click(function () {
      SOP.Crud.cfxSubmit("mainForm", "delete");

  });

</script>
</>