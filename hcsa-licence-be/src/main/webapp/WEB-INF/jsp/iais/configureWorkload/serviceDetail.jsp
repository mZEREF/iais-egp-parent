<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-intranet"/>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<div class="main-content">
    <form id="mainForm1" method="post" action=<%=process.runtime.continueURL()%>>
        <div class="col-xs-12 col-sm-12 col-md-12">
            <div class="center-content">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <h3>
                        <span>${stageValue}</span>
                    </h3>

                    <div class="table-gp">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr >
                                <th scope="col" >S/N</th>
                                <th scope="col" >Service</th>
                                <th scope="col" >Workload Manhours<br>(For illustration only)</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${stageList}" varStatus="status">
                                <tr>
                                    <td>
                                            ${status.index + 1}
                                    </td>
                                    <td>
                                            ${item.serviceName}
                                    </td>
                                    <td>
                                        <input name="service${status.index + 1}" value="${item.manhourCount}">
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="col-xs-12 col-sm-6">
                <div class="text-right text-center-mobile"><a class="btn btn-primary next"
                                                              href="javascript:void(0);"
                                                              onclick="javascript: doSubmit();">Submit</a></div>
            </div>

        </div>
    </form>
</div>

<script>
    function doSubmit() {
        SOP.Crud.cfxSubmit("mainForm1", "submit");
    }
</script>