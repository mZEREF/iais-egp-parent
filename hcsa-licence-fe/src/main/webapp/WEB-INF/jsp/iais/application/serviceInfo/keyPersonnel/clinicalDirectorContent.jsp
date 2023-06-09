<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<%@include file="/WEB-INF/jsp/iais/application/common/prsLoad.jsp" %>

<c:set var="pcdType" value="${ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR}"/>
<c:set var="personList" value="${currSvcInfoDto.appSvcClinicalDirectorDtoList}"/>
<style>
    label{font-weight: normal;!important;}
</style>
<div class="row form-horizontal normal-label">
    <c:if test="${AppSubmissionDto.needEditController }">
        <input id="isEditHiddenVal" class="person-content-edit" type="hidden" name="isEdit" value="0"/>
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            <div class="app-font-size-16 col-xs-12 text-right">
                <a class="back" id="RfcSkip" href="javascript:void(0);">
                    Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                </a>
            </div>
        </c:if>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    </c:if>


<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
    <iais:row>
        <div class="col-xs-12">
            <p class="app-title"><c:out value="${currStepName}"/></p>
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

    <input type="hidden" name="cdLength" value="${pageLength}" />
    <c:forEach begin="0" end="${personCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}"/>
        <c:set var="person" value="${personList[index]}"/>
        <%@include file="clinicalDirectorDetail.jsp" %>
    </c:forEach>

    <c:if test="${!isRfi}">
        <div class="col-md-12 col-xs-12 addClinicalDirectorDiv">
            <span class="addClinicalDirectorBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add <c:out value="${singleName}"/></span>
            </span>
        </div>
    </c:if>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        let psnContent = '.person-content';
        removePersonEvent(psnContent);
        assignSelectEvent(psnContent);
        psnEditEvent(psnContent);
        profRegNoEvent(psnContent);
        $('.addClinicalDirectorBtn').on('click', function () {
            addPersonnel(psnContent);
            checkBoxEvent();
        });
        <c:if test="${AppSubmissionDto.needEditController}">
        $(psnContent).each(function () {
            disablePsnContent($(this), psnContent);
        });
        </c:if>
        initPerson(psnContent);
        checkBoxEvent();
        $(psnContent).find('.check-boxs').trigger('change');
    });

    function refreshPersonOthers($target, action) {
        if (action == 1) {
            removeTag('.addClinicalDirectorDiv');
        } else {
            const maxCount = eval('${currStepConfig.maximumCount}');
            toggleTag('.addClinicalDirectorDiv', $('div.person-content').length < maxCount);
        }
    }
    let checkBoxEvent = function (){
        $('.check-boxs').change(function(){
            let value = $(this).prop("checked")
            let personContent = $(this).closest('.person-content');
            if (value){
                personContent.find('.professionBoards .mandatory').remove();
                personContent.find('.profRegNos .mandatory').remove();
            }else {
                personContent.find('.professionBoards .mandatory').remove();
                personContent.find('.profRegNos .mandatory').remove();
                personContent.find('.professionBoards').append('<span class="mandatory">*</span>');
                personContent.find('.profRegNos').append('<span class="mandatory">*</span>');
            }
        })
    }

</script>