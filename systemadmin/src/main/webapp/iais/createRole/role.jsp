<%--
  Created by IntelliJ IDEA.
  User: yaoxia
  Date: 2019/12/23
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>


<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <br>
  <div class="main-content" >

    <div class="personnel-list">
      <h2>LICENCE-SPECIFIC ROLES</h2>

        <div class="form-group">
          <label class="control-label" for="inboxType">Roles</label>
          <div class="col-xs-12 col-md-8 col-lg-9">
            <select id="inboxType" style="display: none;">
              <option>Select a role</option>
              <option selected="">All</option>
              <option>Notification</option>
              <option>Announcement</option>
              <option>Query</option>
            </select><div class="nice-select" tabindex="0"><span class="current">Announcement</span><ul class="list mCustomScrollbar _mCS_1 mCS_no_scrollbar"><div id="mCSB_1" class="mCustomScrollBox mCS-light mCSB_vertical mCSB_inside" style="max-height: none;" tabindex="0"><div id="mCSB_1_container" class="mCSB_container mCS_y_hidden mCS_no_scrollbar_y" style="position:relative; top:0; left:0;" dir="ltr"><li data-value="Select a role" class="option">Select a role</li><li data-value="All" class="option">All</li><li data-value="Notification" class="option focus">Notification</li><li data-value="Announcement" class="option selected">Announcement</li><li data-value="Query" class="option">Query</li></div><div id="mCSB_1_scrollbar_vertical" class="mCSB_scrollTools mCSB_1_scrollbar mCS-light mCSB_scrollTools_vertical" style="display: none;"><div class="mCSB_draggerContainer"><div id="mCSB_1_dragger_vertical" class="mCSB_dragger" style="position: absolute; min-height: 30px; height: 0px; top: 0px;"><div class="mCSB_dragger_bar" style="line-height: 30px;"></div></div><div class="mCSB_draggerRail"></div></div></div></div></ul></div>
          </div>
        </div>
        <div class="form-group large right-side">
          <div class="search-wrap">
            <div class="input-group">
              <input class="form-control" id="inboxAdvancedSearch" type="text" placeholder="Search your keywords" name="inboxAdvancedSearch" aria-label="inboxAdvancedSearch"><span class="input-group-btn">
                            <button class="btn btn-default buttonsearch" title="Search your keywords"><i class="fa fa-search"></i></button></span>
            </div>
          </div>
        </div>

      <div class="table-gp">
        <table class="table">
          <thead>
          <tr>
            <th>Role Name </th>
            <th>Role ID</th>
            <th>tag</th>
            <th width="220">Roles</th>
          </tr>
          </thead>
          <tbody>


          </tbody>
        </table>
        <div>
          <a class="btn btn-primary" name="createRole"  id="createRole" href="#">create role</a>
          <a class="btn btn-primary" name="import" href="#">import</a>
        </div>
        <div class="table-footnote">
          <div class="row">
            <div class="col-xs-12 col-md-4">
              <p class="count">4 out of 14</p>
            </div>
            <div class="col-xs-6 col-md-8 text-right">
              <div class="nav">
                <ul class="pagination">
                  <li class="hidden"><a href="#" aria-label="Previous"><span aria-hidden="true"><i class="fa fa-chevron-left"></i></span></a></li>
                  <li class="active"><a href="#">1</a></li>
                  <li><a href="#">2</a></li>
                  <li><a href="#">3</a></li>
                  <li><a href="#" aria-label="Next"><span aria-hidden="true"><i class="fa fa-chevron-right"></i></span></a></li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-xs-12 text-right"><a class="btn btn-secondary" href="#">DOWNLOAD THE LIST</a></div>
        </div>
      </div>
    </div>

  </div>
</form>

<script type="text/javascript">

  $('#createRole').click(function () {

      submit("create",null,null);

  });


</script>
</>