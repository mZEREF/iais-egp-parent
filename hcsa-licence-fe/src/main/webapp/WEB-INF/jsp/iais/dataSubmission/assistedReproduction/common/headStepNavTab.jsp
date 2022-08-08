<input type="hidden" name="action_type" id="action_type" value="">
<input type="hidden" name="action_value" id="action_value" value="">
<%--@elvariable id="stageList" type="java.util.List<com.ecquaria.cloud.moh.iais.dto.ARCycleStageDto>"--%>
<ul class="progress-tracker">
    <c:if test="${not empty stageList}">
        <c:forEach var="stage" items="${stageList}" varStatus="status" begin="0" end="6">
            <li class="tracker-item ${stage.status}" data-step-key="${stage.stepKey}">${stage.stepValue}</li>
        </c:forEach>
    </c:if>
</ul>
<ul class="progress-tracker">
    <c:if test="${not empty stageList}">
        <c:forEach var="stage" items="${stageList}" varStatus="status" begin="7" end="13">
            <li class="tracker-item ${stage.status}" data-step-key="${stage.stepKey}">${stage.stepValue}</li>
        </c:forEach>
    </c:if>
</ul>
<ul class="icon-tracker">
    <li class="notSubmittedStage">Not Submitted</li>
    <li class="ongoingStage">Ongoing</li>
    <li class="submittedStage">Submitted</li>
    <li class="invalidStage">Invalid</li>
</ul>
<hr style="margin-bottom: 10px"/>

<style>
    .icon-tracker li{
        display: inline-block;
        text-indent: 25px;
        margin-right: 35px;
    }
    .icon-tracker li:before{
        width: 30px;
        height: 30px;
        border: 1px solid #BFBFBF;
        border-radius: 50%;
        top: -3px;
    }

    .progress-tracker li{
        width: 14%;
    }
    li.tracker-item:before {
        width: 30px;
        height: 30px;
        top: -8px;
    }
    li.ongoingStage:before {
        background-color: rgb(251,255,0);
    }
    li.submittedStage:before {
        background-color: rgb(153,247,4);
    }
    li.notSubmittedStage:before {
        background-color: white;
    }
    li.invalidStage:before {
        background-color: rgb(127,127,127);
    }
</style>

<script>
    $(function () {
        $("li[data-step-key]").click(jumpToStep);
    });

    /* Jump for any element contains 'data-step-key' */
    function jumpToStep() {
        showWaiting();
        $("input[name='action_type']").val("jumpStage");
        $("input[name='action_value']").val($(this).attr("data-step-key"));
        $("[name='crud_type']").val("return");
        $("#mainForm").submit();
    }
</script>