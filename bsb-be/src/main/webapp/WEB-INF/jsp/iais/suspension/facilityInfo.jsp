<div class="panel panel-default">
    <div class="panel-heading">Submission Details</div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <%--@elvariable id="suspensionReinstatementDto" type="sg.gov.moh.iais.egp.bsb.dto.suspension.SuspensionReinstatementDto"--%>
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <th scope="col" style="display: none"></th>
                    </tr>
                    <tr>
                        <td class="col-xs-6" style="text-align: right">Active Approval No. to be revoked</td>
                        <td style="padding-left : 20px">${suspensionReinstatementDto.approvalNo}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility Name</td>
                        <td style="padding-left : 20px">${suspensionReinstatementDto.facName}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility Address</td>
                        <td style="padding-left : 20px">${suspensionReinstatementDto.facAddress}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility Classification</td>
                        <td style="padding-left : 20px"><iais:code code="${suspensionReinstatementDto.facClassification}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Activity Type</td>
                        <td style="padding-left : 20px"><iais:code code="${suspensionReinstatementDto.activityType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Approval Status</td>
                        <td style="padding-left : 20px"><iais:code code="${suspensionReinstatementDto.approvalStatus}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="text-align: left">
    <c:if test="${back eq 'fac'}">
        <a class="back" href="/bsb-be/eservice/INTRANET/FacilityList"><em class="fa fa-angle-left"></em>Back</a>
    </c:if>
    <c:if test="${back eq 'app'}">
        <a class="back" href="/bsb-be/eservice/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em>Back</a>
    </c:if>
</div>