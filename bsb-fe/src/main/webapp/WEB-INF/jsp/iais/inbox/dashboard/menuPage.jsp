<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>

<style>
    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu {
        width: auto;
        padding: 0;
        border-radius: 0;
    }

    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li.bsb-menu-cate {
        width: 290px;
        padding: 5px 0;
        border-radius: 0;
        text-align: center;
    }

    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li.bsb-menu-cate a.bsb-menu-tab {
        font-weight: bold;
    }

    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li.bsb-menu-item {
        display: block;
        width: 290px;
    }

    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li.bsb-menu-item-app {
        width: 49%;
        min-width: 284px;
        max-width: 290px;
    }

</style>
<script>
    $(function () {
        $("a.bsb-menu-tab").mouseover(function () {
            $(this).tab("show");
        });
    });
</script>

<div class="row">
    <div class="col-xs-12 col-md-10">
        <div class="navigation">
            <ul class="nav nav-tabs nav-menu">
                <li><a href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg" style="cursor: pointer"><span>Dashboard</span></a></li>
                <menu:load id="inbox-top-menus">
                    <menu:include name="BSB_INTER_INBOX"/>
                </menu:load>

                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" role="button"
                       aria-haspopup="true" aria-expanded="false"
                       href="javascript:void(0);"><span>eServices</span></a>
                    <ul class="dropdown-menu menuDropHeight" style="z-index: 10000;">
                        <ul class="nav nav-tabs subtab-nav" style="margin: 0;">
                            <c:set var="firstMenuTab" value="${true}"/>
                            <menu:iterate id="inbox-top-menus" var="item" varStatus="status" >
                                <c:if test="${item.depth eq 1}">
                                    <li class='bsb-menu-cate <c:if test="${firstMenuTab}">active</c:if>'><a class="bsb-menu-tab" data-toggle="tab" href="#${item.left}MenuTab">${item.displayLabel}</a></li>
                                    <c:set var="firstMenuTab" value="${false}"/>
                                </c:if>
                            </menu:iterate>
                        </ul>
                        <div class="tab-content">
                            <c:set var="firstMenuTab" value="${true}"/>
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
                                            <c:choose>
                                                <c:when test="${nextDepth < currDepth}">
                                                    <li class="<c:choose><c:when test='${item.fullPath.contains("Application")}'>bsb-menu-item-app</c:when><c:otherwise>bsb-menu-item</c:otherwise></c:choose>"><a href="${item.url}"><egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel></a></li>
                                                    </ul></div>
                                                </c:when>
                                                <c:otherwise>
                                                    <li class="<c:choose><c:when test='${item.fullPath.contains("Application")}'>bsb-menu-item-app</c:when><c:otherwise>bsb-menu-item</c:otherwise></c:choose>"><a href="${item.url}"><egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel></a></li>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <div id="${item.left}MenuTab" class='tab-pane fade in <c:if test="${firstMenuTab}">active</c:if>'>
                                                <ul class="subnav-list">
                                            <c:set var="firstMenuTab" value="${false}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </menu:iterate>
                        </div>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <div class="col-xs-10 col-xs-offset-1 col-lg-offset-0 col-lg-2">
        <div class="dropdown profile-dropdown">
            <a class="profile-btn btn" id="profileBtn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" href="javascript:" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">${iais_Login_User_Info_Attr.userName}</a>
            <ol class="dropdown-menu" aria-labelledby="profileBtn">
                <li class="management-account"><a href="/main-web/eservice/INTERNET/MohFeAdminUserManagement">Manage Account</a></li>
                <li class="logout"><a href="${pageContext.request.contextPath}/eservice/INTERNET/InterLogout">Logout</a></li>
            </ol>
        </div>
    </div>
</div>