<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#preDetails">
                Pre-Termination of Pregnancy Counselling
            </a>
        </h4>
    </div>
    <div id="preDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
                <c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}" />
                <iais:row>
                    <iais:field width="6" value="Counselling Given"/>
                    <iais:value width="6" display="true">
                        <c:if test="${preTerminationDto.counsellingGiven == true }">
                            Yes
                        </c:if>
                        <c:if test="${preTerminationDto.counsellingGiven == false }">
                            No
                        </c:if>
                    </iais:value>
                </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Reason for No Counselling"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.noCounsReason}"/>
                        </iais:value>
                    </iais:row>
                <iais:row>
                    <iais:field width="6" value="Given Counselling On Mid-Trimester Pregnancy Termination"/>
                    <iais:value width="6" display="true">
                        <c:if test="${preTerminationDto.counsellingGivenOnMin == true }">
                            Yes
                        </c:if>
                        <c:if test="${preTerminationDto.counsellingGivenOnMin == false }">
                            No
                        </c:if>
                    </iais:value>
                </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Patient Sign the Acknowledgement For Counselling On Mid-Trimester Pregnancy Termination"/>
                        <iais:value width="6" display="true">
                            <c:if test="${preTerminationDto.patientSign == true }">
                                Yes
                            </c:if>
                            <c:if test="${preTerminationDto.patientSign == false }">
                                No
                            </c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Pre-Termination Counsellor ID Type" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${preTerminationDto.counsellorIdType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Pre-Termination Counsellor ID No."/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.counsellorIdNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Pre-Termination Counsellor Name"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.counsellorName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Pre-Termination Counselling Date"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.counsellingDate}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Pre-Counselling Place"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${preTerminationDto.counsellingPlace}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Reason why pre-Counselling was Not Conducted at HPB Counselling Centre"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.preCounsNoCondReason}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Pre-Counselling Result" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${preTerminationDto.counsellingResult}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Did Patient Make Appointment for Additional Pre-Counselling Sessions?"/>
                        <iais:value width="6" display="true">
                            <iais:code code="${preTerminationDto.patientAppointment}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date of Second or Final Pre-Counselling" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.secCounsellingDate}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Second or Final Pre-Counselling result" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${preTerminationDto.secCounsellingResult}"/>
                        </iais:value>
                    </iais:row>
            </div>
        </div>
    </div>
</div>


