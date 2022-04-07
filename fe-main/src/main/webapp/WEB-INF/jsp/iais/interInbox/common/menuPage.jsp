<div class="row d-flex">
    <div class="col-xs-12 col-md-10">
        <div class="navigation">
            <ul class="nav nav-tabs nav-menu">
                <li class="active"><a href="#"><span>Dashboard</span></a></li> <%--NOSONAR--%>
                <c:set var="roleMeunForEServices" value="${appTab == 1 && dssTab == 1 ? 2 : (appTab == 1 ? 1 : 0)}" />
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
                <li class="dropdown"> <%--NOSONAR--%><a class="dropdown-toggle" data-toggle="dropdown" role="button"
                                                        aria-haspopup="true" aria-expanded="false"
                                                        href="javascript:;"><span>eServices</span></a>
                    <ul class="dropdown-menu">
                        <ul class="nav nav-tabs subtab-nav">
                            <c:if test="${appTab == 1}">
                                <li class="active"><a data-toggle="tab" href="#lics">Licensing </a></li>
                            </c:if>
                            <c:if test="${dssTab == 1}">
                                <li><a data-toggle="tab" href="#datasub">Data Submission</a></li>
                            </c:if>
                        </ul>
                        <c:if test="${roleMeunForEServices ==0}">
                        <div class="tab-content">
                            <c:if test="${dssTab == 1}">
                                <div id="datasub" class="tab-pane fade">
                                    <ul class="subnav-list">
                                        <%@ include file="../../interInbox/app/dataSubmissionMenuParam.jsp" %>
                                        <%@ include file="../../interInbox/app/eServicesMenuParam.jsp" %>
                                    </ul>
                                </div>
                            </c:if>
                        </div>
                    </ul>
                </li>
                </c:if>
                <c:if test="${roleMeunForEServices !=0}">
                <div class="tab-content">
                    <div id="lics" class="tab-pane fade in active">
                        <ul class="subnav-list">
                            </c:if>
                <c:if test="${item.depth > 0}">
                <c:choose>
                <c:when test="${item.depth > 1}">
                    <c:if test="${nextDepth == currDepth}">
                        <c:choose>
                            <c:when test="${fn:contains(item.url,'INTERNET')}">
                                <li><%--NOSONAR--%>
                                    <a href="<c:out value="${item.url}" />" onclick="clickMenu('${item.displayLabel}','${tabCode}PageMenu')" >
                                        <egov-smc:commonLabel><c:out
                                                value="${item.displayLabel}"/></egov-smc:commonLabel>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li> <%--NOSONAR--%>
                                    <a href="#" id="${item.displayLabel}" onclick="clickMenu('${item.displayLabel}','${tabCode}PageMenu')">
                                        <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    <c:if test="${nextDepth < currDepth && roleMeunForEServices != 0}">
                        <c:choose>
                            <c:when test="${fn:contains(item.url,'INTERNET')}">
                                <li> <%--NOSONAR--%>
                                    <a href="<c:out value="${item.url}" />" onclick="clickMenu('${item.displayLabel}','${tabCode}PageMenu')">
                                        <egov-smc:commonLabel><c:out
                                                value="${item.displayLabel}"/></egov-smc:commonLabel>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li> <%--NOSONAR--%>
                                    <a href="#" onclick="clickMenu('${item.displayLabel}','${tabCode}PageMenu')">
                                        <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                        <%@ include file="../../interInbox/app/eServicesMenuParam.jsp" %>
                        <c:if test="${roleMeunForEServices == 1}">
                                   </ul>
                                 </div>
                              </div>
                            </ul>
                          </li>
                        </c:if>
                  <c:if test="${roleMeunForEServices == 2}">
                     </ul>
                     </div>
                     <div id="datasub" class="tab-pane fade">
                     <ul class="subnav-list">
                    <%@ include file="../../interInbox/app/dataSubmissionMenuParam.jsp" %>
                    <%@ include file="../../interInbox/app/eServicesMenuParam.jsp" %>
                     </ul>
                    </div>
                    </div>
                     </ul>
                     </li>
                  </c:if>
                    </c:if>
                </c:when>
                <c:otherwise>
                        <c:if test="${nextDepth == currDepth}">
                        <li>
                            <a href="<c:out value="${item.url}" />" onclick="clickMenu('${item.displayLabel}','${tabCode}PageMenu')">
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
        <div class="dropdown profile-dropdown"><a class="profile-btn btn" id="profileBtn" data-toggle="dropdown"
                                                  aria-haspopup="true" aria-expanded="false" href="javascript:;" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">${iais_Login_User_Info_Attr.userName}</a>
            <ol class="dropdown-menu" aria-labelledby="profileBtn">
                <li class="management-account"><a href="/main-web/eservice/INTERNET/MohFeAdminUserManagement">Manage Account</a></li>
                <li class="logout"><a href="${pageContext.request.contextPath}/eservice/INTERNET/InterLogout">Logout</a></li>
            </ol>
        </div>
    </div>
</div>