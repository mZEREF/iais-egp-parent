<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Type of medical records
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoMedDto.isMedicalTypeIt}">
                   <c:if test="${otherInfo.appSvcOtherInfoMedDto.isMedicalTypeIt eq '0'}">IT System</c:if>
                </span>
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoMedDto.isMedicalTypePaper}">
                    <c:if test="${otherInfo.appSvcOtherInfoMedDto.isMedicalTypePaper eq '0'}">Paper cards</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoMedDto.isMedicalTypeIt}" style="display: none">
                   <c:if test="${oldOtherInfo.appSvcOtherInfoMedDto.isMedicalTypeIt eq '0'}">IT System</c:if>
                </span>
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoMedDto.isMedicalTypePaper}" style="display: none">
                    <c:if test="${oldOtherInfo.appSvcOtherInfoMedDto.isMedicalTypePaper eq '0'}">Paper cards</c:if>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>List of options for IT system and paper cards / IT system only
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoMedDto.systemOption}">
                    <c:if test="${otherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED01'}">Clinic Assist</c:if>
                    <c:if test="${otherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED02'}">Gloco</c:if>
                    <c:if test="${otherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED03'}">GPConnect</c:if>
                    <c:if test="${otherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED04'}">Medi2000</c:if>
                    <c:if test="${otherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED05'}"> Freeware/ Shareware/ GP Self-developed Software</c:if>
                    <c:if test="${otherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED06'}">Others (please specify)</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoMedDto.systemOption}" style="display: none">
                    <c:if test="${oldOtherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED01'}">Clinic Assist</c:if>
                    <c:if test="${oldOtherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED02'}">Gloco</c:if>
                    <c:if test="${oldOtherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED03'}">GPConnect</c:if>
                    <c:if test="${oldOtherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED04'}">Medi2000</c:if>
                    <c:if test="${oldOtherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED05'}"> Freeware/ Shareware/ GP Self-developed Software</c:if>
                    <c:if test="${oldOtherInfo.appSvcOtherInfoMedDto.systemOption eq 'MED06'}">Others (please specify)</c:if>
                </span>
            </div>
        </td>
    </tr>

    <c:if test="${otherInfo.appSvcOtherInfoMedDto.systemOption == 'MED06' || oldOtherInfo.appSvcOtherInfoMedDto.systemOption == 'MED06'}">
        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Please specify
                </p>
            </td>
            <td>
                <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoMedDto.otherSystemOption}">
                    <iais:code code="${otherInfo.appSvcOtherInfoMedDto.otherSystemOption}"/>
                </span>
                </div>
                <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoMedDto.otherSystemOption}" style="display: none">
                    <iais:code code="${oldOtherInfo.appSvcOtherInfoMedDto.otherSystemOption}"/>
                </span>
                </div>
            </td>
        </tr>
    </c:if>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Is clinic open to general public?
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoMedDto.isOpenToPublic}">
                    <c:if test="${otherInfo.appSvcOtherInfoMedDto.isOpenToPublic eq '1'}">Yes</c:if>
                    <c:if test="${otherInfo.appSvcOtherInfoMedDto.isOpenToPublic eq'0'}">No</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoMedDto.isOpenToPublic}" style="display: none">
                     <c:if test="${oldOtherInfo.appSvcOtherInfoMedDto.isOpenToPublic eq '1'}">Yes</c:if>
                    <c:if test="${oldOtherInfo.appSvcOtherInfoMedDto.isOpenToPublic eq '0'}">No</c:if>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>GFA Value (in sqm)
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoMedDto.gfaValue}">
                     <iais:code code="${otherInfo.appSvcOtherInfoMedDto.gfaValue}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoMedDto.gfaValue}" style="display: none">
                      <iais:code code="${oldOtherInfo.appSvcOtherInfoMedDto.gfaValue}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>I declare that I have met URA's requirements for gross floor area
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.dsDeclaration}">
                     <c:if test="${empty otherInfo.dsDeclaration}">Yes</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.dsDeclaration}" style="display: none">
                     <c:if test="${empty oldOtherInfo.dsDeclaration eq '1'}">Yes</c:if>
                </span>
            </div>
        </td>
    </tr>
</table>
