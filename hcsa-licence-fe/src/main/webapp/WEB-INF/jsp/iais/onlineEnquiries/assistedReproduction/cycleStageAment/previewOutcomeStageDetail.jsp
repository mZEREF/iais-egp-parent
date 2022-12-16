<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#">
                Outcome of IUI Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="comPart.jsp" %>
                <c:set var="outcomeStageDto" value="${arSuperDataSubmissionDto.outcomeStageDto}" />
                <c:set var="outcomeStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.outcomeStageDto}" />
                <iais:row>
                    <iais:field width="4" value="Is Clinical Pregnancy Detected?"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${outcomeStageDto.pregnancyDetected == 'Y' }">Yes</c:if>
                        <c:if test="${outcomeStageDto.pregnancyDetected == 'N' }">No</c:if>
                        <c:if test="${outcomeStageDto.pregnancyDetected == 'U' }">Unknown</c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${outcomeStageDtoVersion.pregnancyDetected == 'Y' }">Yes</c:if>
                        <c:if test="${outcomeStageDtoVersion.pregnancyDetected == 'N' }">No</c:if>
                        <c:if test="${outcomeStageDtoVersion.pregnancyDetected == 'U' }">Unknown</c:if>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>
