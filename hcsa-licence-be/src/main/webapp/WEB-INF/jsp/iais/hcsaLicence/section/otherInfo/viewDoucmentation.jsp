
<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Outcome of procedures are recorded
            </div>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoTopDto.outcomeProcRecord}">
                    <c:if test="${true == otherInfo.appSvcOtherInfoTopDto.outcomeProcRecord}">Yes</c:if>
                    <c:if test="${false == otherInfo.appSvcOtherInfoTopDto.outcomeProcRecord}">No</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoTopDto.outcomeProcRecord}" style="display: none">
                    <c:if test="${true == oldOtherInfo.appSvcOtherInfoTopDto.outcomeProcRecord}">Yes</c:if>
                    <c:if test="${false == oldOtherInfo.appSvcOtherInfoTopDto.outcomeProcRecord}">No</c:if>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Number of cases with complications, if any
            </div>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoTopDto.compCaseNum}">
                    <c:out value="${otherInfo.appSvcOtherInfoTopDto.compCaseNum}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoTopDto.compCaseNum}" style="display: none">
                    <c:out value="${oldOtherInfo.appSvcOtherInfoTopDto.compCaseNum}"/>
                </span>
            </div>
        </td>
    </tr>
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Statistics on abortion (For renewal application only)
            </div>
        </td>
        <td>

        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label longWord" aria-label="premise-1-cytology">
                <span class="check-square"></span>Number of abortions performed during the previous 2 years
            </div>
        </td>
        <td>

        </td>
    </tr>
</table>
