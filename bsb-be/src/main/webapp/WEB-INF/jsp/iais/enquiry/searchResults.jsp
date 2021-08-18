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
                                            <iais:sortableHeader needSort="true"
                                                                 field="applicationNo"
                                                                 value="Application No"/>
                                            <iais:sortableHeader needSort="true"
                                                                 field="applicationType"
                                                                 value="Application Type"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="Application Status"
                                                                 value="Application Status"/>
                                            <iais:sortableHeader needSort="true"
                                                                 field="applicationSubmissionDate"
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
                                       <c:forEach var="items" items="${appInfoSearchResult}" varStatus="status">
                                        <tr name="basicData">
                                            <td><c:out  value="${status.index + 1}"/></td>
                                            <td><a onclick="javascript:doAppInfo()"><c:out  value="${items.applicationNo}"/></a></td>
                                            <td><iais:code code="${items.appType}"></iais:code></td>
                                            <td><iais:code code="${items.status}"></iais:code></td>
                                            <td><fmt:formatDate value='${items.applicationDt}' pattern='dd/MM/yyyy'/></td>
                                            <td><fmt:formatDate value='${items.approvalDate}' pattern='dd/MM/yyyy'/></td>
                                            <td><iais:code code="${items.facility.facilityClassification}"></iais:code></td>
                                            <td><iais:code code="${items.facility.facilityType}"></iais:code></td>
                                            <td><iais:code code="${items.facility.facilityName}"></iais:code></td>
                                            <td><c:out value="${items.facility.biological.name}"></c:out></td>
                                            <td><iais:code code="${items.facility.biological.riskLevel}"></iais:code></td>
                                            <td><iais:code code="${items.processType}"></iais:code></td>
                                            <td><fmt:formatDate value='${items.doVerifiedDt}' pattern='dd/MM/yyyy'/></td>
                                            <td><fmt:formatDate value='${items.aoVerifiedDt}' pattern='dd/MM/yyyy'/></td>
                                            <td><fmt:formatDate value='${items.hmVerifiedDt}' pattern='dd/MM/yyyy'/></td>
                                            <td>  <iais:select name="action" id="action" options="action" firstOption="Please Select"></iais:select></td>
                                        </tr>
                                       </c:forEach>
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
                                        <c:forEach var="items" items="${facInfoSearchResult}" varStatus="status">
                                            <tr name="basicData">
                                                <td><c:out  value="${status.index + 1}"/></td>
                                                <td><c:out  value="${items.facilityName}"/></td>
                                                <td><c:out value="${items.facilityAddress}"/></td>
                                                <td><iais:code code="${items.facilityClassification}"></iais:code></td>
                                                <td><iais:code code="${items.facilityType}"></iais:code></td>
                                                <td><c:out  value="${items.biologicalAgent}"/></td>
                                                <td><iais:code code="${items.riskLevelOfTheBiologicalAgent}"></iais:code></td>
                                                <td><fmt:formatDate value='${items.facilityExpiryDate}' pattern='dd/MM/yyyy'/></td>
                                                <td>
                                                    <c:if test="${items.gazettedArea == 'Y'}">
                                                        <c:out  value="yes"/></c:if>
                                                    <c:if test="${items.gazettedArea =='N'}">
                                                        <c:out  value="No"/></c:if>
                                                </td>
                                                <td><c:out  value="${items.facilityOperator}"/></td>
                                                <td><c:out  value="${items.facilityAdmin}"/></td>
                                                <td><iais:code code="${items.currentFacilityStatus}"></iais:code></td>
                                                <td><c:out  value="${items.approvedFacilityCertifier}"/></td>
                                                <td>  <iais:select name="action" id="action" options="action" firstOption="Please Select"></iais:select></td>
                                            </tr>
                                        </c:forEach>
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
                                        <c:forEach var="items" items="${approvalSearchResult}" varStatus="status">
                                            <tr name="basicData">
                                                <td><c:out  value="${status.index + 1}"/></td>
                                                <td><iais:code code="${items.approvalType}"></iais:code></td>
                                                <td><iais:code code="${items.approvalStatus}"></iais:code></td>
                                                <td><iais:code code="${items.facilityClassification}"></iais:code></td>
                                                <td><iais:code code="${items.facilityType}"></iais:code></td>
                                                <td><iais:code code="${items.facilityName}"></iais:code></td>
                                                <td><c:out value="${items.facilityAddress}"></c:out></td>
                                                <td><iais:code code="${items.facilityStatus}"></iais:code></td>
                                                <td><iais:code code="${items.agent}"></iais:code></td>
                                                <td><iais:code code="${items.natureOfTheSample}"></iais:code></td>
                                                <td><iais:code code="${items.riskLevelOfTheBiologicalAgent}"></iais:code></td>
                                                <td></td>
                                                <td>  <iais:select name="action" id="action" options="action" firstOption="Please Select"></iais:select></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>