<c:choose>
    <c:when test="${businessDto.weeklyDtoList.size()>0}">
        <c:set var="weeklySize" value="${businessDto.weeklyDtoList.size()-1}"/>
    </c:when>
    <c:otherwise>
        <c:set var="weeklySize" value="0"/>
    </c:otherwise>
</c:choose>
<c:forEach begin="0" end="${weeklySize}" step="1" varStatus="stat">
    <c:set var="weeklyDto" value="${businessDto.weeklyDtoList[stat.index]}"/>

    <div class="row">
        <div class="col-md-6 col-sm-6 col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span>
                    <c:forEach var="weeklyName" items="${weeklyDto.selectValList}"
                               varStatus="weeklyStat">
                        <iais:code code="${weeklyName}"/><c:if
                            test="${!weeklyStat.last}">,</c:if>
                    </c:forEach>
                </span>
            </p>
        </div>
        <div class="col-md-6 col-sm-6 col-xs-6">
            <div class="row">
                <div class="col-md-4 col-sm-4 col-xs-4">
                    <p class="form-check-label" aria-label="premise-1-cytology">
                        <span>
                            <c:if test="${!weeklyDto.selectAllDay}">
                                <c:if test="${weeklyDto.startFromHH != null}">
                                    <c:choose>
                                        <c:when test="${weeklyDto.startFromHH.length()>1}">
                                            ${weeklyDto.startFromHH}
                                        </c:when>
                                        <c:otherwise>
                                            0${weeklyDto.startFromHH}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${!weeklyDto.selectAllDay && (weeklyDto.startFromHH != null || weeklyDto.startFromMM != null)}">
                                    :
                                </c:if>
                                <c:if test="${weeklyDto.startFromMM != null}">
                                    <c:choose>
                                        <c:when test="${weeklyDto.startFromMM.length()>1}">
                                            ${weeklyDto.startFromMM}
                                        </c:when>
                                        <c:otherwise>
                                            0${weeklyDto.startFromMM}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:if>
                        </span>
                    </p>
                </div>
                <div class="col-md-4 col-sm-4 col-xs-4">
                    <p class="form-check-label" aria-label="premise-1-cytology">
                        <span>
                            <c:if test="${!weeklyDto.selectAllDay}">
                                <c:if test="${weeklyDto.endToHH != null}">
                                    <c:choose>
                                        <c:when test="${weeklyDto.endToHH.length()>1}">
                                            ${weeklyDto.endToHH}
                                        </c:when>
                                        <c:otherwise>
                                            0${weeklyDto.endToHH}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${!weeklyDto.selectAllDay && (weeklyDto.endToHH != null || weeklyDto.endToMM != null)}">
                                    :
                                </c:if>
                                <c:if test="${weeklyDto.endToMM != null}">
                                    <c:choose>
                                        <c:when test="${weeklyDto.endToMM.length()>1}">
                                            ${weeklyDto.endToMM}
                                        </c:when>
                                        <c:otherwise>
                                            0${weeklyDto.endToMM}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:if>
                        </span>
                    </p>
                </div>
                <div class="col-md-3 col-sm-3 col-xs-3">
                    <c:if test="${weeklyDto.selectAllDay}">
                        <div class="form-check active">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</c:forEach>