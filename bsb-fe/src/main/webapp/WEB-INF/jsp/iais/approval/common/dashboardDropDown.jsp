<c:set var="loginContext" value="${iais_Login_User_Info_Attr}"/>
<div class="col-xs-10 col-xs-offset-1 col-lg-offset-10 col-lg-2">
    <div class="dropdown profile-dropdown"><a class="profile-btn btn" id="profileBtn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" href="javascript:;">${loginContext.userName}</a>
        <ul class="dropdown-menu" aria-labelledby="profileBtn">
            <li class="dashboard-icon"><a href="/main-web">Dashboard</a></li>
            <li class="logout"><a href="${pageContext.request.contextPath}/eservice/INTERNET/InterLogout">Logout</a></li>
        </ul>
    </div>
</div>