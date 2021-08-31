<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="profRegNo" value="${param.profRegNo}" />
<c:set var="cssClass" value="${param.cssClass}" />

<c:if test="${not empty proHashMap[profRegNo]}">
    <div class="disciplinary-record ${cssClass}" style="display: none">
        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;width: 40%">
            <label style="font-weight: normal">The name of this personnel as listed in PRS is:
            </label><span style="position: absolute;right: 0px;color: black"
                          onclick="javascript:closeThis(this)">X</span>
            <table aria-describedby="" border="1px" class="col-xs-12" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff">
                <tr>
                    <th scope="col" style="display: none"></th>
                    <td>${proHashMap[profRegNo].name}</td>
                </tr>
            </table>
        </div>
    </div>
</c:if>