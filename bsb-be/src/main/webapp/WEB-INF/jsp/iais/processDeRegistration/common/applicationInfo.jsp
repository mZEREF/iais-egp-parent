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
                        <td class="col-xs-6" style="text-align: right">Application Type</td>
                        <td class="col-xs-6" style="padding-left : 20px"><iais:code code="${submissionDetailsDto.applicationType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application No.</td>
                        <td style="padding-left : 20px"><c:out value="${submissionDetailsDto.applicationNo}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility Type</td>
                        <td style="padding-left : 20px"><iais:code code="${submissionDetailsDto.facilityType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Process Type</td>
                        <td style="padding-left : 20px"><iais:code code="${submissionDetailsDto.processType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Submission Date</td>
                        <td style="padding-left : 20px"><iais:code code="${submissionDetailsDto.submissionDate}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Current Status</td>
                        <td style="padding-left : 20px"><iais:code code="${submissionDetailsDto.currentStatus}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<c:set var="maskedAppId" value="${MaskUtil.maskValue('appId', appId)}"/>
<c:set var="maskedAppViewModuleType" value="${MaskUtil.maskValue('appViewModuleType', appViewModuleType)}"/>
<div style="text-align: center">
    <a href="javascript:void(0);" onclick="viewApplication('${maskedAppId}', '${maskedAppViewModuleType}')">
        <button type="button" class="btn btn-primary">
            View Application
        </button>
    </a>
</div>
<div>&nbsp</div>
<div class="panel panel-default">
    <div class="panel-heading"><strong>Applicant Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <th scope="col" style="display: none"></th>
                    </tr>
                    <tr>
                        <td class="col-xs-6" style="text-align: right">Facility/Organisation Name</td>
                        <td class="col-xs-6" style="padding-left : 20px"><c:out value="${submissionDetailsDto.facilityOrOrganisationName}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility/Organisation Address</td>
                        <td style="padding-left : 20px"><c:out value="${submissionDetailsDto.facilityOrOrganisationAddress}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility/Organisation Admin</td>
                        <td style="padding-left : 20px"><c:out value="${submissionDetailsDto.facilityOrOrganisationAdmin}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Telephone</td>
                        <td style="padding-left : 20px"><c:out value="${submissionDetailsDto.telephone}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Email</td>
                        <td style="padding-left : 20px"><c:out value="${submissionDetailsDto.email}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="text-align: left">
    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
</div>