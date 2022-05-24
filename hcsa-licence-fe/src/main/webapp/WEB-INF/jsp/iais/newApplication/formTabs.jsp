<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator" %>

<%
    String actionForm = (String)ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);

%>
<input type="hidden" id = "controlFormLi" value="<%=actionForm%>">
<input type="hidden" name="crud_action_type_tab" value="">
<!-- for desktop -->
<c:if test="${hcsaServiceDtoList.size()>1}">
<ul id = "tabUl" class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
    <c:forEach items="${hcsaServiceDtoList}" var="list">
        <li id = "${list.svcCode}li"  class="complete" role="presentation"><a id = "${list.svcCode}" aria-controls="lorem1" role="tab" data-toggle="tab">${list.svcName}</a></li>
    </c:forEach>
</ul>
<!-- for Mobile -->
<div class="mobile-side-nav-tab visible-xs visible-sm">
    <select id="mobile-tab-ui" aria-label="serviceSelectMobile">
    <c:forEach items="${hcsaServiceDtoList}" var="list">
        <option value="${list.svcCode}">${list.svcName}</option>
    </c:forEach>
    </select>
</div>
</c:if>

<script type="text/javascript">
    $(document).ready(function() {
        var controlFormLi = $('#controlFormLi').val();
        $('#'+controlFormLi+'li').addClass('active');
        fillValue('#mobile-tab-ui', controlFormLi);

        $('#tabUl > li >a').click(function () {
            showWaiting();
            submitFormTabs(this.id,null,null);
        });
        $('#mobile-tab-ui').change(function () {
            console.log($(this).val());
            showWaiting();
            submitFormTabs($(this).val(),null,null);
        });

        <c:if test="${AppSubmissionDto.appEditSelectDto!=null && !AppSubmissionDto.appEditSelectDto.serviceEdit}">
        disabledPage();
        </c:if>
    });

    function submitFormTabs(action){
        $("[name='crud_action_type']").val('serviceForms');
        $("[name='crud_action_type_tab']").val(action);
        $("[name='crud_action_type_form_page']").val('jump');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    }

    function initNationality(parent, idTypeTag, nationalityDiv) {
        $(parent).find(idTypeTag).on('change', function () {
            var $content = $(this).closest(parent.replace(':last', ''));
            console.log(nationalityDiv + ': ' + $content.find(nationalityDiv).length);
            toggleIdType(this, $content.find(nationalityDiv));
        });
        $(parent).each(function (index, ele) {
            console.log(idTypeTag + ': ' + $(ele).find(idTypeTag).length);
            toggleIdType($(ele).find(idTypeTag), $(ele).find(nationalityDiv));
        });
    }

    function toggleIdType(sel, elem) {
        if (isEmpty(sel) || isEmpty(elem)) {
            return;
        }
        var $sel = $(sel);
        var $elem = $(elem);
        if ($sel.length == 0 || $elem.length == 0) {
            return;
        }
        console.log($sel.val());
        if ($sel.val() == 'IDTYPE003') {
            $elem.removeClass('hidden');
        } else {
            $elem.addClass('hidden');
            clearFields($elem);
        }
    }
</script>