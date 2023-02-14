<input type="hidden" name="action_type" id="action_type" value="">
<input type="hidden" name="action_value" id="action_value" value="">
<input type="hidden" name="jump_to_submitted_stage" id="jump_to_submitted_stage" value="">
<input type="hidden" name="target_stage_user_permissions" id="target_stage_user_permissions" value="">
<%--@elvariable id="stageList" type="java.util.List<com.ecquaria.cloud.moh.iais.dto.ARCycleStageDto>"--%>

<c:if test="${not empty stageList}">
    <ul class="stage-tracker">
        <c:forEach var="stage" items="${stageList}" varStatus="status" begin="0" end="7">
            <li class="tracker-item ${stage.status}" data-step-key="${stage.stepKey}" data-permissions="${stage.permissions}"><a href="javascript:void(0)">${stage.stepValue}</a></li>
        </c:forEach>
    </ul>
    <ul class="stage-tracker">
        <c:forEach var="stage" items="${stageList}" varStatus="status" begin="8" end="14">
            <li class="tracker-item ${stage.status}" data-step-key="${stage.stepKey}" data-permissions="${stage.permissions}"><a href="javascript:void(0)">${stage.stepValue}</a></li>
        </c:forEach>
    </ul>
    <ul class="stage-tracker-legend">
        <li class="tracker-item"><p>Not Submitted</p></li>
        <li class="tracker-item active"><p>Ongoing</p></li>
        <li class="tracker-item completed"><p>Submitted</p></li>
        <li class="tracker-item disabled"><p>Invalid</p></li>
    </ul>
    <span id="error_topErrorMsg" name="iaisErrorMsg" class="error-msg"></span>
</c:if>



