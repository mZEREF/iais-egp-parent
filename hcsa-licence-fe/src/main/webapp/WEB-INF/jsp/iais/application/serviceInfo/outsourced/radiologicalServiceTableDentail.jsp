<c:forEach end="${rlen-1}" begin="0" step="1" varStatus="r">
    <c:set var="index" value="${r.index}" />
    <c:set var="msgTemplateResult" value="${cL.radiologicalServiceList[index]}"/>
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
            <button type="button" class="btn btn-default btn-sm btn-rSBtn" onclick="">DELETE</button>
        </td>
    </tr>
</c:forEach>