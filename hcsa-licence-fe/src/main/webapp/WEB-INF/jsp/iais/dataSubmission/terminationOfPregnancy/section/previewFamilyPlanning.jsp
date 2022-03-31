<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#familyDetails">
                Family Planning
            </a>
        </h4>
    </div>
    <div id="familyDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
                <c:set var="patientInformationDto" value="${topSuperDataSubmissionDto.patientInformationDto}"/>
                <c:set var="familyPlanDto" value="${terminationOfPregnancyDto.familyPlanDto}" />
                <iais:row>
                    <iais:field width="5" value="Contraceptive History"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${familyPlanDto.contraHistory}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Most Recent Contraceptive Methods Used" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${familyPlanDto.mostRecentContraMethod}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Previous Termination of Pregnancy"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${familyPlanDto.previousTopNumber}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="First Day of Last Menstrual Period"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${familyPlanDto.firstDayOfLastMenstPer}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient Age(Years)"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="age">
                        ${patientInformationDto.patientAge}
                    </iais:value>
                </iais:row>
                <div <c:if test="${empty familyPlanDto.gestAgeBaseOnUltrWeek}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Gestation Age based on Ultrasound(Weeks)"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${familyPlanDto.gestAgeBaseOnUltrWeek}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${empty familyPlanDto.gestAgeBaseOnUltrDay}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Gestation Age based on Ultrasound(Days)"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${familyPlanDto.gestAgeBaseOnUltrDay}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${empty familyPlanDto.gestAgeBaseNotOnUltrWeek}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Gestation Age not based on Ultrasound(Weeks)"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${familyPlanDto.gestAgeBaseNotOnUltrWeek}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${empty familyPlanDto.gestAgeBaseNotOnUltrDay}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Gestation Age not based on Ultrasound(Days)"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${familyPlanDto.gestAgeBaseNotOnUltrDay}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${empty familyPlanDto.abortChdMoreWksGender}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Gender of the Aborted Child if Gestation Age is 15 weeks and above" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${familyPlanDto.abortChdMoreWksGender}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${empty familyPlanDto.mainTopReason}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Main Reason for Request to Terminate Pregnancy" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${familyPlanDto.mainTopReason}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${familyPlanDto.mainTopReason !='TOPRTP008'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Main Reason for Termination of Pregnancy"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${familyPlanDto.otherMainTopReason}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Indicate the Maternal High Risk condition(s) that led to the Request to Terminate Pregnancy"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${familyPlanDto.topRiskCondition}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Indicate the Medical Condition(s) that led to the Request to Terminate Pregnancy”"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${familyPlanDto.topMedCondition}"/>
                    </iais:value>
                </iais:row>
                <div <c:if test="${empty familyPlanDto.subRopReason}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Sub Reason for Request to Terminate Pregnancy" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${familyPlanDto.subRopReason}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${familyPlanDto.subRopReason !='TOPSCTP009'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Sub Reason for Request to Terminate Pregnancy"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${familyPlanDto.otherSubTopReason}"/>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>
