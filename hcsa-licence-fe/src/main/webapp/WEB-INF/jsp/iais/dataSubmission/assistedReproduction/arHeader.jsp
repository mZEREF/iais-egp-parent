<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div class="dashboard" id="comDashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
    <div class="container">
        <div class="navigation-gp">
            <c:if test="${appeal!='appeal'}">
                <div class="row d-flex">
                    <%@ include file="/WEB-INF/jsp/iais/common/dashboardDropDown.jsp" %>
                </div>
            </c:if>
            <div class="row">

                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>${title}</h1>
                        <c:if test="${not empty smallTitle}">
                            <p>${smallTitle}</p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
