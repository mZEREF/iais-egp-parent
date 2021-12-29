<%--@elvariable id="insFacInfo" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsFacInfoDto"--%>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<div class="panel panel-default">
    <div class="panel-heading"><strong>Facility Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                        <tr>
                            <td class="col-xs-6" style="text-align: right">Facility Name</td>
                            <td class="col-xs-6" style="padding-left : 20px">${insFacInfo.facName}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Facility Address</td>
                            <td style="padding-left : 20px">${TableDisplayUtil.getOneLineAddress(insFacInfo.blk, insFacInfo.street, insFacInfo.street, insFacInfo.unit, insFacInfo.postalCode)}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="${insFacInfo.classification}"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Person-in-charge</td>
                            <td style="padding-left : 20px">${insFacInfo.adminName}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Contact information of person-in-charge</td>
                            <td style="padding-left : 20px">${insFacInfo.adminMobileNo} / ${insFacInfo.adminEmail}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Current Status</td>
                            <td style="padding-left : 20px"><iais:code code="${insFacInfo.appStatus}"/></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div style="text-align: center">
    <a href="javascript:void(0);" onclick="viewApplication()">
        <button type="button" class="btn btn-primary">
            View Application
        </button>
    </a>

    <button id="viewSelfAssessmt" type="button" class="btn btn-primary">
        Self-Assessment Checklists
    </button>
</div>