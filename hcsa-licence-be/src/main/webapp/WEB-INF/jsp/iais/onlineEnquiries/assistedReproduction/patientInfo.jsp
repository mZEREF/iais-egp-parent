<div class="col-md-12">
    <div class="row">
        <strong class="col-md-12" style="font-size:2.0rem">
            Current Identity
        </strong>
    </div>

    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >
                <iais:sortableHeader field="BUSINESS_NAME" needSort="false" style="width: 35%;"
                                     value="AR Centre"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
                                     field="NAME"
                                     value="Patient Name"/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="ID_TYPE"
                                     value="Patient ID Type"/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="ID_NUMBER"
                                     value="Patient ID No."/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="DATE_OF_BIRTH"
                                     value="Patient Date of Birth"/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="NATIONALITY"
                                     value="Patient Nationality"/>
            </tr>
            </thead>
            <tbody class="form-horizontal">
                <c:choose>
                    <c:when test="${empty patientInfoDto.patient}">
                        <tr>
                            <td colspan="15">
                                <iais:message key="GENERAL_ACK018"
                                              escape="true"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>

                            <td style="vertical-align:middle;">
                                <p class="visible-xs visible-sm table-row-title">AR Centre</p>
                                <c:forEach var="arCentre" items="${patientInfoDto.patient.arCentres}">
                                    <c:if test="${not empty arCentre}">
                                        <c:out value="${arCentre.getPremiseLabel()}"/><br>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td style="vertical-align:middle;">
                                <p class="visible-xs visible-sm table-row-title">Patient Name</p>
                                <c:out value="${patientInfoDto.patient.name}"/>
                            </td>
                            <td style="vertical-align:middle;">
                                <p class="visible-xs visible-sm table-row-title">Patient ID Type</p>
                                <iais:code code="${patientInfoDto.patient.idType}"/>
                            </td>
                            <td style="vertical-align:middle;">
                                <p class="visible-xs visible-sm table-row-title">Patient ID No.</p>
                                <c:out value="${patientInfoDto.patient.idNumber}"/>
                            </td>
                            <td style="vertical-align:middle;">
                                <p class="visible-xs visible-sm table-row-title">Patient Date of Birth</p>
                                <c:out value="${patientInfoDto.patient.birthDate}"/>
                                <c:if test="${ patientInfoDto.patient.getAgeFlag()!=''}">
                                    <a  href="#errAge"  data-toggle="modal" data-target="#errAge"  style="padding: 3px 10px;border-radius: 30px;background: #f22727;color: #FFF;">?</a>
                                </c:if>
                            </td>
                            <td style="vertical-align:middle;">
                                <p class="visible-xs visible-sm table-row-title">Patient Nationality</p>
                                <iais:code code="${patientInfoDto.patient.nationality}"/>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
        <div id="errAge" class="modal fade" tabindex="-1" role="dialog" >
            <div class="modal-dialog modal-dialog-centered"  role="document" >
                <div class="row">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-12"><span style="font-size: 2rem">
                                        <c:choose>
                                            <c:when test="${patientInfoDto.patient.getAgeFlag()!=''}">
                                                ${patientInfoDto.patient.getAgeFlag()}
                                            </c:when>
                                            <c:otherwise >
                                                ${patientInfoDto.previous.getAgeFlag()}
                                            </c:otherwise>
                                        </c:choose>
                                    </span></div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br>
    <div class="row">
        <b class="col-md-12" style="font-size:2.0rem">
            Previous Identities
        </b>
    </div>
    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >
                <iais:sortableHeader field="BUSINESS_NAME" needSort="false" style="width: 35%;"
                                     value="Previous AR Centre"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
                                     field="NAME"
                                     value="Previous Patient Name"/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="ID_TYPE"
                                     value="Previous Patient ID Type"/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="ID_NUMBER"
                                     value="Previous Patient ID No."/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="DATE_OF_BIRTH"
                                     value="Previous Patient Date of Birth"/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="NATIONALITY"
                                     value="Previous Patient Nationality"/>
            </tr>
            </thead>
            <tbody class="form-horizontal">
            <c:choose>
                <c:when test="${empty patientInfoDto.previous}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="GENERAL_ACK018"
                                          escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Previous AR Centre</p>
                            <c:forEach var="arCentre" items="${patientInfoDto.previous.arCentres}">
                                <p>
                                    <c:out value="${arCentre.getPremiseLabel()}"/>
                                </p>

                            </c:forEach>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Previous Patient Name</p>
                            <c:out value="${patientInfoDto.previous.name}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Previous Patient ID Type</p>
                            <iais:code code="${patientInfoDto.previous.idType}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Previous Patient ID No.</p>
                            <c:out value="${patientInfoDto.previous.idNumber}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Previous Patient Date of Birth</p>
                            <c:out value="${patientInfoDto.previous.birthDate}"/>
                            <c:if test="${ patientInfoDto.previous.getAgeFlag()!=''}">
                                <a  href="#errAge"  data-toggle="modal" data-target="#errAge"  style="padding: 3px 10px;border-radius: 30px;background: #f22727;color: #FFF;">?</a>
                            </c:if>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Previous Patient Nationality</p>
                            <iais:code code="${patientInfoDto.previous.nationality}"/>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>