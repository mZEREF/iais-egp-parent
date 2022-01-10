<%--@elvariable id="activityTypes" type="java.util.List<java.lang.String>"--%>
<%--@elvariable id="activeNodeKey" type="java.lang.String"--%>
<ul id = "tabUl" class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
    <c:if test='${activityTypes.contains("ACTVITY001")}'>
        <li <c:if test="${activeNodeKey eq 'ACTVITY001'}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_ACTVITY001" role="tab">First and/or Second Schedule Biological Agent</a></li>
    </c:if>
    <c:if test='${activityTypes.contains("ACTVITY002")}'>
        <li <c:if test="${activeNodeKey eq 'ACTVITY002'}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_ACTVITY002" role="tab">First Schedule Biological Agent</a></li>
    </c:if>
    <c:if test='${activityTypes.contains("ACTVITY003")}'>
        <li <c:if test="${activeNodeKey eq 'ACTVITY003'}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_ACTVITY003" role="tab">Fifth Schedule Toxin</a></li>
    </c:if>
    <c:if test='${activityTypes.contains("ACTVITY004")}'>
        <li <c:if test="${activeNodeKey eq 'ACTVITY004'}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_ACTVITY004" role="tab">Poliovirus Infectious Materials</a></li>
    </c:if>
    <c:if test='${activityTypes.contains("ACTVITY005")}'>
        <li <c:if test="${activeNodeKey eq 'ACTVITY005'}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_ACTVITY005" role="tab">Large-Scale Production of First Schedule Biological Agent</a></li>
    </c:if>
    <c:if test='${activityTypes.contains("ACTVITY006")}'>
        <li <c:if test="${activeNodeKey eq 'ACTVITY006'}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_ACTVITY006" role="tab">Large-Scale Production of Third Schedule Biological Agent</a></li>
    </c:if>
    <c:if test='${activityTypes.contains("ACTVITY007")}'>
        <li <c:if test="${activeNodeKey eq 'ACTVITY007'}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_ACTVITY007" role="tab">Handling of Fifth Schedule Toxin</a></li>
    </c:if>
    <c:if test='${activityTypes.contains("ACTVITY008")}'>
        <li <c:if test="${activeNodeKey eq 'ACTVITY008'}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_ACTVITY008" role="tab">Handling of Fifth Schedule Toxin for Exempted Purposes</a></li>
    </c:if>
    <c:if test='${activityTypes.contains("ACTVITY009")}'>
        <li <c:if test="${activeNodeKey eq 'ACTVITY009'}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_ACTVITY009" role="tab">Handling of Poliovirus Infectious Materials</a></li>
    </c:if>
    <c:if test='${activityTypes.contains("ACTVITY010")}'>
        <li <c:if test="${activeNodeKey eq 'ACTVITY010'}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_ACTVITY010" role="tab">Handling of Poliovirus Potentially Infectious Materials</a></li>
    </c:if>
</ul>