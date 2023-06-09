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
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" style="display: none"></th>
                            </tr>
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
                                <c:when test="${empty appResult}">
                                    <tr>
                                        <td colspan="6">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="items" items="${appResult}" varStatus="status">
                                        <tr name="basicData">
                                            <td><c:out value="${status.index+1}"/></td>
                                            <td>
                                                    <%--todo: check this app status and logic--%>
                                                <c:choose>
                                                <c:when test="${items.appType eq 'BSBAPTY006' and items.appStatus eq 'BSBAPST031'}">
                                                    <a href="/bsb-web/eservicecontinue/INTRANET/MohAOProcessingRevocation?appId=<iais:mask name='id' value='${items.id}'/>&OWASP_CSRFTOKEN=null"><c:out value="${items.applicationNo}"/></a>
                                                </c:when>
                                                <c:when test="${items.appType eq 'BSBAPTY001' and (items.appStatus eq 'BSBAPST030' or items.appStatus eq 'BSBAPST031' or items.appStatus eq 'BSBAPST032')}">
                                                    <a href="/bsb-web/eservicecontinue/INTRANET/MohOfficersProcess?appId=<iais:mask name='id' value='${items.id}'/>&OWASP_CSRFTOKEN=null"><c:out value="${items.applicationNo}"/></a>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${items.applicationNo}"/>
                                                </c:otherwise>
                                            </c:choose>
                                            </td>
                                            <td><iais:code code="${items.appType}"/></td>
                                            <td><iais:code code="${items.appStatus}"/></td>
                                            <td><fmt:formatDate value='${items.applicationDt}'
                                                                pattern='dd/MM/yyyy'/></td>
                                            <td><iais-bsb:format-LocalDate localDate='${items.approvalDate}'/></td>
                                            <td><iais:code
                                                    code="${items.facilityClassification}"/></td>
                                            <td><iais:code code="${items.facilityType}"/></td>
                                            <td><iais:code code="${items.facilityName}"/></td>
                                            <td><c:out value="${items.bioName}"/></td>
                                            <td><iais:code code="${items.riskLevel}"/></td>
                                            <td><iais:code code="${items.processType}"/></td>
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
                    <c:if test="${count=='fac'}">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" style="display: none"></th>
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
                                <c:when test="${empty facResult}">
                                    <tr>
                                        <td colspan="6">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="items" items="${facResult}" varStatus="status">
                                        <tr name="basicData">
                                            <td><c:out value="${status.index + 1}"/></td>
                                            <td><c:out value="${items.facility.facilityName}"/></td>
                                            <td><c:out value="${items.facility.blkNo}"/> <c:out
                                                    value="${items.facility.streetName}"/> <c:out
                                                    value="${items.facility.floorNo}"/>-<c:out
                                                    value="${items.facility.unitNo}"/> <c:out
                                                    value="${items.facility.postalCode}"/></td>
                                            <td><iais:code
                                                    code="${items.facility.facilityClassification}"/></td>
                                            <td><iais:code
                                                    code="${items.activityType}"/></td>
                                            <td><c:out value="${items.bioName}"/></td>
                                            <td><iais:code code="${items.riskLevel}"/></td>
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
                                                    code="${items.approval.status}"/></td>
                                            <td><c:out value="approval01"/></td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${count=='approval'}">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" style="display: none"></th>
                            </tr>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field=""
                                                     value="S/N"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Approval Type"
                                                     value="Approval Type"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Approval No"
                                                     value="Approval No"/>
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
                                                     field=""
                                                     value="action"/>
                            </tr>
                            </thead>
                            <tbody class="form-horizontal">
                            <c:choose>
                                <c:when test="${empty approvalResult}">
                                    <tr>
                                        <td colspan="6">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="items" items="${approvalResult}" varStatus="status">
                                        <tr name="basicData">
                                            <td><c:out value="${status.index + 1}"/></td>
                                            <td><iais:code code="${items.apprNo}"/></td>
                                            <td><iais:code code="${items.type}"/></td>
                                            <td><iais:code code="${items.status}"/></td>
                                            <td><iais:code code="${items.facClassification}"/></td>
                                            <td><iais:code code="${items.facType}"/></td>
                                            <td><iais:code code="${items.facName}"/></td>
                                            <td><c:out value="${items.facAddress}"/></td>
                                            <td><iais:code code="${items.facStatus}"/></td>
                                            <td><iais:code code="${items.bat}"/></td>
                                            <td><iais:code code="${items.sampleName}"/></td>
                                            <td><iais:code code="${items.riskLevel}"/></td>
                                            <td>12</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${items.status eq 'APPRSTA001' or items.status eq 'APPRSTA004'}">
                                                        <a href="/bsb-web/eservicecontinue/INTRANET/MohDOSubmitRevocation?approvalId=<iais:mask name='id' value='${items.id}'/>&OWASP_CSRFTOKEN=null&from=fac">revoke</a>
                                                    </c:when>
                                                    <c:otherwise>

                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${count=='afc'}">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" style="display: none"></th>
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
                                <iais:sortableHeader needSort="true"
                                                     field="Expiry Date"
                                                     value="Expiry Date"/>
                            </tr>
                            </thead>
                            <tbody class="form-horizontal">
                            <c:choose>
                                <c:when test="${empty afcResult}">
                                    <tr>
                                        <td colspan="6">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="items" items="${afcResult}" varStatus="status">
                                        <tr name="basicData">
                                            <td><c:out value="${status.index + 1}"/></td>
                                            <td><c:out value = "${items.orgName}"/></td>
                                            <td><c:out value = "${items.address}"/><</td>
                                            <td><iais:code code="${items.approval.status}"/></td>
                                            <td><c:out value="${items.adminName}"/></td>
                                            <td><fmt:formatDate value='${items.approval.approvalDate}'
                                                                pattern='dd/MM/yyyy'/></td>
                                            <td><fmt:formatDate value='${items.approval.approvalExpiryDate}' pattern='dd/MM/yyyy'/></td>
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
