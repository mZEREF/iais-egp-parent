<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}" />
<c:if test="${preTerminationDto.secCounsellingResult !='TOPSP001' && preTerminationDto.secCounsellingResult !='TOPSP003' && preTerminationDto.counsellingResult!='TOPPCR003'}">
<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#terminationDetails">
                Termination Of Pregnancy
            </a>
        </h4>
    </div>
    <div id="terminationDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}"/>
                <c:set var="doctorInformationDto" value="${topSuperDataSubmissionDto.doctorInformationDto}" />
                <c:set var="terminationDto" value="${terminationOfPregnancyDto.terminationDto}"/>
                <iais:row>
                    <iais:field width="5" value="Type of Termination of Pregnancy"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${terminationDto.topType}"/>
                    </iais:value>
                </iais:row>
                <div <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Type of Surgical Procedure"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${terminationDto.spType}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${terminationDto.spType!='TOPTSP003' || (terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003')}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Type of Surgical Procedure"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${terminationDto.otherSpType}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Type of Anaesthesia"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${terminationDto.anType}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${terminationDto.anType!='TOPTA004' || (terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003')}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Type of Anaesthesia"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${terminationDto.otherAnType}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Type of Drug"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${terminationDto.drugType}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${terminationDto.drugType!='TOPTOD005' || (terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002')}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Type of Drug"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${terminationDto.otherDrugType}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="Result of Termination of Pregnancy - Any Complications"/>
                    <iais:value width="6" display="true">
                        <c:if test="${terminationDto.complicationForOperRslt == true }">
                            Yes
                        </c:if>
                        <c:if test="${terminationDto.complicationForOperRslt == false }">
                            No
                        </c:if>
                    </iais:value>
                </iais:row>
                <div <c:if test="${terminationDto.complicationForOperRslt != true}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Complications Arising From Operation"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${terminationDto.ariseOperationComplication}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <c:if test="${topLateSubmit}">
                        <c:set var="toolMsgLate"><iais:message key="Late" paramKeys="1" paramValues="counsellor"/></c:set>
                    </c:if>
                    <iais:field width="5" value="Date of Termination of Pregnancy" info="${toolMsgLate}" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${terminationDto.topDate}"/>
                    </iais:value>
                </iais:row>
                <div <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Is Surgical Termination of Pregnancy Performed in Own Premises?"/>
                        <iais:value width="6" display="true">
                            <c:if test="${terminationDto.performedOwn == true }">
                                Yes
                            </c:if>
                            <c:if test="${terminationDto.performedOwn == false }">
                                No
                            </c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${(terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003') || terminationDto.performedOwn==null}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Place of Surgical Termination of Pregnancy" info="${MessageUtil.getMessageDesc('DS_MSG029')}"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:if test="${terminationDto.performedOwn == true}">${'unknown'}</c:if>
                            <c:if test="${terminationDto.performedOwn == false}"><iais:optionText value="${terminationDto.topPlace}" selectionOptions="TopPlace"/></c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Is Drug for Termination of Pregnancy Prescribed in Own Premises?"/>
                        <iais:value width="6" display="true">
                            <c:if test="${terminationDto.pregnancyOwn == true }">
                                Yes
                            </c:if>
                            <c:if test="${terminationDto.pregnancyOwn == false }">
                                No
                            </c:if>
                        </iais:value>
                    </iais:row>
                </div>

                <div <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Place where Drug for Termination of Pregnancy was Prescribed"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:if test="${terminationDto.pregnancyOwn == true}">${'unknown'}</c:if>
                            <c:if test="${terminationDto.pregnancyOwn == false}"><iais:optionText value="${terminationDto.prescribeTopPlace}" selectionOptions="TopPlace"/></c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Is Drug for Termination of Pregnancy Taken in Own Premises?"/>
                        <iais:value width="6" display="true">
                            <c:if test="${terminationDto.takenOwn == true }">
                                Yes
                            </c:if>
                            <c:if test="${terminationDto.takenOwn == false }">
                                No
                            </c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Place where Drug for Termination of Pregnancy was Taken"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:if test="${terminationDto.takenOwn == true}">${'unknown'}</c:if>
                            <c:if test="${terminationDto.takenOwn == false}"><iais:optionText value="${terminationDto.topDrugPlace}" selectionOptions="TopDrugPlace"/></c:if>
                        </iais:value>
                    </iais:row>
                </div>

                <div <c:if test="${terminationDto.topDrugPlace!='AR_SC_001'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Places where Drug for Termination of Pregnancy is used"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${terminationDto.otherTopDrugPlace}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:value width="7" cssClass="col-md-7">
                        <strong>Doctor who Performed the Termination of Pregnancy</strong>
                    </iais:value>
                </iais:row>
                <c:if test="${terminationDto.topDoctorInformations eq 'false'}">
                    <c:if test="${terminationDto.doctorInformationPE eq 'true'}">
                        <c:set var="toolMsg"><iais:message key="GENERAL_ERR0063" paramKeys="1" paramValues="counsellor"/></c:set>
                    </c:if>
                    <c:if test="${terminationDto.doctorInformationPE eq 'false'}">
                        <c:set var="toolMsg"><iais:message key="GENERAL_ERR0064" paramKeys="1" paramValues="counsellor"/></c:set>
                    </c:if>
                </c:if>
                <c:if test="${terminationDto.topDoctorInformations eq 'true'}">
                    <c:set var="toolMsg"><iais:message key="GENERAL_ERR0065" paramKeys="1" paramValues="counsellor" escape="false"/></c:set>
                </c:if>
                <iais:row>
                    <iais:field width="5" value="Doctor's Professional Regn / MCR No." info="${toolMsg}"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${terminationDto.doctorRegnNo}"/>
                    </iais:value>
                </iais:row>
                <div <c:if test="${terminationDto.topDoctorInformations eq 'true'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Name of Doctor" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${terminationDto.doctorName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Specialty"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${terminationDto.specialty}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Sub-Specialty"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" >
                            <c:out value="${terminationDto.subSpecialty}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Qualification"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" >
                            <c:out value="${terminationDto.qualification}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${terminationDto.topDoctorInformations eq 'false' || terminationDto.topDoctorInformations eq null}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Name of Doctor" />
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${doctorInformationDto.name}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Specialty"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${doctorInformationDto.speciality}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Sub-Specialty"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" >
                            <c:out value="${doctorInformationDto.subSpeciality}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Qualification"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" >
                            <c:out value="${doctorInformationDto.qualification}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row >
                    <iais:field width="5" value="Other Qualification"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" >
                        <c:out value="${terminationDto.otherQualification}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
</c:if>