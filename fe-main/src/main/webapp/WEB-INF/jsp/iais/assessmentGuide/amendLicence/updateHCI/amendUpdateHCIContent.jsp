<div class="form-check-gp">
    <p class="form-check-title">Please select the business to amend:</p>
    <iais:pagination  param="amendHCISearchParam" result="amendHCISearchResult"/>
    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >
                <th scope="col" ></th>
                <iais:sortableHeader needSort="true"  field="p.HCI_NAME" value="Name" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Mode of Service Delivery" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="ADDRESS" value="Address" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="HCI_CONTACT_NO" value="Contact" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="SVC_NAME" value="Active Licence" isFE="true"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty amendHCISearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="GENERAL_ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${amendHCISearchResult.rows}" varStatus="status">
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
                                    <input type="hidden" name="amendLicenseId${status.index}hiddenIndex" value="<iais:mask name= "amendLicenseId${status.index}hiddenIndex" value="${status.index}"/>"/>
                                    <input type="hidden" name="amendLicenseId${status.index}premiseId" value="<iais:mask name= "amendLicenseId${status.index}premiseId" value="${pool.premisesId}"/>"/>
                                </div>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Name</p>
                                ${pool.hciName}<c:if test="${empty pool.hciName}">N/A</c:if></td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Mode of Service Delivery</p>
                                <c:if test="${'ONSITE'==pool.premisesType}">
                                    <c:out value="Premises"/>
                                </c:if>
                                <c:if test="${'CONVEYANCE'==pool.premisesType}">
                                    <c:out value="Conveyance"/>
                                </c:if>
                                <c:if test="${'EASMTS'==pool.premisesType}">
                                    <c:out value="Conveyance (in a mobile clinic / ambulance)"/>
                                </c:if>
                                <c:if test="${'OFFSITE'==pool.premisesType}">
                                    <c:out value="Off-site"/>
                                </c:if>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Address</p>
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
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Contact</p>
                                ${pool.hciContactNo}<c:if test="${empty pool.hciContactNo}">N/A</c:if>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Active Licence</p>
                                ${pool.svcId}
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <c:choose>
        <c:when test="${!empty amendHCISearchResult.rows}">
            <a class="btn btn-primary " onclick="guideSubmit('amendLic2','second')" href="javascript:void(0);">NEXT</a>
        </c:when>
    </c:choose>
</div>
