        <%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
            <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
            <%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
                <%
        sop.webflow.rt.api.BaseProcessClass process =
                (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    %>
            <webui:setLayout name="iais-internet"/>
            <div class="main-content">
                <div class="alert alert-info" role="alert">
                    <strong>
                        <h1>Amendment</h1>
                    </strong>
                </div>
                <div>&nbsp</div>
                <h3>You are amending the Clinical Laboratory licence (Licence No. <strong>${prepareTranfer.licenceNo}</strong>)</h3>
                <div>&nbsp</div>
                <div>&nbsp</div>
             <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                <div class="row">
                 <div class="col-xs-12">
                     <div class="table-gp">
                         <table class="table">
                             <tr>
                              <td class="col-xs-6">Licence No.</td>
                              <td class="col-xs-6">${prepareTranfer.licenceNo}</td>
                             </tr>
                             <tr>
                                <td>Service Name</td>
                                <td>${prepareTranfer.serviceName}</td>
                             </tr>
                             <tr>
                                 <td>Select Premises</td>
                                 <td><c:forEach items="${prepareTranfer.appGrpPremisesDtoList}"
                                                var="premises">
                                     <input type="checkbox" name="premisesInput" value="${premises.premisesIndexNo}">${premises.tranferSelect}&nbsp;
                                 </c:forEach>
                                 </td>
                             </tr>
                             <tr>
                                 <td>UEN of Licence to transfer licence to</td>
                                 <td><input type="text" name="UNID"></td>
                             </tr>
                          </table>
                      </div>
                     <p><span class="error-msg">${ErrorMsg}</span></p>
                     <div class="col-xs-12 col-md-4">
                         <a class="back" href="https://egp.sit.inter.iais.com/hcsa-licence-web/eservice/INTERNET/MohRequestForChange?licenceId=${prepareTranfer.licenceId}"><em class="fa fa-angle-left"></em> Back</a>
                     </div>
                     <div class="col-xs-12 col-md-3">
                         <a class="btn btn-primary next premiseId" id="Next">Next</a>
                     </div>

                  </div>
                 </div>
                </form>
            </div>
        <script>
            $(document).ready(function(){
                var hci=${premises.hciName};
                var vecNo=${premises.conveyanceVehicleNo};
                if(hci=="onsite"){
                    $('#rollBackDropdown').addClass('hidden');
                }
            });
            $("#Next").click(function () {
                showWaiting();
                document.getElementById("mainForm").submit();
            })
            function  showWaiting(){
                $.blockUI({message: '<div style="padding:3px;">We are processing your request now, please do not click the Back or Refresh buttons in the browser.</div>',
                    css: {width: '25%', border: '1px solid #aaa'},
                    overlayCSS: {opacity: 0.2}});
            }
        </script>