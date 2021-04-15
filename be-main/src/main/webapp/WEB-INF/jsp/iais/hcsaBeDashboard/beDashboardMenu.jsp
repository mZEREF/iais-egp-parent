<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/4/13
  Time: 12:46
  To change this template use File | Settings | File Templates.
--%>
<div class="dashboard-multiple">
  <c:choose>
    <c:when test="${'INSPECTOR' eq iais_Login_User_Info_Attr.curRoleId ||
                  'ASO' eq iais_Login_User_Info_Attr.curRoleId ||
                  'PSO' eq iais_Login_User_Info_Attr.curRoleId}">
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardApplicantReply()">
            <h1 class="dashboard-count" style="color: #f5333f;">2</h1>
            <p class="dashboard-txt"> Tasks Pending Applicant's Reply</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardKpiTask()">
            <h1 class="dashboard-count" style="color: #f5333f;">1</h1>
            <p class="dashboard-txt"> New Application Tasks Exceeding x Days</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardRenewalExpiry()">
            <h1 class="dashboard-count" style="color: #f5333f;">3</h1>
            <p class="dashboard-txt"> Renewal Tasks Nearing Expiry Date</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardWaitApproval()">
            <h1 class="dashboard-count" style="color: #f5333f;">10</h1>
            <p class="dashboard-txt"> Tasks waiting for Approval</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardCommonPool()">
            <h1 class="dashboard-count" style="color: #f5333f;">10</h1>
            <p class="dashboard-txt"> Tasks Not Assigned</p>
          </a>
        </div>
      </div>
    </c:when>
    <c:when test="${'AO2' eq iais_Login_User_Info_Attr.curRoleId ||
                    'AO3' eq iais_Login_User_Info_Attr.curRoleId ||
                    'AO2_LEAD' eq iais_Login_User_Info_Attr.curRoleId ||
                    'AO3_LEAD' eq iais_Login_User_Info_Attr.curRoleId ||
                    'INSPECTOR_LEAD' eq iais_Login_User_Info_Attr.curRoleId ||
                    'ASO_LEAD' eq iais_Login_User_Info_Attr.curRoleId ||
                    'PSO_LEAD' eq iais_Login_User_Info_Attr.curRoleId
                    }">
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardApplicantReply()">
            <h1 class="dashboard-count" style="color: #f5333f;">2</h1>
            <p class="dashboard-txt"> Tasks Pending Applicant's Reply</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardKpiTask()">
            <h1 class="dashboard-count" style="color: #f5333f;">1</h1>
            <p class="dashboard-txt"> New Application Tasks Exceeding x Days</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardRenewalExpiry()">
            <h1 class="dashboard-count" style="color: #f5333f;">3</h1>
            <p class="dashboard-txt"> Renewal Tasks Nearing Expiry Date</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardWaitApproval()">
            <h1 class="dashboard-count" style="color: #f5333f;">10</h1>
            <p class="dashboard-txt"> Tasks waiting for Approval</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardCommonPool()">
            <h1 class="dashboard-count" style="color: #f5333f;">10</h1>
            <p class="dashboard-txt"> Tasks Not Assigned</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardSupervisorPool()">
            <h1 class="dashboard-count" style="color: #f5333f;">10</h1>
            <p class="dashboard-txt"> Tasks Assigned to My Team</p>
          </a>
        </div>
      </div>
    </c:when>
    <c:when test="${'BROADCAST' eq iais_Login_User_Info_Attr.curRoleId}">
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardCommonPool()">
            <h1 class="dashboard-count" style="color: #f5333f;">10</h1>
            <p class="dashboard-txt"> Tasks Not Assigned</p>
          </a>
        </div>
      </div>
    </c:when>
    <c:when test="${'SYSTEM_USER_ADMIN' eq iais_Login_User_Info_Attr.curRoleId}">

    </c:when>
    <c:otherwise>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" href="javascript:;" onclick="javascript:dashboardAssignMe()">
            <h1 class="dashboard-count" style="color: #f5333f;">6</h1>
            <p class="dashboard-txt"> Tasks assigned to me</p>
          </a>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</div>
<script type="text/javascript">
  function dashboardAssignMe() {
      showWaiting();
      $('#action').val('assignme');
      intraDashboardSubmit('assignme');
  }

  function dashboardApplicantReply() {
      showWaiting();
      $('#action').val('reply');
      intraDashboardSubmit('reply');
  }

  function dashboardKpiTask() {
      showWaiting();
      $('#action').val('kpi');
      intraDashboardSubmit('kpi');
  }

  function dashboardRenewalExpiry() {
      showWaiting();
      $('#action').val('renew');
      intraDashboardSubmit('renew');
  }

  function dashboardWaitApproval() {
      showWaiting();
      $('#action').val('wait');
      intraDashboardSubmit('wait');
  }

  function dashboardCommonPool() {
      showWaiting();
      $('#action').val('common');
      intraDashboardSubmit('common');
  }

  function dashboardSupervisorPool() {
      showWaiting();
      $('#action').val('group');
      intraDashboardSubmit('group');
  }
</script>