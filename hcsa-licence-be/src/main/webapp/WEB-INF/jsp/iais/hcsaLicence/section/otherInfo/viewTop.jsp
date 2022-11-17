<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Do you provide Termination of Pregnancy
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.provideTop}">
                    <c:if test="${otherInfo.provideTop eq '1'}">Yes</c:if>
                    <c:if test="${otherInfo.provideTop eq'0'}">No</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class="oldVal" attr="${oldOtherInfo.provideTop}" style="display: none">
                   <c:if test="${oldOtherInfo.provideTop eq '1'}">Yes</c:if>
                    <c:if test="${oldOtherInfo.provideTop eq'0'}">No</c:if>
                </span>
            </div>
        </td>
    </tr>

    <c:if test="${otherInfo.provideTop eq '1'}">
        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Please indicate
                </p>
            </td>
            <td>
                <div class="col-xs-6 ">
                    <span class="newVal" attr="${otherInfo.appSvcOtherInfoTopDto.topType}">
                        <c:if test="${'1' eq otherInfo.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy (Solely by Drug)</c:if>
                        <c:if test="${'0' eq otherInfo.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy (Solely by Surgical Procedure)</c:if>
                        <c:if test="${'-1' eq otherInfo.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy (Drug and Surgical Procedure)</c:if>
                    </span>
                </div>
                <div class="col-xs-6 ">
                    <span class="oldVal" attr="${oldOtherInfo.appSvcOtherInfoTopDto.topType}" style="display: none">
                        <c:if test="${'1' eq oldOtherInfo.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy (Solely by Drug)</c:if>
                        <c:if test="${'0' eq oldOtherInfo.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy (Solely by Surgical Procedure)</c:if>
                        <c:if test="${'-1' eq oldOtherInfo.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy (Drug and Surgical Procedure)</c:if>
                    </span>
                </div>
            </td>
        </tr>
    </c:if>
</table>
