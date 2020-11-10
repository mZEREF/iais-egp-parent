<p class="form-check-title">Please select licence(s) to cease:</p>
<div class="form-check">
    <iais:pagination param="ceaseLicenceSearchParam" result="ceaseLicenceSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr >
                <th></th>
                <iais:sortableHeader needSort="true" field="HCI_NAME" value="HCI Name"/>
                <iais:sortableHeader needSort="true" field="ADDR_TYPE" value="Type"/>
                <iais:sortableHeader needSort="true" field="LICENCE_NO" value="Licence No."/>
                <iais:sortableHeader needSort="true" field="PREMISES_TYPE" value="Premises Type"/>
                <iais:sortableHeader needSort="true" field="ADDRESS" value="Address"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty ceaseLicenceSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="GENERAL_ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${ceaseLicenceSearchResult.rows}" varStatus="status">
                        <tr>
                            <td onclick="javascript:controlCease()">
                                <div class="form-check hover">
                                    <input class="form-check-input" id="ceaseLicence" type="checkbox" name="ceaseLicIds" aria-invalid="false" <c:if test="${fn:contains(licence_err_list, pool.licPremId)}">checked</c:if> value="ceaseLicence${status.index}">
                                    <label class="form-check-label" for="ceaseLicence"><span class="check-square"></span></label>
                                </div>
                                <input type="hidden" name="ceaseLicence${status.index}" value="<iais:mask name= "ceaseLicence${status.index}" value="${pool.licPremId}"/>"/>
                            </td>
                            <td>${pool.hciName}<c:if test="${empty pool.hciName}">N/A</c:if></td>
                            <td>${pool.svcId}</td>
                            <td>${pool.licenceNo}</td>
                            <td>
                                <c:if test="${'ONSITE'==pool.premisesType}">
                                    <c:out value="On-site"/>
                                </c:if>
                                <c:if test="${'CONVEYANCE'==pool.premisesType}">
                                    <c:out value="Conveyance"/>
                                </c:if>
                                <c:if test="${'OFFSITE'==pool.premisesType}">
                                    <c:out value="Off-site"/>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${pool.premisesDtoList.size() == 1}">
                                        <P>${pool.premisesDtoList[0]}</P>
                                    </c:when>
                                    <c:otherwise>
                                        <select>
                                            <option value ="">Multiple</option>
                                            <c:forEach items="${pool.premisesDtoList}" var="address" varStatus="index">
                                                <option value ="${address}">${address}</option>
                                            </c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <c:if test="${!empty ceaseLicenceSearchResult.rows}">
        <button type="button" class="btn btn-primary CeaseBtn "  <c:if test="${empty ceasedErrResult }">disabled</c:if> onclick="ceaseSubmit()">NEXT</button>
    </c:if>
</div>
<script type="text/javascript">
    function ceaseSubmit() {
        var checkOne = false;
        var checkBox = $("input[name='ceaseLicIds']");
        for (var i = 0; i < checkBox.length; i++) {
            if (checkBox[i].checked) {
                checkOne = true;
            }
            ;
        }
        ;

        if (checkOne) {
            guideSubmit('ceaseLic','second')
        }
    }
    function controlCease() {
        var checkOne = false;
        var checkBox = $("input[name='ceaseLicIds']");
        for (var i = 0; i < checkBox.length; i++) {
            if (checkBox[i].checked) {
                checkOne = true;
            }
            ;
        }
        ;

        if (checkOne) {
            $('.CeaseBtn').prop('disabled',false);
        } else {
            $('.CeaseBtn').prop('disabled',true);
        }
    }
</script>