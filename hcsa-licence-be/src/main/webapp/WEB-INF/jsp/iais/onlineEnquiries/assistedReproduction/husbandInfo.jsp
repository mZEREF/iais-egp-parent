<div class=" col-md-12">
    <div class="row">
        <b class="col-md-12" style="font-size:2.0rem">
            Current Identity
        </b>
    </div>

    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >

                <iais:sortableHeader needSort="false" style="width: 30%;"
                                     field="NAME"
                                     value="Name"/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="ID_TYPE"
                                     value="ID Type"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
                                     field="ID_NUMBER"
                                     value="ID No."/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="DATE_OF_BIRTH"
                                     value="Date of Birth"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
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
                            <c:if test="${patientInfoDto.husband.getAgeYear()<14 or patientInfoDto.husband.getAgeYear() >74}">
                                <a  href="#errHusbandAge"  data-toggle="modal" data-target="#errAge"  style="padding: 3px 10px;border-radius: 30px;background: #f22727;color: #FFF;">?</a>
                            </c:if>
                        </td>
                        <td style="vertical-align:middle;">
                            <iais:code code="${patientInfoDto.husband.nationality}"/>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
        <div id="errHusbandAge" class="modal fade" tabindex="-1" role="dialog" >
            <div class="modal-dialog modal-dialog-centered"  role="document" >
                <div class="row">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-12"><span style="font-size: 2rem">Husband's age does not fall within the range of 14 to 75.</span></div>
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
    <hr>
    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >

                <iais:sortableHeader needSort="false" style="width: 30%;"
                                     field="NAME"
                                     value="Name"/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="ID_TYPE"
                                     value="ID Type"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
                                     field="ID_NUMBER"
                                     value="ID No."/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="DATE_OF_BIRTH"
                                     value="Date of Birth"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
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
                            <c:if test="${patientInfoDto.previousHusband.getAgeYear()<14 or patientInfoDto.previousHusband.getAgeYear() >74}">
                                <a  href="#errHusbandAge"  data-toggle="modal" data-target="#errAge"  style="padding: 3px 10px;border-radius: 30px;background: #f22727;color: #FFF;">?</a>
                            </c:if>
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