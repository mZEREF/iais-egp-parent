<c:choose>
    <c:when test="${businessDto.phDtoList.size()>0}">
        <c:set var="phSize" value="${businessDto.phDtoList.size()-1}"/>
    </c:when>
    <c:otherwise>
        <c:set var="phSize" value="0"/>
    </c:otherwise>
</c:choose>
<c:forEach begin="0" end="${phSize}" step="1" varStatus="stat">
    <c:set var="phDto" value="${businessDto.phDtoList[stat.index]}"/>
    <c:if test="${stat.first}">
        <div class="row">
            <div class="col-md-12 hidden-xs">
                <p class="form-check-label" aria-label="premise-1-cytology"><strong>Public Holiday</strong></p>
            </div>
        </div>
    </c:if>

    <div class="amend-preview-info form-horizontal min-row">
        <iais:row>
            <div class="col-xs-12 visible-xs">
                <label class="control-label"><strong>Public Holiday</strong></label>
            </div>
            <div class="col-md-4 col-xs-4">
                <p class="form-check-label" aria-label="premise-1-cytology">
                <span>
                    <c:forEach var="weeklyName" items="${phDto.selectValList}" varStatus="weeklyStat">
                        <iais:code code="${weeklyName}"/><c:if test="${!weeklyStat.last}">,</c:if>
                    </c:forEach>
                </span>
                </p>
            </div>
            <div class="col-md-8 col-xs-8">
                <c:if test="${!phDto.selectAllDay}">
                    <div class="col-xs-12 visible-xs time-padding-left">
                        <label class="control-label"><strong>Start</strong></label>
                    </div>
                </c:if>
                <div class="col-md-4 col-xs-4 time-padding-left" style="padding-left: 0;">
                    <p class="form-check-label" aria-label="premise-1-cytology">
                    <span>
                        <c:if test="${!phDto.selectAllDay}">
                            <c:if test="${phDto.startFromHH != null}">
                                <c:choose>
                                    <c:when test="${phDto.startFromHH.length()>1}">
                                        ${phDto.startFromHH}
                                    </c:when>
                                    <c:otherwise>
                                        0${phDto.startFromHH}
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                            <c:if test="${phDto.startFromHH != null || phDto.startFromMM != null}">
                                :
                            </c:if>
                            <c:if test="${phDto.startFromMM != null}">
                                <c:choose>
                                    <c:when test="${phDto.startFromMM.length()>1}">
                                        ${phDto.startFromMM}
                                    </c:when>
                                    <c:otherwise>
                                        0${phDto.startFromMM}
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:if>
                    </span>
                    </p>
                </div>
                <c:if test="${!phDto.selectAllDay}">
                    <div class="col-xs-12 visible-xs time-padding-left">
                        <label class="control-label"><strong>End</strong></label>
                    </div>
                </c:if>
                <div class="col-md-4 col-xs-4 time-padding-left">
                    <p class="form-check-label" aria-label="premise-1-cytology">
                    <span>
                        <c:if test="${!phDto.selectAllDay}">
                            <c:if test="${phDto.endToHH != null}">
                                <c:choose>
                                    <c:when test="${phDto.endToHH.length()>1}">
                                        ${phDto.endToHH}
                                    </c:when>
                                    <c:otherwise>
                                        0${phDto.endToHH}
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                            <c:if test="${phDto.endToHH != null || phDto.endToMM != null}">
                                :
                            </c:if>
                            <c:if test="${phDto.endToMM != null}">
                                <c:choose>
                                    <c:when test="${phDto.endToMM.length()>1}">
                                        ${phDto.endToMM}
                                    </c:when>
                                    <c:otherwise>
                                        0${phDto.endToMM}
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:if>
                    </span>
                    </p>
                </div>
                <c:if test="${phDto.selectAllDay}">
                    <div class="col-xs-12 visible-xs time-padding-left">
                        <label class="control-label"><strong>24 Hours</strong></label>
                    </div>
                </c:if>
                <div class="col-md-3 col-xs-3 time-padding-left">
                    <c:if test="${phDto.selectAllDay}">
                        <div class="form-check active" style="padding-left: 0;">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                        </div>
                    </c:if>
                </div>
            </div>
        </iais:row>
    </div>
</c:forEach>