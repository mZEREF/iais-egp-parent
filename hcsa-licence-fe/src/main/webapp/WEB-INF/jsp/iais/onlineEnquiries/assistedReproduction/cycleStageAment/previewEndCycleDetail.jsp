<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#">
                Completed/Abandoned Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="comPart.jsp" %>
                <c:set var="endCycleStageDto" value="${arSuperDataSubmissionDto.endCycleStageDto}" />
                <c:set var="endCycleStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.endCycleStageDto}" />
                <iais:row>
                    <iais:field width="4" value="Is Current Cycle Completed?"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${endCycleStageDto.cycleAbandoned == true }">No</c:if>
                        <c:if test="${endCycleStageDto.cycleAbandoned == false }">Yes, Cycle has ended</c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${endCycleStageDtoVersion.cycleAbandoned == true }">No</c:if>
                        <c:if test="${endCycleStageDtoVersion.cycleAbandoned == false }">Yes, Cycle has ended</c:if>
                    </iais:value>
                </iais:row>
                <div <c:if test="${endCycleStageDto.cycleAbandoned !=true && endCycleStageDtoVersion.cycleAbandoned !=true}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Reason for Abandonment" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4"  display="true">
                            <iais:code code="${endCycleStageDto.abandonReason}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4"  display="true">
                            <iais:code code="${endCycleStageDtoVersion.abandonReason}"/>
                        </iais:value>
                    </iais:row>
                    <div id="otherAbandonReason" <c:if test="${endCycleStageDto.abandonReason!='ENDRA005' && endCycleStageDtoVersion.abandonReason!='ENDRA005'}">style="display: none"</c:if> >
                        <iais:row>
                            <iais:field width="4" value="Reason for Abandonment (Others)" />
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${endCycleStageDto.otherAbandonReason}"/>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${endCycleStageDtoVersion.otherAbandonReason}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

