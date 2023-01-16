<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<div class="flex-shrink-0 arrow-adj-left hidden-xs hidden-sm">
    <a href="#" class="btn-left btn-link p-2 toggle text-dark" id="sliderBack"><i class="fa fa-angle-left fa-3x"></i></a>
</div>
<div class="flex-grow-1 o-hidden hidden-xs hidden-sm" id="container-nav-scroll">
<ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li class="complete ${(errorSpecTab == null || errorSpecTab== 'General') ? 'active' : ''}" role="presentation"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General Regulations</a></li>
    <li class="complete ${errorSpecTab == 'ServiceInfo' ? 'active' : ''}" role="presentation"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab" data-toggle="tab"><c:out value="${serListDto.serviceName}"/></a></li>
    <c:forEach var="service" items="${specialServiceForChecklistDecideDtos}" >
        <c:set value = "ServiceInfo${service.identify}" var = "errorTabName"/>
        <li class="complete ${errorSpecTab == errorTabName ? 'active' : ''}" role="presentation"><a href="#ServiceInfo${service.identify}" aria-controls="ServiceInfo${service.identify}" role="tab"
                                                    data-toggle="tab"><c:out value="${service.serviceTab}"/></a></li>
      </c:forEach>
</ul>
</div>
<div class="flex-shrink-0 arrow-adj-right hidden-xs hidden-sm">
    <a href="#" class="btn-right btn-link toggle p-2 text-dark" id="sliderForward"><i class="fa fa-angle-right fa-3x"></i></a>
</div>

<div class="tab-nav-mobile visible-xs visible-sm">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General</a></div>
        <div class="swiper-slide"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab" data-toggle="tab"><c:out value="${serListDto.serviceName}"/></a></div>
        <c:forEach var="service" items="${specialServiceForChecklistDecideDtos}" >
            <div class="swiper-slide"><a href="#ServiceInfo${service.identify}" aria-controls="ServiceInfo${service.identify}" role="tab" data-toggle="tab"><c:out value="${service.serviceTab}"/></a></div>
        </c:forEach>

    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>



