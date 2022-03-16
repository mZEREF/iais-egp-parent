<c:if test="${empty personnelsDto}">
    <iais:message key="GENERAL_ACK018" escape="true"/>
</c:if>

<!-- Default panel contents -->
<c:forEach var="personnel" items="${personnelsDto}">
    <c:if test="${personnel.licKeyPersonnelDto.psnType!='Clinical Director'}">
        <c:if test="${personnel.licKeyPersonnelDto.psnType=='Key Appointment Holder'}">
            <div class="panel panel-default">
                <div class="panel-heading"><strong>${personnel.licKeyPersonnelDto.psnType}</strong></div>
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
                                    <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelDto.salutation}<c:if test="${empty personnel.keyPersonnelDto.salutation}">-</c:if></td>
                                </tr>
                                <tr>
                                    <td align="right">Name</td>
                                    <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelDto.name}<c:if test="${empty personnel.keyPersonnelDto.name}">-</c:if></td>
                                </tr>
                                <tr>
                                    <td align="right">ID Type</td>
                                    <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${personnel.keyPersonnelDto.idType}"/><c:if test="${empty personnel.keyPersonnelDto.idType}">-</c:if></td>
                                </tr>
                                <c:if test="${personnel.keyPersonnelDto.idType == 'IDTYPE003'}">
                                    <tr>
                                        <td align="right">Country of issuance</td>
                                        <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${personnel.keyPersonnelDto.nationality}"/><c:if test="${empty personnel.keyPersonnelDto.nationality}">-</c:if></td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td align="right">ID No</td>
                                    <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelDto.idNo}<c:if test="${empty personnel.keyPersonnelDto.idNo}">-</c:if></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${personnel.licKeyPersonnelDto.psnType!='Key Appointment Holder'}">
            <div class="panel panel-default">
                <div class="panel-heading"><strong>${personnel.licKeyPersonnelDto.psnType}</strong></div>
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
                                    <td class="col-xs-6" align="right">Name</td>
                                    <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelDto.name}<c:if test="${empty personnel.keyPersonnelDto.name}">-</c:if></td>
                                </tr>
                                <tr>
                                    <td align="right">Salutation</td>
                                    <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelDto.salutation}<c:if test="${empty personnel.keyPersonnelDto.salutation}">-</c:if></td>
                                </tr>
                                <tr>
                                    <td align="right">ID Type</td>
                                    <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${personnel.keyPersonnelDto.idType}"/><c:if test="${empty personnel.keyPersonnelDto.idType}">-</c:if></td>
                                </tr>
                                <c:if test="${personnel.keyPersonnelDto.idType == 'IDTYPE003'}">
                                    <tr>
                                        <td align="right">Country of issuance</td>
                                        <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${personnel.keyPersonnelDto.nationality}"/><c:if test="${empty personnel.keyPersonnelDto.nationality}">-</c:if></td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td align="right">ID No</td>
                                    <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelDto.idNo}<c:if test="${empty personnel.keyPersonnelDto.idNo}">-</c:if></td>
                                </tr>
                                <c:if test="${personnel.licKeyPersonnelDto.psnType!='MedAlert'}">
                                    <tr>
                                        <td align="right">Designation</td>
                                        <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelDto.designation}<c:if test="${empty personnel.keyPersonnelDto.designation}">-</c:if></td>
                                    </tr>
                                </c:if>
                                <c:if test="${personnel.licKeyPersonnelDto.psnType=='Clinical Governance Officer'}">
                                    <tr>
                                        <td align="right">Professional Type</td>
                                        <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelExtDto.professionType}<c:if test="${empty personnel.keyPersonnelExtDto.professionType}">-</c:if></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Professional Regn. No.</td>
                                        <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelExtDto.profRegNo}<c:if test="${empty personnel.keyPersonnelExtDto.profRegNo}">-</c:if></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Specialty</td>
                                        <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelExtDto.speciality}<c:if test="${empty personnel.keyPersonnelExtDto.speciality}">-</c:if></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Sub-specialty</td>
                                        <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelExtDto.subSpeciality}<c:if test="${empty personnel.keyPersonnelExtDto.subSpeciality }">-</c:if></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Qualification</td>
                                        <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelExtDto.qualification}<c:if test="${empty personnel.keyPersonnelExtDto.qualification}">-</c:if></td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td align="right">Mobile No</td>
                                    <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelDto.mobileNo}<c:if test="${empty personnel.keyPersonnelDto.mobileNo}">-</c:if></td>
                                </tr>
                                <c:if test="${personnel.licKeyPersonnelDto.psnType!='MedAlert' && personnel.licKeyPersonnelDto.psnType!='Clinical Governance Officer'}">
                                    <tr>
                                        <td align="right">Office Telephone No</td>
                                        <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelDto.officeTelNo}<c:if test="${empty personnel.keyPersonnelDto.officeTelNo}">-</c:if></td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td align="right">Email Address</td>
                                    <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelDto.emailAddr}<c:if test="${empty personnel.keyPersonnelDto.emailAddr}">-</c:if></td>
                                </tr>
                                    <%--                                                                    <c:if test="${personnel.licKeyPersonnelDto.psnType=='MedAlert'}">--%>
                                    <%--                                                                        <tr>--%>
                                    <%--                                                                            <td align="right">Preferred Mode of Receiving MedAlert</td>--%>
                                    <%--                                                                            <td class="col-xs-6" style="padding-left: 15px;">${personnel.keyPersonnelExtDto.preferredMode}<c:if test="${empty personnel.keyPersonnelExtDto.preferredMode}">-</c:if></td>--%>
                                    <%--                                                                        </tr>--%>
                                    <%--                                                                    </c:if>--%>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

    </c:if>

</c:forEach>
<c:if test="${not empty appSvcSectionLeaderList}">
    <c:forEach var="sectionLeader" items="${appSvcSectionLeaderList}">
        <div class="panel panel-default">
            <div class="panel-heading"><strong><iais:code code="${sectionLeader.personnelType}"/></strong></div>
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
                                <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${sectionLeader.salutation}"/><c:if test="${empty sectionLeader.salutation}">-</c:if></td>
                            </tr>
                            <tr>
                                <td class="col-xs-6" align="right">Name</td>
                                <td class="col-xs-6" style="padding-left: 15px;">${sectionLeader.name}<c:if test="${empty sectionLeader.name}">-</c:if></td>
                            </tr>
                            <tr>
                                <td align="right">Qualification</td>
                                <td class="col-xs-6" style="padding-left: 15px;">${sectionLeader.qualification}<c:if test="${empty sectionLeader.qualification}">-</c:if></td>
                            </tr>
                            <tr>
                                <td align="right">Working Experience (in terms of years)</td>
                                <td class="col-xs-6" style="padding-left: 15px;">${sectionLeader.wrkExpYear}<c:if test="${empty sectionLeader.wrkExpYear}">-</c:if></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</c:if>
<c:forEach items="${AppSvcChargesPageDto.generalChargesDtos}" var="generalChargesDtos" varStatus="index">
    <div class="panel panel-default">
        <div class="panel-heading"><strong>General Conveyance Charges</strong></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table aria-describedby="" class="table table-bordered">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Charge</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                                                              <span class="newVal " attr="${generalChargesDtos.chargesType}">
                                                                                <iais:code code="${generalChargesDtos.chargesType}"></iais:code>
                                                                              </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td  class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount From</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                                                              <span class="newVal " attr="${generalChargesDtos.minAmount}">
                                                                                      ${generalChargesDtos.minAmount}
                                                                              </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    <span class="check-square"></span>Amount To
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                                                              <span class="newVal " attr="${generalChargesDtos.maxAmount}">
                                                                                      ${generalChargesDtos.maxAmount}
                                                                              </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Remarks</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                                                              <span class="newVal " attr="${generalChargesDtos.remarks}">
                                                                                      ${generalChargesDtos.remarks}
                                                                              </span>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>

</c:forEach>
<c:forEach items="${AppSvcChargesPageDto.otherChargesDtos}" var="otherChargesDtos" varStatus="index">
    <div class="panel panel-default">
        <div class="panel-heading"><strong>General Conveyance Charges</strong></div>

        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table aria-describedby="" class="table table-bordered">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Category</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                                                              <span class="newVal " attr="${otherChargesDtos.chargesCategory}">
                                                                                <iais:code code="${otherChargesDtos.chargesCategory}"></iais:code>
                                                                              </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Charge</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                                                              <span class="newVal " attr="${otherChargesDtos.chargesType}">
                                                                                <iais:code code="${otherChargesDtos.chargesType}"></iais:code>
                                                                              </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount From</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                                                              <span class="newVal " attr="${otherChargesDtos.minAmount}">
                                                                                      ${otherChargesDtos.minAmount}
                                                                              </span>
                                </div>

                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    <span class="check-square"></span>Amount To
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                                                              <span class="newVal " attr="${otherChargesDtos.maxAmount}">
                                                                                      ${otherChargesDtos.maxAmount}
                                                                              </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Remarks</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                                                              <span class="newVal " attr="${otherChargesDtos.remarks}">
                                                                                      ${otherChargesDtos.remarks}
                                                                              </span>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>

</c:forEach>
<c:forEach var="appSvcVehicleDto" items="${AppSvcVehicleDtoList}" varStatus="status">
    <div class="panel panel-default">
        <div class="panel-heading"><strong>Vehicle</strong></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table aria-describedby="" class="table table-bordered">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Vehicle Number</p>
                            </td>
                            <td >
                                <div class="col-xs-6">
                    <span class="newVal " attr="${appSvcVehicleDto.vehicleName}">
                        <c:out value="${appSvcVehicleDto.vehicleName}"></c:out>
                    </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Chassis Number</p>
                            </td>
                            <td >
                                <div class="col-xs-6">
                    <span  class="newVal " attr="${appSvcVehicleDto.chassisNum}">
                        <c:out value="${appSvcVehicleDto.chassisNum}"></c:out>
                    </span>
                                </div>

                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Engine Number</p>
                            </td>
                            <td >
                                <div class="col-xs-6">
                    <span class="newVal " attr="${appSvcVehicleDto.engineNum}">
                       <c:out value="${appSvcVehicleDto.engineNum}"></c:out>
                    </span>
                                </div>

                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</c:forEach>
<c:forEach var="appSvcClinicalDirectorDto" items="${AppSvcClinicalDirectorDtoList}" varStatus="status">
    <div class="panel panel-default">
        <div class="panel-heading"><strong>Clinical Director</strong></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table aria-describedby="" class="table table-bordered">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Board</p>
                            </td>
                            <td >
                                <div class="col-xs-6">
                          <span class="newVal " attr="${appSvcClinicalDirectorDto.professionBoard}">
                            <iais:code code="${appSvcClinicalDirectorDto.professionBoard}"></iais:code>
                          </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Profession Regn. No.</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.profRegNo}">
                                ${appSvcClinicalDirectorDto.profRegNo}
                        </span>
                                </div>
                            </td>
                        </tr>
                        <c:if test="${'Medical Transport Service'==setLicInfoSvcName}">
                            <tr>
                                <td class="col-xs-6" align="right">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Not registered with a Professional Board</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.noRegWithProfBoard}">
                          <c:choose>
                              <c:when test="${appSvcClinicalDirectorDto.noRegWithProfBoard=='1'}">
                              <div class="form-check active">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                              </div>
                              </c:when>
                              <c:otherwise>
                              <div class="form-check ">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                              </div>
                              </c:otherwise>
                          </c:choose>
                        </span>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.salutation}">
                          <iais:code code="${appSvcClinicalDirectorDto.salutation}"></iais:code>
                        </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                          <span class="newVal " attr="${appSvcClinicalDirectorDto.name}">
                                  ${appSvcClinicalDirectorDto.name}
                          </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.idType}">
                            <iais:code code="${appSvcClinicalDirectorDto.idType}"></iais:code>
                        </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.idNo}">
                                ${appSvcClinicalDirectorDto.idNo}
                        </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.designation}">
                            <iais:code code="${appSvcClinicalDirectorDto.designation}"></iais:code>
                        </span>
                                </div>

                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Speciality</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal" attr="${appSvcClinicalDirectorDto.speciality}">
                          <iais:code code="${appSvcClinicalDirectorDto.speciality}"></iais:code>
                        </span>
                                </div>

                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Date when specialty was obtained</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.specialtyGetDate}">
                            <fmt:formatDate value="${appSvcClinicalDirectorDto.specialtyGetDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                                </div>

                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Registration Date</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.typeOfCurrRegi}">
                                ${appSvcClinicalDirectorDto.typeOfCurrRegi}
                        </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Current Registration Date</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.currRegiDate}">
                          <fmt:formatDate value="${appSvcClinicalDirectorDto.currRegiDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                                </div>

                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Practicing Certificate End Date </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.praCerEndDate}">
                          <fmt:formatDate value="${appSvcClinicalDirectorDto.praCerEndDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                                </div>

                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Register </p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                          <span class="newVal " attr="${appSvcClinicalDirectorDto.typeOfRegister}">
                                  ${appSvcClinicalDirectorDto.typeOfRegister}
                          </span>
                                </div>

                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant Experience </p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.relevantExperience}">
                                ${appSvcClinicalDirectorDto.relevantExperience}
                        </span>

                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Clinical Director (CD) holds a valid certification issued by an Emergency Medical Services ("EMS") Medical Directors workshop </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal" attr="${appSvcClinicalDirectorDto.holdCerByEMS}">
                          <c:if test="${appSvcClinicalDirectorDto.holdCerByEMS=='1'}">Yes</c:if>
                          <c:if test="${appSvcClinicalDirectorDto.holdCerByEMS=='0'}">No</c:if>
                        </span>
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Expiry Date (ACLS) </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.aclsExpiryDate}">
                            <fmt:formatDate value="${appSvcClinicalDirectorDto.aclsExpiryDate}" pattern="dd/MM/yyyy"/>
                        </span>
                                </div>

                            </td>
                        </tr>
                        <c:if test="${'Medical Transport Service'==setLicInfoSvcName}">

                            <tr>
                                <td class="col-xs-6" align="right">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Expiry Date (BCLS and AED) </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.bclsExpiryDate}">
                            <fmt:formatDate value="${appSvcClinicalDirectorDto.bclsExpiryDate}" pattern="dd/MM/yyyy"/>
                        </span>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.  </p>
                            </td>
                            <td>
                                <div  class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.mobileNo}">
                                ${appSvcClinicalDirectorDto.mobileNo}
                        </span>
                                </div>

                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-6" align="right">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address  </p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.emailAddr}">
                                ${appSvcClinicalDirectorDto.emailAddr}
                        </span>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</c:forEach>