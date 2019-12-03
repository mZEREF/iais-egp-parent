$('document').ready(function() {
    $('.repeatable-section-control-button button.btn i.fa-plus-circle').addClass('fa-plus');
    $('.repeatable-section-control-button button.btn i.fa-plus-circle').removeClass('fa-plus-circle');
    // $('table.control-grid.columns1 table.control-grid >tbody >tr:first-child >td> div.control >.form-group > .formtext,.new-officer-form > table> tbody> tr > td >div.control > div.form-group > div.formtext').removeClass('col-sm-4');
    $('.new-officer-form > table> tbody> tr > td >div.control > div.form-group > div.formtext').removeClass('col-sm-4');
    // $('table.control-grid.columns1 table.control-grid >tbody >tr:first-child >td> div.control >.form-group > .formtext,.new-officer-form > table> tbody> tr > td >div.control > div.form-group > div.formtext').addClass('col-sm-5');
    $('.new-officer-form > table> tbody> tr > td >div.control > div.form-group > div.formtext').addClass('col-sm-5');
    $('table.control-grid.columns1 > tbody> tr:nth-child(3)> td> .control > table.control-grid >tbody >tr:first-child >td> div.control >.form-group > div> div>select').addClass('officer-select');

    $('.new-officer-form > table> tbody> tr> td >div.control > div.form-group > div:nth-child(2)').removeClass('col-sm-5');

    $('.new-officer-form > table> tbody> tr:first-child > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-5 col-sm-2');

    $('.new-officer-form > table> tbody> tr:nth-child(2) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-11 col-sm-6');

    $('.new-officer-form > table> tbody> tr:nth-child(3) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-5 col-sm-2');

    $('.new-officer-form > table> tbody> tr:nth-child(4) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-8 col-sm-4');

    $('.new-officer-form > table> tbody> tr:nth-child(5) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-5 col-sm-3');

    $('.new-officer-form > table> tbody> tr:nth-child(6) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-10 col-sm-5');

    $('.new-officer-form > table> tbody> tr:nth-child(7) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-5 col-sm-3');

    $('.new-officer-form > table> tbody> tr:nth-child(8) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-8 col-sm-4');

    $('.new-officer-form > table> tbody> tr:nth-child(9) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-9 col-sm-5');

    $('.new-officer-form > table> tbody> tr:nth-child(10) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-9 col-sm-5');

    $('.new-officer-form > table> tbody> tr:nth-child(11) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-6 col-sm-3');

    $('.new-officer-form > table> tbody> tr:nth-child(12) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-xs-9 col-sm-5');
    $('.add-officer-gp > div > table.control-grid >tbody >tr:first-child >td> div.control >.form-group > div> div>select').each(function() {
        var class_name = "officer-select-" + officer_count;
        $(this).addClass('officer-select-btn');
        $(this).addClass(class_name);
    });


    $('.parent-form-check input[type="checkbox"]').on('change', function() {
        var parentID = $(this).closest('.parent-form-check').attr('data-parent');
        if ($(this).prop('checked') == true) {
            $('.sub-form-check[data-child="'+parentID+'"]').removeClass('disabled');
            

        } else {
            
            $('.sub-form-check[data-child="'+parentID+'"] input[type="checkbox"]').prop('checked', false);
            if($('.sub-form-check[data-child="'+parentID+'"]').hasClass('parent-form-check')) {
                var secondChild = $('.sub-form-check[data-child="'+parentID+'"]').attr("data-parent");
                $('.sub-form-check[data-child="'+secondChild+'"] input[type="checkbox"]').prop('checked', false);
                $('.sub-form-check[data-child="'+secondChild+'"]').addClass('disabled');
            }
            $('.sub-form-check[data-child="'+parentID+'"]').addClass('disabled');
           
        }
    });

    $('table.control-grid tbody tr td select').niceSelect();
    $('.nice-select ul').mCustomScrollbar();

    /*$('select.officer-select').on('change', function() {
        $parentEle = $(this).closest('td.first');
        if ($(this).val() == "newOfficer") {
            $parentEle.find('> .new-officer-form').removeClass('hidden');
            $parentEle.find('> .profile-info-gp').addClass('hidden');
        } else {
            $parentEle.find('> .profile-info-gp').removeClass('hidden');
            $parentEle.find('> .new-officer-form').addClass('hidden');
        }

        $('.add-officer').removeClass('hidden');

        var iframe_top = parseInt(sessionStorage.getItem('iframe_top'));
        var offset_top = 50;
        var scroll_top = $(this).closest('td').offset().top + iframe_top - offset_top;

        sessionStorage.setItem('scrollHeight', scroll_top);
    });*/

    var officer_count = 2;
    $('.officer-select-btn').on('change', function() {
        $('.more-officer-btn').removeClass('hidden');
        $parentEle = $(this).closest('.officer-items');
        if ($(this).val() == "newOfficer") {
            $parentEle.find('.new-officer-form').removeClass('hidden');
            $parentEle.find('.profile-info-gp').addClass('hidden');
        } else {
            $parentEle.find('.profile-info-gp').removeClass('hidden');
            $parentEle.find('.new-officer-form').addClass('hidden');
        }

        var iframe_top = parseInt(sessionStorage.getItem('iframe_top'));
        var offset_top = 50;
        var scroll_top = $(this).closest('td').offset().top + iframe_top - offset_top;

        sessionStorage.setItem('scrollHeight', scroll_top);
    });
    $('.more-officer-btn').on('click', function() {
        $('.add-officer-gp > *.hidden').first().removeClass('hidden');
        $('.more-officer-btn').addClass('hidden');
        $('.add-officer').addClass('shown');

        var iframe_top = parseInt(sessionStorage.getItem('iframe_top'));
        var offset_top = 50;
        var scroll_top = $(this).closest('.add-officer').find('.add-officer-gp .officer-items').offset().top + iframe_top - offset_top;
        sessionStorage.setItem('scrollHeight', scroll_top);
    });

    $('#remove-clinical').on('click', function() {
        $(this).closest('.add-officer-gp .officer-items').removeClass('shown').addClass('hidden');
        $(this).closest('.add-officer').removeClass('shown');
        $('.more-officer-btn').removeClass('hidden');
    });

    
});


function doClear() {
    $('.more-officer-btn').removeClass('hidden');
    $parentEle = $(this).closest('.officer-items');
    if ($(this).val() == "newOfficer") {
        $parentEle.find('.new-officer-form').removeClass('hidden');
        $parentEle.find('.profile-info-gp').addClass('hidden');
    } else {
        $parentEle.find('.profile-info-gp').removeClass('hidden');
        $parentEle.find('.new-officer-form').addClass('hidden');
    }

    var iframe_top = parseInt(sessionStorage.getItem('iframe_top'));
    var offset_top = 50;
    var scroll_top = $(this).closest('td').offset().top + iframe_top - offset_top;

    sessionStorage.setItem('scrollHeight', scroll_top);
}