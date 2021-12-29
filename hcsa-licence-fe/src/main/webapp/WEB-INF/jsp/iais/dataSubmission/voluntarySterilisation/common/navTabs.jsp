<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<input type="hidden" name="crud_action_type_form_page" value="">
<input type="hidden" id="controlLi" value="${VSS_CURRENT_STEP.name}">

<ul id="nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <c:forEach var="config" items="${VSS_CONFIG}">
        <c:if test="${config.status == 0}">
            <c:set var="stepStatus" value="incomplete"/>
        </c:if>
        <c:if test="${config.status == 1}">
            <c:set var="stepStatus" value="complete"/>
        </c:if>
        <c:if test="${config.active}">
            <c:set var="stepStatus" value="active"/>
        </c:if>
        <c:if test="${config.canShow}">
        <li id="${config.name}li" role="presentation" class="${stepStatus}">
            <a id="${config.name}" aria-controls="${config.name}Tab" role="tab" data-toggle="tab"
               data-config-click="${config.canClick}" data-config-code="${config.code}">
                <c:out value="${config.text}" escapeXml="false"/>
            </a>
        </li>
        </c:if>
    </c:forEach>
</ul>
<div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
    <div class="swiper-wrapper" role="tablist">
        <c:forEach var="config" items="${VSS_CONFIG}">
            <c:if test="${config.active}">
                <c:set var="stepStatus" value="active"/>
            </c:if>
            <c:if test="${config.canShow}">
            <div class="swiper-slide " >
                <a href="#${config.name}" aria-controls="${config.name}Tab" role="tab" data-toggle="tab"
                   data-config-click="${config.canClick}">
                    <c:out value="${config.text}" escapeXml="false"/>
                </a>
            </div>
            </c:if>
        </c:forEach>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        // tab click
        var controlLi = $('#controlLi').val();
        var $tarSel = $('#'+controlLi+'li');
        if ($tarSel.length > 0) {
            $tarSel.addClass('active');
            if ($tarSel.attr("class").match("active")){
                $tarSel.removeClass("incomplete");
                $tarSel.removeClass("complete");
            }
        }
        $('#nav-tabs-ul a').on('click', function() {
            var $target = $(this);
            var currId = $target.attr('id');
            var canClick = $target.data('config-click');
            if (isEmpty(canClick)) {
                canClick = $target.attr('data-config-click');
            }
            var configCode = $target.data('config-code');
            if (isEmpty(configCode)) {
                configCode = $target.attr('data-config-code');
            }
            console.info(currId);
            if (controlLi == currId) {
                return;
            } else if (canClick) {
                submit(configCode);
            }
        });
    });

</script>