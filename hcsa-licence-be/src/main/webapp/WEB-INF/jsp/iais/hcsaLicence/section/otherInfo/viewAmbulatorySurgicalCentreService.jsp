<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>GFA Value (in sqm)
            </p>
        </td>
        <td>
            <div class="col-xs-6">
                <span class="newVal" attr="${otherInfo.otherInfoMedAmbulatorySurgicalCentre.gfaValue}">
                     <iais:code code="${otherInfo.otherInfoMedAmbulatorySurgicalCentre.gfaValue}"/>
                </span>
            </div>
            <div class="col-xs-6">
                <span class=" oldVal" attr="${oldOtherInfo.otherInfoMedAmbulatorySurgicalCentre.gfaValue}" style="display: none">
                      <iais:code code="${oldOtherInfo.otherInfoMedAmbulatorySurgicalCentre.gfaValue}"/>
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
            <div class="col-xs-6">
                <span class="newVal" attr="${otherInfo.ascsDeclaration}">
                     <c:if test="${empty  otherInfo.ascsDeclaration}">Yes</c:if>
                </span>
            </div>
            <div class="col-xs-6">
                <span class=" oldVal" attr="${oldOtherInfo.ascsDeclaration}" style="display: none">
                      <c:if test="${empty  oldOtherInfo.ascsDeclaration}">Yes</c:if>
                </span>
            </div>
        </td>
    </tr>
</table>
