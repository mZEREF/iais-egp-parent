<div class="row">
    <div class="col-xs-12 col-md-9">
        <div class="navigation">
            <div class="nav nav-tabs nav-menu">
                <li class="active"><a href="#"><span>Dashboard</span></a></li>
                <menu:load id="inbox-top-menus">
                    <menu:include name="INTER_INBOX"/>
                </menu:load>
                <menu:iterate id="inbox-top-menus" var="item" varStatus="status">
                    <c:if test="${item.depth > 0 && !empty item.url}">
                        <c:choose>
                            <c:when test="${item.depth > 1}">
                                <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" role="button"
                                                        aria-haspopup="true" aria-expanded="false"
                                                        href="javascript:;"><span>eServices</span></a>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <a href="<c:out value="${item.url}" />">
                                                <egov-smc:commonLabel><c:out
                                                        value="${item.displayLabel}"/></egov-smc:commonLabel>
                                            </a>
                                        </li>
                                        <li class="divider" role="separator"></li>
                                        <li><a href="#">Step-by-step guide to eServices</a></li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li>
                                    <a href="<c:out value="${item.url}" />">
                                        <egov-smc:commonLabel><c:out
                                                value="${item.displayLabel}"/></egov-smc:commonLabel>
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </menu:iterate>
                <li><a href="#"><span>Activity Log</span></a></li>
                <li><a href="#"><span>Licensee Details</span></a></li>
            </div>
        </div>
    </div>
    <div class="col-xs-10 col-xs-offset-1 col-lg-offset-0 col-lg-3">
        <div class="dropdown profile-dropdown"><a class="profile-btn btn" id="profileBtn" data-toggle="dropdown"
                                                  aria-haspopup="true" aria-expanded="false" href="javascript:;">Tan Mei
            Ling Joyce</a>
            <ul class="dropdown-menu" aria-labelledby="profileBtn">
                <li class="management-account"><a href="#">Manage Account</a></li>
                <li class="logout"><a href="#">Logout</a></li>
            </ul>
        </div>
    </div>
</div>
</div>