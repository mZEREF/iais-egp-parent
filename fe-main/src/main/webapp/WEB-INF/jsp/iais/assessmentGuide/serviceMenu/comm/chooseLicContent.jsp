<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
    .align-lic-table{
        margin-left: -30px;
    }
</style>
<div class="row">
    <div class="col-xs-12 col-md-7">
        <h3>
            You may choose to align the expiry date of your new licence(s) to any of your existing licences.
        </h3>
        <p><span>If you don't select a licence, MOH will assign an expiry date</span></p>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-md-7">
        <div class="table-responsive">
        <table aria-describedby="" class="table">
            <thead>
            <tr style="font-size: 16px;">
                <th scope="col"></th>
                <th scope="col"><strong>Licence No.</strong></th>
                <th scope="col"><strong>Type</strong></th>
                <th scope="col"><strong>Mode of Service Delivery</strong></th>
                <th scope="col" style="width:17%;"><strong>Expires on</strong></th>
            </tr>
            </thead>
            <tbody id="licBodyDiv"></tbody>
        </table>
    </div>
    </div>
</div>
<div class="row">
    <div class="col-xs-6 col-md-7">
        <div id="licPagDiv"></div>
    </div>
</div>