<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="CategoryCreateForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Create Master Code Category</h2>
                        </div>
                        <div class="form-group">
                            <iais:field value="Code Category." required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="codeKey" type="text" name="codeKey">
                                    <span id="error_categoryDescription" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Is Editable." required="true"/>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <iais:value>
                                    <div class="col-md-3">
                                        <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="1" name="editable"></div>
                                        <label class="col-md-2 control-label" >Yes</label>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="col-md-1"><input type="radio" style="margin-top: 19px" value="0" name="editable"></div>
                                        <label class="col-md-2 control-label" >No</label>
                                    </div>
                                </iais:value>
                            </div>
                            <div class="col-md-4"></div>
                            <div class="col-md-8">
                                <span id="error_isEditable" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                            <div class="row">
                                <div class="col-xs-2 col-sm-2">
                                    <div><a href="#" onclick="submitAction('back')"><em class="fa fa-angle-left"></em> Back</a></div>
                                </div>
                                <div class="col-xs-10 col-sm-10">
                                    <div class="text-right text-center-mobile">
                                        <button type="button" class="btn btn-primary" onclick="doNext()">Next</button>
                                    </div>
                                </div>
                            </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/include/validation.jsp"%>
</div>

<script>

    function doNext(){
        $("[name='crud_action_type']").val('save');
        $("#CategoryCreateForm").submit();
    }
    function submitAction(action){
        $("[name='crud_action_type']").val(action);
        $("#CategoryCreateForm").submit();
    }
</script>