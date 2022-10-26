<c:set var="per" value="${processDto.incidentPersonnelDto}"/>
<div class="panel panel-default">
    <div class="panel-heading"><strong>Facility Administrator/Officer</strong></div>
    <div class="panel-collapse in collapse">
        <div class="panel-main-content form-horizontal min-row">
            <div class="table-responsive">
                <div class="table-gp">
                    <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                            <th scope="col" style="display: none"></th>
                        </tr>
                        <tr>
                            <td>Name</td>
                            <td>Organisation</td>
                            <td>Address</td>
                            <td>Designation</td>
                            <td>Contact No.</td>
                            <td>Email Address</td>
                            <td>Employment Start Date</td>
                            <td>Type</td>
                            <td>Is Main</td>
                        </tr>
                        </thead>
                        <tbody class="form-horizontal">
                        <c:forEach var="admin" items="${per.facilityAdminList}">
                            <tr style="text-align: center">
                                <td><c:out value="${admin.name}"/></td>
                                <td><iais:code code="${admin.organisation}"/></td>
                                <td><c:out value="${admin.address}"/></td>
                                <td><c:out value="${admin.designation}"/></td>
                                <td><c:out value="${admin.contactNo}"/></td>
                                <td><c:out value="${admin.email}"/></td>
                                <td><c:out value="${admin.employmentDate}"/></td>
                                <td><c:out value="${admin.type}"/></td>
                                <td><c:out value="${admin.isMain}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-heading"><strong>Biosafety Commitee Member</strong></div>
    <div class="panel-collapse in collapse">
        <div class="panel-main-content form-horizontal min-row">
            <div class="table-responsive">
                <div class="table-gp">
                    <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                            <th scope="col" style="display: none"></th>
                        </tr>
                        <tr style="text-align: center">
                            <td>Name</td>
                            <td>Nationality</td>
                            <td>NRIC/FIN</td>
                            <td>Designation</td>
                            <td>Contact No.</td>
                            <td>Email Address</td>
                            <td>Employment Start Date</td>
                            <td>Area of Expertise</td>
                            <td>Role</td>
                            <td>Is Employee</td>
                        </tr>
                        </thead>
                        <tbody class="form-horizontal">
                        <c:forEach var="commitee" items="${per.commiteeDtoList}">
                            <tr style="text-align: center">
                                <td><c:out value="${commitee.name}"/></td>
                                <td><iais:code code="${commitee.nationality}"/></td>
                                <td><c:out value="${commitee.idNumber}"/></td>
                                <td><c:out value="${commitee.designation}"/></td>
                                <td><c:out value="${commitee.contactNo}"/></td>
                                <td><c:out value="${commitee.emailAddr}"/></td>
                                <td><c:out value="${commitee.employmentDate}"/></td>
                                <td><c:out value="${commitee.areaOfExpertise}"/></td>
                                <td><c:out value="${commitee.role}"/></td>
                                <td><c:out value="${commitee.employeeOfComp}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-heading"><strong>Personnel Authorised to Access the Facility</strong></div>
    <div class="panel-collapse in collapse">
        <div class="panel-main-content form-horizontal min-row">
            <div class="table-responsive">
                <div class="table-gp">
                    <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                            <th scope="col" style="display: none"></th>
                        </tr>
                        <tr style="text-align: center">
                            <td>Name</td>
                            <td>Nationality</td>
                            <td>NRIC/FIN</td>
                            <td>Designation</td>
                            <td>Contact No.</td>
                            <td>Email Address</td>
                            <td>Employment Start Date</td>
                            <td>Employment Period</td>
                            <td>Security Clearance Date</td>
                            <td>Area of Work</td>
                        </tr>
                        </thead>
                        <tbody class="form-horizontal">
                        <c:forEach var="author" items="${per.personnelAuthorisedList}">
                            <tr style="text-align: center">
                                <td><c:out value="${author.name}"/></td>
                                <td><iais:code code="${author.nationality}"/></td>
                                <td><c:out value="${author.idNumber}"/></td>
                                <td><c:out value="${author.designation}"/></td>
                                <td><c:out value="${author.contactNo}"/></td>
                                <td><c:out value="${author.email}"/></td>
                                <td><c:out value="${author.employmentDate}"/></td>
                                <td><c:out value="${author.employmentPeriod}"/></td>
                                <td><c:out value="${author.clearanceDate}"/></td>
                                <td><c:out value="${author.workArea}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div style="text-align: left">
    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
</div>
