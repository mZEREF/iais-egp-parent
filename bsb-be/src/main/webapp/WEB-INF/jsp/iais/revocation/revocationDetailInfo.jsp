<div class="panel panel-default">
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                        <tr>
                            <th scope="col" style="display: none"></th>
                        </tr>
                        <tr>
                            <td style="text-align: right">Active Approval No. to be revoked</td>
                            <td style="padding-left : 20px"><c:out value="${revokeDto.approvalNo}"/></td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" style="text-align: right">Facility Name</td>
                            <td style="padding-left : 20px"><c:out value="${revokeDto.facName}"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Facility Address</td>
                            <td style="padding-left : 20px"><c:out value="${revokeDto.facAddress}"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="${revokeDto.facClassification}"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Activity Type</td>
                            <td style="padding-left : 20px">
                                <iais:code code="${revokeDto.activityType}"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Approval</td>
                            <td style="padding-left : 20px"></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Approval Status</td>
                            <td style="padding-left : 20px"><iais:code code="${revokeDto.approvalStatus}"/></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="text-align: left">
    <a class="back" href="${backUrl}"><em class="fa fa-angle-left"></em>Back</a>
</div>