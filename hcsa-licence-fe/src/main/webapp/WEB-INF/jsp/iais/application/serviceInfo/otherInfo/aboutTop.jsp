<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<input type="hidden" name="t" value="${t}">
<div class="topTypeAbout">
    <div class="topByDrug1
<c:if test="${('0' == appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <p class="bold">TOP (By Drug)</p>
    </div>
    <c:choose>
        <c:when test="${empty topByDrug}">
            <c:set var="dCount" value="1"/>
        </c:when>
        <c:otherwise>
            <c:set var="dCount" value="${topByDrug.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" class="atdLength" name="${prefix}atdLength" value="${dCount}" data-prefix="${prefix}"/>
    <input type="hidden" name="${prefix}rfcDrugEdit" class="rfcDrugEdit" value="" data-prefix="${prefix}">
    <c:forEach begin="0" end="${dCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${topByDrug[index]}"/>
        <%@include file="aboutTopDetail1.jsp" %>
    </c:forEach>
    <c:if test="${!isRfi}">
        <div class="col-md-12 col-xs-12 addTopByDrugDiv <c:if test="${('0' == appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <span class="addTopByDrugBtn" style="color:deepskyblue;cursor:pointer;">
            <span style="">Add more</span>
        </span>
        </div>
    </c:if>

    <div class="topBySurgicalProcedure1
<c:if test="${('1' == appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <p class="bold">TOP (By Surgical Procedure)</p>
    </div>
    <c:choose>
        <c:when test="${empty topBySurgicalProcedure}">
            <c:set var="pCount" value="1"/>
        </c:when>
        <c:otherwise>
            <c:set var="pCount" value="${topBySurgicalProcedure.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" class="pLength" name="${prefix}pLength" value="${pCount}" data-prefix="${prefix}"/>
    <input type="hidden" name="${prefix}rfcSurgicalEdit" class="rfcSurgicalEdit" value="" data-prefix="${prefix}">
    <c:forEach begin="0" end="${pCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${topBySurgicalProcedure[index]}"/>
        <%@include file="aboutTopDetail2.jsp" %>
    </c:forEach>
    <c:if test="${!isRfi}">
        <div class="col-md-12 col-xs-12 addTopBySurgicalProcedureDiv
<c:if test="${('1' == appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <span class="addTopBySurgicalProcedureBtn" style="color:deepskyblue;cursor:pointer;">
            <span>Add more</span>
        </span>
        </div>
    </c:if>

    <div class="topByDrugandSurgicalProcedure1
<c:if test="${('-1' != appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <p class="bold">TOP (By Drug and Surgical Procedure)</p>
    </div>
    <c:choose>
        <c:when test="${empty topByAll}">
            <c:set var="aCount" value="1"/>
        </c:when>
        <c:otherwise>
            <c:set var="aCount" value="${topByAll.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" class="aLength" name="${prefix}aLength" value="${aCount}" data-prefix="${prefix}"/>
    <input type="hidden" name="${prefix}rfcAllEdit" class="rfcAllEdit" value="" data-prefix="${prefix}">
    <c:forEach begin="0" end="${aCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${topByAll[index]}"/>
        <%@include file="aboutTopDetail3.jsp" %>
    </c:forEach>
    <c:if test="${!isRfi}">
        <div class="col-md-12 col-xs-12 addTopAllDiv <c:if test="${('-1' != appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <span class="addTopAllBtn" style="color:deepskyblue;cursor:pointer;">
            <span>Add more</span>
        </span>
        </div>
    </c:if>
    <div class="de <c:if test="${('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <iais:row>
            <div class="col-xs-12 col-md-10">
                <p class="bold">Declaration</p>
            </div>
        </iais:row>
        <iais:row>
            <div class="form-check">
                <input class="form-check-input" name="${prefix}declaration" value="0"
                       type="checkbox" aria-invalid="false" <c:if test="${'0' == appSvcOtherInfoDto.declaration}">checked="checked"</c:if> />
                <label class="form-check-label">
                    <span class="check-square"></span><c:out value="I declare the information in my application to be true, to the best of my knowledge.
                        I also understand that approval of the licence is dependent on satisfactory compliance with the relevant requirements under
                        the Healthcare Services Act, Regulations and Guidelines and the TOP Act, Regulations and Guidelines."/>
                    <span class="mandatory">*</span>
                </label>
            </div>
        </iais:row>
        <iais:row cssClass="row control control-caption-horizontal">
            <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
            <iais:value width="7" cssClass="col-md-7 col-xs-12">
                <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}declaration"></span>
            </iais:value>
        </iais:row>
    </div>
</div>
<script>
    $(document).ready(function (){
        addTopByDrug();
        removeTopDrug();

        addTopBySurgicalProcedure();
        removeTopP();

        addTopByAll();
        removeTopAll();
    });

    function addTopByDrug(){
        resTopDrug();
        $('.addTopByDrugBtn').unbind('click');
        $('.addTopByDrugBtn').click(function (){
            showWaiting();
            if (${AppSubmissionDto.needEditController }){
                $('a.otherInfoTopEdit').trigger('click');
            }
            let $tag = $(this);
            let $target = $tag.closest('.addTopByDrugDiv');
            let prefix = $target.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            let target = $('div.topByDrug[data-prefix="' + prefix + '"]').first();
            let src = target.clone();
            $('div.addTopByDrugDiv[data-prefix="' + prefix + '"]').before(src);
            let atdLength = $('.topByDrug[data-prefix="' + prefix + '"]').length;
            $('input.atdLength[data-prefix="' + prefix + '"]').val(atdLength);
            let $c = $('div.topByDrug[data-prefix="' + prefix + '"]').last();
            clearFields($c);
            removeTopDrug();
            $('.topByDrug[data-prefix="' + prefix + '"]').each(function (k,v) {
                toggleTag($(this).find('div.removeTopByDrugBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('input.isPartEditDrug').prop('name',prefix+'isPartEditDrug'+k);
                $(this).find('input.DrugId').prop('name',prefix+'DrugId'+k);
                $(this).find('input.year').prop('name',prefix +'year'+k);
                $(this).find('input.abortNum').prop('name',prefix +'abortNum'+k);
            });
            let rfcEdit = $('input.rfcDrugEdit[data-prefix="' + prefix + '"]').val();
            console.log("addRfcEdit:"+rfcEdit);
            if (!isEmpty(rfcEdit) && 'doEditDrug' == rfcEdit){
                $('input.isPartEditDrug[data-prefix="' + prefix + '"]').val('1');
            }
            dismissWaiting();
        })
    }
    function resTopDrug(){
        //reset number
        $('.topByDrug').each(function (k,v) {
            toggleTag($(this).find('div.removeTopByDrugBtn'), k != 0);
        });
        $('#isEditHiddenVal').val('1');
    }

    function removeTopDrug() {
        $('.removeTopByDrugBtn').unbind('click');
        $('.removeTopByDrugBtn').click(function () {
            let $v = $(this);
            let $tag = $v.closest('.rDiv');
            let prefix = $tag.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $(this).closest('div.topByDrug[data-prefix="' + prefix + '"]').remove();
            let atdLength = $('.topByDrug[data-prefix="' + prefix + '"]').length;
            $('input.atdLength[data-prefix="' + prefix + '"]').val(atdLength);
            //reset number
            $('div.topByDrug[data-prefix="' + prefix + '"]').each(function (k,v) {
                toggleTag($(this).find('div.removeTopByDrugBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('input.isPartEditDrug').prop('name',prefix+'isPartEditDrug'+k);
                $(this).find('input.DrugId').prop('name',prefix+'DrugId'+k);
                $(this).find('input.year').prop('name',prefix +'year'+k);
                $(this).find('input.abortNum').prop('name',prefix +'abortNum'+k);
            });
            $('#isEditHiddenVal').val('1');
        });
    }

    function addTopBySurgicalProcedure(){
        resTopP();
        $('.addTopBySurgicalProcedureBtn').unbind('click');
        $('.addTopBySurgicalProcedureBtn').click(function (){
            showWaiting();
            if (${AppSubmissionDto.needEditController }){
                $('a.otherInfoTopEdit').trigger('click');
            }
            let $tag = $(this);
            let $target = $tag.closest('.addTopBySurgicalProcedureDiv');
            let prefix = $target.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            let target = $('div.topBySurgicalProcedure[data-prefix="' + prefix + '"]').first();
            let src = target.clone();
            $('div.addTopBySurgicalProcedureDiv[data-prefix="' + prefix + '"]').before(src);
            let pLength = $('.topBySurgicalProcedure[data-prefix="' + prefix + '"]').length;
            $('input.pLength[data-prefix="' + prefix + '"]').val(pLength);
            let $c = $('div.topBySurgicalProcedure[data-prefix="' + prefix + '"]').last();
            clearFields($c);
            removeTopP();
            $('.topBySurgicalProcedure[data-prefix="' + prefix + '"]').each(function (k,v) {
                toggleTag($(this).find('div.removeTopBySurgicalProcedureBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('input.isPartEditSurgical').prop('name',prefix+'isPartEditSurgical'+k);
                $(this).find('input.SurgicalId').prop('name',prefix+'SurgicalId'+k);
                $(this).find('input.pyear').prop('name',prefix +'pyear'+k);
                $(this).find('input.pabortNum').prop('name',prefix +'pabortNum'+k);
            });
            let rfcEdit = $('input.rfcSurgicalEdit[data-prefix="' + prefix + '"]').val();
            console.log("addRfcEdit:"+rfcEdit);
            if (!isEmpty(rfcEdit) && 'doEditSurgical' == rfcEdit){
                $('input.isPartEditSurgical[data-prefix="' + prefix + '"]').val('1');
            }
            dismissWaiting();
        })
    }
    function resTopP(){
        //reset number
        $('.topBySurgicalProcedure').each(function (k,v) {
            toggleTag($(this).find('div.removeTopBySurgicalProcedureBtn'), k != 0);
        });
        $('#isEditHiddenVal').val('1');
    }

    function removeTopP() {
        $('.removeTopBySurgicalProcedureBtn').unbind('click');
        $('.removeTopBySurgicalProcedureBtn').click(function () {
            let $v = $(this);
            let $tag = $v.closest('.rdDiv');
            let prefix = $tag.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $(this).closest('div.topBySurgicalProcedure[data-prefix="' + prefix + '"]').remove();
            let pLength = $('.topBySurgicalProcedure[data-prefix="' + prefix + '"]').length;
            $('input.pLength[data-prefix="' + prefix + '"]').val(pLength);
            //reset number
            $('.topBySurgicalProcedure[data-prefix="' + prefix + '"]').each(function (k,v) {
                toggleTag($(this).find('div.removeTopBySurgicalProcedureBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('input.isPartEditSurgical').prop('name',prefix+'isPartEditSurgical'+k);
                $(this).find('input.SurgicalId').prop('name',prefix+'SurgicalId'+k);
                $(this).find('input.pyear').prop('name',prefix +'pyear'+k);
                $(this).find('input.pabortNum').prop('name',prefix +'pabortNum'+k);
            });
            $('#isEditHiddenVal').val('1');
        });
    }

    function addTopByAll(){
        resTopA();
        $('.addTopAllBtn').unbind('click');
        $('.addTopAllBtn').click(function (){
            showWaiting();
            if (${AppSubmissionDto.needEditController }){
                $('a.otherInfoTopEdit').trigger('click');
            }
            let $v = $(this);
            let $tag = $v.closest('.addTopAllDiv');
            let prefix = $tag.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            let target = $('div.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').first();
            let src = target.clone();
            $('div.addTopAllDiv[data-prefix="' + prefix + '"]').before(src);
            let aLength = $('.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').length;
            $('input.aLength[data-prefix="' + prefix + '"]').val(aLength);
            let $c = $('div.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').last();
            clearFields($c);
            removeTopAll();
            $('.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').each(function (k,v) {
                toggleTag($(this).find('div.removeTopByAllBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('input.isPartEditAll').prop('name',prefix+'isPartEditAll'+k);
                $(this).find('input.AllId').prop('name',prefix+'AllId'+k);
                $(this).find('input.ayear').prop('name',prefix +'ayear'+k);
                $(this).find('input.aabortNum').prop('name',prefix +'aabortNum'+k);
            });
            let rfcEdit = $('input.rfcAllEdit[data-prefix="' + prefix + '"]').val();
            console.log("addRfcEdit:"+rfcEdit);
            if (!isEmpty(rfcEdit) && 'doEditAll' == rfcEdit){
                $('input.isPartEditAll[data-prefix="' + prefix + '"]').val('1');
            }
            dismissWaiting();
        })
    }
    function resTopA(){
        //reset number
        $('.topByDrugandSurgicalProcedure').each(function (k,v) {
            toggleTag($(this).find('div.removeTopByAllBtn'), k != 0);
        });
        $('#isEditHiddenVal').val('1');
    }

    function removeTopAll() {
        $('.removeTopByAllBtn').unbind('click');
        $('.removeTopByAllBtn').click(function () {
            let $v = $(this);
            let $tag = $v.closest('.rTDiv');
            let prefix = $tag.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $(this).closest('div.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').remove();
            let aLength = $('.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').length;
            $('input.aLength[data-prefix="' + prefix + '"]').val(aLength);
            //reset number
            $('.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').each(function (k,v) {
                toggleTag($(this).find('div.removeTopByAllBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('input.isPartEditAll').prop('name',prefix+'isPartEditAll'+k);
                $(this).find('input.AllId').prop('name',prefix+'AllId'+k);
                $(this).find('input.ayear').prop('name',prefix +'ayear'+k);
                $(this).find('input.aabortNum').prop('name',prefix +'aabortNum'+k);
            });
            $('#isEditHiddenVal').val('1');
        });
    }
</script>




