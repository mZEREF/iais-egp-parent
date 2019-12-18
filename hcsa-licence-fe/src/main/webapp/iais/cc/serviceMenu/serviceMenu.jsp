<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="container">
    <div class="component-gp">
        <br>
        <form  method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
            <%@ include file="/include/formHidden.jsp" %>
            <%--Validation fields Start--%>
            <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.ServiceMenuDelegator"/>
            <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto"/>

            <input type="hidden" name="chosedservicelist" id="chosedservicelist" value="">
            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="self-assessment-checkbox-gp gradient-light-grey">
                        <p class="assessment-title">Base Services</p>
                        <div class="form-check-gp">
                            <c:forEach var="base" items="${baseService}">
                                <div class="form-check">
                                    <input class="form-check-input" name="chk" id="clinical" type="checkbox" aria-invalid="false" value="${base.getId()}">
                                    <label class="form-check-label" for="clinical"><span class="check-square"></span>${base.getSvcName()}</label>
                                </div>
                            </c:forEach>
                        </div>
                        <p class="assessment-title">Specified Services</p>
                        <div class="form-check-gp">
                            <c:forEach var="specified" items="${specifiedService}">
                                <div class="form-check">
                                    <input class="form-check-input" name="chk"  id="clinical" type="checkbox" aria-invalid="false" value="${specified.getId()}">
                                    <label class="form-check-label" for="clinical"><span class="check-square"></span>${specified.getSvcName()}</label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        <br>
            <div class="row">
                <div class="col-xs-12 col-md-4">
                </div>
                <div class="col-xs-10 col-md-3">
                    <div class="components">
                        <a class="btn btn-secondary " id="submitService" >Submit</a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">

    $(document).ready(function() {
        $('#submitService').click(function(){
            var arr = "";

            $('input[type="checkbox"][name="chk"]:checked').each(
                function() {
                    arr += $(this).val()+",";
                }
            );

            console.log(arr +"-----<")
            $('#chosedservicelist').attr("value",arr);

            SOP.Crud.cfxSubmit("mainForm", "validation");
        });


    });
</script>
