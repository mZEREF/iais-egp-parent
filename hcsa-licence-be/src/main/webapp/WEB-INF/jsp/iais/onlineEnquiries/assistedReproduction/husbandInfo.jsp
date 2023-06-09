<div class=" col-md-12">
    <div class="row">
        <strong class="col-md-12" style="font-size:2.0rem">
            Current Identity
        </strong>
    </div>

    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >

                <iais:sortableHeader needSort="false" style="width: 30%;"
                                     field="NAME"
                                     value="Husband Name"/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="ID_TYPE"
                                     value="Husband ID Type"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
                                     field="ID_NUMBER"
                                     value="Husband ID No."/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="DATE_OF_BIRTH"
                                     value="Husband Date of Birth"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
                                     field="NATIONALITY"
                                     value="Husband Nationality"/>
            </tr>
            </thead>
            <tbody class="form-horizontal">
            <c:choose>
                <c:when test="${empty patientInfoDto.husband}">
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
                            <p class="visible-xs visible-sm table-row-title">Husband Name</p>
                            <p ><c:out value="${patientInfoDto.husband.name}"/>
                            </p>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Husband ID Type</p>
                            <iais:code code="${patientInfoDto.husband.idType}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Husband ID No.</p>
                            <c:out value="${patientInfoDto.husband.idNumber}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Husband Date of Birth</p>
                            <c:out value="${patientInfoDto.husband.birthDate}"/>
                            <c:if test="${patientInfoDto.husband.getAgeFlag()!=''}">
                                <a  href="#errHusbandAge"  data-toggle="modal" data-target="#errHusbandAge"  style="padding: 3px 10px;border-radius: 30px;background: #f22727;color: #FFF;">?</a>
                            </c:if>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Husband Nationality</p>
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
                                    <div class="col-md-12"><span style="font-size: 2rem">
                                        <c:choose>
                                            <c:when test="${patientInfoDto.husband.getAgeFlag()!=''}">
                                                ${patientInfoDto.husband.getAgeFlag()}
                                            </c:when>
                                            <c:otherwise >
                                                ${patientInfoDto.previousHusband.getAgeFlag()}
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
        <strong class="col-md-12" style="font-size:2.0rem">
            Previous Identities
        </strong>
    </div>
    <hr>
    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >

                <iais:sortableHeader needSort="false" style="width: 30%;"
                                     field="NAME"
                                     value="Previous Husband Name"/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="ID_TYPE"
                                     value="Previous Husband ID Type"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
                                     field="ID_NUMBER"
                                     value="Previous Husband ID No."/>
                <iais:sortableHeader needSort="false" style="width: 15%;"
                                     field="DATE_OF_BIRTH"
                                     value="Previous Husband Date of Birth"/>
                <iais:sortableHeader needSort="false" style="width: 20%;"
                                     field="NATIONALITY"
                                     value="Previous Husband Nationality"/>
            </tr>
            </thead>
            <tbody class="form-horizontal">
            <c:choose>
                <c:when test="${empty patientInfoDto.previousHusband}">
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
                            <p class="visible-xs visible-sm table-row-title">Previous Husband Name</p>
                            <p ><c:out value="${patientInfoDto.previousHusband.name}"/>
                            </p>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Previous Husband ID Type</p>
                            <iais:code code="${patientInfoDto.previousHusband.idType}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Previous Husband ID No.</p>
                            <c:out value="${patientInfoDto.previousHusband.idNumber}"/>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Previous Husband Date of Birth</p>
                            <c:out value="${patientInfoDto.previousHusband.birthDate}"/>
                            <c:if test="${patientInfoDto.previousHusband.getAgeFlag()!=''}">
                                <a  href="#errHusbandAge"  data-toggle="modal" data-target="#errHusbandAge"  style="padding: 3px 10px;border-radius: 30px;background: #f22727;color: #FFF;">?</a>
                            </c:if>
                        </td>
                        <td style="vertical-align:middle;">
                            <p class="visible-xs visible-sm table-row-title">Previous Husband Nationality</p>
                            <iais:code code="${patientInfoDto.previousHusband.nationality}"/>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>