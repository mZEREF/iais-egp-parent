<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<li class="divider" role="separator"></li>
<li>
    <a href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=toElis">Go To eLis</a>
</li>
<c:if test="${ConfigHelper.getString('go.to.alert.switch','0') == '1'}">
    <li>
        <a href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=toMOHAlert">Go To MOH Alert</a>
    </li>
</c:if>
<br>
<li><a href="/main-web/eservice/INTERNET/MohAccessmentGuide">Step-by-step guide to eServices</a></li>
