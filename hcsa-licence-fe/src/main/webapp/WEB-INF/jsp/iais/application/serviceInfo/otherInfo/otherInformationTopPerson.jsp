<iais:row cssClass="edit-content">
    <c:if test="${canEdit}">
        <div class="text-right app-font-size-16">
            <a class="edit psnEdit" href="javascript:void(0);">
                <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
            </a>
        </div>
    </c:if>
</iais:row>
<iais:row>
    <div class="col-xs-12 col-md-6">
        <p class="bold">Other Information</p>
    </div>
</iais:row>

<iais:row cssClass="row control control-caption-horizontal holdPregnancyDiv">
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Do you provide Termination of Pregnancy&nbsp;"/>
    <input type="hidden" class="provideTopVal" name="provideTopVal" value="${provideTop}"/>
    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input provideTop" <c:if test="${'1' == provideTop}">checked="checked"</c:if>  type="radio" name="provideTop" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </iais:value>

    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input provideTop" <c:if test="${'0' == provideTop}">checked="checked"</c:if>  type="radio" name="provideTop" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </iais:value>
</iais:row>

<iais:row cssClass="row control control-caption-horizontal">
    <iais:field width="5" mandatory="" value=""/>
    <iais:value width="7" cssClass="col-md-7 col-xs-12">
        <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_holdCerByEMS${index}"></span>
    </iais:value>
</iais:row>

<div class="allOtherInformation">

    <div class="topt <c:if test="${'1' != provideTop}">hidden</c:if>">
        <iais:row cssClass="row control control-caption-horizontal">
            <iais:field width="12" cssClass="col-md-12 " mandatory="true" value="Please indicate&nbsp;"/>
        </iais:row>

        <iais:row cssClass="row control control-caption-horizontal">
            <input type="hidden" class="topTypeVal" name="topTypeVal" value="${currSvcInfoDto.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}"/>
            <iais:value width="4" cssClass="form-check col-md-4">
                <input class="form-check-input topType " <c:if test="${'1' == currSvcInfoDto.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">checked="checked"</c:if>  type="radio" name="topType" value = "1" aria-invalid="false" onclick="getTopTypeValue(this)">
                <label class="form-check-label" ><span class="check-circle"></span>Termination of Pregnancy(Solely by Drug)</label>
            </iais:value>

            <iais:value width="4" cssClass="form-check col-md-4">
                <input class="form-check-input topType" <c:if test="${'0' == currSvcInfoDto.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">checked="checked"</c:if>  type="radio" name="topType" value = "0" aria-invalid="false" onclick="getTopTypeValue(this)">
                <label class="form-check-label" ><span class="check-circle"></span>Termination of Pregnancy(Solely by Surgical Procedure)</label>
            </iais:value>

            <iais:value width="4" cssClass="form-check col-md-4">
                <input class="form-check-input topType" <c:if test="${'-1' == currSvcInfoDto.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">checked="checked"</c:if>  type="radio" name="topType" value = "-1" aria-invalid="false" onclick="getTopTypeValue(this)">
                <label class="form-check-label" ><span class="check-circle"></span>Termination of Pregnancy(Drug and Surgical Procedure)</label>
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
    <input type="hidden" name="cdLength" value="${personCount}" />
    <c:forEach begin="0" end="${personCount-1}" step="1" varStatus="pStat">
        <c:set var="index" value="${pStat.index}" />
        <c:set var="person" value="${practitioners[index]}"/>
        <%@include file="practitionersDetail.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addPractitionersDiv <c:if test="${'1' != provideTop}">hidden</c:if>">
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
    <input type="hidden" name="anaLength" value="${aCount}" />
    <c:forEach begin="0" end="${aCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${anaesthetists[index]}"/>
        <%@include file="anaesthetistsDetail.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addAnaesthetistsDiv <c:if test="${'1' != provideTop}">hidden</c:if>">
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
    <input type="hidden" name="nLength" value="${nCount}" />
    <c:forEach begin="0" end="${nCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${nurses[index]}"/>
        <%@include file="nursesDetail.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addNursesDiv <c:if test="${'1' != provideTop}">hidden</c:if>">
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
    <input type="hidden" name="cLength" value="${cCount}" />
    <c:forEach begin="0" end="${cCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="person" value="${counsellors[index]}"/>
        <%@include file="counsellorsDetail.jsp" %>
    </c:forEach>
    <div class="col-md-12 col-xs-12 addCounsellorsDiv <c:if test="${'1' != provideTop}">hidden</c:if>">
            <span class="addCounsellorsBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">Add more</span>
            </span>
    </div>

    <div class="lowt <c:if test="${'1' != provideTop}">hidden</c:if>">
        <iais:row cssClass="row control control-caption-horizontal">
            <iais:value width="5" cssClass="col-md-5">
                <label class="form-check-label" style="padding-top: 25px;">My counsellor(s) has attended the TOP counselling refresher course (Please upload the certificates in the document page)
                    <span class="mandatory">*</span>
                </label>
            </iais:value>
            <input type="hidden" class="hasConsuAttendCourseVal" name="hasConsuAttendCourseVal" value="${appSvcOtherInfoTop.hasConsuAttendCourse}"/>
            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input hasConsuAttendCourse" <c:if test="${true == appSvcOtherInfoTop.hasConsuAttendCourse}">checked="checked"</c:if>  type="radio" name="hasConsuAttendCourse" value = "1" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
            </iais:value>

            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input hasConsuAttendCourse" <c:if test="${false == appSvcOtherInfoTop.hasConsuAttendCourse}">checked="checked"</c:if>  type="radio" name="hasConsuAttendCourse" value = "0" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </iais:value>
        </iais:row>

        <iais:row cssClass="row control control-caption-horizontal">
            <iais:value width="5" cssClass="col-md-5">
                <label class="form-check-label" style="padding-top: 25px;">The service provider has the necessary counselling facilities e.g. TV set, video player, video on abortion produced by HPB in different languages and the pamphlets produced by HPB
                    <span class="mandatory">*</span>
                </label>
            </iais:value>
            <input type="hidden" class="isProvideHpbVal" name="isProvideHpbVal" value="${appSvcOtherInfoTop.isProvideHpb}"/>
            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input isProvideHpb" <c:if test="${true == appSvcOtherInfoTop.isProvideHpb}">checked="checked"</c:if>  type="radio" name="isProvideHpb" value = "1" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
            </iais:value>

            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input isProvideHpb" <c:if test="${false == appSvcOtherInfoTop.isProvideHpb}">checked="checked"</c:if>  type="radio" name="isProvideHpb" value = "0" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
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
        let cdLength = $('.practitioners').length;
        refresh();
        $('.addPractitionersBtn').unbind('click');
        $('.addPractitionersBtn').click(function () {
            showWaiting();
            let target = $('div.practitioners:first');
            let src = target.clone();
            $('div.addPractitionersDiv').before(src);
            cdLength = cdLength + 1 ;
            $('input[name="cdLength"]').val(cdLength);
            let $currContent = $('div.practitioners:last');
            clearFields($currContent);
            removePractitioners();
            if(cdLength <= 1){
                console.log("init.........")
                $('.practitioners:eq(0) .assign-psn-item').html('');
            }
            $('.practitioners').each(function (k,v) {
                console.log("k....."+k);
                toggleTag($(this).find('div.removePractitionersBtn'), k != 0);
                $(this).find('.assign-psn-item').html(k+1);
                console.log("k....."+k)
                $(this).find('input.psnType').prop('name','psnType'+k);
                $('input[name="psnType"]').val("practitioners");
                $(this).find('input.profRegNo').prop('name','profRegNo'+k);
                $(this).find('input.name').prop('name','name'+k);
                $(this).find('input.idNo').prop('name','idNo'+k);
                $(this).find('input.regType').prop('name','regType'+k);
                $(this).find('input.qualification').prop('name','qualification'+k);
                $(this).find('input.medAuthByMoh').prop('name','medAuthByMoh'+k);
                $(this).find('input.specialties').prop('name','specialties'+k);
            });
            $('#isEditHiddenVal').val('1');
            dismissWaiting();
        })
    }

    function refresh(){
        let cdLength = $('.practitioners').length;
        $('input[name="cdLength"]').val(cdLength);
        //reset number
        $('.practitioners').each(function (k,v) {
            console.log("k....."+k);
            toggleTag($(this).find('div.removePractitionersBtn'), k != 0);

        });
    }

    let removePractitioners = function () {
        $('.removePractitionersBtn').unbind('click');
        $('.removePractitionersBtn').click(function () {
            $(this).closest('div.practitioners').remove();
            let cdLength = $('.practitioners').length;
            $('input[name="cdLength"]').val(cdLength);
            //reset number
            $('div.practitioners').each(function (k,v) {
                console.log("k....."+k)
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.psnType').prop('name','psnType'+k);
                $(this).find('input.profRegNo').prop('name','profRegNo'+k);
                $(this).find('input.profRegNo').prop('id','profRegNo'+k);
                $(this).find('input.name').prop('name','name'+k);
                $(this).find('input.idNo').prop('name','idNo'+k);
                $(this).find('input.regType').prop('name','regType'+k);
                $(this).find('input.qualification').prop('name','qualification'+k);
                $(this).find('input.specialties').prop('name','specialties'+k);
                $(this).find('input.holdMPA').prop('name','holdMPA'+k);
            });
            //display add more
            if(cdLength <= 1){
                $('.practitioners:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }
    <%--Anaesthetists--%>
    function addAnaesthetistsBtn(){
        let anaLength = $('.anaesthetists').length;
        refreshAnaesthetists();
        $('.addAnaesthetistsBtn').unbind('click');
        $('.addAnaesthetistsBtn').click(function () {
            showWaiting();
            let target = $('div.anaesthetists:first');
            let src = target.clone();
            $('div.addAnaesthetistsDiv').before(src);
            anaLength = anaLength + 1 ;
            $('input[name="anaLength"]').val(anaLength);
            let $currContent = $('div.anaesthetists:last');
            clearFields($currContent);
            removeAnaesthetists();
            if(anaLength <= 1){
                console.log("init.........")
                $('.anaesthetists:eq(0) .assign-psn-item').html('');
            }
            $('.anaesthetists').each(function (k,v) {
                toggleTag($(this).find('div.removeAnaesthetistsBtn'), k != 0);
                console.log("k...."+k);
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.apsnType').prop('name','apsnType'+k);
                $('input[name="apsnType"]').val("anaesthetists");
                $(this).find('input.aprofRegNo').prop('aname','profRegNo'+k);
                $(this).find('input.aname').prop('name','aname'+k);
                $(this).find('input.idANo').prop('name','idANo'+k);
                $(this).find('input.aregType').prop('name','aregType'+k);
                $(this).find('input.aqualification').prop('name','aqualification'+k);
            });
            $('#isEditHiddenVal').val('1');
            dismissWaiting();
        })
    }

    function refreshAnaesthetists(){
        let anaLength = $('.anaesthetists').length;
        $('input[name="anaLength"]').val(anaLength);
        //reset number
        $('.anaesthetists').each(function (k,v) {
            toggleTag($(this).find('div.removeAnaesthetistsBtn'), k != 0);
        });
    }

    let removeAnaesthetists = function () {
        $('.removeAnaesthetistsBtn').unbind('click');
        $('.removeAnaesthetistsBtn').click(function () {
            $(this).closest('div.anaesthetists').remove();
            let anaLength = $('.anaesthetists').length;
            $('input[name="anaLength"]').val(anaLength);
            //reset number
            $('div.anaesthetists').each(function (k,v) {
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.apsnType').prop('name','apsnType'+k);
                $('input[name="apsnType"]').val("anaesthetists");
                $(this).find('input.aprofRegNo').prop('aname','profRegNo'+k);
                $(this).find('input.aname').prop('name','aname'+k);
                $(this).find('input.idANo').prop('name','idANo'+k);
                $(this).find('input.aregType').prop('name','aregType'+k);
                $(this).find('input.aqualification').prop('name','aqualification'+k);
            });
            //display add more
            if(anaLength <= 1){
                $('.anaesthetists:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

    <%--nurses--%>
    function addNursesBtn(){
        let nLength = $('.nurses').length;
        refreshNurses();
        $('.addNursesBtn').unbind('click');
        $('.addNursesBtn').click(function () {
            showWaiting();
            let target = $('div.nurses:first');
            let src = target.clone();
            $('div.addNursesDiv').before(src);
            nLength = nLength + 1 ;
            $('input[name="nLength"]').val(nLength);
            let $currContent = $('div.nurses:last');
            clearFields($currContent);
            removeNurses();
            if(nLength <= 1){
                console.log("init.........")
                $('.nurses:eq(0) .assign-psn-item').html('');
            }
            $('.nurses').each(function (k,v) {
                toggleTag($(this).find('div.removeNursesBtn'), k != 0);
                console.log("k...."+k);
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.npsnType').prop('name','npsnType'+k);
                $(this).find('input.nname').prop('name','nname'+k);
                $(this).find('input.nqualification').prop('name','nqualification'+k);
            });
            $('#isEditHiddenVal').val('1');
            dismissWaiting();
        })
    }

    function refreshNurses(){
        let nLength = $('.nurses').length;
        $('input[name="nLength"]').val(nLength);
        //reset number
        $('.nurses').each(function (k,v) {
            toggleTag($(this).find('div.removeNursesBtn'), k != 0);
            $(this).find('input.npsnType').prop('name','npsnType'+k);
            $('input[name="npsnType"]').val("nurses");
            $(this).find('input.nname').prop('name','nname'+k);
            $(this).find('input.nqualification').prop('name','nqualification'+k);
        });
    }

    let removeNurses = function () {
        $('.removeNursesBtn').unbind('click');
        $('.removeNursesBtn').click(function () {
            $(this).closest('div.nurses').remove();
            let nLength = $('.nurses').length;
            $('input[name="nLength"]').val(nLength);
            //reset number
            $('div.nurses').each(function (k,v) {
                console.log("k....."+k)
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.npsnType').prop('name','npsnType'+k);
                $(this).find('input.nname').prop('name','nname'+k);
                $(this).find('input.nqualification').prop('name','nqualification'+k);
            });
            if(nLength <= 1){
                $('.nurses:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

    <%--counsellors--%>
    function addCounsellorsBtn(){
        let cLength = $('.counsellors').length;
        refreshCounsellors();
        $('.addCounsellorsBtn').unbind('click');
        $('.addCounsellorsBtn').click(function () {
            showWaiting();
            let target = $('div.counsellors:first');
            let src = target.clone();
            $('div.addCounsellorsDiv').before(src);
            cLength = cLength + 1 ;
            $('input[name="cLength"]').val(cLength);
            let $currContent = $('div.counsellors:last');
            clearFields($currContent);
            removeCounsellors();
            if(cLength <= 1){
                console.log("init.........")
                $('.counsellors:eq(0) .assign-psn-item').html('');
            }
            $('.counsellors').each(function (k,v) {
                toggleTag($(this).find('div.removeBtn'), k != 0);
                console.log("k...."+k);
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.cpsnType').prop('name','cpsnType'+k);
                $('input[name="cpsnType"]').val("counsellors");
                $(this).find('input.cname').prop('name','cname'+k);
                $(this).find('input.cidNo').prop('name','cidNo'+k);
                $(this).find('input.cqualification').prop('name','cqualification'+k);
            });
            $('#isEditHiddenVal').val('1');
            dismissWaiting();
        })
    }

    function refreshCounsellors(){
        let cLength = $('.counsellors').length;
        $('input[name="cLength"]').val(cLength);
        //reset number
        $('.counsellors').each(function (k,v) {
            toggleTag($(this).find('div.removeBtn'), k != 0);
        });
    }

    let removeCounsellors = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            $(this).closest('div.counsellors').remove();
            let cLength = $('.counsellors').length;
            $('input[name="cLength"]').val(cLength);
            //reset number
            $('div.counsellors').each(function (k,v) {
                console.log("k....."+k)
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('input.cpsnType').prop('name','cpsnType'+k);
                $(this).find('input.cprofRegNo').prop('name','cprofRegNo'+k);
                $(this).find('input.cprofRegNo').prop('id','cprofRegNo'+k);
                $(this).find('input.cname').prop('name','cname'+k);
                $(this).find('input.cqualification').prop('name','cqualification'+k);
            });
            if(cLength <= 1){
                $('.counsellors:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }
</script>