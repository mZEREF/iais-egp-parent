<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#">
                End Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="comPart.jsp" %>
                <c:set var="endCycleStageDto" value="${arSuperDataSubmissionDto.endCycleStageDto}" />
                <iais:row>
                    <iais:field width="5" value="Is Current Cycle Abandoned?"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${endCycleStageDto.cycleAbandoned == true }">Yes</c:if>
                        <c:if test="${endCycleStageDto.cycleAbandoned == false }">No, Cycle has ended</c:if>
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
            </div>
        </div>
    </div>
</div>

