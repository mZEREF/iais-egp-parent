<c:if test="${not empty currentPreviewSvcInfo.appSvcOutsouredDto.clinicalLaboratoryList}">
    <p class="col-xs-12">
        <strong>
            Clinical Laboratory Service
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
            <th style="width: 12%;">
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
            <th style="width: 18%;">
                <p style="margin-left: 12px;width: 100%;">
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
        <c:forEach var="msgTemplateResult" items="${currentPreviewSvcInfo.appSvcOutsouredDto.clinicalLaboratoryList}" varStatus="status">
            <c:set var="oldMsgTemplateResult"  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcOutsouredDto.clinicalLaboratoryList[status.index]}" />
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

                        <div class="oldVal" attr="${oldMsgTemplateResult.address}" style="display: none">
                            <c:out value="${oldMsgTemplateResult.address}"/>
                        </div>
                    </td>
                    <td>
                        <div class="newVal" attr="${msgTemplateResult.expiryDate}">
                            <c:out value="${msgTemplateResult.expiryDate}"/>
                        </div>
                        <div class="oldVal" attr="${oldMsgTemplateResult.expiryDate}" style="display: none">
                            <c:out value="${oldMsgTemplateResult.expiryDate}"/>
                        </div>
                    </td>
                    <td>
                        <div class="newVal" attr="${msgTemplateResult.startDateStr}">
                            <c:out value="${msgTemplateResult.startDateStr}"/>
                        </div>
                        <div class="oldVal" attr="${oldMsgTemplateResult.startDateStr}" style="display: none">
                            <c:out value="${oldMsgTemplateResult.startDateStr}"/>
                        </div>
                    </td>
                    <td>
                        <div class="newVal" attr="${msgTemplateResult.endDateStr}">
                            <c:out value="${msgTemplateResult.endDateStr}"/>
                        </div>

                        <div class="oldVal" attr="${oldMsgTemplateResult.endDateStr}" style="display: none">
                            <c:out value="${oldMsgTemplateResult.endDateStr}"/>
                        </div>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal" attr="${appPremOutSourceLicenceDto.outstandingScope}" style="width: 200px;">
                                <c:out value="${appPremOutSourceLicenceDto.outstandingScope}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class=" oldVal" attr="${oldAppPremOutSourceLicenceDto.outstandingScope}" style="display: none;width: 200px;">
                                <c:out value="${oldAppPremOutSourceLicenceDto.outstandingScope}"/>
                            </div>
                        </div>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
        </tbody>
    </table>
</c:if>
