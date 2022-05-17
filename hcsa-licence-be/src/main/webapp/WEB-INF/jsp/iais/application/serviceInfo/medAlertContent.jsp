<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<style>
    .cursorPointer{
        cursor: pointer;
    }
</style>
<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>
<div class="row">
    <div class="col-xs-12">
        <p style="font-weight: 600;font-size: 2.2rem">MedAlert Person</p>
        <p>A MedAlert Person is appointed by the licensee to receive medical alert notifications and circulars issued by MOH.</p>
        <hr>
        <p><span class="error-msg" name="iaisErrorMsg" id="error_psnMandatory"></span></p>
        <div class="row">
            <c:if test="${AppSubmissionDto.needEditController }">
                <c:set var="isClickEdit" value="false"/>
                <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                    <c:if test="${'APPSPN08' == clickEditPage}">
                        <c:set var="isClickEdit" value="true"/>
                    </c:if>
                </c:forEach>
                <c:choose>
                    <c:when test="${'true' != isClickEdit && !('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
                        <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                    </c:when>
                    <c:otherwise>
                        <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
                    </c:otherwise>
                </c:choose>
                <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                    <div class="text-right app-font-size-16">
                        <a class="back" id="RfcSkip" href="javascript:void(0);">
                            Skip<span>&nbsp;</span><em class="fa fa-angle-right"></em>
                        </a>
                    </div>
                </c:if>
                <c:if test="${'true' != isClickEdit}">
                    <c:set var="locking" value="true"/>
                    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
                </c:if>
            </c:if>
        </div>
        <div class="medAlertContent">
        </div>
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
            <c:set var="medAlertPsn" value="${AppSvcMedAlertPsn[status.index]}"/>
            <div class="medAlertContent">
                <c:choose>
                    <c:when test="${medAlertPsn.licPerson}">
                        <input type="hidden" name="licPerson" value="1"/>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="licPerson" value="0"/>
                    </c:otherwise>
                </c:choose>
                <input type="hidden" name="existingPsn" value="0"/>
                <input type="hidden" name="isPartEdit" value="0"/>
                <input type="hidden" name="mapIndexNo" value="${medAlertPsn.indexNo}"/>
                <input type="hidden" name="loadingType" value="${medAlertPsn.loadingType}"/>
                <div class="row">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-6 control-label formtext col-md-8">
                                <div class="cgo-header">
                                    <strong>MedAlert Person <label class="assign-psn-item"><c:if test="${AppSvcMedAlertPsn.size() > 1}">${status.index+1}</c:if></label></strong>
                                </div>
                            </div>
                            <div class="col-sm-5 col-md-4 text-right">
                                <c:if test="${status.index - mandatoryCount >=0}">
                                    <h4 class="text-danger"><em class="fa fa-times-circle del-size-36 mapDelBtn cursorPointer"></em></h4>
                                </c:if>
                            </div>
                            <c:if test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType || requestInformationConfig != null}">
                                <div class="col-sm-10">
                                    <label class="control-font-label">
                                        <c:if test="${!empty medAlertPsn.name && !empty medAlertPsn.idNo && !empty medAlertPsn.idType}">
                                            ${medAlertPsn.name}, ${medAlertPsn.idNo} (<iais:code code="${medAlertPsn.idType}"/>)
                                        </c:if>
                                    </label>
                                </div>
                                <div class="col-sm-2 text-right">
                                    <div class="edit-content">
                                        <c:if test="${'true' == canEdit}">
                                            <div class="text-right app-font-size-16">
                                                <a class="edit mapEdit" href="javascript:void(0);">
                                                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                                </a>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
                <div class="">
                    <div class="row <c:if test="${'true' == canEdit && '-1' != medAlertPsn.assignSelect}">hidden</c:if>">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-6 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Assign a MedAlert Person</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-5 col-md-7" id="assignSelect">
                                    <div class="">
                                        <iais:select cssClass="assignSel"  name="assignSel" options="MedAlertAssignSelect" needSort="false"  value="${medAlertPsn.assignSelect}" ></iais:select>
                                        <span id="error_assignSelect${status.index}" name="iaisErrorMsg" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="medAlertPerson hidden">
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Name</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-3 col-xs-12">
                                    <iais:select cssClass="salutation"  name="salutation" codeCategory="CATE_ID_SALUTATION" value="${medAlertPsn.salutation}" firstOption="Please Select"></iais:select>
                                    <span class="error-msg" id="error_salutation${status.index}" name="iaisErrorMsg"></span>
                                </div>

                                <div class="col-sm-4 col-xs-12">
                                    <iais:input maxLength="66" type="text" name="name" value="${medAlertPsn.name}"></iais:input>
                                    <span class="error-msg" id="error_name${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">ID No.
                                        <span class="mandatory">*</span>
                                    </label>
                                </div>
                                <div class="col-sm-3 col-xs-12">
                                    <div class="">
                                        <iais:select cssClass="idType idTypeSel"  name="idType" needSort="false" value="${medAlertPsn.idType}" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE"></iais:select>
                                        <span class="error-msg" id="error_idTyp${status.index}" name="iaisErrorMsg"></span>
                                    </div>
                                </div>
                                <div class="col-sm-4 col-xs-12">
                                    <iais:input cssClass="idNoVal" maxLength="20" type="text" name="idNo"
                                                value="${medAlertPsn.idNo}" />
                                    <span class="error-msg" id="error_idNo${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row nationalityDiv">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-6 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Country of issuance</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-5 col-md-7">
                                    <div class="">
                                        <iais:select firstOption="Please Select" name="nationality" codeCategory="CATE_ID_NATIONALITY"
                                                     cssClass="nationality" value="${medAlertPsn.nationality}" needErrorSpan="false"/>
                                        <span id="error_nationality${status.index}" name="iaisErrorMsg" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                </div>
                                <div class="col-sm-7">
                                    <span class="error-msg" id="error_idTypeNo${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Mobile No.</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-4 col-md-7">
                                    <iais:input maxLength="8" type="text" name="mobileNo" value="${medAlertPsn.mobileNo}"></iais:input>
                                    <span class="error-msg" id="error_mobileNo${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Email Address</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-4 col-md-7">
                                    <iais:input maxLength="320" type="text" name="emailAddress" value="${medAlertPsn.emailAddr}"></iais:input>
                                    <span class="error-msg" id="error_emailAddr${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                   <%-- <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Description</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-4 col-md-7">
                                    <iais:input maxLength="66" type="text" name="description" value="${medAlertPsn.description}"></iais:input>
                                    <span class="error-msg" id="error_descriptionModeVal${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>--%>
                </div>
                <br/>
            </div>
        </c:forEach>
        </c:if>
        <c:if test="${requestInformationConfig==null}">
            <%--<c:choose>
                <c:when test="${!empty AppSvcMedAlertPsn}">
                    <c:set var="mapDtoLength" value="${AppSvcMedAlertPsn.size()}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="mapDtoLength" value="0"/>
                </c:otherwise>
            </c:choose>--%>

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
            <div class="row <c:if test="${!needAddPsn}">hidden</c:if>" id="addPsnDiv" >
                <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="col-sm-3 control-label formtext col-md-5">
                            <span id="addMapBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another MedAlert Person</span>
                        </div>
                        <div class="col-sm-3 control-label formtext col-md-7">
                            <span class="mapErrorMsg" style="color: red;"></span>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>
<div style="display: none;">
    <select id="nice_select_effect"></select>
</div>

<script>
    var init;
    $(function () {
        //init start
        init = 0;
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
            // $('#addMapBtn').addClass('hidden');
        }
        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        //new and not rfi
        if('APTY002' == appType && '0' == rfiObj){
            <c:choose>
                <c:when test="${!empty AppSvcMedAlertPsn}">
            console.log('map true');
                    <c:set var="psnLength" value="${AppSvcMedAlertPsn.size()-1}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="psnLength" value="0"/>
                </c:otherwise>
            </c:choose>
            console.log('psnLength:'+${psnLength});
            <c:forEach begin="0" end="${psnLength}" step="1" varStatus="stat">
                var $currentPsn = $('.medAlertContent').eq(${stat.index+1}).find('.medAlertPerson');
                //remove dis style
                $currentPsn.find('input[type="text"]').css('border-color','');
                $currentPsn.find('input[type="text"]').css('color','');
                //add edit and set style
                var psnDto = {};
                <c:if test="${!empty AppSvcMedAlertPsn[stat.index].psnEditFieldStr}">
                psnDto = ${AppSvcMedAlertPsn[stat.index].psnEditFieldStr};
                </c:if>
                setPsnDisabled($currentPsn,psnDto);
            </c:forEach>
        }
        //doEdit();
        //init end
        init =1;
        if($("#errorMapIs").val()=='error'){
            $('.edit').trigger('click');
        }
    })


    var mapDel = function () {
        $('.mapDelBtn').click(function () {
            $(this).closest('div.medAlertContent').remove();
            changePsnItem();
            //show add more
            var psnLength = $('.medAlertContent').length-1;
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

        $('.assignSel').change(function () {
            var assignSelVal = $(this).val();
            var $medAlertContentEle = $(this).closest('div.medAlertContent');
            if('-1' == assignSelVal){
                $medAlertContentEle.find('div.medAlertPerson').addClass('hidden');
                if(1 == init){
                    var emptyData = {};
                    fillPsnForm($medAlertContentEle,emptyData);
                    $medAlertContentEle.find('input[name="licPerson"]').val('0');
                    $medAlertContentEle.find('input[name="loadingType"]').val('');
                }
            }else if('newOfficer' == assignSelVal){
                $medAlertContentEle.find('div.medAlertPerson').removeClass('hidden');
                unDisabledPartPage($medAlertContentEle.find('.medAlertPerson'));
                if(1 == init){
                    var emptyData = {};
                    fillPsnForm($medAlertContentEle,emptyData);
                    $medAlertContentEle.find('input[name="licPerson"]').val('0');
                    $medAlertContentEle.find('input[name="loadingType"]').val('');
                }
            }else{
                $medAlertContentEle.find('div.medAlertPerson').removeClass('hidden');
                if(1 == init){
                    var arr = $(this).val().split(',');
                    var nationality = arr[0];
                    var idType = arr[1];
                    var idNo = arr[2];
                    loadSelectPsn($medAlertContentEle, nationality, idType, idNo, 'MAP');
                }
            }
        });
    }


    var addMAP = function(){
        $('#addMapBtn').click(function () {
            showWaiting();
            var hasNumber = $('.medAlertContent').length - 1;
            console.log("hasNumber" + hasNumber);
            $.ajax({
                url:'${pageContext.request.contextPath}/med-alert-person-html',
                dataType:'json',
                type:'POST',
                data:{
                    'HasNumber':hasNumber,
                },
                'success':function (data) {
                    if ('success' == data.res) {
                        console.log("suc");
                        $('.medAlertContent:last').after(data.sucInfo);
                        $('.assignSel').unbind();

                        assignSel();

                        mapDel();

                        changePsnItem();

                        retrieveData();
                        <!--set Scrollbar -->
                        /*$("div.assignSel->ul").mCustomScrollbar({
                                advanced:{
                                    updateOnContentResize: true
                                }
                            }
                        );*/
                        //hidden add more
                        var psnLength = $('.medAlertContent').length-1;
                        if(psnLength >='${mapHcsaSvcPersonnel.maximumCount}'){
                            $('#addPsnDiv').addClass('hidden');
                        }
                        if(psnLength <='${mapHcsaSvcPersonnel.mandatoryCount}'){
                            $('.medAlertContent:last .mapDelBtn').remove();
                        }
                        //get data from page
                        $('#isEditHiddenVal').val('1');

                        initNationality('div.medAlertContent:last', '.idType', '.nationalityDiv');
                    }else{
                        $('.mapErrorMsg').html(data.errInfo);
                    }
                    dismissWaiting();
                },
                error:function (data) {
                    console.log("err");
                    dismissWaiting();
                }
            });
        });
    }

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
        /*var idNo = data.idNo;
        if(idNo != '' && idNo != null && idNo != 'undefined'){
            $mapContentEle.find('input[name="idNo"]').val(idNo);
        }*/
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