<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><strong>Submission Details</strong></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td align="right">Application Type</td>
                            <td style="padding-left : 20px">${applicationViewDto.applicationType}</td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">Application No. (Overall)
                            </td>
                            <td class="col-xs-6" style="padding-left : 20px">${applicationViewDto.applicationNoOverAll}</td>
                        </tr>
                        <tr>
                            <td align="right">Application No.</td>
                            <td style="padding-left : 20px">${applicationViewDto.applicationDto.applicationNo}</td>
                        </tr>
                        <tr>
                            <td align="right">Service Type</td>
                            <td style="padding-left : 20px">${applicationViewDto.serviceType}</td>
                        </tr>
                        <tr>
                            <td align="right">Submission Date</td>
                            <td style="padding-left : 20px">${applicationViewDto.submissionDate}</td>
                        </tr>
                        <tr>
                            <td align="right">Current Status</td>
                            <td style="padding-left : 20px">${applicationViewDto.currentStatus}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div align="center">
        <a   href="javascript:void(0);" onclick="javascript:doOpenApp()">
            <button type="button" class="btn btn-primary">
                View Application
            </button>
        </a>
        <c:if test="${'APST004' eq applicationViewDto.applicationDto.status}">
            <c:if test="${not empty commonDto}">
                <button type="button" class="btn btn-primary" onclick="javascript:doInspectionPreTaskSelfBack()">
                    Self-Assessment Checklists
                </button>
            </c:if>
            <c:if test="${empty commonDto}">
                <button type="button" class="btn btn-primary" disabled>
                    Self-Assessment Checklists
                </button>
            </c:if>
        </c:if>
        <c:if test="${'APST022' eq applicationViewDto.applicationDto.status}">
            <button type="button" class="btn btn-primary" onclick="javascript:doInspectorProRecView()">
                View Checklist
            </button>
        </c:if>
    </div>
    <div>&nbsp</div>
    <div class="panel panel-default">
        <div class="panel-heading"><strong>Applicant Details</strong></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td class="col-xs-6" align="right">HCI Code</td>
                                <td class="col-xs-6" style="padding-left : 20px">${applicationViewDto.hciCode == null || applicationViewDto.hciCode == "" ? '-' : applicationViewDto.hciCode}</td>
                            </tr>
                            <tr>
                                <td align="right">HCI Name</td>
                                <td style="padding-left : 20px">${applicationViewDto.hciName == null || applicationViewDto.hciName == "" ? '-' : applicationViewDto.hciName}</td>
                            </tr>
                            <tr>
                                <td align="right">HCI Address</td>
                                <td style="padding-left : 20px">${applicationViewDto.hciAddress == null || applicationViewDto.hciAddress == "" ? '-' : applicationViewDto.hciAddress}</td>
                            </tr>
                            <tr>
                                <td align="right">Telephone</td>
                                <td style="padding-left : 20px">${applicationViewDto.telephone == null || applicationViewDto.telephone == "" ? '-' : applicationViewDto.telephone}</td>
                            </tr>
                            <c:if test="${'INSPECTOR' eq iais_Login_User_Info_Attr.curRoleId}">
                                <c:if test="${'APST003' eq applicationViewDto.applicationDto.status || 'APST058' eq applicationViewDto.applicationDto.status}">
                                    <tr>
                                        <td align="right">Preferred Inspection Date</td>
                                        <c:if test="${applicationViewDto.applicationGroupDto.prefInspStartDate != null &&
                                                    applicationViewDto.applicationGroupDto.prefInspEndDate != null}">
                                            <td style="padding-left : 20px">
                                                <fmt:formatDate value='${applicationViewDto.applicationGroupDto.prefInspStartDate}' pattern='dd/MM/yyyy'/> - <fmt:formatDate value='${applicationViewDto.applicationGroupDto.prefInspEndDate}' pattern='dd/MM/yyyy'/>
                                            </td>
                                        </c:if>
                                        <c:if test="${applicationViewDto.applicationGroupDto.prefInspStartDate == null ||
                                                    applicationViewDto.applicationGroupDto.prefInspEndDate == null}">
                                            <td style="padding-left : 20px">
                                                <c:out value="-"></c:out>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:if>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div align="left">
        <a class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
    </div>
    <script type="text/javascript">
        function doOpenApp() {
            window.open ("/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService");
        }
    </script>

