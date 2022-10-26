<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<webui:setLayout name="iais-internet"/>

<div class="container">
  <br>
  <div id="printContent">
    <div class="row">
      <div class="col-xs-12">
        <div class="center-content">
          <div class="form-horizontal">
            <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Approved Facility Certifier</h3>
            <div class="form-group ">
              <div class="col-sm-5 control-label">
                <label>Has the facility appointed an Approved Facility Certifier </label>
              </div>
              <div class="col-sm-6 col-md-7">
                <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                  <label for="hasAppointedCertifier">Yes</label>
                  <input type="radio" name="appointed" id="hasAppointedCertifier" value="Y" <c:if test="${afc.appointed eq 'Y'}">checked="checked"</c:if> disabled />
                </div>
                <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                  <label for="notAppointedCertifier">No</label>
                  <input type="radio" name="appointed" id="notAppointedCertifier" value="N" <c:if test="${afc.appointed eq 'N'}">checked="checked"</c:if> disabled />
                </div>
              </div>
            </div>

            <c:if test="${afc.appointed ne 'Y'}">
              <div class="form-group">
                <div class="col-sm-5 control-label">
                  <label>Select Approved Facility Certifier </label>
                </div>
                <div class="col-sm-6 col-md-7">
                  <label><c:out value="${afc.afc}"/></label>
                </div>
              </div>

              <c:if test="${afc.selectReason ne null && afc.selectReason ne ''}">
                <div class="form-group">
                  <div class="col-sm-5 control-label">
                    <label>Reasons for choosing this AFC </label>
                  </div>
                  <div class="col-sm-6 col-md-7">
                    <label><c:out value="${afc.selectReason}"/></label>
                  </div>
                </div>
              </c:if>
            </c:if>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
  $(function () {
    $('.collapse').collapse();
    document.body.innerHTML = document.getElementById('printContent').innerHTML;
    setTimeout(function () {
      window.print();
    }, 1000);
  });
</script>