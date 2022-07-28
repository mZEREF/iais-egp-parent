<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<c:set var="pcdType" value="${ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR}"/>
<c:set var="personList" value="${currSvcInfoDto.appSvcClinicalDirectorDtoList}"/>

<iais:row cssClass="row col-xs-12 col-md-12 text-right">
    <c:if test="${AppSubmissionDto.needEditController }">
        <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
        <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
            <div class="app-font-size-16">
                <a class="back" id="RfcSkip" href="javascript:void(0);">
                    Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                </a>
            </div>
        </c:if>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    </c:if>
</iais:row>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>

<div class="row form-horizontal">
    <iais:row cssClass="col-md-12 col-xs-12">
        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <iais:value width="10" cssClass="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">
                        <p class="app-title"><c:out value="${currStepName}"/></p>
                    </label>
                </iais:value>
            </div>
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
        <c:set var="index" value="${status.index}" />
        <c:set var="person" value="${personList[index]}"/>
        <%@include file="personnelDetail.jsp" %>
    </c:forEach>

    <c:if test="${!isRfi}">
        <c:set var="needAddPsn" value="true"/>
        <c:choose>
            <c:when test="${currStepConfig.status =='CMSTAT003'}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${personCount >= currStepConfig.maximumCount}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
        </c:choose>
        <div class="col-md-12 col-xs-12 addClinicalDirectorDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addClinicalDirectorBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add <c:out value="${singleName}"/></span>
            </span>
        </div>
    </c:if>
</div>
<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        refresh();
    });


    $(function() {
        $('.addClinicalDirectorBtn').on('click', function () {
            addPersonnel('div.person-content');
        });
    });

    function refreshPersonOthers($target, k) {
        var maxCount = eval('${currStepConfig.maximumCount}');
        toggleTag('.addKeyAppointmentHolderDiv', $('div.person-content').length < maxCount);
    }

    function refresh(){
        var cdLength = $('.person-content').length;
        $('input[name="cdLength"]').val(cdLength);
        //reset number
        $('div.person-content').each(function (k,v) {
            toggleTag($(this).find('div.removeBtn'), k != 0);
            $(this).find('select.assignSel').prop('name','assignSel'+k);
            $(this).find('select.assignSel').prop('id','assignSel'+k);

            $('div.person-content').find('select').niceSelect('update');
            //display add more
            if (cdLength < '${person-content.maximumCount}') {
                $('.addClinicalDirectorDiv').removeClass('hidden');
            }
            if(cdLength <= 1){
                $('.person-content:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

</script>