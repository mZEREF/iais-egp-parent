<c:if test="${AppSubmissionDto.needEditController}">
    <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
        <c:if test="${'APPSPN01' == clickEditPage}">
            <c:set var="isClickEdit" value="true"/>
        </c:if>
    </c:forEach>
    <c:choose>
        <c:when test="${'true' != isClickEdit}">
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
        <div id="edit-content">
            <c:choose>
                <c:when test="${'true' == canEdit}">
                    <div class="text-right app-font-size-16">
                        <a id="edit" class="disciplineEdit" href="javascript:void(0);">
                            <em class="fa fa-pencil-square-o"></em><span >&nbsp;</span>Edit
                        </a>
                    </div>
                </c:when>
                <c:otherwise>

                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
</c:if>

<c:set value="${reloadLaboratoryDisciplines}" var="reloadData"/>
<c:set var="rfiNo" value="0"/>
<c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
    <c:set value="${appGrpPremisesDto.premisesIndexNo}" var="premIndexNo"/>
    <c:if test="${appGrpPremisesDto.rfiCanEdit}">
        <c:set var="rfiNo" value="${status.index}"/>
    </c:if>
    <fieldset class="fieldset-content" id="fieldset-content${status.index}" <c:if test="${AppSubmissionDto.needEditController && !isClickEdit}">disabled</c:if> >
        <legend></legend>
        <p><strong class="cgo-header">Mode of Service Delivery ${status.index+1}</strong></p>
        <p>
            <strong class="cgo-header">
                <c:choose>
                    <c:when test="${'ONSITE' == appGrpPremisesDto.premisesType}">
                        <c:out value="Address"/>: <c:out value="${appGrpPremisesDto.address}"/>
                    </c:when>
                    <c:when test="${'CONVEYANCE' == appGrpPremisesDto.premisesType}">
                        <c:out value="Address"/>: <c:out value="${appGrpPremisesDto.address}"/>
                    </c:when>
                    <c:when test="${'OFFSITE'  == appGrpPremisesDto.premisesType}">
                        <c:out value="Address"/>: <c:out value="${appGrpPremisesDto.address}"/>
                    </c:when>
                </c:choose>
            </strong>
        </p>
        <span class="error-msg" name="iaisErrorMsg" id="error_checkError"></span>
        <div class="wrapper">
            <div class="form-inner-content editableMode">
                <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
                <div class="canvasContent">
                    <input type="hidden" id="testCode" value="HST"/>
                    <div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
                        <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
                            <div id="control--runtime--0" class="page control control-area  container-p-1">
                                <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
                                <table aria-describedby="" class="control-grid">
                                    <thead style="display: none">
                                    <tr><th scope="col"></th></tr>
                                    </thead>
                                    <tbody><tr height="100%"><td class="first last" style="width: 100%;"><div id="control--runtime--1" class="control control-caption-horizontal">
                                        <div class="control-label-span control-set-alignment">
                                            <label id="control--runtime--1--label" class="control-label control-set-font control-font-label"></label>
                                            <span class="upload_controls"></span>
                                        </div>
                                        <div class="control-input-span control-set-alignment">
                                            <div class="normal-indicator">
                                                <table aria-describedby="" class="check-${premIndexNo}">
                                                    <thead style="display: none">
                                                    <tr><th scope="col"></th></tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach var="levelOneList" items="${HcsaSvcSubtypeOrSubsumedDto}" varStatus="levelOne">
                                                        <c:set var="checkIndexNo1" value="${premIndexNo};${levelOneList.name};${levelOneList.code};${levelOneList.parentId}"/>
                                                        <c:set var="reloadIndexNo1" value="${currentServiceId}${premIndexNo}${levelOneList.id}"/>
                                                        <!--one -->
                                                        <c:choose>
                                                            <c:when test="${reloadData[reloadIndexNo1] != null && reloadData[reloadIndexNo1] != ''}">
                                                                <c:set var="checkStat" value="true"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:set var="checkStat" value="false"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <c:choose>
                                                            <c:when test="${levelOneList.name=='Please indicate'}">
                                                                <c:set var="needTextArea" value="true"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:set var="needTextArea" value="false"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <tr>
                                                            <td>
                                                                <div class="control-item-container parent-form-check" data-parent="<c:out value="${premIndexNo}${levelOneList.type}${levelOneList.name}" />" >
                                                                    <input type="checkbox"
                                                                    <c:if test="${reloadData[reloadIndexNo1] != null && reloadData[reloadIndexNo1] != ''}">
                                                                           checked="checked"
                                                                    </c:if>
                                                                           id="<c:out value="control--${levelOne.index}--${levelOne.index}" />"
                                                                           name="<c:out value="${premIndexNo}control--runtime--1" />" class="control-input"
                                                                           value="<c:out value="${checkIndexNo1}" />">
                                                                    <label  for="<c:out value="control--${levelOne.index}--${levelOne.index}" />" data-code="<c:out value="${premIndexNo}${levelOneList.type}${levelOneList.name}" />" class="control-label control-set-font control-font-normal" />
                                                                    <span class="check-square"></span><c:out value="${levelOneList.name}" />
                                                                    </label>
                                                                    <input class="checkValue" type="hidden" name="<c:out value="${checkIndexNo1}" />" value="<iais:mask name="${checkIndexNo1}" value="${levelOneList.id}"/>"/>
                                                                </div>
                                                            </td>
                                                            <td >
                                                                <c:choose>
                                                                    <c:when test="${needTextArea}">
                                                                        <c:set var="otherScope" value=""/>
                                                                        <c:forEach var="svcScope" items="${svcLaboratoryDisciplinesDto}" >
                                                                            <c:choose>
                                                                                <c:when test="${appGrpPremisesDto.premisesIndexNo == svcScope.premiseVal}">
                                                                                    <c:forEach var="svcChkLst" items="${svcScope.appSvcChckListDtoList}">
                                                                                        <c:if test="${'Please indicate' == svcChkLst.chkName}">
                                                                                            <c:set var="otherScope" value="${svcChkLst.otherScopeName}"/>
                                                                                        </c:if>
                                                                                    </c:forEach>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:forEach>
                                                                        <input type="hidden" name="needTextArea" value="1"/>
                                                                        <textarea class="pleaseIndicate" name="pleaseIndicate${status.index}" maxlength="200" cols="45" <c:if test="${!checkStat}">disabled</c:if> >${otherScope}</textarea>
                                                                        <br/>
                                                                        <span class="error-msg" name="iaisErrorMsg" id="error_pleaseIndicateError${status.index}"></span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <input type="hidden" name="needTextArea" value="0"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                        <c:if test="${not empty levelOneList.list}">
                                                            <c:forEach var="levelTwoList" items="${levelOneList.list}" varStatus="levelTwo">
                                                                <c:set var="checkIndexNo2" value="${premIndexNo};${levelTwoList.name};${levelTwoList.code};${levelTwoList.parentId}"/>
                                                                <c:set var="reloadIndexNo2" value="${currentServiceId}${premIndexNo}${levelTwoList.id}"/>
                                                                <!--two -->
                                                                <c:choose>
                                                                    <c:when test="${reloadData[reloadIndexNo2] != null && reloadData[reloadIndexNo2] != ''}">
                                                                        <c:set var="checkStat" value="true"/>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:set var="checkStat" value="false"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <c:choose>
                                                                    <c:when test="${levelTwoList.name=='Please indicate'}">
                                                                        <c:set var="needTextArea" value="true"/>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:set var="needTextArea" value="false"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <tr>
                                                                    <td>
                                                                        <div class="control-item-container sub-form-check parent-form-check disabled" data-parent="<c:out value="${premIndexNo}${levelTwoList.type}${levelTwoList.name}"/>" data-child="<c:out value="${premIndexNo}${levelOneList.type}${levelOneList.name}"/>" >
                                                                            <input type="checkbox"
                                                                            <c:if test="${checkStat}">
                                                                                   checked="checked"
                                                                            </c:if>
                                                                                   id="<c:out value="control--${levelTwo.begin}--${levelTwo.index}"/>"
                                                                                   name="<c:out value="${premIndexNo}control--runtime--1"/>" class="control-input"
                                                                                   value="<c:out value="${checkIndexNo2}" />">
                                                                            <label  for="<c:out value="control--${levelTwo.index}--${levelTwo.index}"/>" class="control-label control-set-font control-font-normal" />
                                                                            <span class="check-square"></span>
                                                                            <c:out value="${levelTwoList.name}"/>
                                                                            </label>
                                                                            <br/>
                                                                            <c:if test="${levelOneList.name=='Others' && levelTwo.last}">
                                                                                <span class="error-msg" name="iaisErrorMsg" id="error_otherScopeError${status.index}"></span>
                                                                            </c:if>
                                                                            <input class="checkValue" type="hidden" name="<c:out value="${checkIndexNo2}"/>" value="<iais:mask name="${checkIndexNo2}" value="${levelTwoList.id}"/>"/>
                                                                        </div>
                                                                    </td>
                                                                    <td >
                                                                        <c:choose>
                                                                            <c:when test="${needTextArea}">
                                                                                <c:set var="otherScope" value=""/>
                                                                                <c:forEach var="svcScope" items="${svcLaboratoryDisciplinesDto}" >
                                                                                    <c:choose>
                                                                                        <c:when test="${appGrpPremisesDto.premisesIndexNo == svcScope.premiseVal}">
                                                                                            <c:forEach var="svcChkLst" items="${svcScope.appSvcChckListDtoList}">
                                                                                                <c:if test="${'Please indicate' == svcChkLst.chkName}">
                                                                                                    <c:set var="otherScope" value="${svcChkLst.otherScopeName}"/>
                                                                                                </c:if>
                                                                                            </c:forEach>
                                                                                        </c:when>
                                                                                        <c:otherwise>
                                                                                        </c:otherwise>
                                                                                    </c:choose>
                                                                                </c:forEach>
                                                                                <input type="hidden" name="needTextArea" value="1"/>
                                                                                <textarea class="pleaseIndicate" name="pleaseIndicate${status.index}" maxlength="200" cols="45" <c:if test="${!checkStat}">disabled</c:if> >${otherScope}</textarea>
                                                                                <br/>
                                                                                <span class="error-msg" name="iaisErrorMsg" id="error_pleaseIndicateError${status.index}"></span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <input type="hidden" name="needTextArea" value="0"/>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                </tr>
                                                                <c:if test="${not empty levelTwoList.list}">
                                                                    <!--three -->
                                                                    <c:forEach var="levelThreeList" items="${levelTwoList.list}" varStatus="levelThree">
                                                                        <c:set var="checkIndexNo3" value="${premIndexNo};${levelThreeList.name};${levelThreeList.code};${levelThreeList.parentId}"/>
                                                                        <c:set var="reloadIndexNo3" value="${currentServiceId}${premIndexNo}${levelThreeList.id}"/>
                                                                        <c:choose>
                                                                            <c:when test="${reloadData[reloadIndexNo3] != null && reloadData[reloadIndexNo3] != ''}">
                                                                                <c:set var="checkStat" value="true"/>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <c:set var="checkStat" value="false"/>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        <c:choose>
                                                                            <c:when test="${levelThreeList.name=='Please indicate'}">
                                                                                <c:set var="needTextArea" value="true"/>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <c:set var="needTextArea" value="false"/>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        <tr>
                                                                            <td>
                                                                                <div class="control-item-container sub-form-check double parent-form-check disabled" data-parent="<c:out value="${premIndexNo}${levelThreeList.type}${levelThreeList.name}"/>" data-child="<c:out value="${premIndexNo}${levelTwoList.type}${levelTwoList.name}"/>" >
                                                                                    <input type="checkbox"
                                                                                    <c:if test="${reloadData[reloadIndexNo3] != null && reloadData[reloadIndexNo3] != ''}">
                                                                                           checked="checked"
                                                                                    </c:if>
                                                                                           id="<c:out value="control--${levelThree.index}--${levelThree.index}"/>"
                                                                                           name="<c:out value="${premIndexNo}control--runtime--1"/>" class="control-input"
                                                                                           value="<c:out value="${checkIndexNo3}"/>">
                                                                                    <label  for="<c:out value="control--${levelThree.index}--${levelThree.index}"/>" class="control-label control-set-font control-font-normal">
                                                                                        <span class="check-square"></span><c:out value="${levelThreeList.name}"/>
                                                                                    </label>
                                                                                    <input class="checkValue" type="hidden" name="<c:out value="${checkIndexNo3}"/>" value="<iais:mask name="${checkIndexNo3}" value="${levelThreeList.id}"/>"/>
                                                                                </div>
                                                                            </td>
                                                                            <td>
                                                                                <c:choose>
                                                                                    <c:when test="${needTextArea}">
                                                                                        <c:set var="otherScope" value=""/>
                                                                                        <c:forEach var="svcScope" items="${svcLaboratoryDisciplinesDto}" >
                                                                                            <c:choose>
                                                                                                <c:when test="${appGrpPremisesDto.premisesIndexNo == svcScope.premiseVal}">
                                                                                                    <c:forEach var="svcChkLst" items="${svcScope.appSvcChckListDtoList}">
                                                                                                        <c:if test="${'Please indicate' == svcChkLst.chkName}">
                                                                                                            <c:set var="otherScope" value="${svcChkLst.otherScopeName}"/>
                                                                                                        </c:if>
                                                                                                    </c:forEach>
                                                                                                </c:when>
                                                                                                <c:otherwise>
                                                                                                </c:otherwise>
                                                                                            </c:choose>
                                                                                        </c:forEach>
                                                                                        <input type="hidden" name="needTextArea" value="1"/>
                                                                                        <textarea class="pleaseIndicate" name="pleaseIndicate${status.index}" maxlength="200" cols="45" <c:if test="${!checkStat}">disabled</c:if> >${otherScope}</textarea>
                                                                                        <span class="error-msg" name="iaisErrorMsg" id="error_pleaseIndicateError${status.index}"></span>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <input type="hidden" name="needTextArea" value="0"/>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </c:forEach>
                                                        </c:if>
                                                    </c:forEach>
                                                    </tbody></table>
                                            </div>
                                            <div id="control--runtime--1--errorMsg_right" style="display: none;" class="error_placements"></div>
                                        </div>
                                    </div></td></tr></tbody></table>
                                <div id="control--runtime--0--errorMsg_page_bottom" class="error_placements"></div>
                            </div>
                        </div>
                        <div id="demo" style="display:none;" class="ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide"></div>
                    </div>
                </div>
            </div>
        </div>
    </fieldset>
    <br/>
</c:forEach>
<script>
    $(document).ready(function () {
        //Binding method
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }
        $('#laboratoryDisciplinesBack').click(function(){
            submit('documents',null,null);
        });
        $('#laboratoryDisciplinesSaveDraft').click(function(){
            submitForms('laboratoryDisciplines','saveDraft',null,'clinical');
        });
        $('#laboratoryDisciplinesNext').click(function(){
            /*  var controlFormLi = $('#controlFormLi').val();
             var aBoolean= $("input[name='control--runtime--1']:checked").length>0;
             if(!aBoolean){
                  $('.laboratory-disciplines>span').removeAttr('style');
                 return;
             }else {
                 $('.laboratory-disciplines>span').attr('style','display: none');
             }
                                */
            var controlFormLi = $('#controlFormLi').val();
            submitForms('governanceOfficers',null,'next',controlFormLi);
        });

        $('#disciplineHr').css('margin-bottom','5px');


        $('input[type="checkbox"]:checked').each(function () {
            var parentID = $(this).closest('.parent-form-check').attr('data-parent');
            $('.sub-form-check[data-child="'+parentID+'"]').removeClass('disabled');
        });
        doEdit();
        if ('${errormapIs}' == 'error') {
            $('#edit').trigger('click');
        }
    });


    var doEdit = function () {
        $('#edit').click(function () {
            $('#edit-content').addClass('hidden');
            var rfiNo = '${rfiNo}';
            $('#fieldset-content'+rfiNo).prop('disabled',false);
            $('#isEditHiddenVal').val('1');
        });
    };

    $('input[type="checkbox"]').click(function () {
        var $chckBox = $(this).closest('tr');
        var needTextArea = $chckBox.find('input[name="needTextArea"]').val();
        if(needTextArea == '1'){
            if($(this).prop('checked')){
                $chckBox.find('.pleaseIndicate').prop('disabled',false);
            }else{
                $chckBox.find('.pleaseIndicate').prop('disabled',true)
            }
        }
        //@override
        var clickObj = $(this);
        if(clickObj.prop('checked') != true){
            clearPleaseIndicate($(this));
            var parentID = clickObj.closest('div.parent-form-check').attr('data-parent');
            downTheRecursive(parentID);
        }
    });

    var downTheRecursive = function (parentID) {
        if(isEmpty(parentID)){
            return;
        }
        $('div[data-child="'+parentID+'"]').each(function () {
            clearPleaseIndicate($(this));
            var currParentID = $(this).closest('div.parent-form-check').attr('data-parent');
            downTheRecursive(currParentID);
        });
    }

    var clearPleaseIndicate = function (clickObj) {
        var currTrObj = clickObj.closest('tr');
        currTrObj.find('.pleaseIndicate').prop('disabled',true);
        currTrObj.find('.pleaseIndicate').val('');
    }

</script>