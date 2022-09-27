<c:forEach end="${clen-1}" begin="0" step="1" varStatus="c">
    <c:set var="index" value="${c.index}" />
    <c:set var="msgTemplateResult" value="${cL.clinicalLaboratoryList[index]}"/>
    <c:if test="${msgTemplateResult.status eq 0}">
        <tr>
            <td>
                <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                <p>${msgTemplateResult.licenceNo}</p>
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
                <p>${msgTemplateResult.agreementStartDate}</p>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">End Date of Agreement</p>
                <p>${msgTemplateResult.agreementEndDate}</p>
            </td>
            <td>
                <p class="visible-xs visible-sm table-row-title">Scope of Outsourcing</p>
                <p>${msgTemplateResult.outstandingScope}</p>
            </td>
            <td>
                <input type="hidden" name="prefixVal" value="${msgTemplateResult.id}">
                <c:set var="prefix" value="${msgTemplateResult.id}"/>
                <button type="button" class="btn btn-default btn-sm btn-cldBtn" data-prefix="${prefix}">DELETE</button>
            </td>
        </tr>
    </c:if>
</c:forEach>
