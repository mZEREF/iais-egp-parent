<div class="panel panel-default">
    <div class="panel-heading completed">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Details of Patient
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="5" value="Name (as per NRIC/Passport)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${husband.name}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID Type"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${husband.idType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${husband.idNumber}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Birth."/>
                    <iais:value width="7" display="true">
                        <c:out value="${husband.birthDate}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Nationality"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${husband.nationality}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Ethnic Group"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${patient.ethnicGroup}" />
                    </iais:value>
                </iais:row>
                <div class="form-group"  style="<c:if test="${husband.ethnicGroup ne 'ETHG005'}">'display:none'</c:if>">
                    <iais:field width="5" value="Ethnic Group (Others)"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${husband.ethnicGroupOther}" />
                    </iais:value>
                </div>
            </div>
        </div>
    </div>
</div>