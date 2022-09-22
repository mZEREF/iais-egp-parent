<iais:row>
    <div class="col-xs-12">
        <p>Radiological Service</p>
    </div>
</iais:row>

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
                <th></th>
            </tr>
            </thead>
            <tbody>
            <%--                                    <c:forEach var="msgTemplateResult" items="${MsgTemplateSearchResult.rows}"--%>
            <%--                                               varStatus="status">--%>
            <tr>
                <td>
                    <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                    <%--                                <p>${msgTemplateResult.messageType}</p>--%>
                </td>
                <td>
                    <p class="visible-xs visible-sm table-row-title">Business Name</p>
                    <%--                                <p>${msgTemplateResult.templateName}</p>--%>
                </td>
                <td>
                    <p class="visible-xs visible-sm table-row-title">Address</p>
                    <%--                                <p>${msgTemplateResult.deliveryMode}</p>--%>
                </td>
                <td>
                    <p class="visible-xs visible-sm table-row-title">Licence Tenure</p>
                    <%--                                <p>${msgTemplateResult.process}</p>--%>
                </td>
                <td>
                    <p class="visible-xs visible-sm table-row-title">Date of Agreement</p>
                    <%--                                <p>${msgTemplateResult.rec}</p>--%>
                </td>
                <td>
                    <p class="visible-xs visible-sm table-row-title">End Date of Agreement</p>
                    <%--                                <p><fmt:formatDate value="${msgTemplateResult.effectiveTo}"--%>
                    <%--                                                   pattern="dd/MM/yyyy"/></p>--%>
                </td>
                <td>
                    <p class="visible-xs visible-sm table-row-title">Scope of Outsourcing</p>
                    <%--                                <p>${(MsgTemplateSearchParam.pageNo - 1) * MsgTemplateSearchParam.pageSize + status.index + 1}</p>--%>
                </td>
                <td>
                    <button type="button" class="btn btn-default btn-sm" onclick="">DELETE</button>
                </td>
            </tr>
            <%--                                    </c:forEach>--%>
            </tbody>
        </table>
    </div>
</div>