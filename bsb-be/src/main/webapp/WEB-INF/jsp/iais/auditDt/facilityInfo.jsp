<div class="panel panel-default">
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <%--@elvariable id="processData" type="sg.gov.moh.iais.egp.bsb.dto.audit.OfficerProcessAuditDto"--%>
                        <tr>
                            <th scope="col" style="display: none"></th>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">Facility Name</td>
                            <td style="padding-left : 20px"><c:out value="${processData.facName}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Address</td>
                            <td style="padding-left : 20px"><c:out value="${processData.facAddress}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="${processData.facClassification}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Type</td>
                            <td style="padding-left : 20px"><c:out value="${processData.activityType}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Audit Type</td>
                            <td style="padding-left : 20px"><iais:code code="${processData.auditType}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Audit Date</td>
                            <td style="padding-left : 20px"><fmt:formatDate value='${processData.auditDate}' pattern='dd/MM/yyyy'/></td>
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