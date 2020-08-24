<div class="amended-service-info-gp">
    <label style="font-size: 2.2rem">${currStepMap.get("SVST004")}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:set value="0" var="poIndex"></c:set>
                    <c:set var="dpoIndex" value="0"></c:set>
                    <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po">
                        <c:if test="${po.psnType=='PO'}">
                            <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>Principal Officer ${poIndex+1}:</strong></p>
                                <c:set value="${poIndex=poIndex+1}" var="poIndex"></c:set>
                            </div>
                        </c:if>
                        <c:if test="${po.psnType=='DPO'}">
                            <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>Deputy Principal Officer ${dpoIndex+1}:</strong></p>
                                <c:set var="dpoIndex" value="${dpoIndex=dpoIndex+1}"></c:set>
                            </div>
                        </c:if>
                        <table class="col-xs-8">
                            <c:choose>
                                <c:when test="${po.psnType=='PO'}">

                                    <tr>
                                        <td  class="col-xs-8">
                                            <p  class="form-check-label" aria-label="premise-1-cytology"></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.salutation}"/></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.name}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.idType}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.idNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.designation}"/></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office Telephone No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.officeTelNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.mobileNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.emailAddr}</p>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:when test="${po.psnType=='DPO'}">

                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.salutation}"/></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.name}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.idType}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.idNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${po.designation}"/></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office Telephone No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.officeTelNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.mobileNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-8">
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
