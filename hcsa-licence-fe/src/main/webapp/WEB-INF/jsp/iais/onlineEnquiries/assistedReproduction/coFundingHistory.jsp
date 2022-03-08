<div class=" col-md-12">

    <hr>
    <div class="row">
        <iais:field width="6" cssClass="col-md-6" value="Total No. of Co-funded IUI Cycles"/>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.iuiCoFundedTotal}
        </div>
    </div>
    <hr>
    <div class="row">
        <iais:field width="6" cssClass="col-md-6" value="Total No. of Co-funded ART Fresh Cycles"/>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.artFreshCoFundedTotal}
        </div>
    </div>
    <hr>
    <div class="row">
        <iais:field width="6" cssClass="col-md-6" value="Total No. of Co-funded ART Frozen Cycles"/>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.artFrozenCoFundedTotal}
        </div>
    </div>
    <hr>
    <div class="row">
        <iais:field width="6" cssClass="col-md-6" value="Total No. of Co-funded PGT Cycles"/>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.pgtCoFundedTotal}
        </div>
    </div>
    <hr>
    <div class="row">
        <iais:field width="6" cssClass="col-md-6" value="Total number of Co-funded ART Cycles claimed by patient when she is 40 years old and above"/>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.artCoFundedOldAgeTotal}
        </div>
    </div>
    <hr>
    <div class="row">
        <iais:field width="6" cssClass="col-md-6" value="Total number of Co-funded PGT Cycles claimed by patient when she is 40 years old and above"/>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.pgtCoFundedOldAgeTotal}
        </div>
    </div>
    <hr>

</div>