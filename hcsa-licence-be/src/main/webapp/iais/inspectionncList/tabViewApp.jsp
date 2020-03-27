

    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><strong>Submission Details</strong></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table table-bordered">
                        <tbody>
                        <tr>
                            <td align="right">Application Type</td>
                            <td>${applicationViewDto.applicationType}</td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">Application No. (Overall)
                            </td>
                            <td class="col-xs-6">${applicationViewDto.applicationNoOverAll}</td>
                        </tr>
                        <tr>
                            <td align="right">Application No.</td>
                            <td>${applicationViewDto.applicationDto.applicationNo}</td>
                        </tr>
                        <tr>
                            <td align="right">Service Type</td>
                            <td>${applicationViewDto.serviceType}</td>
                        </tr>
                        <tr>
                            <td align="right">Submission Date</td>
                            <td>${applicationViewDto.submissionDate}</td>
                        </tr>
                        <tr>
                            <td align="right">Current Status</td>
                            <td>${applicationViewDto.currentStatus}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div align="center">
        <a href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?appId=${applicationViewDto.applicationDto.id}" target="_blank">
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
                    <table class="table table-bordered">
                        <tbody>
                        <tr>
                            <td class="col-xs-6" align="right">HCI Code</td>
                            <td class="col-xs-6">-</td>
                        </tr>
                        <tr>
                            <td align="right">HCI Name</td>
                            <td>${applicationViewDto.hciName}</td>
                        </tr>
                        <tr>
                            <td align="right">HCI Address</td>
                            <td>${applicationViewDto.hciAddress}</td>
                        </tr>
                        <tr>
                            <td align="right">Telephone</td>
                            <td>${applicationViewDto.telephone}</td>
                        </tr>
                        <tr>
                            <td align="right">Fax</td>
                            <td>-</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
