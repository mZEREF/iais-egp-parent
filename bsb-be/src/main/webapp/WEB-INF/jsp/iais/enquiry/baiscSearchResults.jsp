          <input type="hidden" name="appId" id="appId" value="" >
            <div class="row">
                <div class="col-xs-12">
                    <div class="components">
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
                         <div class="table-responsive">
                            <div class="table-gp">
                                <c:if test="${count=='app'}">
                                    <table class="table" aria-describedby="">
                                        <thead>
                                        <tr>
                                            <th scope="col"></th>
                                        </tr>
                                        <tr align="center">
                                            <iais:sortableHeader needSort="false" field=""
                                                                 value="S/N"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Application No"
                                                                 value="Application No."/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Application Type"
                                                                 value="Application Type"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Application Status"
                                                                 value="Application Status"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Application Submission Date"
                                                                 value="Application Submission Date"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Approval Date"
                                                                 value="Approval Date"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Classification"
                                                                 value="Facility Classification"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Type"
                                                                 value="Facility Type"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Name"
                                                                 value="Facility Name"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Biological Agent/Toxin"
                                                                 value="Biological Agent/Toxin"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Risk Level of the Biological Agent/Toxin"
                                                                 value="Risk Level of the Biological Agent/Toxin"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Process Type"
                                                                 value="Process Type"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Verified By DO"
                                                                 value="Verified By DO"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Verified By AO"
                                                                 value="Verified By AO"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Verified By HM"
                                                                 value="Verified By HM"/>
                                        </tr>
                                        </thead>
                                        <tbody class="form-horizontal">
                                        <c:choose>
                                            <c:when test="${empty applicationInfoDto}">
                                                <tr>
                                                    <td colspan="6">
                                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="items" items="${applicationInfoDto}" varStatus="status">
                                                    <tr name="basicData">
                                                        <td><c:out value="${status.index+1}"/></td>
                                                        <td><a onclick="javascript:doAppInfo('<iais:mask name="appId"
                                                                                                         value="${items.id}"/>')"><c:out
                                                                value="${items.applicationNo}"/></a></td>
                                                        <td><iais:code code="${items.appType}"></iais:code></td>
                                                        <td><iais:code code="${items.status}"></iais:code></td>
                                                        <td><fmt:formatDate value='${items.applicationDt}'
                                                                            pattern='dd/MM/yyyy'/></td>
                                                        <td><fmt:formatDate value='${items.approvalDate}'
                                                                            pattern='dd/MM/yyyy'/></td>
                                                        <td><iais:code
                                                                code="${items.facility.facilityClassification}"></iais:code></td>
                                                        <td><iais:code code="${items.facility.activeType}"></iais:code></td>
                                                        <td><iais:code code="${items.facility.facilityName}"></iais:code></td>
                                                        <td><c:out value="${items.bioName}"></c:out></td>
                                                        <td><iais:code code="${items.riskLevel}"></iais:code></td>
                                                        <td><iais:code code="${items.processType}"></iais:code></td>
                                                        <td><fmt:formatDate value='${items.doVerifiedDt}'
                                                                            pattern='dd/MM/yyyy'/></td>
                                                        <td><fmt:formatDate value='${items.aoVerifiedDt}'
                                                                            pattern='dd/MM/yyyy'/></td>
                                                        <td><fmt:formatDate value='${items.hmVerifiedDt}'
                                                                            pattern='dd/MM/yyyy'/></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                    </table>
                                </c:if>
                                <c:if test="${count=='fn'}">
                                    <table class="table" aria-describedby="">
                                        <thead>
                                        <tr>
                                            <th scope="col"></th>
                                        </tr>
                                        <tr align="center">
                                            <iais:sortableHeader needSort="false" field=""
                                                                 value="S/N"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Name"
                                                                 value="Facility Name"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Address"
                                                                 value="Facility Address"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Classification"
                                                                 value="Facility Classification"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Type"
                                                                 value="Facility Type"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Biological Agent/Toxin"
                                                                 value="Biological Agent/Toxin"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Risk Level of the Biological Agent/Toxin"
                                                                 value="Risk Level of the Biological Agent/Toxin"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Expiry Date"
                                                                 value="Facility Expiry Date"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Gazetted Area"
                                                                 value="Gazetted Area"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Operator"
                                                                 value="Facility Operator"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Admin"
                                                                 value="Facility Admin"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Current Facility Status"
                                                                 value="Current Facility Status"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Approved Facility Certifier"
                                                                 value="Approved Facility Certifier"/>
                                        </tr>
                                        </thead>
                                        <tbody class="form-horizontal">
                                        <c:choose>
                                            <c:when test="${empty facInfoSearchResult}">
                                                <tr>
                                                    <td colspan="6">
                                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="items" items="${facInfoSearchResult}" varStatus="status">
                                                    <tr name="basicData">
                                                        <td><c:out value="${status.index + 1}"/></td>
                                                        <td><c:out value="${items.facility.facilityName}"/></td>
                                                        <td><c:out value="${items.facility.blkNo}"/> <c:out
                                                                value="${items.facility.streetName}"/> <c:out
                                                                value="${items.facility.floorNo}"/>-<c:out
                                                                value="${items.facility.unitNo}"/> <c:out
                                                                value="${items.facility.postalCode}"/></td>
                                                        <td><iais:code
                                                                code="${items.facility.facilityClassification}"></iais:code></td>
                                                        <td><iais:code
                                                                code="${items.activityType}"></iais:code></td>
                                                        <td><c:out value="${items.bioName}"/></td>
                                                        <td><iais:code code="${items.riskLevel}"></iais:code></td>
                                                        <td><fmt:formatDate value='${items.approval.approvalExpiryDate}' pattern='dd/MM/yyyy'/></td>
                                                        <td>
                                                            <c:if test="${items.facility.isProtected == 'Y'}">
                                                                <c:out value="yes"/></c:if>
                                                            <c:if test="${items.facility.isProtected =='N'}">
                                                                <c:out value="No"/></c:if>
                                                        </td>
                                                        <td><c:out value="${items.facility.operator.facOperator}"/></td>
                                                        <td><c:out value="${items.admin}"/></td>
                                                        <td><iais:code
                                                                code="${items.approval.status}"></iais:code></td>
                                                        <td><c:out value="approval01"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                    </table>
                                </c:if>
                                <c:if test="${count=='on'}"   >
                                    <table class="table" aria-describedby="">
                                        <thead>
                                        <tr>
                                            <th scope="col"></th>
                                        </tr>
                                        <tr align="center">
                                            <iais:sortableHeader needSort="false" field=""
                                                                 value="S/N"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Organisation Name"
                                                                 value="Organisation Name"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Organisation Address"
                                                                 value="Organisation Address"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="AFC Status"
                                                                 value="AFC Status"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Administrator"
                                                                 value="Administrator"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Approved Date"
                                                                 value="Approved Date"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Expiry Date"
                                                                 value="Expiry Date"/>
                                        </tr>
                                        </thead>
                                        <tbody class="form-horizontal">
                                        <c:choose>
                                            <c:when test="${empty afcInfoDto}">
                                                <tr>
                                                    <td colspan="6">
                                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="items" items="${afcInfoDto}" varStatus="status">
                                                    <tr name="basicData">
                                                        <td><c:out value="${status.index + 1}"/></td>
                                                        <td><c:out value = "${items.facilityName}"/></td>
                                                        <td><c:out value="0915 xxxx tech 4-168"></c:out></td>
                                                        <td><iais:code code="${items.facilityStatus}"></iais:code></td>
                                                        <td><c:out value="${items.admin}"/></td>
                                                        <td><fmt:formatDate value='${items.approvalDate}'
                                                                            pattern='dd/MM/yyyy'/></td>
                                                        <td><fmt:formatDate value='${items.expiryDt}' pattern='dd/MM/yyyy'/></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                    </table>
                                </c:if>
                                <c:if test="${count=='an'}">
                                    <table class="table" aria-describedby="">
                                        <thead>
                                        <tr>
                                            <th scope="col"></th>
                                        </tr>
                                        <tr align="center">
                                            <iais:sortableHeader needSort="false" field=""
                                                                 value="S/N"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Approval Type"
                                                                 value="Approval Type"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Approval Status"
                                                                 value="Approval Status"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Classification"
                                                                 value="Facility Classification"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Type"
                                                                 value="Facility Type"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Name"
                                                                 value="Facility Name"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Address"
                                                                 value="Facility Address"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Facility Status"
                                                                 value="Facility Status"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Biological Agent/Toxin"
                                                                 value="Biological Agent/Toxin"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Nature of the sample"
                                                                 value="Nature of the sample"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Risk Level of the Biological Agent/Toxin"
                                                                 value="Risk Level of the Biological Agent/Toxin"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Physical Possession of BA/T"
                                                                 value="Physical Possession of BA/T"/>
                                        </tr>
                                        </thead>
                                        <tbody class="form-horizontal">
                                        <c:choose>
                                            <c:when test="${empty appInfoSearchResult}">
                                                <tr>
                                                    <td colspan="6">
                                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="items" items="${approvedSearchResult}" varStatus="status">
                                                    <tr name="basicData">
                                                        <td><c:out value="${status.index + 1}"/></td>
                                                        <td><c:out value = "${items.facilityName}"/></td>
                                                        <td><c:out value="0915 xxxx tech 4-168"></c:out></td>
                                                        <td><iais:code code="${items.facilityStatus}"></iais:code></td>
                                                        <td><c:out value="${items.admin}"/></td>
                                                        <td><fmt:formatDate value='${items.approvalDate}'
                                                                            pattern='dd/MM/yyyy'/></td>
                                                        <td><fmt:formatDate value='${items.expiryDt}' pattern='dd/MM/yyyy'/></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                    </table>
                                </c:if>
                                <c:if test="${count == '0'}">
                                    <iais:message key="GENERAL_ACK018"
                                                  escape="true"/>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
