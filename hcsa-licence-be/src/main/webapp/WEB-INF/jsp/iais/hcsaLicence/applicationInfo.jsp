
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><strong>Submission Details</strong></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table table-bordered" style="margin-bottom: 0">
                        <tbody>
                        <tr>
                            <td align="right">Application Type</td>
                            <td>${applicationViewDto.applicationType}</td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">Application No. (Overall)
                            </td>
                            <td class="col-xs-6">${applicationViewDto.applicationNoOverAll}</td>
                        </tr>
                        <tr>
                            <td align="right">Application No.</td>
                            <td>${applicationViewDto.applicationDto.applicationNo}</td>
                        </tr>
                        <tr>
                            <td align="right">Service Type</td>
                            <td>${applicationViewDto.serviceType}</td>
                        </tr>
                        <tr>
                            <td align="right">Submission Date</td>
                            <td>${applicationViewDto.submissionDate}</td>
                        </tr>
                        <tr>
                            <td align="right">Current Status</td>
                            <td>${applicationViewDto.currentStatus}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div align="center">
        <a href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService" target="_blank">
            <button type="button" class="btn btn-primary">
                View Application
            </button>
            <c:if test="${'APST004' eq applicationViewDto.applicationDto.status}">
                <c:if test="${not empty commonDto.sectionDtoList}">
                    <button type="button" class="btn btn-primary" onclick="javascript:doInspectionPreTaskSelfBack()">
                        Self-Assessment Checklists
                    </button>
                </c:if>
                <c:if test="${empty commonDto.sectionDtoList}">
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
        </a>
    </div>
    <div>&nbsp</div>
    <div class="panel panel-default">
        <div class="panel-heading"><strong>Applicant Details</strong></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table table-bordered" style="margin-bottom: 0">
                        <tbody>
                        <tr>
                            <td class="col-xs-6" align="right">HCI Code</td>
                            <td class="col-xs-6">${applicationViewDto.hciCode == null || applicationViewDto.hciCode == "" ? '-' : applicationViewDto.hciCode}</td>
                        </tr>
                        <tr>
                            <td align="right">HCI Name</td>
                            <td>${applicationViewDto.hciName == null || applicationViewDto.hciName == "" ? '-' : applicationViewDto.hciName}</td>
                        </tr>
                        <tr>
                            <td align="right">HCI Address</td>
                            <td>${applicationViewDto.hciAddress == null || applicationViewDto.hciAddress == "" ? '-' : applicationViewDto.hciAddress}</td>
                        </tr>
                        <tr>
                            <td align="right">Telephone</td>
                            <td>${applicationViewDto.telephone == null || applicationViewDto.telephone == "" ? '-' : applicationViewDto.telephone}</td>
                        </tr>
                        <%--                                                    <tr>--%>
                        <%--                                                        <td align="right">Fax</td>--%>
                        <%--                                                        <td>-</td>--%>
                        <%--                                                    </tr>--%>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

