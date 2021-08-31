<div class="row">
    <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
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
                                    <table class="table">
                                        <thead>
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
                                            <iais:sortableHeader needSort="false"
                                                                 field="Action"
                                                                 value="Action"/>
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
                                                        <td><c:out  value="${status.index+1}"/></td>
                                                        <td><a onclick="javascript:doAppInfo()"><c:out  value="${items.applicationNo}"/></a></td>
                                                        <td><iais:code code="${items.appType}"></iais:code></td>
                                                        <td><iais:code code="${items.appStatus}"></iais:code></td>
                                                        <td><fmt:formatDate value='${items.applicationDt}' pattern='dd/MM/yyyy'/></td>
                                                        <td><fmt:formatDate value='${items.approvalDate}' pattern='dd/MM/yyyy'/></td>
                                                        <td><iais:code code="${items.facilityClassification}"></iais:code></td>
                                                        <td><iais:code code="${items.facilityType}"></iais:code></td>
                                                        <td><iais:code code="${items.facilityName}"></iais:code></td>
                                                        <td><c:out value="${items.bioName}"></c:out></td>
                                                        <td><iais:code code="${items.riskLevel}"></iais:code></td>
                                                        <td><iais:code code="${items.processType}"></iais:code></td>
                                                        <td><fmt:formatDate value='${items.doVerifiedDt}' pattern='dd/MM/yyyy'/></td>
                                                        <td><fmt:formatDate value='${items.aoVerifiedDt}' pattern='dd/MM/yyyy'/></td>
                                                        <td><fmt:formatDate value='${items.hmVerifiedDt}' pattern='dd/MM/yyyy'/></td>
                                                        <td>  <iais:select name="action" id="action" options="action" firstOption="Please Select"></iais:select></td>
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
                                            <iais:sortableHeader needSort="false"
                                                                 field="Action"
                                                                 value="Action"/>
                                        </tr>
                                        </thead>
                                        <tbody class="form-horizontal">
                                        <c:choose>
                                            <c:when test="${empty facilityInfoDto}">
                                                <tr>
                                                    <td colspan="6">
                                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="items" items="${facilityInfoDto}" varStatus="status">
                                                    <tr name="basicData">
                                                        <td><c:out  value="${status.index + 1}"/></td>
                                                        <td><c:out  value="${items.facilitySchedule.facility.facilityName}"/></td>
                                                        <td><c:out value="${items.facilitySchedule.facility.blkNo}"/> <c:out value="${items.facilitySchedule.facility.streetName}"/> <c:out value="${items.facilitySchedule.facility.floorNo}"/>-<c:out value="${items.facilitySchedule.facility.unitNo}"/> <c:out value="${items.facilitySchedule.facility.postalCode}"/></td>
                                                        <td><iais:code code="${items.facilitySchedule.facility.facilityClassification}"></iais:code></td>
                                                        <td><iais:code code="${items.facilitySchedule.facility.facilityType}"></iais:code></td>
                                                        <td><c:out  value="${items.bioName}"/></td>
                                                        <td><iais:code code="${items.riskLevel}"></iais:code></td>
                                                        <td><fmt:formatDate value='${items.facilitySchedule.facility.expiryDt}' pattern='dd/MM/yyyy'/></td>
                                                        <td>
                                                            <c:if test="${items.facilitySchedule.facility.isProtected == 'Y'}">
                                                                <c:out  value="yes"/></c:if>
                                                            <c:if test="${items.facilitySchedule.facility.isProtected =='N'}">
                                                                <c:out  value="No"/></c:if>
                                                        </td>
                                                        <td><c:out  value="${items.facilitySchedule.facility.operatorName}"/></td>

                                                        <td>
                                                            <c:forEach var="admins" items="${items.facilitySchedule.facility.admins}" varStatus="status">
                                                                <c:choose>
                                                                    <c:when test="${status.last}">
                                                                        <c:out value="${admins.name}"></c:out>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:out value="${admins.name}"></c:out>,
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </td>
                                                        <td><iais:code code="${items.facilitySchedule.facility.facilityStatus}"></iais:code></td>
                                                        <td><c:out  value="${items.facilitySchedule.facility.approval}"/></td>
                                                        <td>  <iais:select name="action" id="action" options="action" firstOption="Please Select"></iais:select></td>
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
                                            <iais:sortableHeader needSort="false"
                                                                 field="Expiry Date"
                                                                 value="Expiry Date"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Action"
                                                                 value="Action"/>
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
                                                        <td><c:out  value="${status.index + 1}"/></td>
                                                        <td><iais:code code="${items.organization.name}"></iais:code></td>
                                                        <td><c:out value="${items.organization.blkNo}"/> <c:out value="${items.organization.streetName}"/> <c:out value="${items.organization.buildingName}"/> <c:out value="${items.organization.floorNo}"/>-<c:out value="${items.organization.unitNo}"/> </td>
                                                        <td><iais:code code="${items.facilityStatus}"></iais:code></td>
                                                        <td>
                                                            <c:forEach var="admins" items="${items.admins}" varStatus="status">
                                                                <c:choose>
                                                                    <c:when test="${status.last}">
                                                                        <c:out value="${admins.name}"></c:out>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:out value="${admins.name}"></c:out>,
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </td>
                                                        <td><fmt:formatDate value='${items.approvalDate}' pattern='dd/MM/yyyy'/></td>
                                                        <td><fmt:formatDate value='${items.expiryDt}' pattern='dd/MM/yyyy'/></td>
                                                        <td>  <iais:select name="action" id="action" options="action" firstOption="Please Select"></iais:select></td>
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
                                            <iais:sortableHeader needSort="false"
                                                                 field="Action"
                                                                 value="Action"/>
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
                                                    <td><c:out  value="${status.index + 1}"/></td>
                                                    <td><iais:code code="${items.facilityBiologicalAgent.facilitySchedule.facility.approvalType}"></iais:code></td>
                                                    <td><iais:code code="${items.facilityBiologicalAgent.facilitySchedule.facility.approvalStatus}"></iais:code></td>
                                                    <td><iais:code code="${items.facilityBiologicalAgent.facilitySchedule.facility.facilityClassification}"></iais:code></td>
                                                    <td><iais:code code="${items.facilityBiologicalAgent.facilitySchedule.facility.facilityType}"></iais:code></td>
                                                    <td><iais:code code="${items.facilityBiologicalAgent.facilitySchedule.facility.facilityName}"></iais:code></td>
                                                    <td><c:out value="${items.facilityBiologicalAgent.facilitySchedule.facility.blkNo}"/> <c:out value="${items.facilityBiologicalAgent.facilitySchedule.facility.streetName}"/> <c:out value="${items.facilityBiologicalAgent.facilitySchedule.facility.floorNo}"/>-<c:out value="${items.facilityBiologicalAgent.facilitySchedule.facility.unitNo}"/> <c:out value="${items.facilityBiologicalAgent.facilitySchedule.facility.postalCode}"/></td>
                                                    <td><iais:code code="${items.facilityBiologicalAgent.facilitySchedule.facility.facilityStatus}"></iais:code></td>
                                                    <td><iais:code code="${items.bioName}"></iais:code></td>
                                                    <td><iais:code code="${items.sampleNature}"></iais:code></td>
                                                    <td><iais:code code="${items.facilityBiologicalAgent.riskLevel}"></iais:code></td>
                                                    <td></td>
                                                    <td>  <iais:select name="action" id="action" options="action" firstOption="Please Select"></iais:select></td>
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
        </div>
    </div>
</div>