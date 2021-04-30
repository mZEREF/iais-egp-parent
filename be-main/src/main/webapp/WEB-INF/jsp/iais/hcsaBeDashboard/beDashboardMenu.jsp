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
    function dashboardStepClear() {
        $('input[name="application_no"]').val("");
        $('input[name="hci_code"]').val("");
        $('input[name="hci_address"]').val("");
        $('input[name="hci_name"]').val("");
        $('input[name="application_status"]').val("");
        $("#beInboxFilter .current").text("Please Select");
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $("#inspector_name option:first").prop("selected", 'selected');
        $("#searchCondition .current").text("Please Select");
    }

    function dashboardAssignMe() {
        showWaiting();
        $('#switchAction').val('assignme');
        intraDashboardSubmit('assignme');
    }

    function dashboardApplicantReply() {
        showWaiting();
        dashboardStepClear();
        $('#switchAction').val('reply');
        intraDashboardSubmit('reply');
    }

    function dashboardKpiTask() {
        showWaiting();
        dashboardStepClear();
        $('#switchAction').val('kpi');
        intraDashboardSubmit('kpi');
    }

    function dashboardRenewalExpiry() {
        showWaiting();
        dashboardStepClear();
        $('#switchAction').val('renew');
        intraDashboardSubmit('renew');
    }

    function dashboardWaitApproval() {
        showWaiting();
        dashboardStepClear();
        $('#switchAction').val('wait');
        intraDashboardSubmit('wait');
    }

    function dashboardCommonPool() {
        showWaiting();
        dashboardStepClear();
        $('#switchAction').val('common');
        intraDashboardSubmit('common');
    }

    function dashboardSupervisorPool() {
        showWaiting();
        dashboardStepClear();
        $('#switchAction').val('group');
        intraDashboardSubmit('group');
    }
</script>