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
                <iais:sortableHeader field="BUSINESS_NAME" needSort="false"
                                     value="AR Centre"/>
                <iais:sortableHeader needSort="false"
                                     field="NAME"
                                     value="Name"/>
                <iais:sortableHeader needSort="false"
                                     field="ID_TYPE"
                                     value="ID Type"/>
                <iais:sortableHeader needSort="false"
                                     field="ID_NUMBER"
                                     value="ID No"/>
                <iais:sortableHeader needSort="false"
                                     field="DATE_OF_BIRTH"
                                     value="Date of Birth"/>
                <iais:sortableHeader needSort="false"
                                     field="NATIONALITY"
                                     value="Nationality"/>
            </tr>
            </thead>
            <tbody class="form-horizontal">
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
                    </td>
                    <td style="vertical-align:middle;">
                        <iais:code code="${patientInfoDto.patient.nationality}"/>
                    </td>
                </tr>
            </tbody>
        </table>
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
                <iais:sortableHeader field="BUSINESS_NAME" needSort="false"
                                     value="AR Centre"/>
                <iais:sortableHeader needSort="false"
                                     field="NAME"
                                     value="Name"/>
                <iais:sortableHeader needSort="false"
                                     field="ID_TYPE"
                                     value="ID Type"/>
                <iais:sortableHeader needSort="false"
                                     field="ID_NUMBER"
                                     value="ID No"/>
                <iais:sortableHeader needSort="false"
                                     field="DATE_OF_BIRTH"
                                     value="Date of Birth"/>
                <iais:sortableHeader needSort="false"
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