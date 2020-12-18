<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<!DOCTYPE html>
<head>
    <title>Query Helper View Page</title>
</head>
<body>
<div class="main-content" style="padding-top: 1%">
    <div class="container">
        <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
            <div id="processingDecision">
                <iais:row>
<%--                    <input type="text" maxlength="999" name="querySql" id="querySql">--%>
                    <textarea style="width: 627px;height: 160px;" maxlength="999" name="querySql" id="querySql">
                        <c:out value="${querySql}"></c:out>
                    </textarea>
                    <iais:value width="10">
                        <iais:select cssClass="moduleNameDropdown" name="moduleNameDropdown" id="moduleNameDropdown"
                                     options="moduleNameDropdown" value="${moduleNameDropdownValue}"></iais:select>
                    </iais:value>
                    <button name="submitBtn" id="submitButton" type="button" class="btn btn-primary">
                        Query
                    </button>
                </iais:row>
            </div>
            <div class="tab-content">
                <c:if test="${queryResult == N}">
                    <tr>
                        <td colspan="5" align="center">
                            <iais:message key="GENERAL_ACK018"
                                          escape="true"/>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${!empty QueryHelperResultDto.columnNameList}">
                    <table class="table">
                        <thead>
                        <tr>
                            <th width="20%">index</th>
                            <c:forEach items="${QueryHelperResultDto.columnNameList}"
                                       var="columnName">
                                <th width="20%"><c:out value="${columnName}"></c:out></th>
                            </c:forEach>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${QueryHelperResultDto.searchResult}"
                                   var="resultList" varStatus="index">
                            <tr>
                                <td width="20%">
                                    <p><c:out value="${index.count}"></c:out></p>
                                </td>
                                <c:forEach items="${resultList}"
                                           var="result">
                                    <td width="20%">
                                        <p><c:out value="${result}"></c:out></p>
                                    </td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </div>
        </form>

    </div>
</div>
</body>
<script type="text/javascript">
    $(document).ready(function () {

    });

    $("#submitButton").click(function () {
        if(queryValidate()){
            var sql = $('#querySql').val();
            var moduleNameDropdown = $('#moduleNameDropdown').val();
            // showWaiting();
            document.getElementById("mainForm").submit();
            $("#submitButton").attr("disabled", true);
        }else{
            alert('sql is null');
        }
    });

    function queryValidate(){
        var sql = $('#querySql').val();
        var flag = true;
        if(sql == null || sql == undefined || sql == ''){
            flag = false;
        }
        return flag;
    }

    function showWaiting() {
        $.blockUI({
            message: '<div style="padding:3px;">We are processing your request now; Please do not click the Back or Refresh button in the browser.</div>',
            css: {width: '25%', border: '1px solid #aaa'},
            overlayCSS: {opacity: 0.2}
        });
    }
</script>
</html>
