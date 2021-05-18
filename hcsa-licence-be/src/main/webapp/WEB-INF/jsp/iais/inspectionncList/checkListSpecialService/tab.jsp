<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li class="complete ${(errorSpecTab == null || errorSpecTab== 'General') ? 'active' : ''}" role="presentation"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General Regulations</a></li>
    <c:forEach var="service" items="${specialServiceForChecklistDecideDtos}" >
        <c:set value = "ServiceInfo${service.identify}" var = "errorTabName"/>
        <li class="complete ${errorSpecTab == errorTabName ? 'active' : ''}" role="presentation"><a href="#ServiceInfo${service.identify}" aria-controls="ServiceInfo${service.identify}" role="tab"
                                                    data-toggle="tab"><c:out value="${service.serviceName}"/></a></li>
      </c:forEach>
</ul>

<div class="tab-nav-mobile visible-xs visible-sm">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General</a></div>
        <c:forEach var="service" items="${specialServiceForChecklistDecideDtos}" >
            <div class="swiper-slide"><a href="#ServiceInfo${service.identify}" aria-controls="ServiceInfo${service.identify}" role="tab" data-toggle="tab"><c:out value="${service.serviceName}"/></a></div>
        </c:forEach>

    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>



