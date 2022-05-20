<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading"><strong>Licensee Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered">
                    <thead style="display: none">
                    <tr>
                        <th scope="col"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="col-xs-6" align="right">Licensee Type</td>
                        <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${organizationLicDto.licenseeDto.licenseeType}"/></td>
                    </tr>
                    <tr>
                        <td align="right">UEN</td>
                        <td class="col-xs-6" style="padding-left: 15px;">${organizationLicDto.uenNo}<c:if test="${empty organizationLicDto.uenNo}">-</c:if></td>
                    </tr>
                    <tr>
                        <td align="right">Are you registered with ACRA/ROS</td>
                        <td class="col-xs-6" style="padding-left: 15px;">${registeredWithACRA}</td>
                    </tr>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div class="panel panel-default">
    <div class="panel-heading"><strong>Company Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" >
                    <thead style="display: none">
                    <tr><th scope="col"></th></tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="col-xs-6" align="right">Company Name</td>
                        <td class="col-xs-6" style="padding-left: 15px;">${organizationLicDto.licenseeDto.name}<c:if test="${empty organizationLicDto.licenseeDto.name}">-</c:if></td>
                    </tr>
                    <tr>
                        <td align="right">Postal Code</td>
                        <td class="col-xs-6" style="padding-left: 15px;">${organizationLicDto.licenseeDto.postalCode}<c:if test="${empty organizationLicDto.licenseeDto.postalCode}">-</c:if></td>
                    </tr>
                    <tr>
                        <td align="right">Block No</td>
                        <td class="col-xs-6" style="padding-left: 15px;">${organizationLicDto.licenseeDto.blkNo}<c:if test="${empty organizationLicDto.licenseeDto.blkNo}">-</c:if></td>
                    </tr>
                    <tr>
                        <td align="right">Floor No</td>
                        <td class="col-xs-6" style="padding-left: 15px;">${organizationLicDto.licenseeDto.floorNo}<c:if test="${empty organizationLicDto.licenseeDto.floorNo}">-</c:if></td>
                    </tr>
                    <tr>
                        <td align="right">Unit No</td>
                        <td class="col-xs-6" style="padding-left: 15px;">${organizationLicDto.licenseeDto.unitNo}<c:if test="${empty organizationLicDto.licenseeDto.unitNo}">-</c:if></td>
                    </tr>
                    <tr>
                        <td align="right">Street Name</td>
                        <td class="col-xs-6" style="padding-left: 15px;">${organizationLicDto.licenseeDto.streetName}<c:if test="${empty organizationLicDto.licenseeDto.streetName}">-</c:if></td>
                    </tr>
                    <tr>
                        <td align="right">Building Name</td>
                        <td class="col-xs-6" style="padding-left: 15px;">${organizationLicDto.licenseeDto.buildingName}<c:if test="${empty organizationLicDto.licenseeDto.buildingName}">-</c:if></td>
                    </tr>
                    <tr>
                        <td align="right">Office Number</td>
                        <td class="col-xs-6" style="padding-left: 15px;">${organizationLicDto.licenseeEntityDto.officeTelNo}<c:if test="${empty organizationLicDto.licenseeEntityDto.officeTelNo}">-</c:if></td>
                    </tr>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<c:if test="${  organizationLicDto.licenseeDto.licenseeType=='Company'}">
    <c:if test="${empty organizationLicDto.licenseeKeyApptPersonDtos}">
        <div class="panel panel-default">
            <div class="panel-heading"><strong>Board Members</strong></div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="table-gp">
                        <table aria-describedby="" class="table table-bordered">
                            <thead style="display: none">
                            <tr>
                                <th scope="col"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td align="right">Salutation</td>
                                <td class="col-xs-6" style="padding-left: 15px;">-</td>
                            </tr>
                            <tr>
                                <td class="col-xs-6" align="right">Name</td>
                                <td class="col-xs-6" style="padding-left: 15px;">-</td>
                            </tr>
                            <tr>
                                <td align="right">ID Type</td>
                                <td class="col-xs-6" style="padding-left: 15px;">-</td>
                            </tr>
                            <tr>
                                <td align="right">ID No</td>
                                <td class="col-xs-6" style="padding-left: 15px;">-</td>
                            </tr>
                            <tr>
                                <td align="right">Designation</td>
                                <td class="col-xs-6" style="padding-left: 15px;">-</td>
                            </tr>
                            <tr>
                                <td align="right">Designation Appointment Date</td>
                                <td class="col-xs-6" style="padding-left: 15px;">-</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <c:forEach var="member" items="${organizationLicDto.licenseeKeyApptPersonDtos}" varStatus="status">

        <div class="panel panel-default">
            <div class="panel-heading"><strong>Board Members</strong></div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="table-gp">
                        <table aria-describedby="" class="table table-bordered">
                            <thead style="display: none">
                            <tr>
                                <th scope="col"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td align="right">Salutation</td>
                                <td class="col-xs-6" style="padding-left: 15px;">${member.salutation}<c:if test="${empty member.salutation}">-</c:if></td>
                            </tr>
                            <tr>
                                <td class="col-xs-6" align="right">Name</td>
                                <td class="col-xs-6" style="padding-left: 15px;">${member.name}<c:if test="${empty member.name}">-</c:if></td>
                            </tr>
                            <tr>
                                <td align="right">ID Type</td>
                                <td class="col-xs-6" style="padding-left: 15px;">${member.idType}<c:if test="${empty member.idType}">-</c:if></td>
                            </tr>
                            <tr>
                                <td align="right">ID No</td>
                                <td class="col-xs-6" style="padding-left: 15px;">${member.idNo}<c:if test="${empty member.idNo}">-</c:if></td>
                            </tr>
                            <tr>
                                <td align="right">Designation</td>
                                <td class="col-xs-6" style="padding-left: 15px;">${member.designation}<c:if test="${empty member.designation}">-</c:if></td>
                            </tr>
                            <tr>
                                <td align="right">Designation Appointment Date</td>
                                <td class="col-xs-6" style="padding-left: 15px;"><fmt:formatDate value="${member.apptDt}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /><c:if test="${empty member.apptDt}">-</c:if></td>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</c:if>

<c:if test="${not empty subLicenseeDto}">
    <div class="panel panel-default">
        <div class="panel-heading"><strong>Licensee</strong></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table aria-describedby="" class="table table-bordered">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td align="right">Licensee Type</td>
                            <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${subLicenseeDto.licenseeType}"/><c:if test="${empty subLicenseeDto.licenseeType}">-</c:if></td>
                        </tr>
                        <tr>
                            <td align="right">UEN</td>
                            <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.uenNo}<c:if test="${empty subLicenseeDto.uenNo}">-</c:if></td>
                        </tr>
                        <tr>
                            <td align="right">Salutation</td>
                            <td class="col-xs-6" style="padding-left: 15px;">-</td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">Name</td>
                            <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.licenseeName}<c:if test="${empty subLicenseeDto.licenseeName}">-</c:if></td>
                        </tr>
                        <tr>
                            <td align="right">ID Type</td>
                            <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${subLicenseeDto.idType}"/><c:if test="${empty subLicenseeDto.idType}">-</c:if></td>
                        </tr>
                        <tr>
                            <td align="right">ID No</td>
                            <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.idNumber}<c:if test="${empty subLicenseeDto.idNumber}">-</c:if></td>
                        </tr>
                        <c:if test="${subLicenseeDto.idType == 'IDTYPE003'}">
                            <tr>
                                <td align="right">Country of issuance</td>
                                <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${subLicenseeDto.nationality}"/><c:if test="${empty subLicenseeDto.nationality}">-</c:if></td>
                            </tr>
                        </c:if>
                        <tr>
                            <td align="right">Postal Code</td>
                            <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.postalCode}<c:if test="${empty subLicenseeDto.postalCode}">-</c:if></td>
                        </tr>
                        <tr>
                            <td align="right">Address Type</td>
                            <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${subLicenseeDto.addrType}"/><c:if test="${empty subLicenseeDto.addrType}">-</c:if></td>
                        </tr>
                        <tr>
                            <td align="right">Block No</td>
                            <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.blkNo}<c:if test="${empty subLicenseeDto.blkNo}">-</c:if></td>
                        </tr>
                        <tr>
                            <td align="right">Floor No</td>
                            <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.floorNo}<c:if test="${empty subLicenseeDto.floorNo}">-</c:if></td>
                        </tr>
                        <tr>
                            <td align="right">Unit No</td>
                            <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.unitNo}<c:if test="${empty subLicenseeDto.unitNo}">-</c:if></td>
                        </tr><tr>
                            <td align="right">Street Name</td>
                            <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.streetName}<c:if test="${empty subLicenseeDto.streetName}">-</c:if></td>
                        </tr>
                        <tr>
                            <td align="right">Building Name</td>
                            <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.buildingName}<c:if test="${empty subLicenseeDto.buildingName}">-</c:if></td>
                        </tr>
                        <c:if test="${subLicenseeDto.licenseeType=='LICTSUB002'}">
                            <tr>
                                <td align="right">Mobile No</td>
                                <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.telephoneNo}<c:if test="${empty subLicenseeDto.telephoneNo}">-</c:if></td>
                            </tr>
                            <tr>
                                <td align="right">Office Telephone No</td>
                                <td class="col-xs-6" style="padding-left: 15px;">-</td>
                            </tr>
                        </c:if>
                        <c:if test="${subLicenseeDto.licenseeType=='LICTSUB001' or subLicenseeDto.licenseeType=='LICT002'}">
                            <tr>
                                <td align="right">Mobile No</td>
                                <td class="col-xs-6" style="padding-left: 15px;">-</td>
                            </tr>
                            <tr>
                                <td align="right">Office Telephone No</td>
                                <td class="col-xs-6" style="padding-left: 15px;">${subLicenseeDto.telephoneNo}<c:if test="${empty subLicenseeDto.telephoneNo}">-</c:if></td>
                            </tr>
                        </c:if>
                        <tr>
                            <td align="right">Email Address</td>
                            <td class="col-xs-6" style="padding-left: 15px;"><c:out value="${subLicenseeDto.emailAddr}"/><c:if test="${empty subLicenseeDto.emailAddr}">-</c:if></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</c:if>