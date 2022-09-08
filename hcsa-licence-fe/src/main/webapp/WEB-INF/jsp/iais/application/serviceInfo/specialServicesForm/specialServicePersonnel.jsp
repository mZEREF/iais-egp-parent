<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>

<c:set var="max" value="" />
<c:forEach var="pMax" items="${max}">
    <c:set var="psnType" value="${pMax.key}" />
    <c:if test="${min[psnType] <= 0}" >
        <c:choose>
            <c:when test="${psnType == ApplicationConsts.PERSONNEL_PSN_TYPE_CGO}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcCgoDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcCgoDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSE_IN_CHARGE}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcNurseDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcCgoDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcCgoDtoList}" />
            </c:when>
        </c:choose>

        <c:choose>
            <c:when test="${empty personList && pMax.value > 1}">
                <c:set var="personCount" value="${pMax.value}"/>
            </c:when>
            <c:when test="${empty personList}">
                <c:set var="personCount" value="1"/>
            </c:when>
            <c:when test="${min[psnType] > personList.size() }">
                <c:set var="personCount" value="${min[psnType]}"/>
            </c:when>
            <c:otherwise>
                <c:set var="personCount" value="${personList.size()}"/>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${psnType == ApplicationConsts.PERSONNEL_PSN_TYPE_CGO}">
                <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                    <c:set var="index" value="${vs.index}" />
                    <c:set var="person" value="${personList[index]}"/>
                    <c:set var="prepsn" value="${psnType}${status.index}"/>
                    <c:set var="singleName" value="${HcsaConsts.CLINICAL_GOVERNANCE_OFFICER}"/>
                    <c:set var="psnContent" value="${psnType}-person-content"/>
                    <%@include file="../keyPersonnel/personnelDetail.jsp" %>
                </c:forEach>
                <%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
            </c:when>
            <c:when test="${psnType == ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER}">
                <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                    <c:set var="index" value="${vs.index}" />
                    <c:set var="sectionLeader" value="${personList[index]}"/>
                    <%--<c:set var="prepsn" value="${psnType}"/>--%>
                    <c:set var="singleName" value="${HcsaConsts.SECTION_LEADER}"/>
                    <%@include file="../svcPersonnel/sectionLeaderDetail.jsp" %>
                </c:forEach>
                <%@include file="../svcPersonnel/sectionLeaderFun.jsp" %>
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSE_IN_CHARGE}">
                <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                    <c:set var="index" value="${vs.index}"/>
                    <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                    <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}nic"/>
                    <c:set var="title" value="Nurse in Charge"/>
                    <%@include file="specialServiceDetail.jsp" %>
                </c:forEach>
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR}">
                <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                    <c:set var="index" value="${vs.index}"/>
                    <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                    <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}dir"/>
                    <c:set var="title" value="Emergency Department Director"/>
                    <%@include file="specialServiceDetail.jsp" %>
                </c:forEach>
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR}">
                <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                    <c:set var="index" value="${vs.index}"/>
                    <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                    <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}dir"/>
                    <c:set var="title" value="Emergency Department Director"/>
                    <%@include file="specialServiceDetail.jsp" %>
                </c:forEach>
            </c:when>
        </c:choose>
        <iais:row>
            <div class="col-md-12 col-xs-12 addDiv <c:if test="${personCount >= pMax.value}">hidden</c:if>">
                <input type="hidden" class ="psnType" value="${psnType}"/>
                <input type="hidden" class ="${psnType}MaxCount" value="${pMax.value}"/>
                <input type="hidden" class ="${psnType}Length" value="${personCount}"/>
                <span class="addBtn" style="color:deepskyblue;cursor:pointer;">
                    <span style="">Add more</span>
                </span>
            </div>
        </iais:row>
    </c:if>
</c:forEach>
<script type="text/javascript">
    //chceck add more button via CGO Max count
    function refreshPersonOthers($target, k) {
        let cntClass = $target.attr('class');
        if ('CGO-person-content' == cntClass) {
            const maxDpoCount = eval('${dpoHcsaSvcPersonnelDto.maximumCount}');
            toggleTag('.addDpoDiv', $('div.CGO-person-content').length < maxDpoCount);
        }
    }

</script>