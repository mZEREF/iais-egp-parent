<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="husband" value="${patientInfoDto.husband}" />
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#husbanDetails">
                Details of Husband
            </a>
        </h4>
    </div>
    <div id="husbanDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <c:set var="person" value="${husband}" />
                <%@include file="previewPersonSection.jsp" %>
                <%--<iais:row>
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
                    <iais:field width="5" value="Date of Birth"/>
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
                        <iais:code code="${husband.ethnicGroup}" />
                    </iais:value>
                </iais:row>
                <div class="form-group"  style="<c:if test="${husband.ethnicGroup ne 'ETHG005'}">display:none</c:if>">
                    <iais:field width="5" value="Ethnic Group (Others)"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${husband.ethnicGroupOther}" />
                    </iais:value>
                </div>--%>
            </div>
        </div>
    </div>
</div>