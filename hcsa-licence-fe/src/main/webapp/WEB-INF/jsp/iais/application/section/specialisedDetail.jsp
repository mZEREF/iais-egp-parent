<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<style>
    .form-check.form-check-1 {
        margin-left: 30px;
        margin-bottom: 5px;
        margin-top: 5px;
    }

    .form-check.form-check-2 {
        margin-left: 60px;
        margin-bottom: 5px;
        margin-top: 5px;
    }

    .form-check.form-check-3 {
        margin-left: 90px;
        margin-bottom: 5px;
        margin-top: 5px;
    }
</style>

<c:forEach var="specialised" items="${AppSubmissionDto.appPremSpecialisedDtoList}">
    <c:if test="${specialised_svc_code == specialised.baseSvcCode}">
        <div class="specialised-content">
            <p class="app-title">${specialised.premName}</p>
            <p class="font-18 bold">${specialised.premAddress}</p>

            <c:if test="${not empty specialised.appPremScopeDtoList}">
                <div>
                    <p class="bold">${specialised.categorySectionName}</p>
                    <fieldset class="fieldset-content col-xs-12">
                        <legend></legend>
                        <div class="form-check-gp">
                            <c:forEach var="item" items="${specialised.appPremScopeDtoList}" varStatus="status">
                                <div class="form-check form-check-${item.seqNum}" data-parent="${specialised.premisesVal}-${item.parentId}">
                                    <input class="form-check-input" id="${specialised.premisesVal}-${item.subTypeId}"
                                           name="${specialised.premisesVal}_sub_type" value="${item.subTypeId}" type="checkbox"
                                           aria-invalid="false" <c:if test="${item.checked}"> checked="checked"</c:if> />
                                    <label class="form-check-label" for="${specialised.premisesVal}-${item.subTypeId}">
                                        <span class="check-square"></span><c:out value="${item.scopeName}"/>
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </fieldset>
                </div>
            </c:if>

            <c:if test="${not empty specialised.appPremSubSvcRelDtoList}">
                <div>
                    <p class="bold">${specialSvcSecName}</p>
                    <fieldset class="fieldset-content col-xs-12">
                        <legend></legend>
                        <div class="form-check-gp">
                            <c:forEach var="item" items="${specialised.appPremSubSvcRelDtoList}" varStatus="status">
                                <div class="form-check form-check-${item.seqNum}" data-parent="${specialised.premisesVal}-${item.parentId}">
                                    <input class="form-check-input" id="${specialised.premisesVal}-${item.svcId}"
                                           name="${specialised.premisesVal}_service" value="${item.svcId}" type="checkbox"
                                           aria-invalid="false" <c:if test="${item.checked}"> checked="checked"</c:if> />
                                    <label class="form-check-label" for="${specialised.premisesVal}-${item.svcId}">
                                        <span class="check-square"></span><c:out value="${item.svcName}"/>
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </fieldset>
                </div>
            </c:if>

            <c:if test="${empty specialised.appPremScopeDtoList && empty specialised.appPremSubSvcRelDtoList}">
                <iais:row>
                    <p class="font-18 bold"><iais:message key="GENERAL_ERR0071"/></p>
                </iais:row>
            </c:if>
        </div>
    </c:if>
</c:forEach>
<script type="text/javascript">
    $(document).ready(function () {
        specialisedCheckboxEvent();
        $('input[type="checkbox"]').trigger('click');
    });

    var specialisedCheckboxEvent = function () {
        $('input[type="checkbox"]').on('click', function () {
            var $input = $(this);
            let data = $input.val();
            let id = $input.attr('id');
            if ($input.is(':checked')) {
                showTag($('.' + id));
            } else {
                hideTag($('.' + id));
            }
        });
    }
</script>