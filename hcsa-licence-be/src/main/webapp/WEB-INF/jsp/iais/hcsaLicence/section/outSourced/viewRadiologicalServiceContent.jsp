<c:if test="${not empty currentPreviewSvcInfo.appSvcOutsouredDto.radiologicalServiceList}">
    <p class="col-xs-12">
        <strong>
            Radiological Service
        </strong>
    </p>

    <table aria-describedby="" class="table">
        <thead>
        <tr>
            <th style="width: 15%;">
                <p style="margin-left: 12px;">
                    Licence No.
                </p>
            </th>
            <th style="width: 15%;">
                <p style="margin-left: 12px;">
                    Business Name
                </p>
            </th>
            <th style="width: 10%;">
                <p style="margin-left: 12px;">
                    Address
                </p>
            </th>
            <th style="width: 15%;">
                <p style="margin-left: 12px;">
                    Licence Tenure
                </p>
            </th>
            <th style="width: 15%;">
                <p style="margin-left: 12px;">
                    Date of Agreement
                </p>
            </th>
            <th style="width: 15%;">
                <p style="margin-left: 12px;">
                    End Date of Agreement
                </p>
            </th>
            <th style="width: 15%;">
                <p style="margin-left: 12px;">
                    Scope of Outsourcing
                </p>
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="msgTemplateResult" items="${currentPreviewSvcInfo.appSvcOutsouredDto.radiologicalServiceList}" varStatus="status">
            <c:set var="oldMsgTemplateResult"  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcOutsouredDto.radiologicalServiceList[status.index]}" />
            <c:set var="appPremOutSourceLicenceDto" value="${msgTemplateResult.appPremOutSourceLicenceDto}"/>
            <c:set var="oldAppPremOutSourceLicenceDto" value="${oldMsgTemplateResult.appPremOutSourceLicenceDto}"/>
            <c:if test="${!empty appPremOutSourceLicenceDto}">
                <tr>
                    <td>
                        <div class="newVal" attr="${appPremOutSourceLicenceDto.licenceNo}">
                            <c:out value="${appPremOutSourceLicenceDto.licenceNo}"/>
                        </div>
                        <div class=" oldVal" attr="${oldAppPremOutSourceLicenceDto.licenceNo}" style="display: none">
                            <c:out value="${oldAppPremOutSourceLicenceDto.licenceNo}"/>
                        </div>
                    </td>
                    <td>
                        <div class="newVal" attr="${msgTemplateResult.businessName}">
                            <c:out value="${msgTemplateResult.businessName}"/>
                        </div>
                        <div class=" oldVal" attr="${oldMsgTemplateResult.businessName}" style="display: none">
                            <c:out value="${oldMsgTemplateResult.businessName}"/>
                        </div>
                    </td>
                    <td>
                        <div class="newVal" attr="${msgTemplateResult.address}">
                            <c:out value="${msgTemplateResult.address}"/>
                        </div>
                        <div class=" oldVal" attr="${oldMsgTemplateResult.address}" style="display: none">
                            <c:out value="${oldMsgTemplateResult.address}"/>
                        </div>
                    </td>
                    <td>
                        <div class="newVal" attr="${msgTemplateResult.expiryDate}">
                            <c:out value="${msgTemplateResult.expiryDate}"/>
                        </div>
                        <div class=" oldVal" attr="${oldMsgTemplateResult.expiryDate}" style="display: none">
                            <c:out value="${oldMsgTemplateResult.expiryDate}"/>
                        </div>
                    </td>
                    <td>
                        <div class="newVal" attr="${appPremOutSourceLicenceDto.agreementStartDate}">
                            <c:out value="${appPremOutSourceLicenceDto.agreementStartDate}"/>
                        </div>
                        <div class=" oldVal" attr="${oldAppPremOutSourceLicenceDto.agreementStartDate}" style="display: none">
                            <c:out value="${oldAppPremOutSourceLicenceDto.agreementStartDate}"/>
                        </div>
                    </td>
                    <td>
                        <div class="newVal" attr="${appPremOutSourceLicenceDto.agreementEndDate}">
                            <c:out value="${appPremOutSourceLicenceDto.agreementEndDate}"/>
                        </div>
                        <div class=" oldVal" attr="${oldAppPremOutSourceLicenceDto.agreementEndDate}" style="display: none">
                            <c:out value="${oldAppPremOutSourceLicenceDto.agreementEndDate}"/>
                        </div>
                    </td>
                    <td>
                        <div class="newVal" attr="${appPremOutSourceLicenceDto.outstandingScope}">
                            <textarea style="border:none;background-color: transparent;resize: none;" class="scopeOutsource">
                                    <c:out value="${appPremOutSourceLicenceDto.outstandingScope}"/></textarea>
                        </div>
                        <div class=" oldVal" attr="${oldAppPremOutSourceLicenceDto.outstandingScope}" style="display: none">
                            <textarea style="border:none;background-color: transparent;resize: none;" class="scopeOutsource">
                                    <c:out value="${oldAppPremOutSourceLicenceDto.outstandingScope}"/></textarea>
                        </div>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
        </tbody>
    </table>
</c:if>
