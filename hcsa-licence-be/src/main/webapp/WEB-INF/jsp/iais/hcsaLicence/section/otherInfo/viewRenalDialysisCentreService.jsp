<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Nurses per Shift
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoNurseDto.perShiftNum}">
                    <c:out value="${otherInfo.appSvcOtherInfoNurseDto.perShiftNum}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoNurseDto.perShiftNum}" style="display: none">
                    <c:out value="${oldOtherInfo.appSvcOtherInfoNurseDto.perShiftNum}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Total number of dialysis stations
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoNurseDto.dialysisStationsNum}">
                    <c:out value="${otherInfo.appSvcOtherInfoNurseDto.dialysisStationsNum}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoNurseDto.dialysisStationsNum}" style="display: none">
                    <c:out value="${oldOtherInfo.appSvcOtherInfoNurseDto.dialysisStationsNum}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Number of Hep B stations
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoNurseDto.helpBStationNum}">
                    <c:out value="${otherInfo.appSvcOtherInfoNurseDto.helpBStationNum}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoNurseDto.helpBStationNum}" style="display: none">
                    <c:out value="${oldOtherInfo.appSvcOtherInfoNurseDto.helpBStationNum}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Is clinic open to general public?
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoNurseDto.openToPublic}">
                    <c:if test="${otherInfo.appSvcOtherInfoNurseDto.openToPublic eq '1'}">Yes</c:if>
                    <c:if test="${otherInfo.appSvcOtherInfoNurseDto.openToPublic eq'0'}">No</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoNurseDto.openToPublic}" style="display: none">
                    <c:if test="${oldOtherInfo.appSvcOtherInfoNurseDto.openToPublic eq '1'}">Yes</c:if>
                    <c:if test="${oldOtherInfo.appSvcOtherInfoNurseDto.openToPublic eq'0'}">No</c:if>
                </span>
            </div>
        </td>
    </tr>

</table>
