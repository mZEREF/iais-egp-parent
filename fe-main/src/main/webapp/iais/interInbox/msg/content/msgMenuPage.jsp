<div class="row">
    <div class="col-xs-12 col-md-10">
        <div class="navigation">
            <div class="nav nav-tabs nav-menu">
                <li class="active"><a href="#"><span>Dashboard</span></a></li>
                <menu:load id="inbox-top-menus">
                    <menu:include name="INTER_INBOX"/>
                </menu:load>
                <menu:iterate id="inbox-top-menus" var="item" varStatus="status" >
                    <c:choose>
                        <c:when test="${!status.last and status.next.depth > 1}">
                            <c:set var="nextDepth" value="${status.next.depth}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="nextDepth" value="1"/>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${item.depth >= 1}">
                            <c:set var="currDepth" value="${item.depth}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="currDepth" value="1"/>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${item.depth > 0}">
                        <c:choose>
                            <c:when test="${item.depth > 1}">
                                <c:if test="${nextDepth == currDepth}">
                                    <c:choose>
                                        <c:when test="${fn:contains(item.url,'INTERNET')}">
                                            <li>
                                                <a href="<c:out value="${item.url}" />">
                                                    <egov-smc:commonLabel><c:out
                                                            value="${item.displayLabel}"/></egov-smc:commonLabel>
                                                </a>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li>
                                                <a href="#">
                                                    <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                                                </a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${nextDepth < currDepth}">
                                        <c:choose>
                                            <c:when test="${fn:contains(item.url,'INTERNET')}">
                                                <li>
                                                    <a href="<c:out value="${item.url}" />">
                                                        <egov-smc:commonLabel><c:out
                                                                value="${item.displayLabel}"/></egov-smc:commonLabel>
                                                    </a>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <li>
                                                    <a href="#">
                                                        <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                                                    </a>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                        <li class="divider" role="separator"></li>
                                        <li><a href="#">Step-by-step guide to eServices</a></li>
                                    </ul>
                                    </c:if>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${nextDepth > currDepth}">
                                    <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" role="button"
                                    aria-haspopup="true" aria-expanded="false"
                                    href="javascript:;"><span>${item.displayLabel}</span></a>
                                        <ul class="dropdown-menu">
                                </c:if>
                                <c:if test="${nextDepth == currDepth}">
                                    <li>
                                        <a href="<c:out value="${item.url}" />">
                                            <egov-smc:commonLabel><c:out
                                                    value="${item.displayLabel}"/></egov-smc:commonLabel>
                                        </a>
                                    </li>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </menu:iterate>
                <li><a href="#"><span>Activity Log</span></a></li>
                <li><a href="#"><span>Licensee Details</span></a></li>
            </div>
        </div>
    </div>
    <div class="col-xs-10 col-xs-offset-1 col-lg-offset-0 col-lg-2">
        <div class="dropdown profile-dropdown"><a class="profile-btn btn" id="profileBtn" data-toggle="dropdown"
                                                  aria-haspopup="true" aria-expanded="false" href="javascript:;">${iais_Login_User_Info_Attr.loginId}</a>
            <ul class="dropdown-menu" aria-labelledby="profileBtn">
                <li class="management-account"><a href="#">Manage Account</a></li>
                <li class="logout"><a href="${pageContext.request.contextPath}/eservice/INTERNET/InterLogout">Logout</a></li>
            </ul>
        </div>
    </div>
</div>
</div>