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
  <div class="main-content">
  <div>
  <div class="personnel-list">
    <h2>Roles</h2>
    <div class="form-group large right-side">
      <div class="search-wrap">
        <div class="input-group">
          <input class="form-control"  type="text" placeholder="Search your Role ID" name="inboxAdvancedSearch" aria-label="inboxAdvancedSearch">
          <span class="input-group-btn"></span>
        </div>
        <div class="input-group">
          <input class="form-control"  type="text" placeholder="Search your Role Name" name="inboxAdvancedSearch" aria-label="inboxAdvancedSearch">
          <span class="input-group-btn"></span>
        </div>
        <div class="input-group">
          <input class="form-control"  type="text" placeholder="Search your Role Tag" name="inboxAdvancedSearch" aria-label="inboxAdvancedSearch">
          <span class="input-group-btn"></span>
        </div>
      </div>
    </div>
    <div class="form-group large right-side">
      <div class="search-wrap">
        <div class="input-group"></div>
      </div>
    </div>
  </div>



    <div>
      <table class="table">
        <tr>
          <th width="220">Role Id</th>
          <th  width="220">Role Name</th>
          <th  width="220">Remakes</th>
        </tr>

    </div>



 </table>

  </div>
  </div>
</>