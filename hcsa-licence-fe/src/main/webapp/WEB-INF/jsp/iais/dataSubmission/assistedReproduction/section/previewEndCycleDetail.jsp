<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#efoDetails">
                Complete/Abandoned Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <c:set var="endCycleStageDto" value="${arSuperDataSubmissionDto.endCycleStageDto}" />
                <iais:row>
                    <iais:field width="6" value="Is Current Cycle Completed?"/>
                    <iais:value width="6" display="true">
                        <c:if test="${endCycleStageDto.cycleAbandoned == true }">No</c:if>
                        <c:if test="${endCycleStageDto.cycleAbandoned == false }">Yes, Cycle has ended</c:if>
                    </iais:value>
                </iais:row>
                <div <c:if test="${endCycleStageDto.cycleAbandoned !=true}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Reason for Abandonment" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7"  display="true">
                            <iais:code code="${endCycleStageDto.abandonReason}"/>
                        </iais:value>
                    </iais:row>
                    <div id="otherAbandonReason" <c:if test="${endCycleStageDto.abandonReason!='ENDRA005'}">style="display: none"</c:if> >
                        <iais:row>
                            <iais:field width="5" value="Reason for Abandonment (Others)" />
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${endCycleStageDto.otherAbandonReason}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>
                <span id="error_inventoryNoZero" name="iaisErrorMsg" class="error-msg col-md-12"
                      style="padding: 0px;"></span>
                <br><br>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>

