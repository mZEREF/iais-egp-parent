<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<%@include file="/WEB-INF/jsp/iais/application/common/prsLoad.jsp" %>

<c:set var="personList" value="${currSvcInfoDto.appSvcKeyAppointmentHolderDtoList}"/>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<div class="row form-horizontal normal-label">
    <c:if test="${AppSubmissionDto.needEditController }">
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            <iais:row>
                <div class="text-right app-font-size-16 col-xs-12">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
                </div>
            </iais:row>
        </c:if>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    </c:if>
    <iais:row>
        <div class="col-xs-12">
            <p class="app-title"><c:out value="${currStepName}"/></p>
            <p><h4><iais:message key="NEW_ACK029"/></h4></p>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
        </div>
    </iais:row>

    <c:choose>
        <c:when test="${empty personList && currStepConfig.mandatoryCount > 1}">
            <c:set var="personCount" value="${currStepConfig.mandatoryCount}"/>
        </c:when>
        <c:when test="${empty personList}">
            <c:set var="personCount" value="1"/>
        </c:when>
        <c:when test="${currStepConfig.mandatoryCount > personList.size() }">
            <c:set var="personCount" value="${currStepConfig.mandatoryCount}"/>
        </c:when>
        <c:otherwise>
            <c:set var="personCount" value="${personList.size()}"/>
        </c:otherwise>
    </c:choose>
    <c:set var="keyPerson" value="keyPerson"/>
    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="status">
        <c:set var="index" value="${status.index}"/>
        <c:set var="person" value="${personList[index]}"/>
        <%@include file="personnelDetail.jsp" %>
    </c:forEach>
<%--    <c:if test="${!isRfi}">--%>
    <div class="col-md-12 col-xs-12 addKeyAppointmentHolderDiv">
            <span class="addKeyAppointmentHolderBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Another <c:out value="${singleName}"/></span>
            </span>
    </div>
<%--    </c:if>--%>
</div>
<script type="text/javascript">
    $(function() {
        let psnContent = '.person-content';
        removePersonEvent(psnContent);
        assignSelectEvent(psnContent);
        psnEditEvent(psnContent);
        profRegNoEvent(psnContent);
        $('.addKeyAppointmentHolderBtn').on('click', function () {
            addPersonnel(psnContent);
        });
        <c:if test="${AppSubmissionDto.needEditController}">
        $(psnContent).each(function () {
            disablePsnContent($(this), psnContent);
        });
        </c:if>
        initPerson(psnContent);
    });

    function refreshPersonOthers($target, action) {
        if (action == 1) {
            removeTag('.addKeyAppointmentHolderDiv');
        } else {
            const maxCount = eval('${currStepConfig.maximumCount}');
            toggleTag('.addKeyAppointmentHolderDiv', $('div.person-content').length < maxCount);
        }
    }
    $('div.personnel-content').each(function (k, v) {
        if ($("#errorMapIs").val() == 'error') {
            $(v).find('.error-msg').on('DOMNodeInserted', function () {
                if ($(this).not(':empty')) {
                    $(v).find('.isPartEdit').val(1);
                    $('#isEditHiddenVal').val('1');
                    $(v).find('a.edit').trigger('click');
                }
            });
        }
    });



</script>

