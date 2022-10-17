<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<div class="row form-horizontal">
    <c:forEach var="specialised" items="${AppSubmissionDto.appPremSpecialisedDtoList}">
        <c:if test="${specialised_svc_code == specialised.baseSvcCode}">
            <div class="specialised-content">
                <iais:row>
                    <div class="col-xs-12">
                        <div class="app-title">${specialised.premName}</div>
                        <p class="font-18 bold">${specialised.premAddress}</p>
                        <p><span class="error-msg" name="iaisErrorMSg" id="error_${specialised.premisesVal}_mandatory"></span></p>
                    </div>
                </iais:row>

                <c:if test="${(isRfi || isRfc || isRenew) && AppSubmissionDto.appEditSelectDto.specialisedEdit}">
                    <iais:row>
                        <div class="col-xs-12 col-md-11 text-right editDiv">
                            <a class="premises-summary-preview specialisedEdit app-font-size-16"><em class="fa fa-pencil-square-o"></em><span style="display: inline-block;">&nbsp;</span>Edit</a>
                        </div>
                    </iais:row>
                </c:if>

                <c:if test="${not empty specialised.allAppPremScopeDtoList}">
                    <div class="">
                        <div class="app-title">${specialised.categorySectionName}</div>
                        <div><iais:message key="NEW_ACK036" paramKeys="data" paramValues="${StringUtil.toLowerCase(specialised.categorySectionName)}"/></div>
                    </div>
                    <iais:row>
                        <fieldset class="fieldset-content col-xs-12">
                            <legend></legend>
                            <div class="form-check-gp">
                                <c:forEach var="item" items="${specialised.allAppPremScopeDtoList}" varStatus="status">
                                    <div class="form-check form-check-${item.level}" data-parent="${specialised.premisesVal}-${item.parentId}">
                                        <input class="form-check-input" id="${specialised.premisesVal}-${item.subTypeId}"
                                               name="${specialised.premisesVal}_${item.parentId}_sub_type" value="${item.subTypeId}"
                                               type="checkbox" aria-invalid="false" data-prem="${specialised.premisesVal}"
                                               <c:if test="${item.checked}">checked="checked"</c:if> />
                                        <label class="form-check-label" for="${specialised.premisesVal}-${item.subTypeId}">
                                            <span class="check-square"></span><c:out value="${item.scopeName}"/>
                                        </label>
                                    </div>
                                </c:forEach>
                                <div class="form-check">
                                    <span class="error-msg" name="iaisErrorMSg" id="error_${specialised.premisesVal}_sub_type"></span>
                                </div>
                            </div>
                        </fieldset>
                    </iais:row>
                </c:if>

                <c:if test="${not empty specialised.allAppPremSubSvcRelDtoList}">
                    <div class="">
                        <div class="app-title">${specialised.specialSvcSecName}</div>
                        <div><iais:message key="NEW_ACK037" paramKeys="data" paramValues="${StringUtil.toLowerCase(specialised.specialSvcSecName)}"/></div>
                    </div>
                    <iais:row>
                        <fieldset class="fieldset-content col-xs-12">
                            <legend></legend>
                            <div class="form-check-gp">
                                <c:forEach var="item" items="${specialised.allAppPremSubSvcRelDtoList}" varStatus="status">
                                    <div class="form-check form-check-${item.level}" data-parent="${specialised.premisesVal}-${item.parentId}">
                                        <input class="form-check-input" id="${specialised.premisesVal}-${item.svcId}"
                                               name="${specialised.premisesVal}_${item.parentId}_service" value="${item.svcId}"
                                               type="checkbox" aria-invalid="false" data-prem="${specialised.premisesVal}"
                                               <c:if test="${item.checked}">checked="checked"</c:if> />
                                        <label class="form-check-label" for="${specialised.premisesVal}-${item.svcId}">
                                            <span class="check-square"></span><c:out value="${item.svcName}"/>
                                        </label>
                                    </div>
                                </c:forEach>
                                <div class="form-check">
                                    <span class="error-msg" name="iaisErrorMSg" id="error_${specialised.premisesVal}_service"></span>
                                </div>
                            </div>
                        </fieldset>
                    </iais:row>
                </c:if>

                <c:if test="${empty specialised.allAppPremScopeDtoList && empty specialised.allAppPremSubSvcRelDtoList}">
                    <iais:row>
                        <span class="font-18 bold error-msg col-xs-12"><iais:message key="NEW_ACK038"/></span>
                    </iais:row>
                </c:if>
            </div>
        </c:if>
    </c:forEach>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        specialisedCheckboxEvent();
        $('input[type="checkbox"]').each(function (k, v) {
            checkspecialisedCheckbox($(v));
        });
        editSpecialisedEvent();
    });

    function specialisedCheckboxEvent() {
        $('input[type="checkbox"]').on('click', function () {
            checkspecialisedCheckbox($(this));
        });
    }

    function checkspecialisedCheckbox($input) {
        let data = $input.data('prem') + '-' + $input.val();
        if ($input.is(':checked')) {
            showTag($('div[data-parent="' + data + '"]'));
        } else {
            hideTag($('div[data-parent="' + data + '"]'));
        }
    }

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