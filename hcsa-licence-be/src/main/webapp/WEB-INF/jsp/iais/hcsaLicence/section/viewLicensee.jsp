<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="curDto" value="${appSubmissionDto.subLicenseeDto}" />
<c:set var="oldDto" value="${appSubmissionDto.oldAppSubmissionDto.subLicenseeDto}" />

<c:set var="companyType" value="LICTSUB001" />
<c:set var="individualType" value="LICTSUB002" />
<c:set var="soloType" value="LICT002" />

<c:if test="${curDto.licenseeType == companyType}">
    <c:set var="telephoneLabel" value="Office Telephone No." />
</c:if>
<c:if test="${curDto.licenseeType == individualType}">
    <c:set var="telephoneLabel" value="Mobile No." />
</c:if>
<c:if test="${curDto.licenseeType == soloType}">
    <c:set var="telephoneLabel" value="Telephone No." />
</c:if>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#previewLicensee">
                Licensee Details
            </a>
        </h4>
    </div>
    <div id="previewLicensee" class="panel-collapse collapse">
        <div class="panel-body">
            <p class="text-right">
                <c:if test="${rfi == 'rfi'}">
                    <c:if test="${appEdit.licenseeEdit}">
                        <input class="form-check-input" id="licenseeCheckbox" type="checkbox" name="editCheckbox"
                               <c:if test="${pageEdit.licenseeEdit}">checked</c:if> aria-invalid="false"
                               value="licensee" />
                    </c:if>
                </c:if>
            </p>
            <div class="panel-main-content">
                <div class="row" style="margin-top: 1%;margin-bottom: 1%;">
                    <div class="col-md-12">
                        <label>Licensee Details</label>
                    </div>
                </div>
                <%-- company / individual --%>
                <c:if test="${curDto.licenseeType ne soloType && oldDto.licenseeType ne soloType}">
                    <div class="row">
                        <div class="col-md-6">Licensee Type</div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal" attr="${curDto.licenseeType}">
                                    <iais:code code="${curDto.licenseeType}" />
                                </span>
                            </div>
                            <div class="col-md-6">
                                <span class="oldVal" attr="${oldDto.licenseeType}">
                                    <iais:code code="${oldDto.licenseeType}" />
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="row img-show" cssClass="company-no" style="${curDto.licenseeType == companyType || oldDto.licenseeType == companyType ?
                                '' : 'display: none;'}">
                        <div class="col-md-6">UEN No.</div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal" attr="${curDto.uenNo}">
                                    <c:out value="${curDto.uenNo}" />
                                    <c:if test="${empty hashMap[curDto.uenNo]}">
                                        <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                    </c:if>
                                    <c:if test="${not empty hashMap[curDto.uenNo]}">
                                        <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNew(this)" width="25" height="25" alt="NETS">
                                    </c:if>
                                </span>
                            </div>
                            <div class="col-md-6">
                                <span class="oldVal" attr="${oldDto.uenNo}">
                                    <c:out value="${oldDto.uenNo}" />
                                    <c:if test="${empty hashMap[oldDto.uenNo]}">
                                        <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                    </c:if>
                                    <c:if test="${not empty hashMap[oldDto.uenNo]}">
                                        <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNew(this)" width="25" height="25" alt="NETS">
                                    </c:if>
                                </span>
                            </div>
                        </div>
                    </div>
                    <c:if test="${not empty hashMap[curDto.uenNo]}">
                        <div class="row new-img-show" style="display: none">
                            <div class="col-xs-12 col-md-12" style="position: absolute;z-index: 100;background-color: #F5F5F5">
                                <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                <div class="table-responsive">
                                    <table aria-describedby="" border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                                        <tr>
                                            <th scope="col">Indentification No.</th>
                                            <th scope="col">Case No.</th>
                                            <th scope="col">Case Type Description</th>
                                            <th scope="col">Case Status Description</th>
                                            <th scope="col">Offence Description</th>
                                            <th scope="col">Outcome Description</th>
                                            <th scope="col">Outcome Issue Date</th>
                                            <th scope="col">Prosecution Outcome Description</th>
                                            <th scope="col">Created Date</th>
                                            <th scope="col">Update Date</th>
                                        </tr>
                                        <c:forEach items="${hashMap[curDto.uenNo]}" var="map">
                                            <tr>
                                                <td>${map.identificationNo}</td>
                                                <td>${map.caseNo}</td>
                                                <td>${map.caseType}</td>
                                                <td>Case Status Description</td>
                                                <td>${map.offenceDesc}</td>
                                                <td>${map.outcome}</td>
                                                <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                <td>${map.prosecutionOutcome}</td>
                                                <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                            </tr>
                                        </c:forEach>
                                        <tr></tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${not empty hashMap[oldDto.uenNo]}">
                        <div class="row old-img-show" style="display: none">
                            <div class="col-xs-12 col-md-12" style="position: absolute;z-index: 100;background-color: #F5F5F5">
                                <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                <div class="table-responsive">
                                    <table aria-describedby="" border="1px"
                                           style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                                        <tr>
                                            <th scope="col">Indentification No.</th>
                                            <th scope="col">Case No.</th>
                                            <th scope="col">Case Type Description</th>
                                            <th scope="col">Case Status Description</th>
                                            <th scope="col">Offence Description</th>
                                            <th scope="col">Outcome Description</th>
                                            <th scope="col">Outcome Issue Date</th>
                                            <th scope="col">Prosecution Outcome Description</th>
                                            <th scope="col">Created Date</th>
                                            <th scope="col">Update Date</th>
                                        </tr>
                                        <c:forEach items="${hashMap[oldDto.uenNo]}" var="map">
                                            <tr>
                                                <td>${map.identificationNo}</td>
                                                <td>${map.caseNo}</td>
                                                <td>${map.caseType}</td>
                                                <td>Case Status Description</td>
                                                <td>${map.offenceDesc}</td>
                                                <td>${map.outcome}</td>
                                                <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                <td>${map.prosecutionOutcome}</td>
                                                <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                            </tr>
                                        </c:forEach>
                                        <tr></tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <div class="row" cssClass="ind-no" style="${curDto.licenseeType == individualType || oldDto.licenseeType == individualType ? '' : 'display: none;'}">
                        <div class="col-md-6">ID Type</div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal" attr="${curDto.idType}">
                                    <iais:code code="${curDto.idType}" />
                                </span>
                            </div>
                            <div class="col-md-6">
                                <span class="oldVal" attr="${oldDto.idType}">
                                    <iais:code code="${oldDto.idType}" />
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="row" cssClass="ind-no" style="${curDto.licenseeType == individualType || oldDto.licenseeType == individualType ? '' : 'display: none;'}">
                        <div class="col-md-6">ID No.</div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal" attr="${curDto.idNumber}">
                                    <c:out value="${curDto.idNumber}" />
                                    <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                </span>
                            </div>
                            <div class="col-md-6">
                                <span class="oldVal" attr="${oldDto.idNumber}">
                                    <c:out value="${oldDto.idNumber}" />
                                    <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                </span>
                            </div>
                        </div>
                    </div>
                </c:if>
                <%-- SOLO --%>
                <c:if test="${curDto.licenseeType eq soloType || oldDto.licenseeType eq soloType}">
                    <div class="row">
                        <div class="col-md-6">NRIC/FIN</div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal" attr="${curDto.idNumber}">
                                    <c:out value="${curDto.idNumber}" />
                                    <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                </span>
                            </div>
                            <div class="col-md-6">
                                <span class="oldVal" attr="${oldDto.idNumber}">
                                    <c:out value="${oldDto.idNumber}" />
                                    <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                </span>
                            </div>
                        </div>
                    </div>
                </c:if>
                <div class="row">
                    <div class="col-md-6">Licensee Name</div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal" attr="<c:out value="${curDto.licenseeName}" />">
                                <c:out value="${curDto.licenseeName}" />
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="<c:out value="${oldDto.licenseeName}" />">
                                <c:out value="${oldDto.licenseeName}" />
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">Postal Code</div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal" attr="${curDto.postalCode}">
                                <c:out value="${curDto.postalCode}" />
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="${oldDto.postalCode}">
                                <c:out value="${oldDto.postalCode}" />
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">Address Type</div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal" attr="${curDto.addrType}">
                                <iais:code code="${curDto.addrType}" />
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="${oldDto.addrType}">
                                <iais:code code="${oldDto.addrType}" />
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">Block / House No.</div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal" attr="<c:out value="${curDto.blkNo}" />">
                                <c:out value="${curDto.blkNo}" />
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="<c:out value="${oldDto.blkNo}" />">
                                <c:out value="${oldDto.blkNo}" />
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">Floor No.</div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal" attr="<c:out value="${curDto.floorNo}" />">
                                <c:out value="${curDto.floorNo}" />
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="<c:out value="${oldDto.floorNo}" />">
                                <c:out value="${oldDto.floorNo}" />
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">Unit No.</div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal" attr="<c:out value="${curDto.unitNo}" />">
                                <c:out value="${curDto.unitNo}" />
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="<c:out value="${oldDto.unitNo}" />">
                                 <c:out value="${oldDto.unitNo}" />
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">Street Name</div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal" attr="<c:out value="${curDto.streetName}" />">
                                <c:out value="${curDto.streetName}" />
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="<c:out value="${oldDto.streetName}" />">
                                 <c:out value="${oldDto.streetName}" />
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">Building Name</div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal" attr="<c:out value="${curDto.buildingName}" />">
                                <c:out value="${curDto.buildingName}" />
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="<c:out value="${oldDto.buildingName}" />">
                                 <c:out value="${oldDto.buildingName}" />
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">${telephoneLabel}</div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal" attr="<c:out value="${curDto.telephoneNo}" />">
                                <c:out value="${curDto.telephoneNo}" />
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="<c:out value="${oldDto.telephoneNo}" />">
                                 <c:out value="${oldDto.telephoneNo}" />
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">${curDto.licenseeType == companyType ? 'Office' : ''} Email Address</div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal" attr="<c:out value="${curDto.emailAddr}" />">
                                <c:out value="${curDto.emailAddr}" />
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="<c:out value="${oldDto.emailAddr}" />">
                                 <c:out value="${oldDto.emailAddr}" />
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>