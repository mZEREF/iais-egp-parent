<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/11/17
  Time: 9:44
  To change this template use File | Settings | File Templates.
--%>

<div class="panel panel-default">
  <div class="panel-heading ">
    <h4 class="panel-title">
      <a href="#freezingDetails" data-toggle="collapse">
        Freezing
      </a>
    </h4>
  </div>
  <div id="freezingDetails" class="panel-collapse collapse in">
    <div class="panel-body">
      <div class="panel-main-content form-horizontal">

        <%@include file="comPart.jsp" %>
        <iais:row>
          <iais:field width="5" value="What was cryopreserved?" />
          <iais:value width="7" cssClass="col-md-7 " display="true">
            <iais:code code="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedType}"/>
          </iais:value>
        </iais:row>

        <iais:row>
          <iais:field width="5" value="No. Cryopreserved" />
          <iais:value width="7" cssClass="col-md-7 " display="true">
            <c:out value="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedNum}"/>
          </iais:value>
        </iais:row>

        <iais:row>
          <iais:field width="5" value="Cryopreservation Date" />
          <iais:value width="7" cssClass="col-md-7 " display="true">
            <fmt:formatDate value='${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedDate}'
                            pattern='dd/MM/yyyy'/>
          </iais:value>
        </iais:row>


      </div>
    </div>
  </div>
</div>
