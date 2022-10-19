<c:forEach var="weeklyDto" items="${businessDto.weeklyDtoList}" varStatus="stat">
    <c:set var="oldweeklyDto" value="${oldBusiness.weeklyDtoList[stat.index]}"/>
    <table  class="col-xs-12" aria-describedby="">
        <thead style="display: none">
        <tr>
            <th scope="col"></th>
        </tr>
        </thead>
        <tr>
            <td class="col-xs-6">
                <div class="col-xs-12 row">
                    <div class="newVal" attr="${weeklyDto.selectValList}">
                        <p>
                            <c:forEach var="weeklyName" items="${weeklyDto.selectValList}" varStatus="weeklyStat">
                                <iais:code code="${weeklyName}"/><c:if test="${!weeklyStat.last}">,</c:if>
                            </c:forEach>
                        </p>
                    </div>
                </div>
                <div class="col-xs-12 row">
                    <div class="oldVal" style="display: none" attr="${oldweeklyDto.selectValList}">
                        <p>
                            <c:forEach var="weeklyName" items="${oldweeklyDto.selectValList}" varStatus="weeklyStat">
                                <iais:code code="${weeklyName}"/><c:if test="${!weeklyStat.last}">,</c:if>
                            </c:forEach>
                        </p>
                    </div>
                </div>
            </td>
            <td>
                <div class="col-xs-4">
                    <div class="col-xs-12 row">
                        <div class="newVal" attr="${weeklyDto.startFromHH}:${weeklyDto.startFromMM}">
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
                        </div>
                    </div>
                    <div class="col-xs-12 row">
                        <div class="oldVal" style="display: none" attr="${oldweeklyDto.startFromHH}:${oldweeklyDto.startFromMM}">
                            <c:if test="${!oldweeklyDto.selectAllDay}">
                                <c:if test="${oldweeklyDto.startFromHH != null}">
                                    <c:choose>
                                        <c:when test="${oldweeklyDto.startFromHH.length()>1}">
                                            ${oldweeklyDto.startFromHH}
                                        </c:when>
                                        <c:otherwise>
                                            0${oldweeklyDto.startFromHH}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${!oldweeklyDto.selectAllDay && (oldweeklyDto.startFromHH != null || oldweeklyDto.startFromMM != null)}">
                                    :
                                </c:if>
                                <c:if test="${oldweeklyDto.startFromMM != null}">
                                    <c:choose>
                                        <c:when test="${oldweeklyDto.startFromMM.length()>1}">
                                            ${oldweeklyDto.startFromMM}
                                        </c:when>
                                        <c:otherwise>
                                            0${oldweeklyDto.startFromMM}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:if>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="col-xs-12 row">
                        <div class="newVal" attr="${weeklyDto.endToHH}:${weeklyDto.endToMM}">
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
                        </div>
                    </div>
                    <div class="col-xs-12 row">
                        <div class="oldVal" style="display: none" attr="${oldweeklyDto.endToHH}:${oldweeklyDto.endToMM}">
                            <c:if test="${!oldweeklyDto.selectAllDay}">
                                <c:if test="${oldweeklyDto.endToHH != null}">
                                    <c:choose>
                                        <c:when test="${oldweeklyDto.endToHH.length()>1}">
                                            ${oldweeklyDto.endToHH}
                                        </c:when>
                                        <c:otherwise>
                                            0${oldweeklyDto.endToHH}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${!oldweeklyDto.selectAllDay && (oldweeklyDto.endToHH != null || oldweeklyDto.endToMM != null)}">
                                    :
                                </c:if>
                                <c:if test="${oldweeklyDto.endToMM != null}">
                                    <c:choose>
                                        <c:when test="${oldweeklyDto.endToMM.length()>1}">
                                            ${oldweeklyDto.endToMM}
                                        </c:when>
                                        <c:otherwise>
                                            0${oldweeklyDto.endToMM}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:if>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="col-xs-12 row">
                        <c:if test="${weeklyDto.selectAllDay}">
                            <div class="newVal" attr="${weeklyDto.selectAllDay}">
                                <div class="form-check active">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <div class="col-xs-12 row">
                        <c:if test="${oldweeklyDto.selectAllDay}">
                            <div class="oldVal" style="display: none" attr="${oldweeklyDto.selectAllDay}">
                                <div class="form-check active">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </td>
        </tr>
    </table>
</c:forEach>