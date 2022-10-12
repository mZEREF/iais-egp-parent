<input type="hidden" name="t" value="${t}">
<div class="topTypeAbout">
    <c:choose>
        <c:when test="${empty topByDrug}">
            <c:set var="dCount" value="1"/>
        </c:when>
        <c:otherwise>
            <c:set var="dCount" value="${topByDrug.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" class="atdLength" name="${prefix}atdLength" value="${dCount}" data-prefix="${prefix}"/>
    <c:forEach begin="0" end="${dCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${topByDrug[index]}"/>
        <%@include file="aboutTopDetail1.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addTopByDrugDiv
<c:if test="${('0' == appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <span class="addTopByDrugBtn" style="color:deepskyblue;cursor:pointer;">
            <span>Add more</span>
        </span>
    </div>

    <c:choose>
        <c:when test="${empty topBySurgicalProcedure}">
            <c:set var="pCount" value="1"/>
        </c:when>
        <c:otherwise>
            <c:set var="pCount" value="${topBySurgicalProcedure.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" class="pLength" name="${prefix}pLength" value="${pCount}" />
    <c:forEach begin="0" end="${pCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${topBySurgicalProcedure[index]}"/>
        <%@include file="aboutTopDetail2.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addTopBySurgicalProcedureDiv
<c:if test="${('1' == appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <span class="addTopBySurgicalProcedureBtn" style="color:deepskyblue;cursor:pointer;">
            <span>Add more</span>
        </span>
    </div>

    <c:choose>
        <c:when test="${empty topByAll}">
            <c:set var="aCount" value="1"/>
        </c:when>
        <c:otherwise>
            <c:set var="aCount" value="${topByAll.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" class="aLength" name="${prefix}aLength" value="${aCount}" />
    <c:forEach begin="0" end="${aCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${topByAll[index]}"/>
        <%@include file="aboutTopDetail3.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addTopAllDiv <c:if test="${('-1' != appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <span class="addTopAllBtn" style="color:deepskyblue;cursor:pointer;">
            <span>Add more</span>
        </span>
    </div>
    <div class="de <c:if test="${('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
        <iais:row>
            <div class="col-xs-12 col-md-10">
                <p class="bold">Declaration</p>
            </div>
        </iais:row>
        <iais:row>
            <div class="form-check">
                <input class="form-check-input" name="${prefix}declaration" value="1"
                       type="checkbox" aria-invalid="false" <c:if test="${'1' == appSvcOtherInfoDto.declaration}">checked="checked"</c:if> />
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
            if (atdLength <= 1){
                console.log("init.........")
                $('.topByDrug[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('.topByDrug[data-prefix="' + prefix + '"]').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopByDrugBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                console.log("k....."+k)
                $(this).find('input.year').prop('name','year'+k);
                $(this).find('input.abortNum').prop('name','abortNum'+k);
            });
            $('#isEditHiddenVal').val('1');
            dismissWaiting();
        })
    }

    function resTopDrug(){
        //reset number
        $('.topByDrug').each(function (k,v) {
            console.log("k....."+k);
            toggleTag($(this).find('div.removeTopByDrugBtn'), k != 0);
        });
    }

    let removeTopDrug = function () {
        $('.removeTopByDrugBtn').unbind('click');
        $('.removeTopByDrugBtn').click(function () {
            let $v = $(this);
            let $tag = $v.closest('.rDiv');
            let prefix = $tag.data('prefix');
            console.log(prefix)
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $(this).closest('div.topByDrug[data-prefix="' + prefix + '"]').remove();
            let atdLength = $('.topByDrug[data-prefix="' + prefix + '"]').length;
            $('input.atdLength[data-prefix="' + prefix + '"]').val(atdLength);
            //reset number
            $('div.topByDrug[data-prefix="' + prefix + '"]').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopByDrugBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.year').prop('name','year'+k);
                $(this).find('input.abortNum').prop('name','abortNum'+k);
            });
            //display add more
            if(atdLength <= 1){
                $('.topByDrug[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

    function addTopBySurgicalProcedure(){
        console.log("start......")
        resTopP();
        $('.addTopBySurgicalProcedureBtn').unbind('click');
        $('.addTopBySurgicalProcedureBtn').click(function (){
            showWaiting();
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
            if (pLength <= 1){
                console.log("init.........")
                $('.topBySurgicalProcedure[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('.topBySurgicalProcedure[data-prefix="' + prefix + '"]').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopBySurgicalProcedureBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                console.log("k....."+k)
                $(this).find('input.pyear').prop('name','pyear'+k);
                $(this).find('input.pabortNum').prop('name','pabortNum'+k);
            });
            $('#isEditHiddenVal').val('1');
            dismissWaiting();
        })
    }

    function resTopP(){
        //reset number
        $('.topBySurgicalProcedure').each(function (k,v) {
            console.log("k....."+k);
            toggleTag($(this).find('div.removeTopBySurgicalProcedureBtn'), k != 0);
        });
    }

    let removeTopP = function () {
        $('.removeTopBySurgicalProcedureBtn').unbind('click');
        $('.removeTopBySurgicalProcedureBtn').click(function () {
            let $v = $(this);
            let $tag = $v.closest('.rdDiv');
            let prefix = $tag.data('prefix');
            console.log(prefix)
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $(this).closest('div.topBySurgicalProcedure[data-prefix="' + prefix + '"]').remove();
            let pLength = $('.topBySurgicalProcedure[data-prefix="' + prefix + '"]').length;
            $('input.pLength[data-prefix="' + prefix + '"]').val(pLength);
            //reset number
            $('.topBySurgicalProcedure[data-prefix="' + prefix + '"]').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopBySurgicalProcedureBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                console.log("k....."+k)
                $(this).find('input.pyear').prop('name','pyear'+k);
                $(this).find('input.pabortNum').prop('name','pabortNum'+k);
            });
            //display add more
            if(pLength <= 1){
                $('.topBySurgicalProcedure[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

    function addTopByAll(){
        console.log("start......")
        resTopA();
        $('.addTopAllBtn').unbind('click');
        $('.addTopAllBtn').click(function (){
            showWaiting();
            let $v = $(this);
            let $tag = $v.closest('.addTopAllDiv');
            let prefix = $tag.data('prefix');
            console.log(prefix)
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
            if (aLength <= 1){
                console.log("init.........")
                $('.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopByAllBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                console.log("k....."+k)
                $(this).find('input.ayear').prop('name','ayear'+k);
                $(this).find('input.aabortNum').prop('name','aabortNum'+k);
            });
            $('#isEditHiddenVal').val('1');
            dismissWaiting();
        })
    }

    function resTopA(){
        //reset number
        $('.topByDrugandSurgicalProcedure').each(function (k,v) {
            console.log("k....."+k);
            toggleTag($(this).find('div.removeTopByAllBtn'), k != 0);
        });
    }

    let removeTopAll = function () {
        $('.removeTopByAllBtn').unbind('click');
        $('.removeTopByAllBtn').click(function () {
            let $v = $(this);
            let $tag = $v.closest('.rTDiv');
            let prefix = $tag.data('prefix');
            console.log(prefix)
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $(this).closest('div.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').remove();
            let aLength = $('.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').length;
            $('input.aLength[data-prefix="' + prefix + '"]').val(aLength);
            //reset number
            $('.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopByAllBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                console.log("k....."+k)
                $(this).find('input.ayear').prop('name','ayear'+k);
                $(this).find('input.aabortNum').prop('name','aabortNum'+k);
            });
            //display add more
            if(aLength <= 1){
                $('.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }
</script>




