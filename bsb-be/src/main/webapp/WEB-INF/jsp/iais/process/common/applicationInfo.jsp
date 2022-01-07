<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil"%>
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
                        <td class="col-xs-6" style="text-align: right">Application No.</td>
                        <td class="col-xs-6" style="padding-left : 20px">${mohProcessDto.submitDetailsDto.applicationNo}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application Type</td>
                        <td style="padding-left : 20px"><iais:code code="${mohProcessDto.submitDetailsDto.appType}"></iais:code></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Process Type</td>
                        <td style="padding-left : 20px"><iais:code code="${mohProcessDto.submitDetailsDto.processType}"></iais:code></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Activity Type</td>
                        <td style="padding-left : 20px"><iais:code code="${mohProcessDto.submitDetailsDto.activityType}"></iais:code></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility Name/Address</td>
                        <td style="padding-left : 20px">${mohProcessDto.submitDetailsDto.facilityName}/${TableDisplayUtil.getOneLineAddress(mohProcessDto.submitDetailsDto.blkNo, mohProcessDto.submitDetailsDto.streetName, mohProcessDto.submitDetailsDto.floorNo, mohProcessDto.submitDetailsDto.unitNo, mohProcessDto.submitDetailsDto.postalCode)}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Submission Date</td>
                        <td style="padding-left : 20px">${mohProcessDto.submitDetailsDto.applicationDate}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application Status</td>
                        <td style="padding-left : 20px"><iais:code code="${mohProcessDto.submitDetailsDto.status}"></iais:code></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility/Approval Expiry Date</td>
                        <td style="padding-left : 20px"><c:out value="${mohProcessDto.submitDetailsDto.facilityOrApprovalExpiryDate}"/></td>
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
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <th scope="col" style="display: none"></th>
                    </tr>
                    <tr>
                        <td class="col-xs-6" style="text-align: right">Facility/Organisation Name</td>
                        <td class="col-xs-6" style="padding-left : 20px">${mohProcessDto.submitDetailsDto.facilityOrOrganisationName}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility/Organisation Address</td>
                        <td style="padding-left : 20px">${mohProcessDto.submitDetailsDto.facilityOrOrganisationAddress}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility/Organisation Admin</td>
                        <td style="padding-left : 20px">${mohProcessDto.submitDetailsDto.facilityOrOrganisationAdmin}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Telephone</td>
                        <td style="padding-left : 20px">${mohProcessDto.submitDetailsDto.telephone}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Email</td>
                        <td style="padding-left : 20px">${mohProcessDto.submitDetailsDto.email}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="text-align: center">
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
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <thead>
                    <tr>
                        <th scope="col" style="text-align:center;width:5%">S/N</th>
                        <th scope="col" style="text-align:center;">Schedule</th>
                        <th scope="col" style="text-align:center;">Biological Agent / Toxin</th>
                    </tr>
                    </thead>
                    <tbody style="text-align:center;">
                    <c:forEach var="item" items="${mohProcessDto.submitDetailsDto.biologicalList}" varStatus="status">
                        <tr>
                            <td>
                                <p><c:out value="${status.index + 1}"/></p>
                            </td>
                            <td>
                                <p><iais:code code="${item.schedule}"></iais:code></p>
                            </td>
                            <td>
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
<div style="text-align: left">
    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
</div>