<iais:row cssClass="edit-content">
    <c:if test="${canEdit}">
        <div class="text-right app-font-size-16">
            <a class="edit psnEdit" href="javascript:void(0);">
                <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
            </a>
        </div>
    </c:if>
</iais:row>

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
    <input type="hidden" name="atdLength" value="${dCount}" />
    <c:forEach begin="0" end="${dCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${topByDrug[index]}"/>
        <%@include file="aboutTopDetail1.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addTopByDrugDiv <c:if test="${'0' == appSvcOtherInfoTop.topType}">hidden</c:if>">
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
    <input type="hidden" name="pLength" value="${pCount}" />
    <c:forEach begin="0" end="${pCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${topBySurgicalProcedure[index]}"/>
        <%@include file="aboutTopDetail2.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addTopBySurgicalProcedureDiv <c:if test="${'1' == appSvcOtherInfoTop.topType}">hidden</c:if>">
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
    <input type="hidden" name="aLength" value="${aCount}" />
    <c:forEach begin="0" end="${aCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${topByAll[index]}"/>
        <%@include file="aboutTopDetail3.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addTopAllDiv <c:if test="${'-1' != appSvcOtherInfoTop.topType}">hidden</c:if>">
        <span class="addTopAllBtn" style="color:deepskyblue;cursor:pointer;">
            <span>Add more</span>
        </span>
    </div>
    <div class="de <c:if test="${'0' == provideTop}">hidden</c:if>">
        <iais:row>
            <div class="col-xs-12 col-md-12">
                <p class="bold">Declaration</p>
            </div>
        </iais:row>
        <iais:row>
            <div class="col-md-1">
                <input class="form-check-input declaration"  type="checkbox" checked="checked" name="declaration">
            </div>
            <div class="col-md-11">
                <span>I declare the information in my application to be true, to the best of my knowledge.
                    I also understand that approval of the licence is dependent on satisfactory compliance with the relevant requirements under
                    the Healthcare Services Act, Regulations and Guidelines and the TOP Act, Regulations and Guidelines.</span>
            </div>
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
        console.log("start......")
        let atdLength = $('.topByDrug').length;
        resTopDrug();
        $('.addTopByDrugBtn').unbind('click');
        $('.addTopByDrugBtn').click(function (){
            showWaiting();
            let target = $('div.topByDrug:first');
            let src = target.clone();
            $('div.addTopByDrugDiv').before(src);
            atdLength = atdLength +1;
            $('input[name="atdLength"]').val(atdLength);
            let $c = $('div.topByDrug:last');
            clearFields($c);
            removeTopDrug();
            if (atdLength <= 1){
                console.log("init.........")
                $('.topByDrug:eq(0) .assign-psn-item').html('');
            }
            $('.topByDrug').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopByDrugBtn'), k != 0);
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
        let atdLength = $('.topByDrug').length;
        $('input[name="atdLength"]').val(atdLength);
        //reset number
        $('.topByDrug').each(function (k,v) {
            console.log("k....."+k);
            toggleTag($(this).find('div.removeTopByDrugBtn'), k != 0);
        });
    }

    let removeTopDrug = function () {
        $('.removeTopByDrugBtn').unbind('click');
        $('.removeTopByDrugBtn').click(function () {
            $(this).closest('div.topByDrug').remove();
            let atdLength = $('.topByDrug').length;
            $('input[name="atdLength"]').val(atdLength);
            //reset number
            $('div.topByDrug').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopByDrugBtn'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.year').prop('name','year'+k);
                $(this).find('input.abortNum').prop('name','abortNum'+k);
            });
            //display add more
            if(atdLength <= 1){
                $('.topByDrug:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

    function addTopBySurgicalProcedure(){
        console.log("start......")
        let pLength = $('.topBySurgicalProcedure').length;
        resTopP();
        $('.addTopBySurgicalProcedureBtn').unbind('click');
        $('.addTopBySurgicalProcedureBtn').click(function (){
            showWaiting();
            let target = $('div.topBySurgicalProcedure:first');
            let src = target.clone();
            $('div.addTopBySurgicalProcedureDiv').before(src);
            pLength = pLength +1;
            $('input[name="pLength"]').val(pLength);
            let $c = $('div.topBySurgicalProcedure:last');
            clearFields($c);
            removeTopP();
            if (pLength <= 1){
                console.log("init.........")
                $('.topBySurgicalProcedure:eq(0) .assign-psn-item').html('');
            }
            $('.topBySurgicalProcedure').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopBySurgicalProcedureBtn'), k != 0);
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
        let pLength = $('.topBySurgicalProcedure').length;
        $('input[name="pLength"]').val(pLength);
        //reset number
        $('.topBySurgicalProcedure').each(function (k,v) {
            console.log("k....."+k);
            toggleTag($(this).find('div.removeTopBySurgicalProcedureBtn'), k != 0);
        });
    }

    let removeTopP = function () {
        $('.removeTopBySurgicalProcedureBtn').unbind('click');
        $('.removeTopBySurgicalProcedureBtn').click(function () {
            $(this).closest('div.topBySurgicalProcedure').remove();
            let pLength = $('.topBySurgicalProcedure').length;
            $('input[name="pLength"]').val(pLength);
            //reset number
            $('.topBySurgicalProcedure').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopBySurgicalProcedureBtn'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                console.log("k....."+k)
                $(this).find('input.pyear').prop('name','pyear'+k);
                $(this).find('input.pabortNum').prop('name','pabortNum'+k);
            });
            //display add more
            if(pLength <= 1){
                $('.topBySurgicalProcedure:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

    function addTopByAll(){
        console.log("start......")
        let aLength = $('.topByDrugandSurgicalProcedure').length;
        resTopA();
        $('.addTopAllBtn').unbind('click');
        $('.addTopAllBtn').click(function (){
            showWaiting();
            let target = $('div.topByDrugandSurgicalProcedure:first');
            let src = target.clone();
            $('div.addTopAllDiv').before(src);
            aLength = aLength +1;
            $('input[name="aLength"]').val(aLength);
            let $c = $('div.topByDrugandSurgicalProcedure:last');
            clearFields($c);
            removeTopAll();
            if (aLength <= 1){
                console.log("init.........")
                $('.topByDrugandSurgicalProcedure:eq(0) .assign-psn-item').html('');
            }
            $('.topByDrugandSurgicalProcedure').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopByAllBtn'), k != 0);
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
        let aLength = $('.topByDrugandSurgicalProcedure').length;
        $('input[name="aLength"]').val(aLength);
        //reset number
        $('.topByDrugandSurgicalProcedure').each(function (k,v) {
            console.log("k....."+k);
            toggleTag($(this).find('div.removeTopByAllBtn'), k != 0);
        });
    }

    let removeTopAll = function () {
        $('.removeTopByAllBtn').unbind('click');
        $('.removeTopByAllBtn').click(function () {
            $(this).closest('div.topByDrugandSurgicalProcedure').remove();
            let aLength = $('.topByDrugandSurgicalProcedure').length;
            $('input[name="aLength"]').val(aLength);
            //reset number
            $('.topByDrugandSurgicalProcedure').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removeTopByAllBtn'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                console.log("k....."+k)
                $(this).find('input.ayear').prop('name','ayear'+k);
                $(this).find('input.aabortNum').prop('name','aabortNum'+k);
            });
            //display add more
            if(aLength <= 1){
                $('.topByDrugandSurgicalProcedure:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }
</script>




