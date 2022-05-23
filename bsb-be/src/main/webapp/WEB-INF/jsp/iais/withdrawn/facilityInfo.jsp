<div class="panel panel-default">
    <div class="panel-heading">Submission Details</div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <%--@elvariable id="withdrawnDto" type="sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto"--%>
                        <tr>
                            <th scope="col" style="display: none"></th>
                        </tr>
                        <tr>
                            <td class="col-xs-6" style="text-align: right">Application Type</td>
                            <td style="padding-left : 20px"><iais:code code="${withdrawnDto.appType}"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Application No.</td>
                            <td style="padding-left : 20px"><c:out value="${withdrawnDto.appNo}"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="${withdrawnDto.facClassification}"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Process Type</td>
                            <td style="padding-left : 20px"><iais:code code="${withdrawnDto.processType}"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Submission Date</td>
                            <td style="padding-left : 20px"><fmt:formatDate value="${withdrawnDto.createDate}" pattern="dd/MM/yyyy"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Current Status</td>
                            <td style="padding-left : 20px"><iais:code code="${withdrawnDto.currentStatus}"/></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="text-align: left">
    <a class="back" href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em>Back</a>
</div>