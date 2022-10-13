<iais:row cssClass="row control control-caption-horizontal holdPregnancyDiv">
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Do you provide Termination of Pregnancy&nbsp;"/>
    <input type="hidden" class="provideTopVal" name="${prefix}provideTopVal" value="${provideTop}"/>
    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input provideTop" <c:if test="${'1' == provideTop}">checked="checked"</c:if>  type="radio" name="${prefix}provideTop" value = "1" aria-invalid="false" onclick="m('${prefix}','1');">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </iais:value>

    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input provideTop" <c:if test="${'0' == provideTop}">checked="checked"</c:if>  type="radio" name="${prefix}provideTop" value = "0" aria-invalid="false" onclick="m('${prefix}','0');">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </iais:value>
</iais:row>

<iais:row cssClass="row control control-caption-horizontal">
    <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
    <iais:value width="7" cssClass="col-md-7 col-xs-12">
        <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}provideTop"></span>
    </iais:value>
</iais:row>

<div class="allOtherInformation" data-prefix="${prefix}">
    <div class="topt <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}">
        <iais:row cssClass="row control control-caption-horizontal">
            <iais:field width="12" cssClass="col-md-12 " mandatory="true" value="Please indicate&nbsp;"/>
        </iais:row>
        <iais:row cssClass="row">
            <input type="hidden" class="topTypeVal" name="topTypeVal" value="${appSvcOtherInfoTop.topType}"/>
            <iais:value width="4" cssClass="form-check col-md-4">
                <input class="form-check-input topType " <c:if test="${'1' == appSvcOtherInfoTop.topType}">checked="checked"</c:if>  type="radio" name="${prefix}topType" value = "1" aria-invalid="false" onclick="getTopTypeValue('${prefix}','1');">
                <label class="form-check-label" ><span class="check-circle"></span>Termination of Pregnancy(Solely by Drug)</label>
            </iais:value>

            <iais:value width="4" cssClass="form-check col-md-4">
                <input class="form-check-input topType" <c:if test="${'0' == appSvcOtherInfoTop.topType}">checked="checked"</c:if>  type="radio" name="${prefix}topType" value = "0" aria-invalid="false" onclick="getTopTypeValue('${prefix}','0');">
                <label class="form-check-label" ><span class="check-circle"></span>Termination of Pregnancy(Solely by Surgical Procedure)</label>
            </iais:value>

            <iais:value width="4" cssClass="form-check col-md-4">
                <input class="form-check-input topType" <c:if test="${'-1' == appSvcOtherInfoTop.topType}">checked="checked"</c:if>  type="radio" name="${prefix}topType" value = "-1" aria-invalid="false" onclick="getTopTypeValue('${prefix}','-1');">
                <label class="form-check-label" ><span class="check-circle"></span>Termination of Pregnancy(Drug and Surgical Procedure)</label>
            </iais:value>
        </iais:row>

        <iais:row cssClass="row control control-caption-horizontal">
            <iais:field width="5" mandatory="" value=""/>
            <iais:value width="7" cssClass="col-md-7 col-xs-12">
                <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_${prefix}topType"></span>
            </iais:value>
        </iais:row>
    </div>



    <%--TODO...practitioners--%>
    <c:choose>
        <c:when test="${empty practitioners}">
            <c:set var="personCount" value="1"/>
        </c:when>
        <c:otherwise>
            <c:set var="personCount" value="${practitioners.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" class="cdLength" name="${prefix}cdLength" value="${personCount}" data-prefix="${prefix}"/>
    <c:forEach begin="0" end="${personCount-1}" step="1" varStatus="pStat">
        <c:set var="index" value="${pStat.index}" />
        <c:set var="person" value="${practitioners[index]}"/>
        <%@include file="practitionersDetail.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addPractitionersDiv <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}">
        <span class="addPractitionersBtn" style="color:deepskyblue;cursor:pointer;">
            <span style="">Add more</span>
        </span>
    </div>


    <%--TODO...anaesthetists--%>
    <c:choose>
        <c:when test="${empty anaesthetists}">
            <c:set var="aCount" value="1"/>
        </c:when>
        <c:otherwise>
            <c:set var="aCount" value="${anaesthetists.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" class="anaLength" name="${prefix}anaLength" value="${aCount}" data-prefix="${prefix}"/>
    <c:forEach begin="0" end="${aCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${anaesthetists[index]}"/>
        <%@include file="anaesthetistsDetail.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addAnaesthetistsDiv <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}">
            <span class="addAnaesthetistsBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">Add more</span>
            </span>
    </div>

    <%--TODO...nurses--%>
    <c:choose>
        <c:when test="${empty nurses}">
            <c:set var="nCount" value="1"/>
        </c:when>
        <c:otherwise>
            <c:set var="nCount" value="${nurses.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" class="nLength" name="${prefix}nLength" value="${nCount}" data-prefix="${prefix}"/>
    <c:forEach begin="0" end="${nCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${nurses[index]}"/>
        <%@include file="nursesDetail.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addNursesDiv <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}">
        <span class="addNursesBtn" style="color:deepskyblue;cursor:pointer;">
            <span style="">Add more</span>
        </span>
    </div>

    <%--TODO...counsellors--%>
    <c:choose>
        <c:when test="${empty counsellors}">
            <c:set var="cCount" value="1"/>
        </c:when>
        <c:otherwise>
            <c:set var="cCount" value="${counsellors.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" class="cLength" name="${prefix}cLength" value="${cCount}" data-prefix="${prefix}"/>
    <c:forEach begin="0" end="${cCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${counsellors[index]}"/>
        <%@include file="counsellorsDetail.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addCounsellorsDiv <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}">
            <span class="addCounsellorsBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">Add more</span>
            </span>
    </div>

    <div class="lowt <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}">
        <iais:row cssClass="row">
            <iais:value width="5" cssClass="col-md-5">
                <label class="form-check-label" style="padding-top: 25px;">My counsellor(s) has attended the TOP counselling refresher course (Please upload the certificates in the document page)
                    <span class="mandatory">*</span>
                </label>
            </iais:value>
            <input type="hidden" class="hasConsuAttendCourseVal" name="${prefix}hasConsuAttendCourseVal" value="${appSvcOtherInfoTop.hasConsuAttendCourse}"/>
            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input hasConsuAttendCourse" <c:if test="${'1' == appSvcOtherInfoTop.hasConsuAttendCourse}">checked="checked"</c:if>  type="radio" name="${prefix}hasConsuAttendCourse" value = "1" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
            </iais:value>

            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input hasConsuAttendCourse" <c:if test="${'0' == appSvcOtherInfoTop.hasConsuAttendCourse}">checked="checked"</c:if>  type="radio" name="${prefix}hasConsuAttendCourse" value = "0" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </iais:value>
        </iais:row>

        <iais:row cssClass="row control control-caption-horizontal">
            <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
            <iais:value width="7" cssClass="col-md-7 col-xs-12">
                <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}hasConsuAttendCourse"></span>
            </iais:value>
        </iais:row>

        <iais:row cssClass="row">
            <iais:value width="5" cssClass="col-md-5">
                <label class="form-check-label" style="padding-top: 25px;">The service provider has the necessary counselling facilities e.g. TV set, video player, video on abortion produced by HPB in different languages and the pamphlets produced by HPB
                    <span class="mandatory">*</span>
                </label>
            </iais:value>
            <input type="hidden" class="isProvideHpbVal" name="${prefix}isProvideHpbVal" value="${appSvcOtherInfoTop.isProvideHpb}"/>
            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input isProvideHpb" <c:if test="${'1' == appSvcOtherInfoTop.isProvideHpb}">checked="checked"</c:if>  type="radio" name="${prefix}isProvideHpb" value = "1" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
            </iais:value>

            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input isProvideHpb" <c:if test="${'0' == appSvcOtherInfoTop.isProvideHpb}">checked="checked"</c:if>  type="radio" name="${prefix}isProvideHpb" value = "0" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </iais:value>
        </iais:row>
        <iais:row cssClass="row control control-caption-horizontal">
            <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
            <iais:value width="7" cssClass="col-md-7 col-xs-12">
                <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}isProvideHpb"></span>
            </iais:value>
        </iais:row>
    </div>
</div>
<script>
    $(document).ready(function () {
        medAuthByMoh();

        addPractitioners();
        removePractitioners();

        addAnaesthetistsBtn();
        removeAnaesthetists();

        addNursesBtn();
        removeNurses();

        addCounsellorsBtn();
        removeCounsellors();
    });

    function m(prefix,value){
        if (value == 1){
            $('input[name="t"]').val(1);
            console.log("input.name.ttt:"+$('input[name="t"]').val());
            $('div.topt[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.practitioners[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.addPractitionersDiv[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.anaesthetists[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.addAnaesthetistsDiv[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.nurses[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.addNursesDiv[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.counsellors[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.addCounsellorsDiv[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.lowt[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.docTop[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.de[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.oitem[data-prefix="' + prefix + '"]').removeClass("hidden");
            topAboutHAS(prefix);
        }else {
            $('input[name="t"]').val(0);
            console.log("input.name.t:"+$('input[name="t"]').val());
            $('div.topt[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.practitioners[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addPractitionersDiv[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.anaesthetists[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addAnaesthetistsDiv[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.nurses[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addNursesDiv[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.counsellors[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addCounsellorsDiv[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.lowt[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.de[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.oitem[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.docTop[data-prefix="' + prefix + '"]').addClass("hidden");
            topAboutHAS(prefix);
        }

    }

    function getTopTypeValue(prefix,value){
        if (value == 1){
            $('div.topByDrug[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.topBySurgicalProcedure[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addTopByDrugDiv[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.addTopBySurgicalProcedureDiv[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addTopAllDiv[data-prefix="' + prefix + '"]').addClass("hidden");
        }else if (value == 0){
            $('div.topByDrug').addClass("hidden");
            $('div.topBySurgicalProcedure[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addTopByDrugDiv[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addTopBySurgicalProcedureDiv[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.addTopAllDiv[data-prefix="' + prefix + '"]').addClass("hidden");
        }else if (value == -1){
            $('div.topByDrug[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.topBySurgicalProcedure[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.addTopByDrugDiv[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.addTopBySurgicalProcedureDiv[data-prefix="' + prefix + '"]').removeClass("hidden");
            $('div.addTopAllDiv[data-prefix="' + prefix + '"]').removeClass("hidden");
        }else {
            $('div.topByDrug[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.topBySurgicalProcedure[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addTopByDrugDiv[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addTopBySurgicalProcedureDiv[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addTopAllDiv[data-prefix="' + prefix + '"]').addClass("hidden");
        }
    }

    function topAboutHAS(prefix){
        let m = $('input[name="t"]').val();
        if (m==0){
            $('div.topByDrug[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.topBySurgicalProcedure[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addTopByDrugDiv[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addTopBySurgicalProcedureDiv[data-prefix="' + prefix + '"]').addClass("hidden");
            $('div.addTopAllDiv[data-prefix="' + prefix + '"]').addClass("hidden");
        }else {
            getTopTypeValue(prefix,m);
        }
    }


    let medAuthByMoh = function() {
        $('.medAuthByMoh').unbind('click');
        $('.medAuthByMoh').click(function () {
            let medAuthByMohVal = $(this).val();
            console.log("medAuthByMohVal"+medAuthByMohVal);
            $('input[name="medAuthByMohVal"]').val(medAuthByMohVal);
        });
    };

    <%--Practitioners--%>
    function addPractitioners(){
        refresh();
        $('.addPractitionersBtn').unbind('click');
        $('.addPractitionersBtn').click(function () {
            showWaiting();
            if (${AppSubmissionDto.needEditController }){
                $('a.otherInfoEdit').trigger('click');
            }
            let $tag = $(this);
            let $target = $tag.closest('.addPractitionersDiv');
            let prefix = $target.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            console.log(prefix)
            let target = $('div.practitioners[data-prefix="' + prefix + '"]').first();
            let src = target.clone();
            $('div.addPractitionersDiv[data-prefix="' + prefix + '"]').before(src);
            let cdLength = $('div.practitioners[data-prefix="' + prefix + '"]').length;
            $('input.cdLength[data-prefix="' + prefix + '"]').val(cdLength);
            let $currContent = $('div.practitioners[data-prefix="' + prefix + '"]').last();
            clearFields($currContent);
            removePractitioners();
            if(cdLength <= 1){
                console.log("init.........")
                $('.practitioners[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('.practitioners[data-prefix="' + prefix + '"]').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removePractitionersBtn[data-prefix="' + prefix + '"]'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                console.log("k....."+k)
                $(this).find('input.psnType').prop('name',prefix+'psnType'+k);
                $('input.psnType[data-prefix="' + prefix + '"]').val("practitioners");
                $(this).find('input.profRegNo').prop('name',prefix+'profRegNo'+k);
                $(this).find('input.name').prop('name',prefix+'name'+k);
                $(this).find('input.idNo').prop('name',prefix+'idNo'+k);
                $(this).find('input.regType').prop('name',prefix+'regType'+k);
                $(this).find('input.qualification').prop('name',prefix+'qualification'+k);
                $(this).find('input.isMedAuthByMoh').prop('name',prefix+'isMedAuthByMoh'+k);
                $(this).find('input.speciality').prop('name',prefix+'speciality'+k);
            });
           dismissWaiting();
        })
    }

    function refresh(){
        //reset number
        $('.practitioners').each(function (k,v) {
            console.log("k....."+k);
            toggleTag($(this).find('div.removePractitionersBtn'), k != 0);
        });
        $('#isEditHiddenVal').val('1');
    }

    let removePractitioners = function () {
        $('.removePractitionersBtn').unbind('click');
        $('.removePractitionersBtn').click(function () {
            let $v = $(this);
            let $tag = $v.closest('.removePDiv');
            let prefix = $tag.data('prefix');
            console.log(prefix)
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $(this).closest('div.practitioners[data-prefix="' + prefix + '"]').remove();
            let cdLength = $('div.practitioners[data-prefix="' + prefix + '"]').length;
            $('input.cdLength[data-prefix="' + prefix + '"]').val(cdLength);
            //reset number
            $('div.practitioners[data-prefix="' + prefix + '"]').each(function (k,v) {
                console.log("k....."+k)
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.psnType').prop('name',prefix+'psnType'+k);
                $(this).find('input.profRegNo').prop('name',prefix+'profRegNo'+k);
                $(this).find('input.profRegNo').prop('id',prefix+'profRegNo'+k);
                $(this).find('input.name').prop('name',prefix+'name'+k);
                $(this).find('input.idNo').prop('name',prefix+'idNo'+k);
                $(this).find('input.regType').prop('name',prefix+'regType'+k);
                $(this).find('input.qualification').prop('name',prefix+'qualification'+k);
                $(this).find('input.specialties').prop('name',prefix+'specialties'+k);
                $(this).find('input.holdMPA').prop('name',prefix+'holdMPA'+k);
            });
            //display add more
            if(cdLength <= 1){
                $('.practitioners[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }
    <%--Anaesthetists--%>
    function addAnaesthetistsBtn(){
        refreshAnaesthetists();
        $('.addAnaesthetistsBtn').unbind('click');
        $('.addAnaesthetistsBtn').click(function () {
            showWaiting();
            if (${AppSubmissionDto.needEditController }){
                $('a.otherInfoEdit').trigger('click');
            }
            let $tag = $(this);
            let $target = $tag.closest('.addAnaesthetistsDiv');
            let prefix = $target.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            console.log(prefix)
            let target = $('div.anaesthetists[data-prefix="' + prefix + '"]').first();
            let src = target.clone();
            $('div.addAnaesthetistsDiv[data-prefix="' + prefix + '"]').before(src);
            let anaLength = $('.anaesthetists[data-prefix="' + prefix + '"]').length;
            $('input.anaLength[data-prefix="' + prefix + '"]').val(anaLength);
            let $currContent = $('div.anaesthetists[data-prefix="' + prefix + '"]').last();
            clearFields($currContent);
            removeAnaesthetists();
            if(anaLength <= 1){
                console.log("init.........")
                $('.anaesthetists[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('.anaesthetists[data-prefix="' + prefix + '"]').each(function (k,v) {
                toggleTag($(this).find('div.removeAnaesthetistsBtn[data-prefix="' + prefix + '"]'), k != 0);
                console.log("k...."+k);
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.apsnType').prop('name',prefix+'apsnType'+k);
                $('input.apsnType[data-prefix="' + prefix + '"]').val("anaesthetists");
                $(this).find('input.aprofRegNo').prop('aname',prefix+'profRegNo'+k);
                $(this).find('input.aname').prop('name',prefix+'aname'+k);
                $(this).find('input.idANo').prop('name',prefix+'idANo'+k);
                $(this).find('input.aregType').prop('name',prefix+'aregType'+k);
                $(this).find('input.aqualification').prop('name',prefix+'aqualification'+k);
            });
            dismissWaiting();
        })
    }

    function refreshAnaesthetists(){
        //reset number
        $('.anaesthetists').each(function (k,v) {
            toggleTag($(this).find('div.removeAnaesthetistsBtn'), k != 0);
        });
        $('#isEditHiddenVal').val('1');
    }

    let removeAnaesthetists = function () {
        $('.removeAnaesthetistsBtn').unbind('click');
        $('.removeAnaesthetistsBtn').click(function () {
            let $tag = $(this);
            let $target = $tag.closest('.removeADiv');
            let prefix = $target.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $(this).closest('div.anaesthetists[data-prefix="' + prefix + '"]').remove();
            let anaLength = $('.anaesthetists[data-prefix="' + prefix + '"]').length;
            $('input.anaLength[data-prefix="' + prefix + '"]').val(anaLength);
            //reset number
            $('div.anaesthetists[data-prefix="' + prefix + '"]').each(function (k,v) {
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.apsnType').prop('name',prefix+'apsnType'+k);
                $(this).find('input.aprofRegNo').prop('aname',prefix+'profRegNo'+k);
                $(this).find('input.aname').prop('name',prefix+'aname'+k);
                $(this).find('input.idANo').prop('name',prefix+'idANo'+k);
                $(this).find('input.aregType').prop('name',prefix+'aregType'+k);
                $(this).find('input.aqualification').prop('name',prefix+'aqualification'+k);
            });
            //display add more
            if(anaLength <= 1){
                $('.anaesthetists[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

    <%--nurses--%>
    function addNursesBtn(){
        refreshNurses();
        $('.addNursesBtn').unbind('click');
        $('.addNursesBtn').click(function () {
            showWaiting();
            if (${AppSubmissionDto.needEditController }){
                $('a.otherInfoEdit').trigger('click');
            }
            let $tag = $(this);
            let $target = $tag.closest('.addNursesDiv');
            let prefix = $target.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            console.log(prefix)
            let target = $('div.nurses[data-prefix="' + prefix + '"]').first();
            let src = target.clone();
            $('div.addNursesDiv[data-prefix="' + prefix + '"]').before(src);
            let nLength = $('.nurses[data-prefix="' + prefix + '"]').length;
            $('input.nLength[data-prefix="' + prefix + '"]').val(nLength);
            let $currContent = $('div.nurses[data-prefix="' + prefix + '"]').last();
            clearFields($currContent);
            removeNurses();
            if(nLength <= 1){
                console.log("init.........")
                $('.nurses[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('.nurses[data-prefix="' + prefix + '"]').each(function (k,v) {
                toggleTag($(this).find('div.removeNursesBtn[data-prefix="' + prefix + '"]'), k != 0);
                console.log("k...."+k);
                $(this).find('.assign-psn-item').html(k+1);
                $('input.npsnType[data-prefix="' + prefix + '"]').val("nurses");
                $(this).find('input.npsnType').prop('name',prefix+'npsnType'+k);
                $(this).find('input.nname').prop('name',prefix+'nname'+k);
                $(this).find('input.nqualification').prop('name',prefix+'nqualification'+k);
            });

            dismissWaiting();
        })
    }

    function refreshNurses(){
        //reset number
        $('.nurses').each(function (k,v) {
            toggleTag($(this).find('div.removeNursesBtn'), k != 0);
        });
        $('#isEditHiddenVal').val('1');
    }

    let removeNurses = function () {
        $('.removeNursesBtn').unbind('click');
        $('.removeNursesBtn').click(function () {
            let $tag = $(this);
            let $target = $tag.closest('.removeNDiv');
            let prefix = $target.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $(this).closest('div.nurses[data-prefix="' + prefix + '"]').remove();
            let nLength = $('.nurses[data-prefix="' + prefix + '"]').length;
            $('input.nLength[data-prefix="' + prefix + '"]').val(nLength);
            //reset number
            $('div.nurses[data-prefix="' + prefix + '"]').each(function (k,v) {
                console.log("k....."+k)
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.npsnType').prop('name',prefix+'npsnType'+k);
                $(this).find('input.nname').prop('name',prefix+'nname'+k);
                $(this).find('input.nqualification').prop('name',prefix+'nqualification'+k);
            });
            if(nLength <= 1){
                $('.nurses[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

    <%--counsellors--%>
    function addCounsellorsBtn(){
        refreshCounsellors();
        $('.addCounsellorsBtn').unbind('click');
        $('.addCounsellorsBtn').click(function () {
            showWaiting();
            if (${AppSubmissionDto.needEditController }){
                $('a.otherInfoEdit').trigger('click');
            }
            let $tag = $(this);
            let $target = $tag.closest('.addCounsellorsDiv');
            let prefix = $target.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            console.log(prefix)
            let target = $('div.counsellors[data-prefix="' + prefix + '"]').first();
            let src = target.clone();
            $('div.addCounsellorsDiv[data-prefix="' + prefix + '"]').before(src);
            let cLength = $('.counsellors[data-prefix="' + prefix + '"]').length;
            $('input.cLength[data-prefix="' + prefix + '"]').val(cLength);
            let $currContent = $('div.counsellors[data-prefix="' + prefix + '"]').last();
            clearFields($currContent);
            removeCounsellors();
            if(cLength <= 1){
                console.log("init.........")
                $('.counsellors[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('.counsellors[data-prefix="' + prefix + '"]').each(function (k,v) {
                toggleTag($(this).find('div.removeBtn[data-prefix="' + prefix + '"]'), k != 0);
                console.log("k...."+k);
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.cpsnType').prop('name',prefix+'cpsnType'+k);
                $('input.cpsnType[data-prefix="' + prefix + '"]').val("counsellors");
                $(this).find('input.cname').prop('name',prefix+'cname'+k);
                $(this).find('input.cidNo').prop('name',prefix+'cidNo'+k);
                $(this).find('input.cqualification').prop('name',prefix+'cqualification'+k);
            });
            dismissWaiting();
        })
    }
    function refreshCounsellors(){
        //reset number
        $('.counsellors').each(function (k,v) {
            toggleTag($(this).find('div.removeBtn'), k != 0);
        });
        $('#isEditHiddenVal').val('1');
    }

    let removeCounsellors = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            let $tag = $(this);
            let $target = $tag.closest('.removeCODiv');
            let prefix = $target.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $(this).closest('div.counsellors[data-prefix="' + prefix + '"]').remove();
            let cLength = $('.counsellors[data-prefix="' + prefix + '"]').length;
            $('input.cLength[data-prefix="' + prefix + '"]').val(cLength);
            //reset number
            $('div.counsellors[data-prefix="' + prefix + '"]').each(function (k,v) {
                console.log("k....."+k)
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.cpsnType').prop('name',prefix+'cpsnType'+k);
                $(this).find('input.cprofRegNo').prop('name',prefix+'cprofRegNo'+k);
                $(this).find('input.cprofRegNo').prop('id',prefix+'cprofRegNo'+k);
                $(this).find('input.cname').prop('name',prefix+'cname'+k);
                $(this).find('input.cqualification').prop('name',prefix+'cqualification'+k);
            });
            if(cLength <= 1){
                $('.counsellors[data-prefix="' + prefix + '"]:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }
</script>