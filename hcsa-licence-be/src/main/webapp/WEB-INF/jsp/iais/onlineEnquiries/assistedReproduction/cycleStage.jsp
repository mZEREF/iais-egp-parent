<div class="col-md-12">
    <div class="col-xs-12">
        <div class="components">

            <iais:pagination param="cycleStageParam" result="cycleStageResult"/>
            <div class="row">
                <b class="col-md-12" style="font-size:2.0rem">
                    Cycles
                </b>
            </div>
            <div class="table-responsive">
                <div class="table-gp">
                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                        <thead>
                        <tr >

                            <iais:sortableHeader needSort="false" style="width: 25%;"
                                                 field="cycle_no"
                                                 value="Cycle No."/>
                            <iais:sortableHeader needSort="false" style="width: 20%;"
                                                 field="CREATED_DT"
                                                 value="Cycle Start Date"/>
                            <iais:sortableHeader needSort="false" style="width: 15%;"
                                                 field="CYCLE_TYPE"
                                                 value="AR/IUI/EFO"/>
                            <iais:sortableHeader needSort="false" style="width: 25%;"
                                                 field="CYCLE_STAGE"
                                                 value="Last Stage Submitted"/>
                            <iais:sortableHeader needSort="false" style="width: 15%;"
                                                 field="STATUS"
                                                 value="Status"/>
                        </tr>
                        </thead>
                        <tbody class="form-horizontal">
                            <c:choose>
                                <c:when test="${empty cycleResult.rows}">
                                    <tr>
                                        <td colspan="15">
                                            <iais:message key="GENERAL_ACK018"
                                                          escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="cycleStage"
                                               items="${cycleResult.rows}"
                                               varStatus="status">
                                        <tr id="advfilterCycle${(status.index + 1) + (cycleStageParam.pageNo - 1) * cycleStageParam.pageSize}">
                                            <td style="vertical-align:middle;">

                                                <p style="white-space: nowrap;"><c:out value="${cycleStage.cycleNo}"/>
                                                    <a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right;color: #2199E8" data-toggle="collapse" data-target="#dropdownCycle${(status.index + 1) + (cycleStageParam.pageNo - 1) * cycleStageParam.pageSize}" onclick="getStageByCycleId('${cycleStage.cycleId}','${(status.index + 1) + (cycleStageParam.pageNo - 1) * cycleStageParam.pageSize}')">
                                                    </a>
                                                </p>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <fmt:formatDate
                                                        value="${cycleStage.cycleStartDate}"
                                                        pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <c:out value="${cycleStage.cycleType}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <iais:code code="${cycleStage.cycleStage}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <iais:code code="${cycleStage.status}"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
            <br>
            <div class="row">
                <b class="col-md-12" style="font-size:2.0rem">
                    Non Cycles
                </b>
            </div>
            <div class="table-responsive">
                <div class="table-gp">
                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                        <thead>
                        <tr >

                            <iais:sortableHeader needSort="false" style="width: 25%;"
                                                 field="cycle_no"
                                                 value="Cycle No."/>
                            <iais:sortableHeader needSort="false" style="width: 20%;"
                                                 field="CREATED_DT"
                                                 value="Cycle Start Date"/>
                            <iais:sortableHeader needSort="false" style="width: 15%;"
                                                 field="CYCLE_TYPE"
                                                 value="AR/IUI/EFO"/>
                            <iais:sortableHeader needSort="false" style="width: 25%;"
                                                 field="CYCLE_STAGE"
                                                 value="Last Stage Submitted"/>
                            <iais:sortableHeader needSort="false" style="width: 15%;"
                                                 field="STATUS"
                                                 value="Status"/>
                        </tr>
                        </thead>
                        <tbody class="form-horizontal">
                        <c:choose>
                            <c:when test="${empty noCycleResult.rows}">
                                <tr>
                                    <td colspan="15">
                                        <iais:message key="GENERAL_ACK018"
                                                      escape="true"/>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="cycleStage"
                                           items="${noCycleResult.rows}"
                                           varStatus="status">
                                    <tr id="advfilterNon${(status.index + 1) + (cycleStageParam.pageNo - 1) * cycleStageParam.pageSize}">
                                        <td style="vertical-align:middle;">

                                            <p style="white-space: nowrap;"><c:out value="${cycleStage.cycleNo}"/>
                                                <a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right;color: #2199E8" data-toggle="collapse" data-target="#dropdownNon${(status.index + 1) + (cycleStageParam.pageNo - 1) * cycleStageParam.pageSize}" onclick="getStageByNonCycleId('${cycleStage.cycleId}','${(status.index + 1) + (cycleStageParam.pageNo - 1) * cycleStageParam.pageSize}')">
                                                </a>
                                            </p>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <fmt:formatDate
                                                    value="${cycleStage.cycleStartDate}"
                                                    pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <c:out value="${cycleStage.cycleType}"/>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <iais:code code="${cycleStage.cycleStage}"/>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <iais:code code="${cycleStage.status}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

