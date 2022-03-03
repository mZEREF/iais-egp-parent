<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#patientDetails">
                Patient Details
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="patientInformationDto" value="${topSuperDataSubmissionDto.patientInformationDto}" />
                <iais:row>
                    <iais:field width="5" value="Name Of Patient"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${patientInformationDto.patientName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID No." />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${patientInformationDto.idType}"/>/<c:out value="${patientInformationDto.idNumber}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Birth"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${patientInformationDto.birthData}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Nationality" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${patientInformationDto.nationality}"/>
                    </iais:value>
                </iais:row>
                <div <c:if test="${empty patientInformationDto.commResidenceInSgDate}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Date Commenced Residence In Singapore"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${patientInformationDto.commResidenceInSgDate}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${empty patientInformationDto.residenceStatus}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Residence Status"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${patientInformationDto.residenceStatus}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Ethnic Group" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${patientInformationDto.ethnicGroup}"/>
                    </iais:value>
                </iais:row>
                <div <c:if test="${patientInformationDto.ethnicGroup!='ETHG005'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Ethnic Group"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${patientInformationDto.otherEthnicGroup}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Marital Status"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${patientInformationDto.maritalStatus}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Education Level" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${patientInformationDto.educationLevel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Living Children"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${patientInformationDto.livingChildrenNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Activity Status" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${patientInformationDto.activityStatus}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Occupation" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${patientInformationDto.occupation}"/>
                    </iais:value>
                </iais:row>
                <div <c:if test="${patientInformationDto.occupation!='TOPOCC014'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Occupation"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${patientInformationDto.otherOccupation}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="gender"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${patientInformationDto.gender}"/>
                    </iais:value>
                </iais:row>
                <div <c:if test="${empty patientInformationDto.livingChildrenGenders}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Gender of Living Children (By Order)"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <%--<c:forEach items="${patientInformationDto.livingChildrenGenders}" var="livingChildrenGenders">--%>
                            <c:out value="${patientInformationDto.livingChildrenGenders}"/>
                            <%--</c:forEach>--%>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="cleanpage">
    <input type="hidden" name="cleanPage" value="${topSuperDataSubmissionDto.patientInformationDto}">
</div>