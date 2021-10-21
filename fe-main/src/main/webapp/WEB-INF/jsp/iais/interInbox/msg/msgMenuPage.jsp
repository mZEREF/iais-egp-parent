<div class="row d-flex">
    <div class="col-xs-12 col-md-10">
        <div class="navigation">
            <ul class="nav nav-tabs nav-menu">
                <li class="active"><a href="#"><span>Dashboard</span></a></li> <%--NOSONAR--%>
                <menu:load id="inbox-top-menus">
                    <menu:include name="INTER_INBOX"/>
                </menu:load>
                <menu:iterate id="inbox-top-menus" var="item" varStatus="status">
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
                                <li> <%--NOSONAR--%>
                                    <a href="<c:out value="${item.url}"/>" onclick="clickMenu('${item.displayLabel}','msgPageMenu')">
                                        <egov-smc:commonLabel ><c:out
                                                value="${item.displayLabel}"/></egov-smc:commonLabel>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li><%--NOSONAR--%>
                                    <a href="#" onclick="clickMenu('${item.displayLabel}','msgPageMenu')">
                                        <egov-smc:commonLabel><c:out
                                                value="${item.displayLabel}"/></egov-smc:commonLabel>
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    <c:if test="${nextDepth < currDepth}">
                        <c:choose>
                            <c:when test="${fn:contains(item.url,'INTERNET')}">
                                <li>  <%--NOSONAR--%>
                                    <a href="<c:out value="${item.url}"/>" onclick="clickMenu('${item.displayLabel}','msgPageMenu')">
                                        <egov-smc:commonLabel><c:out
                                                value="${item.displayLabel}"/></egov-smc:commonLabel>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li>  <%--NOSONAR--%>
                                    <a href="#" onclick="clickMenu('${item.displayLabel}','msgPageMenu')">
                                        <egov-smc:commonLabel><c:out
                                                value="${item.displayLabel}"/></egov-smc:commonLabel>
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                        <%@ include file="../../interInbox/app/eServicesMenuParam.jsp" %>
                        <%@ include file="../../interInbox/app/dataSubmissionMenuParam.jsp" %>
                        </ol>
                    </c:if>
                </c:when>
                <c:otherwise>
                <c:if test="${nextDepth > currDepth}">
                <li class="dropdown">  <%--NOSONAR--%><a class="dropdown-toggle" data-toggle="dropdown" role="button"
                                        aria-haspopup="true" aria-expanded="false"
                                        href="javascript:;"><span>${item.displayLabel}</span></a>
                    <ol class="dropdown-menu">
                        </c:if>
                        <c:if test="${nextDepth == currDepth}">
                        <li>
                            <a href="<c:out value="${item.url}" />" onclick="clickMenu('${item.displayLabel}','msgPageMenu')">
                                <egov-smc:commonLabel><c:out
                                        value="${item.displayLabel}"/></egov-smc:commonLabel>
                            </a>
                        </li>
                        </c:if>
                        </c:otherwise>
                        </c:choose>
                        </c:if>
                        </menu:iterate>
            </ul>
        </div>
    </div>
    <div class="col-xs-10 col-xs-offset-1 col-lg-offset-0 col-lg-2">
        <div class="dropdown profile-dropdown">
            <a class="profile-btn btn" id="profileBtn" data-toggle="dropdown" href="javascript:;" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">${iais_Login_User_Info_Attr.userName}</a>
            <ol class="dropdown-menu" aria-labelledby="profileBtn">
                <li class="management-account"><a href="/main-web/eservice/INTERNET/MohFeAdminUserManagement">Manage Account</a></li>
                <li class="logout"><a href="${pageContext.request.contextPath}/eservice/INTERNET/InterLogout">Logout</a></li>
            </ol>
        </div>
    </div>
</div>