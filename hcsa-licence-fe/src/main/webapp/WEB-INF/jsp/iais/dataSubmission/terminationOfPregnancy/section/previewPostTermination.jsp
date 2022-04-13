<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}" />
<c:if test="${preTerminationDto.secCounsellingResult !='TOPSP001' && preTerminationDto.secCounsellingResult !='TOPSP002'}">
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#postDetails">
                Post-Termination of Pregnancy Counselling
            </a>
        </h4>
    </div>
    <div id="postDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
                <c:set var="postTerminationDto" value="${terminationOfPregnancyDto.postTerminationDto}" />
                <iais:row>
                    <iais:field width="6" value="Whether given Post-counselling"/>
                    <iais:value width="6" display="true">
                        <c:if test="${postTerminationDto.givenPostCounselling == true }">
                            Yes
                        </c:if>
                        <c:if test="${postTerminationDto.givenPostCounselling == false }">
                            No
                        </c:if>
                    </iais:value>
                </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Post-Counselling Result"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${postTerminationDto.counsellingRslt}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Post-Counselling Result - Others"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${postTerminationDto.otherCounsellingRslt}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="If post-counselling was not given"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${postTerminationDto.ifCounsellingNotGiven}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Post-Termination Counsellor ID Type"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${postTerminationDto.counsellorIdType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Post-Termination Counsellor ID No."/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${postTerminationDto.counsellorIdNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Doctor Professional Regn No."/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${postTerminationDto.counsellorName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Post-Counselling Date"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${postTerminationDto.counsellingDate}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Place of Post-Counselling"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${postTerminationDto.counsellingPlace}"/>
                        </iais:value>
                    </iais:row>
            </div>
        </div>
    </div>
</div>
</c:if>