<div class="panel panel-default">
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                        <tr>
                            <td class="col-xs-6" align="right">Facility Name</td>
                            <td style="padding-left : 20px"><c:out value="${facility.facilityName}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Address</td>
                            <td style="padding-left : 20px"><c:out value="${facility.facilityAddress}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="${facility.facilityClassification}"></iais:code></td>
                        </tr>
                        <tr>
                            <td align="right">Approval</td>
                            <td style="padding-left : 20px"><c:out value="${facility.approval}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Approval Status</td>
                            <td style="padding-left : 20px"><iais:code code="${facility.approvalStatus}"></iais:code></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div align="left">
    <a class="back" id="back" name="back" href="#"><em class="fa fa-angle-left"></em>Back</a>
</div>