<p class="col-xs-12">
    <strong>
        Dental Service
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
    <c:forEach var="msgTemplateResult" items="${currentPreviewSvcInfo.appPremOutSourceLicenceDto.radiologicalServiceList}" varStatus="status">
        <c:set var="oldMsgTemplateResult"  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appPremOutSourceLicenceDto.radiologicalServiceList[status.index]}" />
        <c:set var="appPremOutSourceLicenceDto" value="${msgTemplateResult.appPremOutSourceLicenceDto}"/>
        <c:set var="oldAppPremOutSourceLicenceDto" value="${oldMsgTemplateResult.appPremOutSourceLicenceDto}"/>
        <tr>
            <td>
                <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                <div class="col-xs-6 col-md-6">
                    <span class="newVal" attr="${appPremOutSourceLicenceDto.licenceNo}">
                        <iais:code code="${appPremOutSourceLicenceDto.licenceNo}}"/>
                    </span>
                </div>
                <div class="col-xs-6 col-md-6">
                    <span class=" oldVal" attr="${oldAppPremOutSourceLicenceDto.licenceNo}" style="display: none">
                        <iais:code code="${oldAppPremOutSourceLicenceDto.licenceNo}}"/>
                    </span>
                </div>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Business Name</p>
                <div class="col-xs-6 col-md-6">
                    <span class="newVal" attr="${msgTemplateResult.businessName}">
                        <iais:code code="${msgTemplateResult.businessName}}"/>
                    </span>
                </div>
                <div class="col-xs-6 col-md-6">
                    <span class=" oldVal" attr="${oldMsgTemplateResult.businessName}" style="display: none">
                        <iais:code code="${oldMsgTemplateResult.businessName}}"/>
                    </span>
                </div>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Address</p>
                <div class="col-xs-6 col-md-6">
                    <span class="newVal" attr="${msgTemplateResult.address}">
                        <iais:code code="${msgTemplateResult.address}}"/>
                    </span>
                </div>
                <div class="col-xs-6 col-md-6">
                    <span class=" oldVal" attr="${oldMsgTemplateResult.address}" style="display: none">
                        <iais:code code="${oldMsgTemplateResult.address}}"/>
                    </span>
                </div>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Licence Tenure</p>
                <div class="col-xs-6 col-md-6">
                    <span class="newVal" attr="${msgTemplateResult.expiryDate}">
                        <iais:code code="${msgTemplateResult.expiryDate}}"/>
                    </span>
                </div>
                <div class="col-xs-6 col-md-6">
                    <span class=" oldVal" attr="${oldMsgTemplateResult.expiryDate}" style="display: none">
                        <iais:code code="${oldMsgTemplateResult.expiryDate}}"/>
                    </span>
                </div>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Date of Agreement</p>
                <div class="col-xs-6 col-md-6">
                    <span class="newVal" attr="${appPremOutSourceLicenceDto.agreementStartDate}">
                        <iais:code code="${appPremOutSourceLicenceDto.agreementStartDate}}"/>
                    </span>
                </div>
                <div class="col-xs-6 col-md-6">
                    <span class=" oldVal" attr="${oldAppPremOutSourceLicenceDto.agreementStartDate}" style="display: none">
                        <iais:code code="${oldAppPremOutSourceLicenceDto.agreementStartDate}}"/>
                    </span>
                </div>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">End Date of Agreement</p>
                <div class="col-xs-6 col-md-6">
                    <span class="newVal" attr="${appPremOutSourceLicenceDto.agreementEndDate}">
                        <iais:code code="${appPremOutSourceLicenceDto.agreementEndDate}}"/>
                    </span>
                </div>
                <div class="col-xs-6 col-md-6">
                    <span class=" oldVal" attr="${oldAppPremOutSourceLicenceDto.agreementEndDate}" style="display: none">
                        <iais:code code="${oldAppPremOutSourceLicenceDto.agreementEndDate}}"/>
                    </span>
                </div>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Scope of Outsourcing</p>
                <div class="col-xs-6 col-md-6">
                    <span class="newVal" attr="${appPremOutSourceLicenceDto.outstandingScope}">
                        <iais:code code="${appPremOutSourceLicenceDto.outstandingScope}}"/>
                    </span>
                </div>
                <div class="col-xs-6 col-md-6">
                    <span class=" oldVal" attr="${oldAppPremOutSourceLicenceDto.outstandingScope}" style="display: none">
                        <iais:code code="${oldAppPremOutSourceLicenceDto.outstandingScope}}"/>
                    </span>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>