<input type="hidden" name="appId" id="appId" value="">
<div class="row">
    <div class="col-xs-12">
        <div class="components">
            <h3>
                <span>Search Results</span>
            </h3>
            <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}"
                                 pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
            <div class="table-responsive">
                <div class="table-gp">
                    <c:if test="${count=='app'}">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field=""
                                                     value="S/N"/>
                                <iais:sortableHeader needSort="true"
                                                     field="applicationNo"
                                                     value="Application No"/>
                                <iais:sortableHeader needSort="true"
                                                     field="appType"
                                                     value="Application Type"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Application Status"
                                                     value="Application Status"/>
                                <iais:sortableHeader needSort="true"
                                                     field="applicationDt"
                                                     value="Application Submission Date"/>
                                <iais:sortableHeader needSort="true"
                                                     field="approvalDate"
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
                                                     field="doVerifiedDt"
                                                     value="Verified By DO"/>
                                <iais:sortableHeader needSort="false"
                                                     field="aoVerifiedDt"
                                                     value="Verified By AO"/>
                                <iais:sortableHeader needSort="false"
                                                     field="hmVerifiedDt"
                                                     value="Verified By HM"/>
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
                                    <c:forEach var="items" items="${appInfoSearchResult}" varStatus="status">
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
                        <table class="table">
                            <thead>
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
                                <iais:sortableHeader needSort="true"
                                                     field="facilitySchedule.facility.expiryDt"
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
                                            <td><c:out value="${items.facilitySchedule.facilityActivity.facility.facilityName}"/></td>
                                            <td><c:out value="${items.facilitySchedule.facilityActivity.facility.blkNo}"/> <c:out
                                                    value="${items.facilitySchedule.facilityActivity.facility.streetName}"/> <c:out
                                                    value="${items.facilitySchedule.facilityActivity.facility.floorNo}"/>-<c:out
                                                    value="${items.facilitySchedule.facilityActivity.facility.unitNo}"/> <c:out
                                                    value="${items.facilitySchedule.facilityActivity.facility.postalCode}"/></td>
                                            <td><iais:code
                                                    code="${items.facilitySchedule.facilityActivity.facility.facilityClassification}"></iais:code></td>
                                            <td><iais:code
                                                    code="${items.facilitySchedule.facilityActivity.activityType}"></iais:code></td>
                                            <td><c:out value="${items.bioName}"/></td>
                                            <td><iais:code code="${items.riskLevel}"></iais:code></td>
                                            <td><fmt:formatDate value='${items.facilitySchedule.facilityActivity.facility.expiryDt}'
                                                                pattern='dd/MM/yyyy'/></td>
                                            <td>
                                                <c:if test="${items.facilitySchedule.facilityActivity.facility.isProtected == 'Y'}">
                                                    <c:out value="yes"/></c:if>
                                                <c:if test="${items.facilitySchedule.facilityActivity.facility.isProtected =='N'}">
                                                    <c:out value="No"/></c:if>
                                            </td>
                                            <td><c:out value="${items.facilitySchedule.facilityActivity.facility.operator.facOperator}"/></td>
                                            <td><c:out value="${items.admin}"/></td>
                                            <td><iais:code
                                                    code="${items.facilitySchedule.facilityActivity.facility.facilityStatus}"></iais:code></td>
                                            <td><c:out value="${items.facilitySchedule.facilityActivity.facility.approval}"/></td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${count=='an'}">
                        <table class="table">
                            <thead>
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
                                <c:when test="${empty approvalSearchResult}">
                                    <tr>
                                        <td colspan="6">
                                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="items" items="${approvalSearchResult}" varStatus="status">
                                        <tr name="basicData">
                                            <td><c:out value="${status.index + 1}"/></td>
                                            <td><iais:code
                                                    code="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.facility.approvalType}"></iais:code></td>
                                            <td><iais:code
                                                    code="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.facility.approvalStatus}"></iais:code></td>
                                            <td><iais:code
                                                    code="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.facility.facilityClassification}"></iais:code></td>
                                            <td><iais:code
                                                    code="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.activityType}"></iais:code></td>
                                            <td><iais:code
                                                    code="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.facility.facilityName}"></iais:code></td>
                                            <td><c:out
                                                    value="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.facility.blkNo}"/>
                                                <c:out value="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.facility.streetName}"/>
                                                <c:out value="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.facility.floorNo}"/>-<c:out
                                                        value="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.facility.unitNo}"/>
                                                <c:out value="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.facility.postalCode}"/></td>
                                            <td><iais:code
                                                    code="${items.facilityBiologicalAgent.facilitySchedule.facilityActivity.facility.facilityStatus}"></iais:code></td>
                                            <td><iais:code code="${items.bioName}"></iais:code></td>
                                            <td><iais:code code="${items.sampleNature}"></iais:code></td>
                                            <td><iais:code
                                                    code="${items.facilityBiologicalAgent.riskLevel}"></iais:code></td>
                                            <td>12</td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${count=='on'}">
                        <table class="table">
                            <thead>
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
                                <iais:sortableHeader needSort="true"
                                                     field="Expiry Date"
                                                     value="Expiry Date"/>
                            </tr>
                            </thead>
                            <tbody class="form-horizontal">
                            <c:choose>
                                <c:when test="${empty approvedSearchResult}">
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
                </div>
            </div>
        </div>
    </div>
</div>
