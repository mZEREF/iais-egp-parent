<c:set var="loginContext" value="${iais_Login_User_Info_Attr}"/>
<div class="col-xs-10 col-xs-offset-1 col-lg-offset-9 col-lg-3">
    <div class="dropdown profile-dropdown"><a class="profile-btn btn" id="profileBtn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" href="javascript:;">${loginContext.userName}</a>
        <ul class="dropdown-menu" aria-labelledby="profileBtn">
            <li class="dashboard-icon"><a href="/main-web">Dashboard</a></li>
            <li class="logout"><a href="${pageContext.request.contextPath}/eservice/INTERNET/InterLogout">Logout</a></li>
        </ul>
    </div>
</div>