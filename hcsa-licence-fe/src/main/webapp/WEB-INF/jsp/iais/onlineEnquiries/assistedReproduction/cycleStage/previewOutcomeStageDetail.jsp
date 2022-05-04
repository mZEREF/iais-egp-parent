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
                <c:set var="outcomeStageDto" value="${arSuperDataSubmissionDto.outcomeStageDto}" />
                <%@include file="comPart.jsp" %>
                <iais:row>
                    <iais:field width="5" value="Is Clinical Pregnancy Detected?"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${outcomeStageDto.pregnancyDetected == true }">Yes</c:if>
                        <c:if test="${outcomeStageDto.pregnancyDetected == false }">No</c:if>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>
