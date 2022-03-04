<div class="amended-service-info-gp">
    <label style="font-size: 2.2rem">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:set value="0" var="poSzie"></c:set>
                    <c:set value="0" var="dpoSize"></c:set>
                    <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po">
                        <c:if test="${po.psnType=='PO'}">
                            <c:set value="${poSzie+1}" var="poSzie"></c:set>
                        </c:if>
                        <c:if test="${po.psnType=='DPO'}">
                            <c:set value="${dpoSize+1}" var="dpoSize"></c:set>
                        </c:if>
                    </c:forEach>

                    <c:set value="0" var="poIndex"></c:set>
                    <c:set var="dpoIndex" value="0"></c:set>
                    <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po">
                        <c:if test="${po.psnType=='PO'}">
                            <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                                <c:set value="${poIndex=poIndex+1}" var="poIndex"></c:set>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>Principal Officer<c:if test="${poSzie > 1}"> ${poIndex}</c:if>:</strong></p>
                            </div>
                        </c:if>
                        <c:if test="${po.psnType=='DPO'}">
                            <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                                <c:set var="dpoIndex" value="${dpoIndex=dpoIndex+1}"></c:set>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>Nominee<c:if test="${dpoSize > 1}"> ${dpoIndex}</c:if>:</strong></p>
                            </div>
                        </c:if>
                        <table aria-describedby="" class="col-xs-12">
                            <thead style="display: none">
                            <tr><th scope="col"></th></tr>
                            </thead>
                            <c:choose>
                                <c:when test="${po.psnType=='PO'}">

                                    <tr>
                                        <td  class="col-xs-6">
                                            <p  class="form-check-label" aria-label="premise-1-cytology"></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.salutation}"/></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.name}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.idType}"></iais:code></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.idNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span class="check-square"></span>Nationality
                                            </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span class="check-square"></span><iais:code code="${po.nationality}" />
                                            </p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.designation}"/></p>
                                        </td>
                                    </tr>
                                    <c:if test="${'DES999' == po.designation}">
                                        <tr>
                                            <td class="col-xs-6">
                                            </td>
                                            <td>
                                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.otherDesignation}"/> </p>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office Telephone No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.officeTelNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.mobileNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.emailAddr}</p>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:when test="${po.psnType=='DPO'}">

                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.salutation}"/></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.name}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.idType}"></iais:code></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.idNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span class="check-square"></span>Nationality
                                            </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span class="check-square"></span><iais:code code="${po.nationality}" />
                                            </p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.designation}"/></p>
                                        </td>
                                    </tr>
                                    <c:if test="${'DES999' == po.designation}">
                                        <tr>
                                            <td class="col-xs-6">
                                            </td>
                                            <td>
                                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.otherDesignation}"/> </p>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office Telephone No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.officeTelNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.mobileNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.emailAddr}</p>
                                        </td>
                                    </tr>


                                </c:when>
                            </c:choose>


                        </table>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
