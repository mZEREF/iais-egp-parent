<div class="amended-service-info-gp">
    <h2>PRINCIPAL OFFICERS</h2>
    <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po">
                        <table class="col-xs-8">
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation :</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.salutation}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name :</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.name}</p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type :</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.idType}</p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No :</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.idNo}</p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation :</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.designation}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office Telephone No :</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.officeTelNo}</p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>MobileNo :</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.mobileNo}</p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address :</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.emailAddr}</p>
                                </td>
                            </tr>
                        </table>
                        <div class="row">
                            <div class="col-xs-8">
                                <hr>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
