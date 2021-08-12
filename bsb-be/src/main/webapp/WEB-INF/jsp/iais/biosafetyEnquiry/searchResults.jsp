<div class="row">
    <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="components">
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <iais:pagination param="biosafetyEnquirySearchParam" result="biosafetyEnquirySearchResult"/>
                        <div class="table-responsive">
                            <div class="table-gp">
                                <c:if test="${count=='1'}">
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
                                       <c:forEach var="items" items="${appInfoSearchResult}" varStatus="status">
                                        <tr name="basicData">
                                            <td><c:out  value="${status.index + 1}"/></td>
                                            <td><a onclick="javascript:doAppInfo()"><c:out  value="${items.applicationNo}"/></a></td>
                                            <td><iais:code code="${items.applicationType}"></iais:code></td>
                                            <td><iais:code code="${items.applicationStatus}"></iais:code></td>
                                            <td><fmt:formatDate value='${items.applicationSubmissionDate}' pattern='dd/MM/yyyy'/></td>
                                            <td><fmt:formatDate value='${items.approvalDate}' pattern='dd/MM/yyyy'/></td>
                                            <td><iais:code code="${items.facilityClassification}"></iais:code></td>
                                            <td><iais:code code="${items.facilityType}"></iais:code></td>
                                            <td><iais:code code="${items.facilityName}"></iais:code></td>
                                            <td><iais:code code="${items.biologicalAgent}"></iais:code></td>
                                            <td><iais:code code="${items.riskLevelOfTheBiologicalAgent}"></iais:code></td>
                                            <td><iais:code code="${items.processType}"></iais:code></td>
                                            <td><fmt:formatDate value='${items.verifiedByDO}' pattern='dd/MM/yyyy'/></td>
                                            <td><fmt:formatDate value='${items.verifiedByAO}' pattern='dd/MM/yyyy'/></td>
                                            <td><fmt:formatDate value='${items.verifiedByHM}' pattern='dd/MM/yyyy'/></td>
                                            <td>  <iais:select name="action" id="action" options="action" firstOption="Please Select"></iais:select></td>
                                        </tr>
                                       </c:forEach>
                                        </tbody>
                                    </table>
                                </c:if>
                                <c:if test="${count=='2'}">
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