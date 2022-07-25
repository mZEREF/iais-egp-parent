<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>

<c:set var="isRfi" value="${not empty requestInformationConfig}"/>

<div class="row form-horizontal">

    <iais:row>
        <div class="col-xs-12">
            <p class="app-title">MedAlert Person</p>
            <p>A MedAlert Person is appointed by the licensee to receive medical alert notifications and circulars issued by MOH.</p>
            <hr>
        </div>
    </iais:row>

    <c:if test="${AppSubmissionDto.needEditController }">
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            <iais:row>
                <div class="text-right app-font-size-16">
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
            <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
        </div>
    </iais:row>

    <c:choose>
        <c:when test="${AppSvcMedAlertPsn.size()>mandatoryCount}">
            <c:set var="pageLength" value="${AppSvcMedAlertPsn.size()}"/>
        </c:when>
        <c:otherwise>
            <c:set var="pageLength" value="${mandatoryCount}"/>
        </c:otherwise>
    </c:choose>

    <c:set var="editControl" value="${(!empty AppSvcMedAlertPsn && AppSubmissionDto.needEditController) || !AppSubmissionDto.needEditController}" />

    <c:if test="${pageLength >0 && editControl}">
        <c:forEach begin="0" end="${pageLength-1}" step="1" varStatus="status">
            <c:set var="index" value="${status.index}"/>
            <c:set var="medAlertPsn" value="${AppSvcMedAlertPsn[index]}"/>

            <%@include file="medAlertDetail.jsp" %>
        </c:forEach>
    </c:if>

    <c:if test="${requestInformationConfig==null}">
        <c:choose>
            <c:when test="${!empty AppSvcMedAlertPsn }">
                <c:set var="mapDtoLength" value="${AppSvcMedAlertPsn.size()}"/>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${AppSubmissionDto.needEditController}">
                        <c:set var="mapDtoLength" value="0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="mapDtoLength" value="${mapHcsaSvcPersonnel.mandatoryCount}"/>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <c:set var="needAddPsn" value="true"/>
        <c:choose>
            <c:when test="${mapHcsaSvcPersonnel.status =='CMSTAT003'}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${mapDtoLength >= mapHcsaSvcPersonnel.maximumCount}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
        </c:choose>
        <div class="col-md-12 col-xs-12 <c:if test="${!needAddPsn}">hidden</c:if>" id="addPsnDiv">
            <span id="addMapBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another MedAlert Person</span>
        </div>
    </c:if>
</div>

<script>
    var init;
    $(function () {
        //init start
        init = 0;
        initDelBtn();
        addMAP();
        assignSel();
        mapDel();
        retrieveData();
        $('select.assignSel').trigger('change');
        $('input[name="licPerson"]').each(function (k,v) {
            if('1' == $(this).val()){
                var $currentPsn = $(this).closest('.medAlertContent').find('.medAlertPerson');
                disabledPartPage($currentPsn);
            }
        });
        initNationality('div.medAlertContent', '.idType', '.nationalityDiv');
        if(${AppSubmissionDto.needEditController && !isClickEdit}){
            disabledPage();
        }
        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        if('APTY002' == appType && '0' == rfiObj){
            <c:choose>
                <c:when test="${!empty AppSvcMedAlertPsn}">
                    <c:set var="psnLength" value="${AppSvcMedAlertPsn.size()-1}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="psnLength" value="0"/>
                </c:otherwise>
            </c:choose>
            <c:forEach begin="0" end="${psnLength}" step="1" varStatus="stat">
                var $currentPsn = $('.medAlertContent').eq(${stat.index+1}).find('.medAlertPerson');
                $currentPsn.find('input[type="text"]').css('border-color','');
                $currentPsn.find('input[type="text"]').css('color','');
                var psnDto = {};
                <c:if test="${!empty AppSvcMedAlertPsn[stat.index].psnEditFieldStr}">
                    psnDto = ${AppSvcMedAlertPsn[stat.index].psnEditFieldStr};
                </c:if>
            var a="${AppSvcMedAlertPsn[stat.index]}";
            console.log(a);
            </c:forEach>
        }
        //init end
        init =1;
        if($("#errorMapIs").val()=='error'){
            $('.edit').trigger('click');
        }
    })

    var initDelBtn=function () {
        $('.medAlertContent:first .mapDelBtn').hide();
    }

    var mapDel = function () {
        $('.mapDelBtn').click(function () {
            $(this).closest('div.medAlertContent').remove();
            changePsnItem();
            //show add more
            var psnLength = $('.medAlertContent').length;
            if(psnLength <'${mapHcsaSvcPersonnel.maximumCount}'){
                $('#addPsnDiv').removeClass('hidden');
            }
            if(psnLength <= 1){
                $('.assign-psn-item:eq(0)').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

    var assignSel= function () {
        $('.assignSel').unbind('change');
        $('.assignSel').on('change', function() {
            showWaiting();
            var assignVal = $(this).val();
            var $currContent = $(this).closest('div.medAlertContent');
            $currContent.find('.assignSelVal').val(assignVal);
            checkPersonContent($currContent, false, false);
        });
    }

    function checkPersonContent($currContent, onlyInit, fromUser, callback) {
        var assignVal = $currContent.find('.assignSelVal').val();
        var $content = $currContent.find('div.medAlertPerson');
        if('-1' == assignVal || isEmpty(assignVal)) {
            hideTag($content);
            $content.find('.idType').trigger('change');
            $currContent.find('input.licPerson').val('0');
            dismissWaiting();
        } else if('newOfficer' == assignVal) {
            showTag($content);
            if (init) {
                clearFields($content);
            }
            unDisableContent($content);
            $content.find('.idType').trigger('change');
            $currContent.find('input.licPerson').val('0');
            dismissWaiting();
        }else {
            showTag($content);
            if (onlyInit) {
                checkPersonDisabled($currContent, true);
                dismissWaiting();
                return;
            }
            var url = "/person-info";
            if (fromUser) {
                url = "/user-account-info";
            }
            var indexNo = $currContent.find('input.indexNo').val();
            var arr = assignVal.split(',');
            var nationality = arr[0];
            var idType = arr[1];
            var idNo = arr[2];
            var jsonData = {
                'nationality':nationality,
                'idType':idType,
                'idNo':idNo,
                'indexNo':indexNo
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}' + url,
                'dataType':'json',
                'data':jsonData,
                'type':'GET',
                'success':function (data) {
                    console.log(data);
                    if (data == null) {
                        clearFields($content);
                        return;
                    }
                    if (typeof callback === 'function') {
                        callback($currContent, data);
                    } else {
                        var cntClass = $currContent.attr('class');
                        fillForm($content, data, "", $('div.' + cntClass).index($currContent));
                        $currContent.find('input.licPerson').val(data.licPerson ? 1 : 0);
                        $currContent.find('input.isPartEdit').val(1);
                        $currContent.find('input.indexNo').val(data.indexNo);
                        $currContent.find('input.psnEditField').val(data.psnEditFieldStr);
                        checkPersonDisabled($currContent);
                    }
                    dismissWaiting();
                },
                'error':function () {
                    dismissWaiting();
                }
            });
        }
    }

    function checkPersonDisabled($currContent, onlyInit) {
        let data;
        try {
            data = $.parseJSON($currContent.find('.psnEditField').val());
        } catch (e) {
            data = {};
        }

        if ('1' == $currContent.find('.licPerson').val()) {
            $.each(data, function (i, val) {
                let $input = $currContent.find('.' + i + ':input');
                if ($input.length > 0 && !val) {
                    disableContent($input);
                }
            });
        } else if (!onlyInit) {
            $.each(data, function (i, val) {
                let $input = $currContent.find('.' + i + ':input');
                if ($input.length > 0) {
                    unDisableContent($input);
                }
            });
        }
    }

    var addMAP = function(){
        $('#addMapBtn').unbind('click');
        $('#addMapBtn').click(function () {
            showWaiting();
            var $target = $('div.medAlertContent:last');
            var src = $target.clone();
            src.find('.mapDelBtn').show();
            src.find('div.medAlertPerson').addClass('hidden');
            clearFields(src);
            $('.medAlertContent:last').after(src);
            var $currContent = $('div.medAlertContent:last').find('div.medAlertPerson');
            assignSel();
            mapDel();
            changePsnItem();
            retrieveData();
            var psnLength = $('.medAlertContent').length;
            if(psnLength >='${mapHcsaSvcPersonnel.maximumCount}'){
                $('#addPsnDiv').addClass('hidden');
            }
            if(psnLength <='${mapHcsaSvcPersonnel.mandatoryCount}'){
                $('.medAlertContent:last .mapDelBtn').remove();
            }
            resetIndex($currContent, psnLength-1);
            $('#isEditHiddenVal').val('1');
            initNationality('div.medAlertContent:last', '.idType', '.nationalityDiv');
            dismissWaiting();
        });
    }

    var changePsnItem = function () {
        $('.assign-psn-item').each(function (k, v) {
            $(this).html(k + 1);
        });
    };

    $('.edit').click(function () {

        var $contentEle = $(this).closest('div.medAlertContent');
        $contentEle.find('input[name="isPartEdit"]').val('1');
        $contentEle.find('.edit-content').addClass('hidden');
        $contentEle.find('input[type="text"]').prop('disabled',false);
        $contentEle.find('div.nice-select').removeClass('disabled');
        $contentEle.find('input[type="text"]').css('border-color','');
        $contentEle.find('input[type="text"]').css('color','');
        $contentEle.find('.description').prop('disabled',false);
        //get data from page
        $contentEle.find('select[name="assignSel"] option[value="newOfficer"]').prop('selected',true);
        var mapSelectVal = $contentEle.find('select[name="assignSel"]').val();
        if('-1' != mapSelectVal && '' != mapSelectVal){
            $contentEle.find('select[name="assignSel"] option[value="newOfficer"]').prop('selected',true);
        }
        $('#isEditHiddenVal').val('1');
    });

    var retrieveData = function () {
        $('.idNoVal').blur(function () {
            var $mapContentEle = $(this).closest('div.medAlertContent');
            var idNo = $(this).val();
            var idType = $mapContentEle.find('select[name="idType"]').val();
            var nationality = $mapContentEle.find('select[name="nationality"]').val();
            if(idNo == '' || idType == ''){
                return;
            }
            var data = {
                'nationality':nationality,
                'idNo':idNo,
                'idType':idType
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}/user-account-info',
                'dataType':'json',
                'data':data,
                'type':'POST',
                'success':function (data) {
                    console.log("suc");
                    if(data != null ) {
                        console.log(data);
                        if(data.resCode == '200'){
                            $mapContentEle.find('input[name="loadingType"]').val('PLT002');
                            fillDataByBlur($mapContentEle,data.resultJson);
                            unDisableContent($mapContentEle.find('input[name="idNo"]'));
                            unDisableContent($mapContentEle.find('input[name="idType"]'));
                            unDisableContent($mapContentEle.find('input[name="nationality"]'));
                        }else{
                            unDisabledPartPage($mapContentEle);
                            $mapContentEle.find('input[name="loadingType"]').val('');
                        }
                    }
                },
                'error':function (data) {
                    console.log("err");
                }
            });
        });
    }

    var fillDataByBlur = function ($mapContentEle,data) {
        var name = data.name;
        if(name != '' && name != null && name != 'undefined'){
            $mapContentEle.find('input[name="name"]').val(name);
        }
        var mobileNo = data.mobileNo;
        if(mobileNo != '' && mobileNo != null && mobileNo != 'undefined'){
            $mapContentEle.find('input[name="mobileNo"]').val(data.mobileNo);
        }
        var officeTelNo = data.officeTelNo;
        if(officeTelNo != '' && officeTelNo != null && officeTelNo != 'undefined'){
            $mapContentEle.find('input[name="officeTelNo"]').val(data.officeTelNo);
        }
        var emailAddr = data.emailAddr;
        if(emailAddr != '' && emailAddr != null && emailAddr != 'undefined'){
            $mapContentEle.find('input[name="emailAddress"]').val(data.emailAddr);
        }

        var salutation = data.salutation;
        if(salutation != null || salutation !='undefined' || salutation != ''){
            $mapContentEle.find('select[name="salutation"]').val(salutation);
            var salutationVal = $mapContentEle.find('option[value="' + salutation + '"]').html();
            $mapContentEle.find('select[name="salutation"]').next().find('.current').html(salutationVal);
        }

        var designation = data.designation;
        if(designation != null || designation !='undefined' || designation != ''){
            $mapContentEle.find('select[name="designation"]').val(designation);
            var designationVal = $mapContentEle.find('option[value="' + designation + '"]').html();
            $mapContentEle.find('select[name="designation"]').next().find('.current').html(designationVal);
        }

        var description = data.description;
        if(description != null && description !='undefined' && description != ''){
            $mapContentEle.find('input[name="description"]').val(data.description);
        }
        $mapContentEle.find('input[name="licPerson"]').val('1');
        $mapContentEle.find('input[name="existingPsn"]').val('1');
    }
</script>