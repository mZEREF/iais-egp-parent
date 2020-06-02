<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form class="form-horizontal" style="margin-left: 1%;margin-right:1%;width: 100%"  method="post" id="ChangeStatusForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
        <br><br><br><br><br><br>
        <h2>Role Management</h2>
        <iais:section title="" id="addRole">
            <iais:row>
                <iais:field value="UserId"/>
                <iais:value width="7">
                    <p>${userIdName}</p>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field value="Add Role" required="true"/>
                <iais:value width="4">
                    <c:forEach items="${assignRoleOption}" var="role">
                        <div class="form-check">
                            <input class="form-check-input" id="roleId" type="checkbox" name="assignRole" value="${role}">
                            <label class="form-check-label" for="roleId"><span class="check-square"></span></label>
                            <c:out value="${role}"/>
                        </div>
                    </c:forEach>
                    <span id="error_userId" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>
            <iais:action>
                <a style="margin-left: 86%" class="btn btn-primary" href="#" onclick="submit('doDeactivate')">Add</a>
            </iais:action>
        </iais:section>

        <iais:section title="" id="addRole">
            <iais:row>
                <iais:field value="Remove Role"/>
                <iais:value width="4">
                    <c:forEach items="${alreadyAssignRoles}" var="role" varStatus="status">
                        <div class="form-check">
                            <input class="form-check-input" id="removeRoleId" type="checkbox" name="removeRole" value="${alreadyAssignRoleIds[status.index]}"><c:out value="${role}"/>
                            <label class="form-check-label" for="removeRoleId"><span class="check-square"></span></label>
                        </div>
                    </c:forEach>
                </iais:value>
            </iais:row>
            <iais:action>
                <a style="margin-left: 0%" class="back" onclick="submit('back')"><em class="fa fa-angle-left"></em> Back</a>
                <a style="margin-left: 83%" class="btn btn-primary" href="#" onclick="submit('doDeactivate')">Remove</a>
            </iais:action>
        </iais:section>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#ChangeStatusForm").submit();
    }
</script>