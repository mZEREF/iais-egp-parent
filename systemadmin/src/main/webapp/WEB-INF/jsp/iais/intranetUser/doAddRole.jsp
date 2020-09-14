<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form class="form-horizontal" style="margin-left: 1%;margin-right:1%;width: 100%" method="post" id="ChangeStatusForm"
      action=<%=process.runtime.continueURL()%>>
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
                <iais:value width="5">
                    <c:forEach items="${assignRoleOption}" var="role">
                        <c:choose>
                            <c:when test="${role=='Approval Officer 1'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="ao1Check" onclick="checkRole()" type="checkbox"
                                           name="assignRoleAo1" value="${role}">
                                    <label class="form-check-label" for="ao1Check"><span
                                            class="check-square"></span></label>
                                    <c:out value="${role}"/>
                                    <div id="ao1" hidden>
                                        <iais:select name="ao1GroupSelect" value="${role}" firstOption="Please Select"
                                                     options="ao1GroupOptions"></iais:select>
                                    </div>
                                </div>
                                <br/>
                            </c:when>
                            <c:when test="${role=='Approval Officer 1 Officer'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="ao1LeadCheck" onclick="checkRole()"
                                           type="checkbox" name="assignRoleAo1Lead" value="${role}">
                                    <label class="form-check-label" for="ao1LeadCheck"><span
                                            class="check-square"></span></label>
                                    <c:out value="${role}"/>
                                    <div id="ao1Lead" hidden>
                                        <iais:select name="ao1GroupLeadSelect" value="${role}" firstOption="Please Select"
                                                     options="ao1GroupOptions"></iais:select>
                                    </div>
                                </div>
                                <br/>
                            </c:when>
                            <c:when test="${role=='Inspector'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="insCheck" onclick="checkRole()" type="checkbox"
                                           name="assignRoleIns" value="${role}">
                                    <label class="form-check-label" for="insCheck"><span
                                            class="check-square"></span></label>
                                    <c:out value="${role}"/>
                                    <div id="ins" hidden>
                                        <iais:select name="insGroupSelect" value="${role}" firstOption="Please Select"
                                                     options="insGroupOptions"></iais:select>
                                    </div>
                                </div>
                                <br/>
                            </c:when>
                            <c:when test="${role=='Inspector Leader'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="insLeadCheck" onclick="checkRole()"
                                           type="checkbox" name="assignRoleInsLead" value="${role}">
                                    <label class="form-check-label" for="insLeadCheck"><span
                                            class="check-square"></span></label>
                                    <c:out value="${role}"/>
                                    <div id="insLead" hidden>
                                        <iais:select name="insGroupLeadSelect" value="${role}" firstOption="Please Select"
                                                     options="insGroupOptions"></iais:select>
                                    </div>
                                </div>
                                <br/>
                            </c:when>
                            <c:when test="${role=='Professional Screening Officer'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="psoCheck" onclick="checkRole()" type="checkbox"
                                           name="assignRolePso" value="${role}">
                                    <label class="form-check-label" for="psoCheck"><span
                                            class="check-square"></span></label>
                                    <c:out value="${role}"/>
                                    <div id="pso" hidden>
                                        <iais:select name="psoGroupSelect" value="${role}" firstOption="Please Select"
                                                     options="psoGroupOptions"></iais:select>
                                    </div>
                                </div>
                                <br/>
                            </c:when>
                            <c:when test="${role=='Professional Screening Officer Leader'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="psoLeadCheck" onclick="checkRole()"
                                           type="checkbox" name="assignRolePsoLead" value="${role}">
                                    <label class="form-check-label" for="psoLeadCheck"><span
                                            class="check-square"></span></label>
                                    <c:out value="${role}"/>
                                    <div id="psoLead" hidden>
                                        <iais:select name="psoGroupLeadSelect" value="${role}" firstOption="Please Select"
                                                     options="ao1GroupOptions"></iais:select>
                                    </div>
                                </div>
                                <br/>
                            </c:when>
                            <c:otherwise>
                                <div class="form-check">
                                    <input class="form-check-input" id="roleId" type="checkbox" name="assignRoleOther"
                                           value="${role}">
                                    <label class="form-check-label" for="roleId"><span
                                            class="check-square"></span></label>
                                    <c:out value="${role}"/>
                                </div>
                                <br/>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <span id="error_userId" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>
            <iais:action>
                <a style="margin-left: 86%" class="btn btn-primary" href="#" onclick="submit('doDeactivate')">Add</a>
            </iais:action>
        </iais:section>

        <iais:section title="" id="removeRole">
            <iais:row>
                <iais:field value="Remove Role"/>
                <iais:value width="5">
                    <c:forEach items="${alreadyAssignRoles}" var="role" varStatus="status">
                        <div class="form-check">
                            <input class="form-check-input" id="removeRoleId" type="checkbox" name="removeRole"
                                   value="${alreadyAssignRoleIds[status.index]}">
                            <label class="form-check-label" for="removeRoleId"><span
                                    class="check-square"></span></label>
                            <c:out value="${role}"/>
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

    function checkRole() {
        if ($('#ao1Check').is(':checked')) {
            $("#ao1").show();
        } else {
            $("#ao1").hide();
        }
        if ($('#ao1LeadCheck').is(':checked')) {
            $("#ao1Lead").show();
        } else {
            $("#ao1Lead").hide();
        }
        if ($('#insCheck').is(':checked')) {
            $("#ins").show();
        } else {
            $("#ins").hide();
        }
        if ($('#insLeadCheck').is(':checked')) {
            $("#insLead").show();
        } else {
            $("#insLead").hide();
        }
        if ($('#psoCheck').is(':checked')) {
            $("#pso").show();
        } else {
            $("#pso").hide();
        }
        if ($('#psoLeadCheck').is(':checked')) {
            $("#psoLead").show();
        } else {
            $("#psoLead").hide();
        }
    }


</script>