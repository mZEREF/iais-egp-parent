<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/5/24
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>

<div class="form-horizontal">
    <div class="form-group">
        <div class="col-xs-12 col-sm-12">
            <h2>Dashboard</h2>
        </div>
    </div>
</div>

<div class="form-horizontal">
    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">Role</label>
        <div class="col-xs-10 col-sm-7 col-md-6">
            <iais:select name="beDashRoleId" onchange="chooseCurRole()" options="beDashRoleIds"
                         cssClass="roleIds" value="${dashRoleCheckDto.checkCurRole}" needSort="true"></iais:select>
        </div>
    </div>
</div>


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

