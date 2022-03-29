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
                        <td class="col-xs-6" style="text-align: right">Reference No.</td>
                        <td class="col-xs-6" style="padding-left : 20px"><c:out value="${infoDto.referenceNo}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application Type</td>
                        <td style="padding-left : 20px"><iais:code code="${infoDto.applicationType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Submission Type</td>
                        <td style="padding-left : 20px"><iais:code code="${infoDto.submissionType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application Status</td>
                        <td style="padding-left : 20px"><iais:code code="${infoDto.applicationStatus}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<c:set var="maskedAppId"><iais:mask name="appId" value="${appId}"/></c:set>
<c:set var="maskedAppViewModuleType"><iais:mask name="appViewModuleType" value="${appViewModuleType}"/></c:set>
<div style="text-align: center">
    <a href="javascript:void(0);" onclick="viewApplication('${maskedAppId}', '${maskedAppViewModuleType}')">
        <button type="button" class="btn btn-primary">
            View Application
        </button>
    </a>
</div>
<div style="text-align: left">
    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Previous</a>
</div>