<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 9/4/2019
  Time: 4:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
    .form-check-gp{
        width: 50%;
        float:left;
    }

</style>


<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12">
                        <td>
                            <label class="col-xs-12 col-md-3 control-label">Domain Type</label>
                            <div class="col-xs-12 col-md-5">
                                <iais:select name="domainType" options="domainTypeSelect" firstOption="Please select" value="${parameterRequestDto.domainType}" ></iais:select>
                            </div>
                        </td>
                    </div>

                    <div class="col-xs-12">
                        <td>
                            <label class="col-xs-12 col-md-3 control-label">Module</label>
                            <div class="col-xs-12 col-md-5">
                                <iais:select name="module" options="moduleTypeSelect" firstOption="Please select" value="${parameterRequestDto.module}"></iais:select>
                            </div>
                        </td>
                    </div>

                    <div class="col-xs-12">
                        <td>
                            <label class="col-xs-12 col-md-3 control-label">Description</label>
                            <div class="col-xs-12 col-md-5">
                                <input type="text" name="description" value="${parameterRequestDto.description}" />
                            </div>
                        </td>
                    </div>

                    <div class="col-xs-12">
                        <td>
                            <label class="col-xs-12 col-md-3 control-label">Value</label>
                            <div class="col-xs-12 col-md-5">
                                <input type="text" name="value" value="${parameterRequestDto.value}" />
                            </div>
                        </td>
                    </div>

                </div>
            </div>

            <div class="application-tab-footer">
                <td>
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doQuery();">Search</a>

                    </div>

                </td>
            </div>
        </div>
    </div>
</>

<script type="text/javascript">


    function doEdit(id){
        if(confirm('are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("mainForm", "doEdit", id);
        }
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm","cancel");
    }

</script>
