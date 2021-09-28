<div class="panel panel-default">
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <td class="col-xs-6" align="right">Application No.</td>
                        <td class="col-xs-6" style="padding-left : 20px">${application.applicationNo}</td>
                    </tr>
                    <tr>
                        <td align="right">Application Type</td>
                        <td style="padding-left : 20px"><iais:code code="${application.appType}"></iais:code></td>
                    </tr>
                    <tr>
                        <td align="right">Process Type</td>
                        <td style="padding-left : 20px"><iais:code code="${application.processType}"></iais:code></td>
                    </tr>
                    <tr>
                        <td align="right">Facility Type</td>
                        <td style="padding-left : 20px"><iais:code code="${application.facility.activeType}"></iais:code></td>
                    </tr>
                    <tr>
                        <td align="right">Facility Name/Address</td>
                        <td style="padding-left : 20px">${application.facility.facilityName} / Block ${application.facility.blkNo} ${application.facility.streetName} ${application.facility.floorNo}-${application.facility.unitNo} Singapore ${application.facility.postalCode}</td>
                    </tr>
                    <tr>
                        <td align="right">Submission Date</td>
                        <td style="padding-left : 20px"><fmt:formatDate value='${application.applicationDt}' pattern='dd/MM/yyyy'/></td>
                    </tr>
                    <tr>
                        <td align="right">Application Status</td>
                        <td style="padding-left : 20px"><iais:code code="${application.status}"></iais:code></td>
                    </tr>
                    <tr>
                        <td align="right">Facility/Approval Expiry Date</td>
                        <td style="padding-left : 20px"><fmt:formatDate value='${application.facility.expiryDt}' pattern='dd/MM/yyyy'/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
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
<div align="center">
    <a href="javascript:void(0);" onclick="javascript:doOpenApp()">
        <button type="button" class="btn btn-primary">
            View Application
        </button>
    </a>
</div>
<div>&nbsp</div>
<div class="panel panel-default">
    <div class="panel-heading"><strong>List of Agent / Toxin</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table table-bordered" style="margin-bottom: 0">
                    <thead>
                    <tr>
                        <th style="text-align:center;width:5%" align="center">S/N</th>
                        <th style="text-align:center;">Schedule</th>
                        <th style="text-align:center;">Biological Agent / Toxin</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${application.biologicalList}" varStatus="status">
                        <tr>
                            <td align="center">
                                <p><c:out value="${status.index + 1}"/></p>
                            </td>
                            <td align="center">
                                <p><iais:code code="${item.schedule}"></iais:code></p>
                            </td>
                            <td align="center">
                                <p><c:out value="${item.name}"/></p>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div align="left">
    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
</div>