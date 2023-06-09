<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>
<div class="row">
    <div class="col-xs-12 col-md-12 text-right">
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
    </div>
</div>
<div class="row normal-label outsourcedContent">
    <%@include file="outsourceService.jsp"%>
    <c:set var="isShow" value="0"/>
    <c:if test="${isRenew || isRfi || isRfc}">
        <c:set var="isShow" value="1"/>
        <%@include file="clinicalLaboratory.jsp"%>
        <%@include file="radiologicalService.jsp"%>
    </c:if>

    <c:if test="${(isShow eq 0) && !empty outSourceParam}">
        <%@include file="clinicalLaboratory.jsp"%>
        <%@include file="radiologicalService.jsp"%>
    </c:if>
</div>
<script>
    function searchValue() {
        let selectValue = $('#serviceCode').val();
        console.log(" selectValue " + selectValue);
        if (!isEmpty(selectValue)) {
            console.log(" selectValue " + !isEmpty(selectValue));
            doEditOutsourcedEvent();
            $('div.edit-content').addClass('hidden').css('display', 'none');
        } else {
            let $currContent = $(this).closest('div.outsourced-content');
            $currContent.find('input.isPartEdit').val('1');
            $('#isEditHiddenVal').val('1');
            disableErrorBtn();
        }
    }

    $(document).ready(function (){
        doEditOutsourcedEvent();
        //rfc,renew,rfi
        let selectValue = $('#serviceCode').val();
        console.log(" selectValue " + selectValue);
        <c:if test="${AppSubmissionDto.needEditController}">
            if (isEmpty(selectValue)){
                disableOutsourcedContent();
                isError();
            }else {
                let $currContent = $(this).closest('div.outsourced-content');
                $currContent.find('input.isPartEdit').val('1');
                $('#isPartEdit').val(1)
                $('#isEditHiddenVal').val('1');
            }
        </c:if>
    })

    function isError(){
        $('div.outsourcedContent').each(function (k, v) {
            if ($("#errorMapIs").val() == 'error') {
                $(v).find('.error-msg').on('DOMNodeInserted', function () {
                    if ($(this).not(':empty')) {
                        $(v).find('.isPartEdit').val(1);
                        $('#isEditHiddenVal').val('1');
                        $('a.outsourcedEdit').trigger('click');
                        disableErrorBtn();
                    }
                });
            }
        });
    }

    function doEditOutsourcedEvent() {
        $('a.outsourcedEdit').click(function () {
            let $currContent = $(this).closest('div.outsourced-content');
            $currContent.find('input.isPartEdit').val('1');
            hideTag($currContent.find('.edit-content'));
            unDisableContent($currContent);
            $('a.btn-outsourced-clear').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
            $('a.btn-outsourced-search').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
            $('button.btn-cldBtn').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
            $('button.btn-rSBtn').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
            $('#isEditHiddenVal').val('1');
        });
    };

    function disableErrorBtn(){
        $('button.btn-cldBtn').prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
        $('button.btn-rSBtn').prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
    }

    function disableOutsourcedContent() {
        // edit btn
        let $currContent = $('.outsourcedContent');
        disableContent($currContent);
        $("a.btn-outsourced-clear").prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
        $("a.btn-outsourced-search").prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
    }
</script>