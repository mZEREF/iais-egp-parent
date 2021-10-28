<ul id = "tabUl" class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
    <c:if test='${schedules.contains("SCHTYPE001")}'>
        <li <c:if test="${activeNodeKey eq 'SCHTYPE001'}">class="active"</c:if> role="presentation"><a data-step-key="approvalProfile_SCHTYPE001" role="tab">First Schedule Part I</a></li>
    </c:if>
    <c:if test='${schedules.contains("SCHTYPE002")}'>
        <li <c:if test="${activeNodeKey eq 'SCHTYPE002'}">class="active"</c:if> role="presentation"><a data-step-key="approvalProfile_SCHTYPE002" role="tab">First Schedule Part II</a></li>
    </c:if>
    <c:if test='${schedules.contains("SCHTYPE003")}'>
        <li <c:if test="${activeNodeKey eq 'SCHTYPE003'}">class="active"</c:if> role="presentation"><a data-step-key="approvalProfile_SCHTYPE003" role="tab">Second Schedule</a></li>
    </c:if>
    <c:if test='${schedules.contains("SCHTYPE004")}'>
        <li <c:if test="${activeNodeKey eq 'SCHTYPE004'}">class="active"</c:if> role="presentation"><a data-step-key="approvalProfile_SCHTYPE004" role="tab">Third Schedule</a></li>
    </c:if>
    <c:if test='${schedules.contains("SCHTYPE005")}'>
        <li <c:if test="${activeNodeKey eq 'SCHTYPE005'}">class="active"</c:if> role="presentation"><a data-step-key="approvalProfile_SCHTYPE005" role="tab">Fourth Schedule</a></li>
    </c:if>
    <c:if test='${schedules.contains("SCHTYPE006")}'>
        <li <c:if test="${activeNodeKey eq 'SCHTYPE006'}">class="active"</c:if> role="presentation"><a data-step-key="approvalProfile_SCHTYPE006" role="tab">Fifth Schedule</a></li>
    </c:if>
</ul>