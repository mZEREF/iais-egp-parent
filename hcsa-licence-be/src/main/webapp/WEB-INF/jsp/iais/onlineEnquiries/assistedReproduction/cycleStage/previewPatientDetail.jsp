<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title" >
            <a  href="#viewArCycleStage" data-toggle="collapse" >
                Assisted Reproduction Cycle
            </a>
        </h4>
    </div>
    <div id="viewArCycleStage" class="panel-collapse collapse in" aria-expanded="true">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
                <c:set var="husband" value="${patientInfoDto.husband}"/>
                <c:set var="patient" value="${patientInfoDto.patient}" />
                <c:set var="previous" value="${patientInfoDto.previous}" />
                <%@include file="comPart.jsp" %>
                <iais:row>
                    <iais:field width="5" value="Patient's ID Type"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${patient.idType}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Patient's ID No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${patient.idNumber}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Patient's Name (as per NRIC/FIN/Passport Number)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${patient.name}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Patient's Date of Birth"/>
                    <iais:value width="7" display="true">
                        <c:out value="${patient.birthDate}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Patient's Nationality"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${patient.nationality}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Ethnic Group"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${patient.ethnicGroup}" />
                    </iais:value>
                </iais:row>


                <iais:row>
                    <iais:field width="5" value="Has patient registered before?"/>
                    <iais:value width="7" display="true">
                        <c:if test="${patient.previousIdentification}">Yes</c:if>
                        <c:if test="${patient.previousIdentification eq false}">No</c:if>
                    </iais:value>
                </iais:row>
                <c:if test="${patient.previousIdentification}">
                <iais:row>
                    <iais:field width="5" value="Previous Name (as per NRIC/FIN/Passport Number)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${previous.name}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Previous Date of Birth"/>
                    <iais:value width="7" display="true">
                        <c:out value="${previous.birthDate}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Previous Nationality"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${previous.nationality}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value=" Previous Ethnic Group"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${previous.ethnicGroup}" />
                    </iais:value>
                </iais:row>
                </c:if>



                <iais:row>
                    <iais:field width="5" value="Husband's ID Type"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${husband.idType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Husband's ID No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${husband.idNumber}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Husband's Name (as per NRIC/FIN/Passport Number)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${husband.name}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Husband's Date of Birth"/>
                    <iais:value width="7" display="true">
                        <c:out value="${husband.birthDate}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Husband's Nationality"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${husband.nationality}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Husband's Ethnic Group"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${husband.ethnicGroup}" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
