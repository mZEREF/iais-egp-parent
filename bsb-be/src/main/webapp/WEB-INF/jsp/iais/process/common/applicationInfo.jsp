<div class="panel panel-default">
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <td class="col-xs-6" align="right">Application No.</td>
                        <td class="col-xs-6" style="padding-left : 20px">${applicationInfo.applicationNo}</td>
                    </tr>
                    <tr>
                        <td align="right">Application Type</td>
                        <td style="padding-left : 20px"><iais:code code="${applicationInfo.appType}"></iais:code></td>
                    </tr>
                    <tr>
                        <td align="right">Process Type</td>
                        <td style="padding-left : 20px"><iais:code code="${applicationInfo.processType}"></iais:code></td>
                    </tr>
                    <tr>
                        <td align="right">Facility Type</td>
                        <td style="padding-left : 20px"><iais:code code="${applicationInfo.facility.facilityType}"></iais:code></td>
                    </tr>
                    <tr>
                        <td align="right">Facility Name/Address</td>
                        <td style="padding-left : 20px">${applicationInfo.facility.facilityName} / Block ${applicationInfo.facility.blkNo} ${applicationInfo.facility.streetName} ${applicationInfo.facility.floorNo}-${applicationInfo.facility.unitNo} Singapore ${applicationInfo.facility.postalCode}</td>
                    </tr>
                    <tr>
                        <td align="right">Agents/Toxins</td>
                        <td style="padding-left : 20px">${applicationInfo.facility.biological.name}</td>
                    </tr>
                    <tr>
                        <td align="right">Submission Date</td>
                        <td style="padding-left : 20px"><fmt:formatDate value='${applicationInfo.applicationDt}' pattern='dd/MM/yyyy'/></td>
                    </tr>
                    <tr>
                        <td align="right">Application Status</td>
                        <td style="padding-left : 20px"><iais:code code="${applicationInfo.status}"></iais:code></td>
                    </tr>
                    <tr>
                        <td align="right">Facility/Approval Expiry Date</td>
                        <td style="padding-left : 20px"><fmt:formatDate value='${applicationInfo.facility.expiryDt}' pattern='dd/MM/yyyy'/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div align="center">
    <a   href="javascript:void(0);" onclick="javascript:doOpenApp()">
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
                <table class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <td class="col-xs-6" align="right">Facility/Organisation Name</td>
                        <td class="col-xs-6" style="padding-left : 20px">ABC</td>
                    </tr>
                    <tr>
                        <td align="right">Facility/Organisation Address</td>
                        <td style="padding-left : 20px">Lot 10,Tao Payoh,Jalan 1,106780,Singapore</td>
                    </tr>
                    <tr>
                        <td align="right">Facility/Organisation Admin</td>
                        <td style="padding-left : 20px">Mr Admin</td>
                    </tr>
                    <tr>
                        <td align="right">Telephone</td>
                        <td style="padding-left : 20px">64825525</td>
                    </tr>
                    <tr>
                        <td align="right">Email</td>
                        <td style="padding-left : 20px">Facility@yahoo.com</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div align="left">
    <a class="back" href="/system-admin-web/eservice/INTRANET/MohDutyApprovingInbox?"><em class="fa fa-angle-left"></em> Back</a>
</div>