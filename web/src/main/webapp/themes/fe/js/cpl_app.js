/*! MOH-IAIS 13-09-2019 */

$(document).ready(function() {
    var mySwiper;

    if($('.file-upload-gp').length > 0) {
        fileUploadFunc();
    }

    $('[data-toggle="tooltip"]').tooltip();
    $('select').each(function () {
        if($(this).prop('multiple')){
            $(this).multiSelect();
        }else{
            $(this).niceSelect();
        }
    });
    setTimeout(function() {}, 500);

    // $('.nice-select ul').mCustomScrollbar();

    if ($('.tab-nav-mobile').length > 0) {
        mobileTabHeader();

    }
    if ($('.prelogin').length > 0) {
        setSameHeightPreloginBox();
    }
    dashboardTileTab();
    tableCollapse();
    licenceBtn();
    mobileMenuBtn();
    premisesFunc();
    serviceInfoFunc();
    checkboxLevelDisabledEnabledFunc();
    selfAssessmentFunc();
    if ($('.personal-detail-info').length > 0) {
        personnelDetailEditFunc();
    }

    if ($(window).width() < 1200) {
        $('.navigation ').mCustomScrollbar();
    }

    $('a[data-toggle="modal"]').on('click', function() {});


    if($('#__egovform-iframe').length > 0) {
        var iframe_top = $('#__egovform-iframe').offset().top
        sessionStorage.setItem('iframe_top', iframe_top)
    }

    $('a').each(function () {
        var h = $(this).attr('href');
        if (isEmpty(h)) {
            $(this).attr('href', 'javascript:void(0)');
        }
    });
});

$menu = $('.navigation');
$(document).on('click touchend',function (e) {
    if ($('.navigation-gp .navigation').hasClass('open') && ($(e.target).hasClass('row') || $(e.target).hasClass('navigation-open') || $(e.target).hasClass('logo-img'))) {
        $('.menu-icon').removeClass('open');
        $('.navigation-gp .navigation').removeClass('open');
        $('body').removeClass('navigation-open');
    }
   
});


function personnelDetailEditFunc() {
    if($('.personal-detail-info').length > 0) {
        $('.application-tab-footer .btn.btn-primary').addClass('disabled');
    }

    $('.edit-info').on('click', function() {
        $('.edit-section-gp').removeClass('hidden');
        $(this).addClass('hidden');
        if ($('.edit-section-gp input[name=editInfoType]:checked').attr("id") == "replacePersonnel") {
            $('.change-detail-personnel').addClass('hidden disabled');
            $('.replace-another-personnel').removeClass('hidden');
            // $('.application-tab-footer .btn.btn-primary').removeClass('disabled');

        } else if ($('.edit-section-gp input[name=editInfoType]:checked').attr("id") == "changeDetailInfo") {
            $('.replace-another-personnel').addClass('hidden');
            $('.change-detail-personnel').removeClass('hidden disabled');
            // $('.application-tab-footer .btn.btn-primary').removeClass('disabled');
        }
    });

    $('.edit-section-gp .cancel-edit').on('click', function() {
        $('.edit-section-gp').addClass('hidden');
        $('.edit-info.hidden').removeClass('hidden');
        $('.replace-another-personnel').addClass('hidden');
        $('.change-detail-personnel').removeClass('hidden');
        $('.change-detail-personnel').addClass('disabled');
        $('.application-tab-footer .btn.btn-primary').addClass('disabled');
        
    });

    $('.edit-section-gp input[type="radio"]').on('change', function() {

        if ($('.edit-section-gp input[name=editInfoType]:checked').attr("id") == "replacePersonnel") {
            $('.change-detail-personnel').addClass('hidden disabled');
            $('.replace-another-personnel').removeClass('hidden');

        } else if ($('.edit-section-gp input[name=editInfoType]:checked').attr("id") == "changeDetailInfo") {
            $('.replace-another-personnel').addClass('hidden');
            $('.change-detail-personnel').removeClass('hidden disabled');
        }
        // $('.application-tab-footer .btn.btn-primary').addClass('disabled');
    });

    $('#officerSelect, .change-detail-personnel select,.change-detail-personnel input').on('change',function(){
        $('.application-tab-footer .btn.btn-primary').removeClass('disabled');

    });
    $('.change-detail-personnel input').on('keyup',function(){
        $('.application-tab-footer .btn.btn-primary').removeClass('disabled');

    });
}

$(window).resize(function() {
    if ($('.prelogin').length > 0) {
        setSameHeightPreloginBox();
    }
});


function setIFrameHeight() {
    var iframeEle = document.getElementById('__egovform-iframe').contentWindow;
    $("iframe").css({
        height: iframeEle.$("body").outerHeight()
    });
}

function setSameHeightPreloginBox() {
    $('.prelogin-content .white-content-box').css('height', 'auto');

    if ($(window).width() > 992) {
        var maxHeight = 0;
        $('.prelogin-content .white-content-box').each(function() {
            if ($(this).outerHeight() > maxHeight) {
                maxHeight = $(this).outerHeight();
            }
        });
        $('.prelogin-content .white-content-box').css('height', maxHeight + "px");

    }
}
setTimeout(function() {
    // setIFrameHeight();
    $('iframe').ready(function() {
        var iframe = $("iframe"),
            iframeEle = $("iframe").contents();

        iframe.each(function() {
            var iframeContent = $(this).contents(),
                $this = $(this);

            setTimeout(function() {
                console.log(iframeContent.find("body").height())
                $this.css({ height: iframeContent.find("body").height() + "px" });
            },1000);
        });


        iframeEle.find('a.more-officer-btn').bind('click', function() {
            $("iframe").css({ 'height': "auto" });
            setTimeout(function() {
                $("iframe").css({ height: iframeEle.find("body").outerHeight() + "px" });

                if(sessionStorage.getItem('scrollHeight'))
                    $('html, body').scrollTop(sessionStorage.getItem('scrollHeight'));
            }, 200);
        });

        iframeEle.find('a#remove-clinical').bind('click', function() {
            $("iframe").css({ 'height': "auto" });
            setTimeout(function() {
                $("iframe").css({ height: iframeEle.find("body").outerHeight() + "px" });
            }, 200);
        });

        iframeEle.find("select.officer-select + .nice-select .list li, select.officer-select-btn + .nice-select .list li").bind("click", function() {
            $('.application-tab-footer a.next').removeClass('disabled');

            $("iframe").css({ 'height': "auto" });
            setTimeout(function() {
                $("iframe").css({ height: iframeEle.find("body").outerHeight() + 20+"px" });

                if(sessionStorage.getItem('scrollHeight'))
                    $('html, body').scrollTop(sessionStorage.getItem('scrollHeight'));
            }, 100);
        });

        iframeEle.find('select.officer-select + .nice-select').bind("click", function() {
            if($(this).hasClass('open')) {
                $("iframe").css({ 'height': "auto" });
                iframeEle.find('table.control-grid.columns1 > tbody > tr > td .section.control').css('min-height', 'auto');
                setTimeout(function() {
                    $("iframe").css({ height: iframeEle.find("body").outerHeight() + "px" });
                }, 200);
            } else {
                $("iframe").css({ 'height': "auto" });
                iframeEle.find('table.control-grid.columns1 > tbody > tr > td .section.control').css('min-height', '500px');
                setTimeout(function() {
                    $("iframe").css({ height: iframeEle.find("body").outerHeight() + "px" });
                }, 200);
            }
        });

        iframeEle.find('select.officer-select + .nice-select').bind("blur", function(e) {
            var $thisText = $(this).find('.current').text()
                $thisOpen = $(this).hasClass('open');


            setTimeout(function() {
                iframeEle.find('select.officer-select + .nice-select').removeClass('open');

                if($thisText == 'Select Personnel') {
                    $("iframe").css({ 'height': "auto" });
                    iframeEle.find('table.control-grid.columns1 > tbody > tr > td .section.control').css('min-height', 'auto');

                    setTimeout(function() {
                        $("iframe").css({ height: iframeEle.find("body").outerHeight() + "px" });
                    }, 200);
                }
            }, 200);
        });

        iframeEle.find('select.officer-select-btn + .nice-select').bind("click", function() {
            if($(this).hasClass('open')) {
                $("iframe").css({ 'height': "auto" });
                iframeEle.find('table.control-grid.columns1 > tbody > tr > td .section.control .add-officer').css('min-height', 'auto');
                setTimeout(function() {
                    $("iframe").css({ height: iframeEle.find("body").outerHeight() + "px" });
                }, 200);
            } else {
                $("iframe").css({ 'height': "auto" });
                iframeEle.find('table.control-grid.columns1 > tbody > tr > td .section.control .add-officer').css('min-height', '450px');
                setTimeout(function() {
                    $("iframe").css({ height: iframeEle.find("body").outerHeight() + "px" });
                }, 200);
            }

            setTimeout(function() {
                if(sessionStorage.getItem('scrollHeight'))
                    $('html, body').scrollTop(sessionStorage.getItem('scrollHeight'));
            }, 200);
        });

        iframeEle.find('select.officer-select-btn + .nice-select').bind("blur", function(e) {
            var $thisText = $(this).find('.current').text()
                $thisOpen = $(this).hasClass('open');

            setTimeout(function() {
                iframeEle.find('select.officer-select-btn + .nice-select').removeClass('open');

                if($thisText == 'Select Personnel') {
                    $("iframe").css({ 'height': "auto" });
                    iframeEle.find('table.control-grid.columns1 > tbody > tr > td .section.control .add-officer').css('min-height', 'auto');

                    setTimeout(function() {
                        $("iframe").css({ height: iframeEle.find("body").outerHeight() + "px" });
                    }, 200);
                }                
            }, 200);

            setTimeout(function() {
                if(sessionStorage.getItem('scrollHeight'))
                    $('html, body').scrollTop(sessionStorage.getItem('scrollHeight'));
            }, 450);
        });
    });

}, 700);
$(window).on('resize', function() {
    // $("iframe").css({ 'height': "auto" });
    // var iframeEle = $("iframe").contents();
    // setTimeout(function() {
    //     $("iframe").css({ height: iframeEle.find("body").outerHeight() + "px" });

    // }, 100);
});;function fileUploadFunc() {
    $('.file-upload-gp .btn-file-upload').on('click', function() {
        $(this).closest('.file-upload-gp').find('input[type="file"]').click();
    });
}

function tableCollapse() {
    $('.table tr > td a[data-toggle="collapse"]').on('click', function() {
        var target = $(this).attr("data-target");
        $('tr[data-child-row="' + target + '"]').toggleClass('in');
        $(this).toggleClass('collapsed');
    });
}

function mobileTabHeader() {
    mySwiper = new Swiper('.tab-nav-mobile', {
        slidesPerView: 2,
        init: true,
        clickable: true,
        prevButton: '.swiper-button-prev',
        nextButton: '.swiper-button-next',
        breakpoints: {
            1200: {
                slidesPerView: 3
            },
            767: {
                slidesPerView: 2
            }
        }


    });

    mySwiper.slideTo('/')
    if ($('.tab-gp').length > 0) {
        mySwiper.slideTo($('.tab-gp .nav-tabs li.active').index());
        setTimeout(function() {
            if ($('.tab-nav-mobile .swiper-wrapper .swiper-slide-active').hasClass('activeItem') && mySwiper.isBeginning) {
                $('.swiper-button-prev').addClass("whitecolor");

            } else {
                if ($('.tab-nav-mobile .swiper-wrapper .swiper-slide-active').hasClass('activeItem')) {
                    $('.swiper-button-prev').addClass("whitecolor");


                } else {
                    $('.swiper-button-prev').removeClass("whitecolor");
                }
            }

        }, 300);


    }


    if (mySwiper.isBeginning) {
        $('.swiper-button-prev').css("opacity", 0);
    } else {
        $('.swiper-button-prev').css("opacity", 1);

    }
    if (mySwiper.isEnd) {
        $('.swiper-button-next').css("opacity", 0);
    } else {
        $('.swiper-button-next').css("opacity", 1);

    }

    $('.tab-nav-mobile .swiper-button-prev').on('click', function() {
        mySwiper.slidePrev();
    });

    $('.tab-nav-mobile .swiper-button-next').on('click', function() {
        mySwiper.slideNext();
    });



    $('.tab-nav-mobile').find('.swiper-slide:nth-child(' + ($('.tab-gp .nav-tabs li.active').index() + 1) + ')').addClass('activeItem');
    // $('.swiper-button-prev').addClass("whitecolor");

    $('.tab-nav-mobile .swiper-slide a').click(function() {
        $parent = $(this).closest('.tab-nav-mobile');
        $this = $(this).parent();
        $parent.find('.swiper-slide').removeClass('activeItem');
        $this.addClass('activeItem');

        if (($this.hasClass('swiper-slide-active') && mySwiper.isBeginning) || $this.hasClass('swiper-slide-active')) {
            $parent.find('.swiper-button-prev').addClass("whitecolor");
        } else {
            $parent.find('.swiper-button-prev').removeClass("whitecolor");
        }

        if (($this.hasClass('swiper-slide-next') && mySwiper.isEnd) || $this.hasClass('swiper-slide-next')) {
            $parent.find('.swiper-button-next').addClass("whitecolor");
        } else {
            $parent.find('.swiper-button-next').removeClass("whitecolor");
        }

    });

    mySwiper.on('slideChangeTransitionStart', function() {
        if ($(this)[0].$wrapperEl.find('.swiper-slide-active').hasClass('activeItem') && mySwiper.isBeginning) {
            $('.swiper-button-prev').addClass("whitecolor");

        } else {
            if ($(this)[0].$wrapperEl.find('.swiper-slide-active').hasClass('activeItem')) {
                $('.swiper-button-prev').addClass("whitecolor");

            } else {
                $('.swiper-button-prev').removeClass("whitecolor");

            }
        }

        if ($(this)[0].$wrapperEl.find('.swiper-slide-next').hasClass('activeItem') && mySwiper.isEnd) {
            $('.swiper-button-next').addClass("whitecolor");
        } else {
            if ($(this)[0].$wrapperEl.find('.swiper-slide-next').hasClass('activeItem')) {
                $('.swiper-button-next').addClass("whitecolor");

            } else {
                $('.swiper-button-next').removeClass("whitecolor");

            }
        }


    });

    mySwiper.on('slideChangeTransitionEnd', function() {

        if (mySwiper.isBeginning) {
            $('.swiper-button-prev').css("opacity", 0);
        } else {
            $('.swiper-button-prev').css("opacity", 1);

        }
        if (mySwiper.isEnd) {
            $('.swiper-button-next').css("opacity", 0);
        } else {
            $('.swiper-button-next').css("opacity", 1);

        }
    });

}

function licenceBtn() {
    $('.licenceCheck').on('change', function() {
        var checkedCheckbox = false;
        var checkCount = 0;
        $('.licenceCheck').each(function() {
            $this = $(this);
            if ($this.prop('checked') == true) {
                checkedCheckbox = true;
                checkCount += 1;
            }

        });

        if (checkedCheckbox) {
            $('.licence-btns a').removeClass('disabled');

            if (checkCount > 1) {
                $('.licence-btns a:last-child').addClass('disabled');
            } else {
                $('.licence-btns a:last-child').removeClass('disabled');
            }
        } else {
            $('.licence-btns a').addClass('disabled');
        }

    });
}

function dashboardTileTab() {
    $('.dashboard-gp .dashboard-tile a[data-tab]').on('click', function() {
        var tab_name = $(this).attr("data-tab");
        $('.dashboard-tab .nav.nav-tabs li a[href="' + tab_name + '"]').click();

        if ($(window).width() < 992) {
            $('.tab-nav-mobile  a[href="' + tab_name + '"]').click();
            mySwiper.slideTo($('.tab-nav-mobile  a[href="' + tab_name + '"]').parent().index());
        }
    });
}

function mobileMenuBtn() {
    $('.menu-icon').click(function() {
        $(this).toggleClass('open');
        $('.navigation-gp .navigation').toggleClass('open');
        $('body').toggleClass('navigation-open');
    });

   
}



function premisesFunc() {
    $('input[name="premisesType"]').on('change', function() {
        $('.premiseLocationSelect').removeClass('hidden');
        $('.premises-summary, .new-premise-form-on-site, .new-premise-form-conveyance, .vehicleSelectForm').addClass('hidden');
        $('#premisesSelect, #vehicleSelect').prop('selectedIndex', 0);
        $('#premisesSelect, #vehicleSelect').niceSelect('update');
    });

    $('#premisesSelect').on('change', function() {
        if ($(this).val() == "newPremise") {

            $('.premises-summary, .vehicleSelectForm').addClass('hidden');
            if ($('input[name="premisesType"]:checked').attr("id") == "premise_conveyance") {

                $('.new-premise-form-conveyance').removeClass('hidden');
                $('.new-premise-form-on-site').addClass('hidden');

            } else {
                $('.new-premise-form-on-site').removeClass('hidden');
                $('.new-premise-form-conveyance').addClass('hidden');
            }
        } else {
            $('.new-premise-form-conveyance,.new-premise-form-on-site').addClass('hidden');

            if ($('input[name="premisesType"]:checked').attr("id") == "premise_conveyance") {
                $('.vehicleSelectForm, .premises-summary .vehicle-txt').removeClass('hidden');
                $('.premise-address-gp .premise-type b').text("Conveyance: ")
            } else {
                $('.premises-summary').removeClass('hidden');
                $('.premises-summary .vehicle-info,.premises-summary .vehicle-txt').addClass('hidden');
                $('.premise-address-gp .premise-type b').text("On-site: ")
            }

            $('.premises-summary .premise-address').text($('#premisesSelect option:selected').text());
        }
    });

    $('#vehicleSelect').on('change', function() {
        $('.premises-summary').removeClass('hidden');
        $('.vehicle-txt').removeClass('hidden');
        $('.premises-summary .vehicle-info').removeClass('hidden').text($('#vehicleSelect option:selected').text());
    });

    $('.application-tab-footer a[data-toggle="tab"]').on('click', function() {


        var target = $(this).attr("href");

        $('.steps-tab .nav li').removeClass('active');
        $('.steps-tab .nav li a[href="' + target + '"]').closest('li').addClass('active');

        if ($(this).hasClass('next')) {
            mySwiper.slideNext();
        } else {
            mySwiper.slidePrev();
        }
        $('.tab-nav-mobile .swiper-slide a[href="' + target + '"]').click();

    });
}

$(window).on('scroll', function() {
    if ($('.dashboard-tab').length > 0 && $('.tab-pane#tabLicence').is(":visible")) {
        if ($(window).scrollTop() >= $('#tabLicence .table-gp').offset().top - $('#tabLicence .tab-search').outerHeight()) {
            $('#tabLicence .tab-search').addClass('sticky');
        } else {
            $('#tabLicence .tab-search.sticky').removeClass('sticky');
        }
    }

});



function serviceInfoFunc() {
    $('input[name="labDisciplines"], .officer-allocation-select').on('change', function() {
        $('.application-tab-footer a.next').removeClass('disabled');
    });

    $('.application-tab-footer a[data-goto]').on('click', function() {
        var current = $(this).attr("data-goto");
        var next = $('.progress-tracker li[data-service-step="' + current + '"]').next().attr("data-service-step");
        var prev = $('.progress-tracker li[data-service-step="' + current + '"]').prev().attr("data-service-step");
        $('.application-service-steps > div').addClass('hidden');
        $('.application-service-steps > div.' + current).removeClass('hidden');
        $('.application-tab-footer a.back[data-goto]').attr('data-goto', prev);
        $('.application-tab-footer a.next[data-goto]').attr('data-goto', next);

        if (current == "laboratory-disciplines") {
            $('.application-tab-footer a.back[data-goto]').addClass('hidden');
            $('.application-tab-footer a.back:not([data-goto])').removeClass('hidden');

        } else {

            $('.application-tab-footer a.back:not([data-goto])').addClass('hidden');
            $('.application-tab-footer a.back[data-goto]').removeClass('hidden');
        }
        if ($(this).hasClass('next')) {
            $('.progress-tracker li[data-service-step="' + current + '"]').addClass('active').removeClass('disabled');
            $('.progress-tracker li[data-service-step="' + current + '"]').prev().removeClass('active').addClass('completed');

            $('.application-tab-footer a[data-goto].next').addClass('disabled');

        } else {
            $('.progress-tracker li[data-service-step="' + current + '"]').addClass('active');
            $('.progress-tracker li[data-service-step="' + current + '"]').next().removeClass('active');
            $('.application-tab-footer a[data-goto].next').removeClass('disabled');


        }

    });

    $('#serviceSelect').on('change', function() {
        var current = $(this).val();

        window.location.replace("application-service-related-" + current + "-lab-discipline.html")
    });
    $('#officerSelect').on('change', function() {
        $('.application-tab-footer a.next').removeClass('disabled');

        if ($(this).val() == "newOfficer") {
            $('.new-officer-form').removeClass('hidden');
            $('.profile-info-gp').addClass('hidden');
        } else {
            $('.profile-info-gp').removeClass('hidden');
            $('.new-officer-form').addClass('hidden');
        }
    });

    $('a[data-toggle="tab"][href="#serviceInformationTab"]').on('shown.bs.tab', function() {
        $('.application-service-steps > div').addClass('hidden');
        $('.application-service-steps > div.laboratory-disciplines').removeClass('hidden');
        $('.application-tab-footer a.back[data-goto]').addClass('hidden');
        $('.application-tab-footer a.back:not([data-goto])').removeClass('hidden');
        $('.progress-tracker li.tracker-item:not(:first-child)').removeClass('active completed').addClass('disabled');
        $('.progress-tracker li.tracker-item:first-child').removeClass('disabled completed').addClass('active');
        $('.application-tab-footer a[data-goto].next').addClass('disabled');
        $('.application-tab-footer a.next[data-goto]').attr('data-goto', "clinical-governance-officer");
    });
}

function checkboxLevelDisabledEnabledFunc() {
    $('input.parent-form-check').on('change', function() {
        if ($(this).prop('checked') == true) {
            $(this).siblings('.sub-form-check').removeClass('disabled');

        } else {
            $(this).siblings('.sub-form-check').addClass('disabled');
        }
    });
}

function selfAssessmentFunc() {
    $('.self-assessment-gp .self-assessment-item .form-check.progress-step-check input[type="radio"]').on('change', function() {
        $this = $(this);
        var show_item = $this.attr("id");
        $this.closest(".self-assessment-item").addClass('completed');
        $this.closest(".self-assessment-item").nextAll().addClass('hidden').removeClass('completed');
        $this.closest(".self-assessment-item").next().removeClass('hidden');
        $this.closest(".self-assessment-item").next().find('input[type="radio"]').prop('checked', false);
        $this.closest(".self-assessment-item").next().find('>div').addClass('hidden');
        $('.' + show_item).removeClass('hidden');
        $('.self-assessment-gp .self-assessment-item .table-gp .table tr.selectedRow').removeClass('selectedRow');
        $('.self-assessment-gp .self-assessment-item .table-gp .table input[type="radio"]').prop('checked', false);

    });

    $('.self-assessment-gp .self-assessment-item .table-gp .table input[type="radio"]').on('change', function() {
        var radioName = $(this).attr("name");
        var itemID = $("input[name='" + radioName + "']:checked").attr("id");
        $("input[name='" + radioName + "']").closest("tr").removeClass('selectedRow');
        $('#' + itemID).closest("tr").addClass('selectedRow');

        $(this).closest('.form-check-gp').find('a.disabled').removeClass('disabled');
    });
}