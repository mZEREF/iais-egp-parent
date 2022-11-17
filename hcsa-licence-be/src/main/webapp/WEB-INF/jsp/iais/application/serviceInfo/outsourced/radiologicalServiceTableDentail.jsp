<c:forEach end="${rlen-1}" begin="0" step="1" varStatus="r">
    <c:set var="index" value="${r.index}" />
    <c:set var="msgTemplateResult" value="${cL.radiologicalServiceList[index]}"/>
    <c:set var="appPremOutSourceLicenceDto" value="${msgTemplateResult.appPremOutSourceLicenceDto}"/>
    <c:set var="outsourcedIndexNo" value="${appPremOutSourceLicenceDto.licenceNo}"/>
    <c:if test="${!empty appPremOutSourceLicenceDto}">
        <tr>
            <td>
                <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                <p>${appPremOutSourceLicenceDto.licenceNo}</p>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Business Name</p>
                <p>${msgTemplateResult.businessName}</p>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Address</p>
                <p>${msgTemplateResult.address}</p>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Licence Tenure</p>
                <p>${msgTemplateResult.expiryDate}</p>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Date of Agreement</p>
                <p>${msgTemplateResult.startDateStr}</p>
<%--                <p>${appPremOutSourceLicenceDto.agreementStartDate}</p>--%>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">End Date of Agreement</p>
                <p>${msgTemplateResult.endDateStr}</p>
<%--                <p>${appPremOutSourceLicenceDto.agreementEndDate}</p>--%>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Scope of Outsourcing</p>
                <p><textarea style="border:none;background-color: transparent;resize: none;" class="scopeOutsource">${appPremOutSourceLicenceDto.outstandingScope}</textarea>
                </p>
            </td>
            <td>
                <input type="hidden" name="prefixVal" value="${appPremOutSourceLicenceDto.id}">
                <c:set var="prefix" value="${appPremOutSourceLicenceDto.id}"/>
                <button type="button" class="btn btn-default btn-sm btn-rSBtn" data-prefix="${prefix}" data-group="${outsourcedIndexNo}">DELETE</button>
            </td>
        </tr>
    </c:if>
</c:forEach>