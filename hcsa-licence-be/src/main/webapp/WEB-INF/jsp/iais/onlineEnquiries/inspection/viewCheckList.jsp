<div class="col-xs-12">
    <div class="tab-gp dashboard-tab">
        <div class="tab-content">
        </div>
        <div class="tab-pane <c:if test="${serListDto.checkListTab=='chkList'}">active</c:if>" id="tabPayment" role="tabpanel">
            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                <li class="complete ${(nowComTabIn == null || nowComTabIn== 'General') ? 'active' : ''}" role="presentation"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General Regulations</a></li>
                <li class="complete ${(nowComTabIn== 'ServiceInfo') ? 'active' : ''}" role="presentation"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab"
                                                                                                             data-toggle="tab"><c:out value="${serListDto.serviceName}"/></a></li>
            </ul>

            <div class="tab-nav-mobile visible-xs visible-sm">
                <div class="swiper-wrapper" role="tablist">
                    <div class="swiper-slide"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General</a></div>
                    <div class="swiper-slide"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab" data-toggle="tab">ServiceInfo</a></div>
                    <div class="swiper-slide"><a href="#chkInfo" aria-controls="chkInfo" role="tab" data-toggle="tab">chkInfo</a></div>
                </div>
                <div class="swiper-button-prev"></div>
                <div class="swiper-button-next"></div>
            </div>
            <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
            <div class="tab-content" id="checkLsitItemArea">
                <%@include file="../../inspectionncList/common/commonCheckList.jsp" %>
                <%@include file="../../inspectionncList/common/serviceCheckList.jsp" %>
            </div>
        </div>
    </div>
</div>