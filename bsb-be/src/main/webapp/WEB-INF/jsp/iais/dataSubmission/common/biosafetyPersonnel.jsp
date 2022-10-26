<div class="panel panel-default">
    <div class="panel-heading"><strong>Facility Administrator/Officer</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <%--@elvariable id="processDto" type="sg.gov.moh.iais.egp.bsb.dto.datasubmission.MohReviewDataSubmissionDto"--%>
                <c:set var="adminList" value="${processDto.facilityAdminDtoList}"/>
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <thead>
                    <tr>
                        <th scope="col" style="text-align:center;">Name</th>
                        <th scope="col" style="text-align:center;">Nationality</th>
                        <th scope="col" style="text-align:center;">NRIC/FIN</th>
                        <th scope="col" style="text-align:center;">Designation</th>
                        <th scope="col" style="text-align:center;">Contact No.</th>
                        <th scope="col" style="text-align:center;">Email Address</th>
                        <th scope="col" style="text-align:center;">Employment Start Date</th>
                        <th scope="col" style="text-align:center;">Type</th>
                        <th scope="col" style="text-align:center;">Is Main</th>
                    </tr>
                    </thead>
                    <tbody style="text-align:center;">
                    <c:if test="${adminList ne null}">
                    <c:forEach var="admin" items="${adminList}" varStatus="status">
                        <tr>
                            <td>
                                <p><c:out value="${admin.name}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${admin.nationality}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${admin.idNumber}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${admin.designation}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${admin.contactNo}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${admin.email}"/></p>
                            </td>
                            <td>
                                <p><fmt:formatDate value="${admin.employmentStartDate}" pattern="dd/MM/yyyy"/></p>
                            </td>
                            <td>
                                <p><c:out value="Facility Admin"/></p>
                            </td>
                            <td>
                                <p><c:out value="????"/></p>
                            </td>
                        </tr>
                    </c:forEach>
                    </c:if>
                    <c:set var="officer" value="${processDto.facilityOfficerDto}"/>
                    <c:if test="${officer ne null}">
                        <tr>
                            <td>
                                <p><c:out value="${officer.name}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${officer.nationality}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${officer.idNumber}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${officer.designation}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${officer.contactNo}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${officer.email}"/></p>
                            </td>
                            <td>
                                <p><fmt:formatDate value="${officer.employmentStartDate}" pattern="dd/MM/yyyy"/></p>
                            </td>
                            <td>
                                <p><c:out value="Facility Officer"/></p>
                            </td>
                            <td>
                                <p><c:out value="????"/></p>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div>&nbsp</div>
<div class="panel panel-default">
    <div class="panel-heading"><strong>Biosafety Committee Member</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <c:set var="committeeList" value="${processDto.facilityBiosafetyCommitteeDtoList}"/>
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <thead>
                    <tr>
                        <th scope="col" style="text-align:center;">Name</th>
                        <th scope="col" style="text-align:center;">Nationality</th>
                        <th scope="col" style="text-align:center;">NRIC/FIN</th>
                        <th scope="col" style="text-align:center;">Designation</th>
                        <th scope="col" style="text-align:center;">Contact No.</th>
                        <th scope="col" style="text-align:center;">Email Address</th>
                        <th scope="col" style="text-align:center;">Employment Start Date</th>
                        <th scope="col" style="text-align:center;">Area of Expertise</th>
                        <th scope="col" style="text-align:center;">Role</th>
                        <th scope="col" style="text-align:center;">Is Employee</th>
                    </tr>
                    </thead>
                    <tbody style="text-align:center;">
                    <c:if test="${committeeList ne null}">
                    <c:forEach var="committee" items="${committeeList}" varStatus="status">
                        <tr>
                            <td>
                                <p><c:out value="${committee.name}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${committee.nationality}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${committee.idNumber}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${committee.designation}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${committee.contactNo}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${committee.emailAddr}"/></p>
                            </td>
                            <td>
                                <p><fmt:formatDate value="${committee.employmentStartDate}" pattern="dd/MM/yyyy"/></p>
                            </td>
                            <td>
                                <p><c:out value="${committee.areaOfExpertise}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${committee.role}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${committee.employeeOfComp}"/></p>
                            </td>
                        </tr>
                    </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div>&nbsp</div>
<div class="panel panel-default">
    <div class="panel-heading"><strong>Personnel Authorised to Access the Facility</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <c:set var="authoriserList" value="${processDto.facilityAuthoriserDtoList}"/>
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <thead>
                    <tr>
                        <th scope="col" style="text-align:center;">Name</th>
                        <th scope="col" style="text-align:center;">Nationality</th>
                        <th scope="col" style="text-align:center;">NRIC/FIN</th>
                        <th scope="col" style="text-align:center;">Designation</th>
                        <th scope="col" style="text-align:center;">Contact No.</th>
                        <th scope="col" style="text-align:center;">Email Address</th>
                        <th scope="col" style="text-align:center;">Employment Start Date</th>
                        <th scope="col" style="text-align:center;">Employment Period</th>
                        <th scope="col" style="text-align:center;">Security Clearance Date</th>
                        <th scope="col" style="text-align:center;">Area of Work</th>
                    </tr>
                    </thead>
                    <tbody style="text-align:center;">
                    <c:if test="${authoriserList ne null}">
                    <c:forEach var="authoriser" items="${authoriserList}" varStatus="status">
                        <tr>
                            <td>
                                <p><c:out value="${authoriser.name}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${authoriser.nationality}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${authoriser.idNumber}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${authoriser.designation}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${authoriser.contactNo}"/></p>
                            </td>
                            <td>
                                <p><c:out value="${authoriser.email}"/></p>
                            </td>
                            <td>
                                <p><fmt:formatDate value="${authoriser.employmentStartDate}" pattern="dd/MM/yyyy"/></p>
                            </td>
                            <td>
                                <p><c:out value="${authoriser.employmentPeriod}"/></p>
                            </td>
                            <td>
                                <p><fmt:formatDate value="${authoriser.securityClearanceDate}" pattern="dd/MM/yyyy"/></p>
                            </td>
                            <td>
                                <p><c:out value="${authoriser.workArea}"/></p>
                            </td>
                        </tr>
                    </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="text-align: left">
    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
</div>