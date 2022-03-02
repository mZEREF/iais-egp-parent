<%--@elvariable id="insInfo" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsFacInfoDto"--%>
<%--@elvariable id="selfAssessmentUnavailable" type="java.lang.Boolean"--%>
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
                            <td class="col-xs-6" style="padding-left : 20px">${insInfo.facName}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Facility Address</td>
                            <td style="padding-left : 20px">${TableDisplayUtil.getOneLineAddress(insInfo.blk, insInfo.street, insInfo.street, insInfo.unit, insInfo.postalCode)}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="${insInfo.classification}"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Person-in-charge</td>
                            <td style="padding-left : 20px">${insInfo.adminName}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Contact information of person-in-charge</td>
                            <td style="padding-left : 20px">${insInfo.adminMobileNo} / ${insInfo.adminEmail}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Current Status</td>
                            <td style="padding-left : 20px"><iais:code code="${insInfo.appStatus}"/></td>
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

    <c:if test="${selfAssessmentUnavailable == null || !selfAssessmentUnavailable}">
    <button id="viewSelfAssessmt" type="button" class="btn btn-primary">
        Self-Assessment Checklists
    </button>
    </c:if>
</div>