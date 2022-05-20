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
            <a class="collapsed" data-toggle="collapse" href="#previewLicensee">
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
            <div class="panel-main-content postion-relative">
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
                    <div class="row img-show ${curDto.licenseeType == companyType || oldDto.licenseeType == companyType ?
                                '' : 'hidden'}">
                        <div class="col-md-6">UEN No.</div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal" attr="${curDto.uenNo}">
                                    <c:out value="${curDto.uenNo}" />
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                        <jsp:param name="idNo" value="${curDto.uenNo}"/>
                                        <jsp:param name="methodName" value="showThisTableNew"/>
                                    </jsp:include>
                                </span>
                            </div>
                            <div class="col-md-6">
                                <span class="oldVal" attr="${oldDto.uenNo}">
                                    <c:out value="${oldDto.uenNo}" />
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                        <jsp:param name="idNo" value="${oldDto.idNumber}"/>
                                        <jsp:param name="methodName" value="showThisTableOld"/>
                                    </jsp:include>
                                </span>
                            </div>
                        </div>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${curDto.uenNo}"/>
                        <jsp:param name="cssClass" value="new-img-show"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${oldDto.uenNo}"/>
                        <jsp:param name="cssClass" value="old-img-show"/>
                    </jsp:include>
                    <c:if test="${curDto.licenseeType == individualType || oldDto.licenseeType == individualType}">
                        <div class="row">
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
                        <div class="row img-show">
                            <div class="col-md-6">ID No.</div>
                            <div class="col-md-6">
                                <div class="col-md-6">
                                    <span class="newVal" attr="${curDto.idNumber}">
                                        <c:out value="${curDto.idNumber}" />
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                            <jsp:param name="idNo" value="${curDto.idNumber}"/>
                                            <jsp:param name="methodName" value="showThisTableNew"/>
                                        </jsp:include>
                                    </span>
                                </div>
                                <div class="col-md-6">
                                    <span class="oldVal" attr="${oldDto.idNumber}">
                                        <c:out value="${oldDto.idNumber}" />
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                            <jsp:param name="idNo" value="${oldDto.idNumber}"/>
                                            <jsp:param name="methodName" value="showThisTableOld"/>
                                        </jsp:include>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                            <jsp:param name="idNo" value="${curDto.idNumber}"/>
                            <jsp:param name="cssClass" value="new-img-show"/>
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                            <jsp:param name="idNo" value="${oldDto.idNumber}"/>
                            <jsp:param name="cssClass" value="old-img-show"/>
                        </jsp:include>
                        <c:if test="${curDto.idType == 'IDTYPE003' || oldDto.idType == 'IDTYPE003'}">
                        <div class="row">
                            <div class="col-md-6">Country of issuance</div>
                            <div class="col-md-6">
                                <div class="col-md-6">
                                        <span class="newVal" attr="${curDto.nationality}">
                                            <iais:code code="${curDto.nationality}" />
                                        </span>
                                </div>
                                <div class="col-md-6">
                                        <span class="oldVal" attr="${oldDto.nationality}">
                                            <iais:code code="${oldDto.nationality}" />
                                        </span>
                                </div>
                            </div>
                        </div>
                        </c:if>
                    </c:if>
                </c:if>
                <%-- SOLO --%>
                <c:if test="${curDto.licenseeType eq soloType || oldDto.licenseeType eq soloType}">
                    <div class="row img-show">
                        <div class="col-md-6">NRIC/FIN</div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal" attr="${curDto.idNumber}">
                                    <c:out value="${curDto.idNumber}" />
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                        <jsp:param name="idNo" value="${curDto.idNumber}"/>
                                        <jsp:param name="methodName" value="showThisTableNew"/>
                                    </jsp:include>
                                </span>
                            </div>
                            <div class="col-md-6">
                                <span class="oldVal" attr="${oldDto.idNumber}">
                                    <c:out value="${oldDto.idNumber}" />
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                        <jsp:param name="idNo" value="${oldDto.idNumber}"/>
                                        <jsp:param name="methodName" value="showThisTableOld"/>
                                    </jsp:include>
                                </span>
                            </div>
                        </div>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${curDto.idNumber}"/>
                        <jsp:param name="cssClass" value="new-img-show"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${oldDto.idNumber}"/>
                        <jsp:param name="cssClass" value="old-img-show"/>
                    </jsp:include>
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