<c:set var="bat" value="${processDto.incidentBatViewDto}"></c:set>
<div class="row">
  <div class="col-xs-12">
    <div class="table-gp">
      <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
        <tbody>
        <tr>
          <th scope="col" style="display: none"></th>
        </tr>
        <tr>
          <td style="text-align: center">Facility Classification</td>
          <td style="text-align: center"><c:out value="${bat.facClassification}"/></td>
        </tr>
        <tr>
          <td style="text-align: center">Service Type</td>
          <td style="text-align: center"><c:out value="${bat.serviceType}"/></td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>
<div class="row" style="margin-top: 30px">
  <div class="col-xs-12">
    <c:forEach var="type" items="${scheduleType}" varStatus="status">
      <c:set var="bats" value="${bat.batInfoDtoMap.get(type)}"/>
      <c:if test="${not empty bats}">
        <div class="panel-group" role="tablist" aria-multiselectable="true">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewInfo${status.index}"><iais:code code="${type}"/></a>
              </h4>
            </div>
            <div id="previewInfo${status.index}" class="panel-collapse collapse">
              <div class="panel-body">
                <c:forEach var="info" items="${bats}">
                  <div class="panel-main-content form-horizontal min-row">
                    <div class="form-group">
                      <label class="col-xs-5 col-md-4 control-label">Approval No</label>
                      <div class="col-sm-7 col-md-5 col-xs-7"><p><c:out value="${info.approvalNo}"/></p></div>
                      <div class="clear"></div>
                    </div>
                    <div class="form-group">
                      <label class="col-xs-5 col-md-4 control-label">Biological Agents / Toxins</label>
                      <div class="col-sm-7 col-md-5 col-xs-7"><p><c:out value="${info.bat}"/></p></div>
                      <div class="clear"></div>
                    </div>
                    <div class="form-group">
                      <label class="col-xs-5 col-md-4 control-label">Physical Possession</label>
                      <div class="col-sm-7 col-md-5 col-xs-7"><p><c:out value="${info.physicalPossession}"/></p></div>
                      <div class="clear"></div>
                    </div>
                    <div class="form-group">
                      <label class="col-xs-5 col-md-4 control-label">Quantity</label>
                      <div class="col-sm-7 col-md-5 col-xs-7"><p><c:out value="${info.quantity}"/></p></div>
                      <div class="clear"></div>
                    </div>
                    <div class="form-group">
                      <label class="col-xs-5 col-md-4 control-label">Nature of samples</label>
                      <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                      <div class="clear"></div>
                    </div>
                  </div>
                </c:forEach>
              </div>
            </div>
          </div>
        </div>
      </c:if>
    </c:forEach>
  </div>
</div>