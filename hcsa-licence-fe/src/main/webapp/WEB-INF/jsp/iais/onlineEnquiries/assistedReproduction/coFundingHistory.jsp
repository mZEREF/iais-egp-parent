<div class=" col-md-12">

    <hr>
    <div class="row">
        <div class="col-md-6">
            Total No. of Co-funded IUI Cycles
        </div>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.iuiCoFundedTotal}
        </div>
    </div>
    <hr>
    <div class="row">
        <div class="col-md-6">
            Total No. of Co-funded ART Fresh Cycles
        </div>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.artFreshCoFundedTotal}
        </div>
    </div>
    <hr>
    <div class="row">
        <div class="col-md-6">
            Total No. of Co-funded ART Frozen Cycles
        </div>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.artFrozenCoFundedTotal}
        </div>
    </div>
    <hr>
    <div class="row">
        <div class="col-md-6">
            Total No. of Co-funded PGT Cycles
        </div>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.pgtCoFundedTotal}
        </div>
    </div>
    <hr>
    <div class="row">
        <div class="col-md-6">
            Total number of co-funded ART cycles claimed by patient when she is 40 years old and above
        </div>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.artCoFundedOldAgeTotal}
        </div>
    </div>
    <hr>
    <div class="row">
        <div class="col-md-6">
            Total number of co-funded PGT cycles claimed by patient when she is 40 years old and above
        </div>
        <div class="col-md-6">
            <c:if test="${empty arCoFundingDto}">-</c:if>${arCoFundingDto.pgtCoFundedOldAgeTotal}
        </div>
    </div>
    <hr>

</div>