<input type="hidden" id="controlFormLi" value="${specialised_svc_code}">
<!-- for desktop -->
<c:if test="${hcsaServiceDtoList.size()>1}">
    <ul id="tabUl" class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
        <c:forEach items="${hcsaServiceDtoList}" var="list">
            <li id="${list.svcCode}li" class="complete" role="presentation">
                <a id="${list.svcCode}" aria-controls="lorem1" role="tab" data-toggle="tab">${list.svcName}</a>
            </li>
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
    $(document).ready(function () {
        let controlFormLi = $('#controlFormLi').val();
        $('#' + controlFormLi + 'li').addClass('active');
        fillValue('#mobile-tab-ui', controlFormLi);

        $('#tabUl > li > a').click(function () {
            showWaiting();
            submitSpecialisedTabs(this.id);
        });
        $('#mobile-tab-ui').change(function () {
            console.log($(this).val());
            showWaiting();
            submitSpecialisedTabs($(this).val());
        });

        <%--<c:if test="${AppSubmissionDto.appEditSelectDto!=null && !AppSubmissionDto.appEditSelectDto.specialisedEdit}">
        disabledPage();
        </c:if>--%>
    });
</script>