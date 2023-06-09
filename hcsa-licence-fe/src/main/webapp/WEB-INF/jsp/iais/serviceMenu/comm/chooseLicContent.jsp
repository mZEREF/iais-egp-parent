<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
    .align-lic-table{
        margin-left: -30px;
    }
</style>
<div class="row">
    <div class="col-xs-12 col-md-3">
    </div>
    <div class="col-xs-12 col-md-6">
        <h3>
            You may choose to align to one of the following licence.
        </h3>
        <p><span>If you don't select a licence, MOH will assign an expiry date</span></p>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-md-3"></div>
    <div class="col-xs-12 col-md-6">
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
    <div class="col-xs-12 col-md-3">

    </div>
    <div class="col-xs-6 col-md-6">
        <div id="licPagDiv"></div>
    </div>
</div>