<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/3
  Time: 10:49
  To change this template use File | Settings | File Templates.
--%>


<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <div class="prelogin" style="background-image: url('/web/themes/fe/img/prelogin-masthead-banner.jpg');">
            <div class="container">
                <div class="col-xs-12">
                    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                        <div class="panel panel-default">
                            <div class="panel-heading completed" id="headingPremise" role="tab">
                                <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">Declares</a></h4>
                            </div>
                            <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
                                <div class="panel-body">
                                    <p class="text-right"></p>
                                    <div class="panel-main-content">
                                        <div class="preview-info">
                                            <p>If the new CorpPass user declares that he/she has been delegated by a key appointment holder to act on behalf of the entity,
                                                he or she will be designated as and given the role of a HCSA Internet Company Administrator. Otherwise, the user will not be allowed to access the System.</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                    <div class="form-check">
                        <input class="form-check-input"  type="checkbox" name="declarationCheckbox" value="Y" aria-invalid="false">
                        <label class="form-check-label" ><span class="check-square"></span>Yes, I have authorization</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input"  type="checkbox" name="declarationCheckbox" value="N" aria-invalid="false">
                        <label class="form-check-label" ><span class="check-square"></span>&lt;No&gt;</label>
                    </div>
                </div>

                <div class="text-right text-center-mobile">
                    <a class="btn btn-primary next" href="javascript:void(0);"
                       onclick="Utils.submit('mainForm', 'doSubmit')">Submit</a>

                </div>

            </div>
        </div>
    </form>
</div>


<%@include file="/include/utils.jsp"%>
<script>
    $(":checkbox").bind("click",function(){
            $(":checkbox").removeAttr("checked");
            $(this).attr("checked","checked");
        }
    );

</script>