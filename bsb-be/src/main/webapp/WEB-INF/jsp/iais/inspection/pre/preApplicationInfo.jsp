<div class="panel panel-default">
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <%--@elvariable id="apptReviewData" type="sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentReviewDataDto"--%>
                <c:set value="${apptReviewData.submissionDetailsDto}" var="detailDto"/>
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <th scope="col" style="display: none"></th>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application No.</td>
                        <td style="padding-left : 20px"><c:out value="${detailDto.applicationNo}"/></td>
                    </tr>
                    <tr>
                        <td class="col-xs-6" style="text-align: right">Application Type</td>
                        <td class="col-xs-6" style="padding-left : 20px"><iais:code code="${detailDto.applicationType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application Sub-Type</td>
                        <td style="padding-left : 20px"><iais:code code="${detailDto.processType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application Status</td>
                        <td style="padding-left : 20px"><iais:code code="${detailDto.currentStatus}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="text-align: center">
    <a href="javascript:void(0);" onclick="viewApplication('${maskedAppId}', '${maskedAppViewModuleType}')">
        <button type="button" class="btn btn-primary">
            View Application
        </button>
    </a>

    <c:if test="${selfAssessmentAvailable eq true}">
        <button id="viewSelfAssessmt" type="button" class="btn btn-primary">
            Self-Assessment Checklists
        </button>
    </c:if>
</div>
<div style="text-align: left">
    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Previous</a>
</div>