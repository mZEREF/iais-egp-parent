<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading completed ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                Outcome
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="outcomeStageDto" value="${arSuperDataSubmissionDto.outcomeStageDto}" />
                <iais:row>
                    <iais:field width="6" value="Is Clinical Pregnancy Detected?" mandatory="false"/>
                    <iais:value width="6" display="true">
                        <c:if test="${outcomeStageDto.pregnancyDetected == true }">
                            Yes</c:if>
                        <c:if test="${outcomeStageDto.pregnancyDetected == false }">
                            No</c:if>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
