<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}"/>
<c:set var="patient" value="${patientInfoDto.patient}"/>
<c:set var="previous" value="${patientInfoDto.previous}"/>
<c:set var="husband" value="${patientInfoDto.husband}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" href="#cycleDetails" data-toggle="collapse">
                Details of Patient
            </a>
        </h4>
    </div>

    <div id="cycleDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="5" value="Name (as per NRIC/FIN/Passport)"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${patient.name}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="NRIC/FIN number Type" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${patient.idType}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="ID No." mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${patient.idType}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>