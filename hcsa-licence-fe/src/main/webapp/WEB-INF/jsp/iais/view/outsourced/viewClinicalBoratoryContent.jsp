<iais:row>
    <div class="col-xs-12">
        <p><strong>Clinical Laboratory</strong></p>
    </div>
</iais:row>
<c:set var="clen" value="${cL.clinicalLaboratoryList.size()}"/>
<div class="col-lg-12 col-xs-12 col-md-12">
    <div class="intranet-content">
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
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>