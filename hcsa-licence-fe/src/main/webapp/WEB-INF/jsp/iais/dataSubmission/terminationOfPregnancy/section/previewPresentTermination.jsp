<div class="panel panel-default">
    <div class="panel-heading ${canEdit}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#terminationDetails">
                Present Termination of Pregnancy
            </a>
        </h4>
    </div>
    <div id="terminationDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}"/>
                <c:set var="terminationDto" value="${terminationOfPregnancyDto.terminationDto}"/>
                <iais:row>
                    <iais:field width="5" value="Type of Termination of Pregnancy"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${terminationDto.topType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Type of Drug"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${terminationDto.drugType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Termination of Pregnancy"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${terminationDto.topDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Place of Termination of Pregnancy"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${terminationDto.topPlace}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Doctor Professional Regn No."/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${terminationDto.doctorRegnNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Name of Doctor who performed the Termination of Pregnancy"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${terminationDto.doctorName}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>