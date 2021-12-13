<div class="col-md-12">
    <div class="col-xs-12">
        <div class="components">

            <iais:pagination param="cycleStageParam" result="cycleStageResult"/>
            <div class="table-responsive">
                <div class="table-gp">
                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                        <thead>
                        <tr >

                            <iais:sortableHeader needSort="false"
                                                 field="cycle_no"
                                                 value="Cycle No"/>
                            <iais:sortableHeader needSort="false"
                                                 field="CREATED_DT"
                                                 value="Cycle Start Date"/>
                            <iais:sortableHeader needSort="false"
                                                 field="CYCLE_TYPE"
                                                 value="AR/IUI/EFO"/>
                            <iais:sortableHeader needSort="false"
                                                 field="CYCLE_STAGE"
                                                 value="Last Stage Submitted"/>
                            <iais:sortableHeader needSort="false"
                                                 field="STATUS"
                                                 value="Status"/>
                        </tr>
                        </thead>
                        <tbody class="form-horizontal">
                            <c:choose>
                                <c:when test="${empty cycleStageResult.rows}">
                                    <tr>
                                        <td colspan="15">
                                            <iais:message key="GENERAL_ACK018"
                                                          escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="cycleStage"
                                               items="${cycleStageResult.rows}"
                                               varStatus="status">
                                        <tr id="advfilter${(status.index + 1) + (cycleStageParam.pageNo - 1) * cycleStageParam.pageSize}">
                                            <td style="vertical-align:middle;">

                                                <p style="width: 165px;"><c:out value="${cycleStage.cycleNo}"/>
                                                    <a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right" data-toggle="collapse" data-target="#dropdown${(status.index + 1) + (cycleStageParam.pageNo - 1) * cycleStageParam.pageSize}" onclick="getStageByCycleId('${cycleStage.cycleId}','${(status.index + 1) + (cycleStageParam.pageNo - 1) * cycleStageParam.pageSize}')">
                                                    </a>
                                                </p>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <fmt:formatDate
                                                        value="${cycleStage.cycleStartDate}"
                                                        pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <iais:code code="${cycleStage.cycleType}"/>
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

<script type="text/javascript">

</script>