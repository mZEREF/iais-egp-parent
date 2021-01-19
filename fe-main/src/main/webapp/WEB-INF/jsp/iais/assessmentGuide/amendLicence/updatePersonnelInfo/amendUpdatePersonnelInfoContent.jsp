<div class="form-check-gp">

    <p class="form-check-title">You select one licence to amend at a time:</p>

    <iais:pagination  param="amendUpdatePersonnelSearchParam" result="amendUpdatePersonnelSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr >
                <th></th>
                <iais:sortableHeader needSort="true"  field="HCI_NAME" value="HCI Name" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="ADDR_TYPE" value="Type" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No." isFE="true"/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises Type" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="ADDRESS" value="Address" isFE="true"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty amendUpdatePersonnelSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="GENERAL_ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${amendUpdatePersonnelSearchResult.rows}" varStatus="status">
                        <tr>
                            <td>
                                <div class="form-check">
                                    <input class="form-check-input licenceCheck" id="amendLicenseId" type="radio" <c:choose>
                                        <c:when test="${fn:contains(licence_err_list, pool.licenceId)}">
                                            checked
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${status.index == 0}">checked</c:if>
                                        </c:otherwise>
                                    </c:choose>
                                           name="amendLicenseId" value="amendLicenseId${status.index}" aria-invalid="false"/>
                                    <label class="form-check-label" for="amendLicenseId"><span
                                            class="check-circle"></span>
                                    </label>
                                    <input type="hidden" name="amendLicenseId${status.index}" value="<iais:mask name= "amendLicenseId${status.index}" value="${pool.licenceId}"/>"/>
                                </div>
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
    <c:choose>
        <c:when test="${!empty amendUpdatePersonnelSearchResult.rows}">
            <a class="btn btn-primary " onclick="guideSubmit('amendLic5','second')" href="javascript:void(0);">NEXT</a>
        </c:when>
    </c:choose>
</div>
