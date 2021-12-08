<div class="arQuickView">
    <div class="row">
        <b class="col-md-12" style="font-size:2.0rem">
            Current Identity
        </b>
    </div>

    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >

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

                    <p style="width: 165px;"><c:out value="${patientInfoDto.husband.name}"/>
                    </p>
                </td>
                <td style="vertical-align:middle;">
                    <iais:code code="${patientInfoDto.husband.idType}"/>
                </td>
                <td style="vertical-align:middle;">
                    <c:out value="${patientInfoDto.husband.idNumber}"/>
                </td>
                <td style="vertical-align:middle;">
                    <c:out value="${patientInfoDto.husband.birthDate}"/>
                </td>
                <td style="vertical-align:middle;">
                    <iais:code code="${patientInfoDto.husband.nationality}"/>
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
    <hr>
    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >

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

                            <p style="width: 165px;"><c:out value="${patientInfoDto.previousHusband.name}"/>
                            </p>
                        </td>
                        <td style="vertical-align:middle;">
                            <iais:code code="${patientInfoDto.previousHusband.idType}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <c:out value="${patientInfoDto.previousHusband.idNumber}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <c:out value="${patientInfoDto.previousHusband.birthDate}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <iais:code code="${patientInfoDto.previousHusband.nationality}"/>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>