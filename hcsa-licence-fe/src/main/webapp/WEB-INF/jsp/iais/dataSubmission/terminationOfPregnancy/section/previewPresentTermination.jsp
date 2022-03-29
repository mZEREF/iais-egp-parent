<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
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
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${terminationDto.topType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Type of Surgical Procedure"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${terminationDto.spType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Type of Surgical Procedure - others"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${terminationDto.otherSpType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Type of Anaesthesia"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${terminationDto.anType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Other Type of Anaesthesia"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${terminationDto.otherAnType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Type of Drug"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${terminationDto.drugType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Type of Drug (Others)"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${terminationDto.otherDrugType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Result of Termination of Pregnancy – Any Complications"/>
                    <iais:value width="6" display="true">
                        <c:if test="${terminationDto.ariseOperationComplication == true }">
                            Yes
                        </c:if>
                        <c:if test="${terminationDto.ariseOperationComplication == false }">
                            No
                        </c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Complications Arising From Operation"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${terminationDto.complicationForOperRslt}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Termination of Pregnancy"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${terminationDto.topDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Place of Termination of Pregnancy"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${terminationDto.topPlace}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Doctor Professional Regn No."/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${terminationDto.doctorRegnNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Name of Doctor who performed the Termination of Pregnancy"/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${terminationDto.doctorName}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>