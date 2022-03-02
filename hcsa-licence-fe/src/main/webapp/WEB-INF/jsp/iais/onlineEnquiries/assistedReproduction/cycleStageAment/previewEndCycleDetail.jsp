<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                End Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="4" cssClass="col-md-4"  value="" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <select id="oldDsSelect" name="oldDsSelect">
                            <c:forEach items="${arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}" var="oldDs" varStatus="index">
                                <option   <c:if test="${oldDs.dataSubmissionDto.id == arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}">checked</c:if> value ="${oldDs.dataSubmissionDto.id}">V ${oldDs.dataSubmissionDto.version}</option>
                            </c:forEach>
                        </select>
                    </iais:value>
                </iais:row>
                <c:set var="endCycleStageDto" value="${arSuperDataSubmissionDto.endCycleStageDto}" />
                <c:set var="endCycleStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.endCycleStageDto}" />
                <iais:row>
                    <iais:field width="4" value="Is Current Cycle Abandoned?"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${endCycleStageDto.cycleAbandoned == true }">Yes</c:if>
                        <c:if test="${endCycleStageDto.cycleAbandoned == false }">No, Cycle has ended</c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${endCycleStageDtoVersion.cycleAbandoned == true }">Yes</c:if>
                        <c:if test="${endCycleStageDtoVersion.cycleAbandoned == false }">No, Cycle has ended</c:if>
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

