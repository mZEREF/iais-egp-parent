
<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Outcome of procedures are recorded
            </p>
        </td>
        <td>
            <div class="col-xs-6 col-md-6">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoTopDto.isOutcomeProcRecord}">
                    <c:if test="${true == otherInfo.appSvcOtherInfoTopDto.isOutcomeProcRecord}">Yes</c:if>
                    <c:if test="${false == otherInfo.appSvcOtherInfoTopDto.isOutcomeProcRecord}">No</c:if>
                </span>
            </div>
            <div class="col-xs-6 col-md-6">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoTopDto.isOutcomeProcRecord}" style="display: none">
                    <c:if test="${true == oldOtherInfo.appSvcOtherInfoTopDto.isOutcomeProcRecord}">Yes</c:if>
                    <c:if test="${false == oldOtherInfo.appSvcOtherInfoTopDto.isOutcomeProcRecord}">No</c:if>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Number of cases with complications, if any
            </p>
        </td>
        <td>
            <div class="col-xs-6 col-md-6">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoTopDto.compCaseNum}">
                    <iais:code code="${otherInfo.appSvcOtherInfoTopDto.compCaseNum}"/>
                </span>
            </div>
            <div class="col-xs-6 col-md-6">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoTopDto.compCaseNum}" style="display: none">
                    <iais:code code="${oldOtherInfo.appSvcOtherInfoTopDto.compCaseNum}"/>
                </span>
            </div>
        </td>
    </tr>
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Statistics on abortion (For renewal application only)
            </p>
        </td>
        <td>

        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Number of abortions performed during the previous 2 years
            </p>
        </td>
        <td>

        </td>
    </tr>
</table>