<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>GFA Value (in sqm)
            </div>
        </td>
        <td>
            <div class="col-xs-6">
                <span class="newVal" attr="${otherInfo.otherInfoMedAmbulatorySurgicalCentre.gfaValue}">
                     <c:out value="${otherInfo.otherInfoMedAmbulatorySurgicalCentre.gfaValue}"/>
                </span>
            </div>
            <div class="col-xs-6">
                <span class=" oldVal" attr="${oldOtherInfo.otherInfoMedAmbulatorySurgicalCentre.gfaValue}" style="display: none">
                      <c:out value="${oldOtherInfo.otherInfoMedAmbulatorySurgicalCentre.gfaValue}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label longWord" aria-label="premise-1-cytology">
                <span class="check-square"></span>I declare that I have met URA's requirements for gross floor area
            </div>
        </td>
        <td>
            <div class="col-xs-6">
                <span class="newVal" attr="${otherInfo.ascsDeclaration}">
                     <c:if test="${otherInfo.ascsDeclaration eq '0'}">Yes</c:if>
                </span>
            </div>
            <div class="col-xs-6">
                <span class="oldVal" attr="${oldOtherInfo.ascsDeclaration}" style="display: none">
                      <c:if test="${oldOtherInfo.ascsDeclaration eq '0'}">Yes</c:if>
                </span>
            </div>
        </td>
    </tr>
</table>
