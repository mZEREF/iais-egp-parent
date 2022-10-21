<c:forEach var="phDto" items="${businessDto.phDtoList}" varStatus="stat">
    <c:set var="oldphDto" value="${oldBusiness.phDtoList[stat.index]}"/>
    <table  class="col-xs-12" aria-describedby="">
        <thead style="display: none">
        <tr>
            <th scope="col"></th>
        </tr>
        </thead>
        <c:if test="${stat.first}">
            <tr>
                <div class="col-xs-12">
                    <label class="control-label">Public Holiday</label>
                </div>
            </tr>
        </c:if>
        <tr>
            <td class="col-xs-4">
                <div class="col-xs-12 row">
                    <div class="newVal" attr="${phDto.selectValList}">
                        <c:forEach var="phName" items="${phDto.selectValList}" varStatus="phStat">
                            <iais:code code="${phName}"/><c:if test="${!phStat.last}">,</c:if>
                        </c:forEach>
                    </div>
                </div>
                <div class="col-xs-12 row">
                    <div class="oldVal" style="display: none" attr="${oldphDto.selectValList}">
                        <c:forEach var="phName" items="${oldphDto.selectValList}" varStatus="phStat">
                            <iais:code code="${phName}"/><c:if test="${!phStat.last}">,</c:if>
                        </c:forEach>
                    </div>
                </div>
            </td>
            <td class="col-xs-8">
                <div class="col-xs-4">
                    <div class="col-xs-12 row">
                        <div class="newVal" attr="${phDto.startFromHH}:${phDto.startFromMM}">
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
                        </div>
                    </div>
                    <div class="col-xs-12 row">
                        <div class="oldVal" style="display: none" attr="${oldphDto.startFromHH}:${oldphDto.startFromMM}">
                            <c:if test="${!oldphDto.selectAllDay}">
                                <c:if test="${oldphDto.startFromHH != null}">
                                    <c:choose>
                                        <c:when test="${oldphDto.startFromHH.length()>1}">
                                            ${oldphDto.startFromHH}
                                        </c:when>
                                        <c:otherwise>
                                            0${oldphDto.startFromHH}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${!oldphDto.selectAllDay && (oldphDto.startFromHH != null || oldphDto.startFromMM != null)}">
                                    :
                                </c:if>
                                <c:if test="${oldphDto.startFromMM != null}">
                                    <c:choose>
                                        <c:when test="${oldphDto.startFromMM.length()>1}">
                                            ${oldphDto.startFromMM}
                                        </c:when>
                                        <c:otherwise>
                                            0${oldphDto.startFromMM}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:if>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="col-xs-12 row">
                        <div class="newVal" attr="${phDto.endToHH}:${phDto.endToMM}">
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
                        </div>
                    </div>
                    <div class="col-xs-12 row">
                        <div class="oldVal" style="display: none" attr="${oldphDto.endToHH}:${oldphDto.endToMM}">
                            <c:if test="${!oldphDto.selectAllDay}">
                                <c:if test="${oldphDto.endToHH != null}">
                                    <c:choose>
                                        <c:when test="${oldphDto.endToHH.length()>1}">
                                            ${oldphDto.endToHH}
                                        </c:when>
                                        <c:otherwise>
                                            0${oldphDto.endToHH}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${!oldphDto.selectAllDay && (oldphDto.endToHH != null || oldphDto.endToMM != null)}">
                                    :
                                </c:if>
                                <c:if test="${oldphDto.endToMM != null}">
                                    <c:choose>
                                        <c:when test="${oldphDto.endToMM.length()>1}">
                                            ${oldphDto.endToMM}
                                        </c:when>
                                        <c:otherwise>
                                            0${oldphDto.endToMM}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:if>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="col-xs-12 row">
                        <c:if test="${phDto.selectAllDay}">
                            <div class="newVal" attr="${phDto.selectAllDay}">
                                <div class="form-check active">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <div class="col-xs-12 row">
                        <c:if test="${oldphDto.selectAllDay}">
                            <div class="oldVal" style="display: none" attr="${oldphDto.selectAllDay}">
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