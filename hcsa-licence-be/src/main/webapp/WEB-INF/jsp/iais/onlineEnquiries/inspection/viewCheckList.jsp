<div class="col-xs-12">
    <div class="tab-gp dashboard-tab">
        <div class="tab-pane <c:if test="${serListDto.checkListTab=='chkList'}">active</c:if>" id="tabPayment" role="tabpanel">
            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                <li class="complete ${(nowComTabIn == null || nowComTabIn== 'General') ? 'active' : ''}" role="presentation"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General Regulations</a></li>
                <li class="complete ${(nowComTabIn== 'ServiceInfo') ? 'active' : ''}" role="presentation"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab"
                                                                                                  data-toggle="tab"><c:out value="${serListDto.serviceName}"/></a></li>
                <c:forEach var="service" items="${specialServiceForChecklistDecideDtos}" >
                    <c:set value = "ServiceInfo${service.identify}" var = "errorTabName"/>
                    <li class="complete ${errorSpecTab == errorTabName ? 'active' : ''}" role="presentation"><a href="#ServiceInfo${service.identify}" aria-controls="ServiceInfo${service.identify}" role="tab"
                                                                                                                data-toggle="tab"><c:out value="${service.serviceTab}"/></a></li>
                </c:forEach>
            </ul>

            <div class="tab-nav-mobile visible-xs visible-sm">
                <div class="swiper-wrapper" role="tablist">
                    <div class="swiper-slide"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General</a></div>
                    <div class="swiper-slide"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab" data-toggle="tab">ServiceInfo</a></div>
                    <c:forEach var="service" items="${specialServiceForChecklistDecideDtos}" >
                        <div class="swiper-slide"><a href="#ServiceInfo${service.identify}" aria-controls="ServiceInfo${service.identify}" role="tab" data-toggle="tab"><c:out value="${service.serviceTab}"/></a></div>
                    </c:forEach>
                </div>
                <div class="swiper-button-prev"></div>
                <div class="swiper-button-next"></div>
            </div>
            <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
            <div class="tab-content" id="checkLsitItemArea">
                <%@include file="commonCheckList.jsp" %>
                <%@include file="serviceChecklist.jsp" %>
                <%@include file="checkListSpec.jsp" %>
            </div>
        </div>
    </div>
</div>