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
            <p class="dashboard-txt"> Tasks Pending Applicant's Reply</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardKpiTask()">
            <p class="dashboard-txt"> New Application Tasks Exceeding x Days</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardRenewalExpiry()">
            <p class="dashboard-txt"> Renewal Tasks Nearing Expiry Date</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardWaitApproval()">
            <p class="dashboard-txt"> Tasks waiting for Approval</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardCommonPool()">
            <p class="dashboard-txt"> Tasks Not Assigned</p>
          </a>
        </div>
      </div>
    </c:when>
    <c:when test="${'AO2' eq iais_Login_User_Info_Attr.curRoleId ||
                    'AO1' eq iais_Login_User_Info_Attr.curRoleId ||
                    'AO1_LEAD' eq iais_Login_User_Info_Attr.curRoleId ||
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
            <p class="dashboard-txt"> Tasks Pending Applicant's Reply</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardKpiTask()">
            <p class="dashboard-txt"> New Application Tasks Exceeding x Days</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardRenewalExpiry()">
            <p class="dashboard-txt"> Renewal Tasks Nearing Expiry Date</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardWaitApproval()">
            <p class="dashboard-txt"> Tasks waiting for Approval</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardCommonPool()">
            <p class="dashboard-txt"> Tasks Not Assigned</p>
          </a>
        </div>
      </div>
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardSupervisorPool()">
            <p class="dashboard-txt"> Tasks Assigned to My Team</p>
          </a>
        </div>
      </div>
    </c:when>
    <c:when test="${'BROADCAST' eq iais_Login_User_Info_Attr.curRoleId}">
      <div class="dashboard-tile-item">
        <div class="dashboard-tile">
          <a data-tab="#" onclick="javascript:dashboardCommonPool()">
            <p class="dashboard-txt"> Tasks Not Assigned</p>
          </a>
        </div>
      </div>
    </c:when>
    <c:when test="${'SYSTEM_USER_ADMIN' eq iais_Login_User_Info_Attr.curRoleId}">

    </c:when>
    <c:otherwise>

    </c:otherwise>
  </c:choose>
</div>
<script type="text/javascript">
  function dashboardAssignMe() {
      showWaiting();
      $('#switchAction').val('assignme');
      intraDashboardSubmit('assignme');
  }

  function dashboardApplicantReply() {
      showWaiting();
      $('#switchAction').val('reply');
      intraDashboardSubmit('reply');
  }

  function dashboardKpiTask() {
      showWaiting();
      $('#switchAction').val('kpi');
      intraDashboardSubmit('kpi');
  }

  function dashboardRenewalExpiry() {
      showWaiting();
      $('#switchAction').val('renew');
      intraDashboardSubmit('renew');
  }

  function dashboardWaitApproval() {
      showWaiting();
      $('#switchAction').val('wait');
      intraDashboardSubmit('wait');
  }

  function dashboardCommonPool() {
      showWaiting();
      $('#switchAction').val('common');
      intraDashboardSubmit('common');
  }

  function dashboardSupervisorPool() {
      showWaiting();
      $('#switchAction').val('group');
      intraDashboardSubmit('group');
  }
</script>