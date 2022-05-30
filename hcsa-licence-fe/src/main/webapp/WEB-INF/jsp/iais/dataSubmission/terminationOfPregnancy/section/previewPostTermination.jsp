<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}" />
<c:if test="${preTerminationDto.secCounsellingResult !='TOPSP001' && preTerminationDto.secCounsellingResult !='TOPSP002'}">
<c:set var="headingSign" value="${postTermination == 'false' ? 'incompleted' : 'completed'}"/>
<div class="panel panel-default">
    <div class="panel-heading <c:if test="${DeclarationsCheckBox != 'hide'}">${headingSign}</c:if>">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#postDetails">
                Post-Termination Of Pregnancy Counselling
            </a>
        </h4>
    </div>
    <div id="postDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
                <c:set var="postTerminationDto" value="${terminationOfPregnancyDto.postTerminationDto}" />
                <iais:row>
                    <iais:field width="6" value="Whether Given Counselling"/>
                    <iais:value width="6" display="true">
                        <c:if test="${postTerminationDto.givenPostCounselling == true }">
                            Yes
                        </c:if>
                        <c:if test="${postTerminationDto.givenPostCounselling == false }">
                            No
                        </c:if>
                    </iais:value>
                </iais:row>
                <div <c:if test="${postTerminationDto.givenPostCounselling !=true}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Result of Counselling"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${postTerminationDto.counsellingRslt}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${postTerminationDto.counsellingRslt != 'TOPCR007' || postTerminationDto.givenPostCounselling !=true}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Post-Counselling Result - Others"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${postTerminationDto.otherCounsellingRslt}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${postTerminationDto.givenPostCounselling !=false}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Reason for No Counselling"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${postTerminationDto.ifCounsellingNotGiven}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <div <c:if test="${postTerminationDto.givenPostCounselling !=true}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Counsellor ID Type"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${postTerminationDto.counsellorIdType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Counsellor ID No."/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${postTerminationDto.counsellorIdNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Name of Counsellor"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${postTerminationDto.counsellorName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date of Counselling"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${postTerminationDto.counsellingDate}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Place Where Counselling Was Done"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:optionText value="${postTerminationDto.counsellingPlace}" selectionOptions="TopPlace"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Doctor's Professional Reign / MCR No."/>
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${postTerminationDto.counsellingReignNo}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
</c:if>