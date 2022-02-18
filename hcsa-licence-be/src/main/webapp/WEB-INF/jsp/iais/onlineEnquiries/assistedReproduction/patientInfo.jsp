<div class="col-md-12">
    <div class="row">
        <b class="col-md-12" style="font-size:2.0rem">
            Current Identity
        </b>
    </div>

    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >
                <iais:sortableHeader field="BUSINESS_NAME" needSort="false" style="width: 35%;"
                                     value="AR Centre"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
                                     field="NAME"
                                     value="Name"/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="ID_TYPE"
                                     value="ID Type"/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="ID_NUMBER"
                                     value="ID No."/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="DATE_OF_BIRTH"
                                     value="Date of Birth"/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="NATIONALITY"
                                     value="Nationality"/>
            </tr>
            </thead>
            <tbody class="form-horizontal">
                <c:choose>
                    <c:when test="${empty patientInfoDto}">
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
                                <c:forEach var="arCentre" items="${patientInfoDto.patient.arCentres}">
                                    <c:if test="${not empty arCentre}">
                                        <c:out value="${arCentre.getPremiseLabel()}"/><br>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td style="vertical-align:middle;">
                                <c:out value="${patientInfoDto.patient.name}"/>
                            </td>
                            <td style="vertical-align:middle;">
                                <iais:code code="${patientInfoDto.patient.idType}"/>
                            </td>
                            <td style="vertical-align:middle;">
                                <c:out value="${patientInfoDto.patient.idNumber}"/>
                            </td>
                            <td style="vertical-align:middle;">
                                <c:out value="${patientInfoDto.patient.birthDate}"/>
                                <c:if test="${patientInfoDto.patient.getAgeYear()<14 or patientInfoDto.patient.getAgeYear() >74}">
                                    <a  href="#errAge"  data-toggle="modal" data-target="#errAge"  style="padding: 3px 10px;border-radius: 30px;background: #f22727;color: #FFF;">?</a>
                                </c:if>
                            </td>
                            <td style="vertical-align:middle;">
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
                                    <div class="col-md-12"><span style="font-size: 2rem">Patient's age does not fall within the range of 14 to 75.</span></div>
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
                                     value="AR Centre"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
                                     field="NAME"
                                     value="Name"/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="ID_TYPE"
                                     value="ID Type"/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="ID_NUMBER"
                                     value="ID No."/>
                <iais:sortableHeader needSort="false" style="width: 10%;"
                                     field="DATE_OF_BIRTH"
                                     value="Date of Birth"/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="NATIONALITY"
                                     value="Nationality"/>
            </tr>
            </thead>
            <tbody class="form-horizontal">
            <c:choose>
                <c:when test="${empty patientInfoDto}">
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

                            <c:forEach var="arCentre" items="${patientInfoDto.previous.arCentres}">
                                <p>
                                    <c:out value="${arCentre.getPremiseLabel()}"/>
                                </p>

                            </c:forEach>
                        </td>
                        <td style="vertical-align:middle;">
                            <c:out value="${patientInfoDto.previous.name}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <iais:code code="${patientInfoDto.previous.idType}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <c:out value="${patientInfoDto.previous.idNumber}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <c:out value="${patientInfoDto.previous.birthDate}"/>
                            <c:if test="${patientInfoDto.previous.getAgeYear()<14 or patientInfoDto.previous.getAgeYear()>75}">
                                <a  href="#errAge"  data-toggle="modal" data-target="#errAge"  style="padding: 3px 10px;border-radius: 30px;background: #f22727;color: #FFF;">?</a>
                            </c:if>
                        </td>
                        <td style="vertical-align:middle;">
                            <iais:code code="${patientInfoDto.previous.nationality}"/>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>