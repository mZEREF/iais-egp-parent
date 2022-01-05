<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="patient" value="${patientInfoDto.patient}" />
<c:set var="previous" value="${patientInfoDto.previous}" />
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#patientDetails">
                Details of Patient
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="5" value="Name (as per NRIC/Passport)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${patient.name}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID Type"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${patient.idType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${patient.idNumber}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Birth"/>
                    <iais:value width="7" display="true">
                        <c:out value="${patient.birthDate}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Nationality"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${patient.nationality}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Ethnic Group"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${patient.ethnicGroup}" />
                    </iais:value>
                </iais:row>
                <div class="form-group" style="<c:if test="${patient.ethnicGroup ne 'ETHG005'}">display:none</c:if>">
                    <iais:field width="5" value="Ethnic Group (Others)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${patient.ethnicGroupOther}" />
                    </iais:value>
                </div>
                <div id="previousData" <c:if test="${patient.previousIdentification}">style="display:none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Is AR Centre aware of patient's previous identification? "/>
                        <iais:value width="7" display="true">No</iais:value>
                    </iais:row>
                </div>
                <div id="previousData" <c:if test="${!patient.previousIdentification}">style="display:none"</c:if> >
                    <iais:row>
                        <iais:value cssClass="col-xs-10 col-md-10 col-sm-12" display="true">
                            <strong>Previous Identification (if applicable)</strong>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Name (as per NRIC/Passport)"/>
                        <iais:value width="7" display="true">
                            <c:out value="${previous.name}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="ID Type"/>
                        <iais:value width="7" display="true">
                            <iais:code code="${previous.idType}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="ID No."/>
                        <iais:value width="7" display="true">
                            <c:out value="${previous.idNumber}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date of Birth"/>
                        <iais:value width="7" display="true">
                            <c:out value="${previous.birthDate}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Nationality"/>
                        <iais:value width="7" display="true">
                            <iais:code code="${previous.nationality}" />
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>