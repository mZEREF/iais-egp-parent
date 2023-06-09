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
                            <td class="col-xs-6" align="right">Facility Name</td>
                            <td style="padding-left : 20px"><c:out value="${selfAudit.facName}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Address</td>
                            <td style="padding-left : 20px"><c:out value="${selfAudit.facAddress}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="${selfAudit.facClassification}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Type</td>
                            <td style="padding-left : 20px"><c:out value="${selfAudit.activityType}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Audit Type</td>
                            <td style="padding-left : 20px"><iais:code code="${selfAudit.auditType}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Audit Date</td>
                            <td style="padding-left : 20px"><fmt:formatDate value='${selfAudit.auditDate}' pattern='dd/MM/yyyy'/></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div align="left">
    <a class="back" href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em>Back</a>
</div>