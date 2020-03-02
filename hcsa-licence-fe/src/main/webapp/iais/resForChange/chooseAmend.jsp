<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<style>
    .margin-botton-10{
        margin-bottom: 10px;
    }
</style>
<div class="tab-pane" id="tabApp" role="tabpanel">
    <form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" id="hiddenIndex" name="hiddenIndex" value=""/>
        <div class="row col-xs-12"></div>

        <%@include file="amendHeader.jsp"%>
        <div class="col-xs-12">
            <br/><br/>
            <h2>Please select the type of amendment</h2>
            <p><span class="error-msg">${ErrorMsg}</span></p>

            <p>
                <input type="hidden" id="amendTypeHidden" value="${AmendType}"/>
            <div class="form-check">
                <input class="form-check-input amendType" id="amend-licensee" type="radio"
                       name="amendType" value="0"/>
                <label class="form-check-label"><span for="amend-licensee"
                                                      class="check-circle"></span>Change of Licensee
                </label>
            </div>
            </p>
            <p>
            <div class="form-check">
                <input class="form-check-input amendType" id="amend-licence" type="radio"
                       name="amendType" value="1"/>
                <label class="form-check-label"><span for="amend-licence"
                                                      class="check-circle"></span>Change of Licence Information
                </label>
            </div>
            </p>


        </div>

        <div class="row col-xs-12 margin-botton-10">
            <div class="col-xs-12 col-md-4">
                <a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-md-3">
                <a class="btn btn-primary next premiseId" id="Next">Next</a>
            </div>
        </div>
        <div class="row-col-xs-12">

        </div>
        <div class="row">
        </div>
    </form>
</div>
<script>
    $(document).ready(function () {

        amendType();

        $('#Next').click(function () {
            $("[name='crud_action_type_form_value']").val('doAmend');
            $('#menuListForm').submit();
        });


    });


    var amendType = function () {
        $('.amendType').click(function () {
            var val = $(this).val();
            if ("1" == val) {
                $('div.amend-type-info').removeClass('hidden');
                $('div.amend-tranfer').addClass('hidden');
            } else {
                $('div.amend-type-info').addClass('hidden');
                $('div.amend-tranfer').removeClass('hidden');
            }
        });
    }

</script>