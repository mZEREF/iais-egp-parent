<c:set var="headingSign" value="${isPrint?'':'completed'}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Patient Details
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <c:set var="patientDto" value="${dpSuperDataSubmissionDto.patientDto}"/>
                <iais:row>
                    <iais:field width="5" value="ID Type" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${patientDto.idType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID No." />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.idNumber}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Nationality" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${patientDto.nationality}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Name of Patient" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.name}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Birth" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.birthDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Postal Code" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.postalCode}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Address Type" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${patientDto.addrType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Blk No" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.blkNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Floor No. / Unit No." />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.floorNo}-${patientDto.unitNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Street Name" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.streetName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Building Name" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.buildingName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Gender" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:if test="${patientDto.gender eq '1'}"> <c:out value="Male"/></c:if>
                        <c:if test="${patientDto.gender eq '0'}"> <c:out value="Female"/></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Ethnic Group" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${patientDto.ethnicGroup}" />
                    </iais:value>
                </iais:row>
                <iais:row style="${patientDto.ethnicGroup eq 'ETHG005' ? '' : 'display: none'}">
                    <iais:field width="5" value="Ethnic Group (Others)" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.ethnicGroupOther}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Mobile No" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.mobileNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Home Telephone No" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.homeTelNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Email Address" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${patientDto.emailAddr}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>