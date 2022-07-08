<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#preDetails">
                Pre-Termination Of Pregnancy Counselling
            </a>
        </h4>
    </div>
    <div id="preDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
                <c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}"/>
                <c:set var="familyPlanDto" value="${terminationOfPregnancyDto.familyPlanDto}"/>
                <c:set var="patientInformationDto" value="${terminationOfPregnancyDto.patientInformationDto}"/>
                <c:if test="${preTerminationDto.counsellingGiven == false }">
                    <c:set var="beforeConsultation"><iais:message key="DS_MSG032" paramKeys="1" paramValues="counsellor"/></c:set>
                </c:if>
                <iais:row>
                    <iais:field width="6" value="Whether Given Counselling" info="${beforeConsultation}"/>
                    <iais:value width="6" display="true">
                        <c:if test="${preTerminationDto.counsellingGiven == true }">
                            Yes
                        </c:if>
                        <c:if test="${preTerminationDto.counsellingGiven == false }">
                            No
                        </c:if>
                    </iais:value>
                </iais:row>
                <div <c:if test="${preTerminationDto.counsellingGiven != false}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Reason for No Counselling"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.noCounsReason}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${familyPlanDto.gestAgeBaseOnUltrWeek<13 || familyPlanDto.gestAgeBaseOnUltrWeek>24}">style="display: none"</c:if> >
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
                </div>
                <div <c:if test="${preTerminationDto.counsellingGiven != true}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Counsellor ID Type" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${preTerminationDto.counsellorIdType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Counsellor ID No."/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.counsellorIdNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Name of Counsellor"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.counsellorName}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${preTerminationDto.counsellingGiven == false}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Doctor's Professional Regn / MCR No."/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.counsellingReignNo}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${preTerminationDto.counsellingGiven != true}">style="display: none"</c:if> >
                    <c:if test="${counsellingLateSubmit}">
                        <c:set var="toolMsg"><iais:message key="Late" paramKeys="1" paramValues="counsellor"/></c:set>
                    </c:if>
                    <c:if test="${topDates}">
                        <c:set var="toolMsgDS_MSG030"><iais:message key="DS_MSG030" paramKeys="1" paramValues="counsellor"/></c:set>
                    </c:if>
                    <iais:row>
                        <iais:field width="5" value="Date of Counselling" info="${toolMsgDS_MSG030}"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.counsellingDate}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Place Where Counselling Was Done"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">

                            <c:if test="${preTerminationDto.counsellingPlace != 'AR_SC_001'}">
                                ${preTerminationDto.counsellingPlace}
                            </c:if>
                            <c:if test="${preTerminationDto.counsellingPlace == 'AR_SC_001'}">
                                Health Promotion Board Counselling Centre
                            </c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Result of Counselling" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${preTerminationDto.counsellingResult}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${preTerminationDto.counsellingGiven != true || preTerminationDto.counsellingAge>=16 || patientInformationDto.maritalStatus =='TOPMS002' || preTerminationDto.counsellingPlace == 'AR_SC_001' || preTerminationDto.counsellingPlace ==null || preTerminationDto.counsellingAge==null}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Reason why pre-Counselling was Not Conducted at HPB Counselling Centre" info="${toolMsgNum}"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.preCounsNoCondReason}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${preTerminationDto.counsellingGiven != true || preTerminationDto.counsellingResult !='TOPPCR001'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Did Patient Make Appointment for Additional Pre-Counselling Sessions?"/>
                        <iais:value width="6" display="true">
                            <c:if test="${preTerminationDto.patientAppointment=='1'}">Yes</c:if>
                            <c:if test="${preTerminationDto.patientAppointment=='0'}">No</c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${preTerminationDto.patientAppointment!='1' || preTerminationDto.counsellingResult !='TOPPCR001'}">style="display: none"</c:if>>
                    <c:if test="${secondLateSubmit}">
                        <c:set var="toolMsgSecondLate"><iais:message key="Late" paramKeys="1" paramValues="counsellor"/></c:set>
                    </c:if>
                    <iais:row>
                        <iais:field width="5" value="Date of Second or Final Pre-Counselling" info="${toolMsgSecondLate}"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${preTerminationDto.secCounsellingDate}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Result of Second or Final Counselling" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${preTerminationDto.secCounsellingResult}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${preTerminationDto.counsellingGiven == false}">style="display: none"</c:if> >
                    <c:if test="${preTerminationDto.counsellingAge<16 && patientInformationDto.maritalStatus !='TOPMS002' && preTerminationDto.counsellingPlace != 'AR_SC_001'}">
                        <c:set var="toolMsgAge"><iais:message key="This patient is below 16 years old from the date of Termination of Pregnancy, not married and pre-counselling is not done at Health Promotion Board Counselling Centre or Student Health Centre" paramKeys="1" paramValues="counsellor" escape="false"/></c:set>
                    </c:if>
                    <iais:row>
                        <iais:field width="5" value="Patient Age (Years)" info="${toolMsgAge}"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="age">
                            ${preTerminationDto.counsellingAge}
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>