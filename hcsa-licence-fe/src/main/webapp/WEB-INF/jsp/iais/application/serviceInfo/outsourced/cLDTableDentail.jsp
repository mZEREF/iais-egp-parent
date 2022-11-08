<c:forEach end="${clen-1}" begin="0" step="1" varStatus="c">
    <c:set var="index" value="${c.index}" />
    <c:set var="msgTemplateResult" value="${cL.clinicalLaboratoryList[index]}"/>
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
                    <%--                <fmt:formatDate value="${appPremOutSourceLicenceDto.agreementStartDate}" pattern="dd/MM/yyyy"/>--%>
                <p>${appPremOutSourceLicenceDto.agreementStartDate}"</p>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">End Date of Agreement</p>
                    <%--                <fmt:formatDate value="${appPremOutSourceLicenceDto.agreementEndDate}"/>--%>
                <p>${appPremOutSourceLicenceDto.agreementEndDate}</p>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Scope of Outsourcing</p>
                <p>${appPremOutSourceLicenceDto.outstandingScope}</p>
            </td>
            <td>
                <input type="hidden" name="prefixVal" value="${appPremOutSourceLicenceDto.id}">
                <c:set var="prefix" value="${appPremOutSourceLicenceDto.id}"/>
                <button type="button" class="btn btn-default btn-sm btn-cldBtn" data-prefix="${prefix}" data-group="${outsourcedIndexNo}">DELETE</button>
            </td>
        </tr>
    </c:if>
</c:forEach>
