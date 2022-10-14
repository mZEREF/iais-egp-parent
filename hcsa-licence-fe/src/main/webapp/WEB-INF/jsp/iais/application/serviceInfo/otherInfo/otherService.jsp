<div class="specialised-content">
    <iais:row>
        <div class="col-xs-12 col-md-11 text-right editDiv">
            <a class="premises-summary-preview specialisedEdit app-font-size-16"><em class="fa fa-pencil-square-o"></em><span style="display: inline-block;">&nbsp;</span>Edit</a>
        </div>
    </iais:row>

    <c:if test="${not empty appSvcOtherInfoDto.allAppPremSubSvcRelDtoList}">
        <div class="">
            <div class="app-title">Other Services</div>
        </div>
        <iais:row>
            <fieldset class="fieldset-content col-xs-12">
                <legend></legend>
                <div class="form-check-gp">
                    <c:forEach var="item" items="${appSvcOtherInfoDto.allAppPremSubSvcRelDtoList}" varStatus="status">
                        <div class="form-check form-check-${item.level}" data-parent="${appSvcOtherInfoDto.premisesVal}-${item.parentId}">
                            <input class="form-check-input" id="${appSvcOtherInfoDto.premisesVal}-${item.svcId}"
                                   name="${appSvcOtherInfoDto.premisesVal}_${item.parentId}_service" value="${item.svcId}"
                                   type="checkbox" aria-invalid="false" data-prem="${appSvcOtherInfoDto.premisesVal}"
                                   <c:if test="${item.checked}">checked="checked"</c:if> />
                            <label class="form-check-label" for="${appSvcOtherInfoDto.premisesVal}-${item.svcId}">
                                <span class="check-square"></span><c:out value="${item.svcName}"/>
                            </label>
                        </div>
                    </c:forEach>
                    <div class="form-check">
                        <span class="error-msg" name="iaisErrorMSg" id="error_${appSvcOtherInfoDto.premisesVal}_service"></span>
                    </div>
                </div>
            </fieldset>
        </iais:row>
    </c:if>
</div>
<script>
    $(document).ready(function (){
        editSpecialisedEvent();
        <c:if test="${AppSubmissionDto.needEditController}">
        disableOtherInfoContent();
        </c:if>
    })
    function editSpecialisedEvent() {
        let $target = $('.specialisedEdit');
        if (isEmptyNode($target)) {
            return;
        }
        $target.unbind('click');
        $target.on('click', function () {
            let $content = $(this).closest('div.specialised-content');
            doEditSpecialised($content);
        });
    }

    function disableSpecialisedContent() {
        disableContent('div.specialised-content');
        let $target = $('.editDiv');
        if (!isEmptyNode($target)) {
            showTag($target);
        }
    }

    function doEditSpecialised($content) {
        if (hideEditBtn($content)) {
            return;
        }
        $('#isEditHiddenVal').val('1');
        unDisableContent($content);
        let $editDiv = $content.find('.editDiv');
        let $editParent = $editDiv.closest('.form-group');
        if (!isEmptyNode($editParent)) {
            hideTag($editParent);
        } else {
            hideTag($editDiv);
        }
    }

    function hideEditBtn ($content) {
        let $target= $content.find('.editDiv');
        if (isEmptyNode($target)) {
            return true;
        }
        return $target.is(':hidden');
    }
</script>