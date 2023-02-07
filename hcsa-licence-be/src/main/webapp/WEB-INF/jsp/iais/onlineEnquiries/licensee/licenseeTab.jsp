<div class="col-md-12">
    <div class="panel panel-default lic-content">

        <div class="panel-heading" id="headingLicensee" role="tab">
            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                       href="#collapseLicensee" aria-expanded="true"
                                       aria-controls="collapseLicensee">
                Licensee Details</a></h4>
        </div>

        <div class=" panel-collapse collapse" id="collapseLicensee" role="tabpanel"
             aria-labelledby="headingLicensee">
            <div class="panel-body">
                <div class="panel-main-content form-horizontal min-row">
                    <iais:row>
                        <iais:field width="5" value="Licensee Type"/>
                        <iais:value width="7" display="true">
                            <iais:code code="${subLicenseeDto.licenseeType}" />
                        </iais:value>
                    </iais:row>

                    <c:set var="companyType" value="LICTSUB001" />
                    <c:set var="individualType" value="LICTSUB002" />
                    <c:set var="soloType" value="LICT002" />

                    <c:if test="${subLicenseeDto.licenseeType == companyType}">
                        <c:set var="telephoneLabel" value="Office Telephone No." />
                    </c:if>
                    <c:if test="${subLicenseeDto.licenseeType == individualType}">
                        <c:set var="telephoneLabel" value="Mobile No." />
                    </c:if>
                    <c:if test="${subLicenseeDto.licenseeType == soloType}">
                        <c:set var="telephoneLabel" value="Telephone No." />
                    </c:if>

                    <iais:row cssClass=" ${subLicenseeDto.licenseeType == companyType ? '' : 'hidden'}">
                        <iais:field width="5" value="UEN No."/>
                        <iais:value width="7" display="true">
                            <iais:code code="${subLicenseeDto.uenNo}" />
                        </iais:value>
                    </iais:row>

                    <iais:row cssClass=" ${subLicenseeDto.licenseeType == individualType ? '' : 'hidden'}">
                        <iais:field width="5" value="ID Type"/>
                        <iais:value width="7" display="true">
                            <iais:code code="${subLicenseeDto.idType}" />
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass=" ${subLicenseeDto.licenseeType == individualType ? '' : 'hidden'}">
                        <iais:field width="5" value="ID No."/>
                        <iais:value width="7" display="true">
                            <c:out value="${subLicenseeDto.idNumber}" />
                        </iais:value>
                    </iais:row>

                    <iais:row cssClass=" ${subLicenseeDto.licenseeType == individualType
            && subLicenseeDto.idType == 'IDTYPE003'? '' : 'hidden'}">
                        <iais:field width="5" value="Nationality"/>
                        <iais:value width="7" display="true">
                            <iais:code code="${subLicenseeDto.nationality}" />
                        </iais:value>
                    </iais:row>

                    <iais:row cssClass=" ${subLicenseeDto.licenseeType == soloType ? '' : 'hidden'}">
                        <iais:field width="5" value="NRIC/FIN"/>
                        <iais:value width="7" display="true">
                            <c:out value="${subLicenseeDto.idNumber}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field value="Licensee Name" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${subLicenseeDto.licenseeName}" />
                        </iais:value>
                    </iais:row>

                    <%-- Address start --%>
                    <iais:row >
                        <iais:field value="Postal Code" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${subLicenseeDto.postalCode}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Address Type" width="5"/>
                        <iais:value width="7" display="true">
                            <iais:code code="${subLicenseeDto.addrType}" />
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field value="Block / House No." width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${not empty subLicenseeDto.blkNo ? subLicenseeDto.blkNo : '-'}" />
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field value="Floor / Unit No." width="5"/>
                        <iais:value width="7">
                            <c:choose>
                                <c:when test="${not empty subLicenseeDto.floorNo && not empty subLicenseeDto.unitNo}">
                                    <c:out value="${subLicenseeDto.floorNo} / ${subLicenseeDto.unitNo}" />
                                </c:when>
                                <c:otherwise>
                                    <c:out value="-" />
                                </c:otherwise>
                            </c:choose>

                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field value="Street Name" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${not empty subLicenseeDto.streetName ? subLicenseeDto.streetName : '-'}" />
                        </iais:value>
                    </iais:row>

                    <iais:row >
                        <iais:field value="Building Name" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${not empty subLicenseeDto.buildingName ? subLicenseeDto.buildingName : '-'}" />
                        </iais:value>
                    </iais:row>
                    <%-- Address end --%>

                    <iais:row>
                        <iais:field value="${telephoneLabel}" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${subLicenseeDto.telephoneNo}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field value="${subLicenseeDto.licenseeType == companyType ? 'Office' : ''} Email Address" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${subLicenseeDto.emailAddr}" />
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass=" ${(subLicenseeDto.licenseeType == individualType || subLicenseeDto.licenseeType == soloType )? '' : 'hidden'}">
                        <iais:field width="5" value="UEN"/>
                        <iais:value width="7" display="true">
                            <iais:code code="${subLicenseeDto.uenNo}" />
                        </iais:value>
                    </iais:row>

                    <iais:row cssClass=" ${subLicenseeDto.licenseeType == companyType || subLicenseeDto.licenseeType == individualType ? '' : 'hidden'}">
                        <iais:field value="Organisation Name" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${subLicenseeDto.licenseeName}" />
                        </iais:value>
                    </iais:row>
                </div>

            </div>
        </div>
    </div>


</div>

