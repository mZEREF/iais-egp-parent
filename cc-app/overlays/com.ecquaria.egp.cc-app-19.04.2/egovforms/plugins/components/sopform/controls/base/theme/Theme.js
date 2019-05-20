(function($jq) {

var FQN = 'sopform.controls.base.Theme';

/**
 * This module contains all predefined themes which are available in the system.
 */
Form.Theme = {
	THEMES: {
		'Default': {
			headerFont: {
				type: 'arial, helvetica, sans-serif',
				color: null,
				size: '14px',
				bold: true,
				italic: false
			},
			titleFont: {
				type: 'tahoma, geneva, sans-serif',
				color: null,
				size: '12px',
				bold: false,
				italic: false
			},
			labelFont: {
				type: 'Arial,Helvetica,sans-serif',
				color: 	'#363636',
				size: '12px',
				bold: true,
				italic: false
				
			},
			font: {
				type: 'tahoma, geneva, sans-serif',
				color: null,
				size: '11px',
				bold: false,
				italic: false
			},
			outerbgimage: 'outbg.gif',
			innerbgimage: 'inbg.gif',
			outerbgcolor: undefined,
			innerbgcolor: undefined,
			RepeatableRow: {
				tr_even_row: {
					bgcolor: undefined
				},
				
				tr_odd_row: {
					bgcolor: undefined
				}
			},
			Page:{
				tr_even_row: {
					bgcolor: undefined
				},
				
				tr_odd_row: {
					bgcolor: undefined
				}
			}
		},
		'Autumn': {
			font: {
				type: 'arial, helvetica, sans-serif',
				color: '#6e3e14',
				size: '12px',
				bold: true,
				italic: true
			},
			outerbgimage: 'outbg.gif',
			innerbgimage: 'inbg.gif',
			outerbgcolor: undefined,
			innerbgcolor: '#EF8D31',
			innerbgrepeat: 'repeat-x'
		},
		'Aquarium': {
			font: {
				type: 'tahoma, geneva, sans-serif',
				color: '#27819D',
				size: '11px',
				bold: false,
				italic: false
			},
			outerbgimage: 'outbg.gif',
			innerbgimage: 'inbg.gif',
			outerbgcolor: undefined,
			innerbgcolor: '#C3E4F7',
			innerbgrepeat: 'repeat-x'
		},
		'Blue': {
			font: {
				type: 'arial, helvetica, sans-serif',
				color: '#000000',
				size: '12px',
				bold: true,
				italic: false
			},
			outerbgimage: undefined,
			innerbgimage: 'inbg.gif',
			outerbgcolor: '#1188c1',
			innerbgcolor: '#A1DBFF',
			innerbgrepeat: 'repeat-x'
		},
		'Midnight': {
			font: {
				type: 'tahoma, geneva, sans-serif',
				color: '#000000',
				size: '11px',
				bold: false,
				italic: false
			},
			outerbgimage: 'outbg.jpg',
			innerbgimage: 'inbg.gif',
			outerbgcolor: '#E0E1DB',
			innerbgcolor: undefined,
			outerbgrepeat: 'repeat-x'
		},
		'Sunshine': {
			font: {
				type: '"Comic Sans MS", cursive',
				color: '#000000',
				size: '14px',
				bold: true,
				italic: false
			},
			outerbgimage: 'outbg.gif',
			innerbgimage: 'inbg.gif',
			outerbgcolor: '#FFC933',
			innerbgcolor: '#FEBF04',
			outerbgrepeat: 'no-repeat',
			innerbgrepeat: 'repeat-x',
			outerbgposition: '100% 100%'
		},
		'Tapestry': {
			font: {
				type: 'verdana, geneva, sans-serif',
				color: '#1174bf',
				size: '12px',
				bold: true,
				italic: false
			},
			outerbgimage: 'outbg.gif',
			innerbgimage: 'inbg.gif',
			outerbgcolor: undefined,
			innerbgcolor: '#ABD8E3',
			innerbgrepeat: 'repeat-x'
		},
		'Yellowish': {
			font: {
				type: 'helvetica, sans-serif',
				color: '#db901f',
				size: '14px',
				bold: true,
				italic: false
			},
			outerbgimage: 'outbg.gif',
			innerbgimage: 'inbg.gif',
			outerbgcolor: undefined,
			innerbgcolor: undefined
		},
		'WhiteBackground': {
			headerFont: {
				type: 'arial, helvetica, sans-serif',
				color: null,
				size: '14px',
				bold: true,
				italic: false
			},
			titleFont: {
				type: 'tahoma, geneva, sans-serif',
				color: null,
				size: '12px',
				bold: false,
				italic: false
			},
			labelFont: {
				type: 'tahoma, geneva, sans-serif',
				color: null,
				size: '11px',
				bold: false,
				italic: false
			},
			font: {
				type: 'tahoma, geneva, sans-serif',
				color: null,
				size: '11px',
				bold: false,
				italic: false
			},
			outerbgimage: 'white.gif',
			innerbgimage: 'white.gif',
			outerbgcolor: undefined,
			innerbgcolor: undefined,
			RepeatableRow: {
				tr_even_row: {
					bgcolor: undefined
				},
				
				tr_odd_row: {
					bgcolor: undefined
				}
			},
			Page:{
				tr_even_row: {
					bgcolor: undefined
				},
				
				tr_odd_row: {
					bgcolor: undefined
				}
			}
		}
	},

	applyTheme: function(page, themeName) {
		/*if ($('#theme-style').length) return;

		var defaultThemeName = this.getDefaultThemeName();
		themeName = themeName || defaultThemeName;
		var theme = this.getTheme(themeName);
		if (theme === undefined) {
			themeName = defaultThemeName;
			theme = this.getTheme(themeName);
		}

		var outerCss = [];
		if (theme.outerbgcolor) outerCss.push('background-color:', theme.outerbgcolor, ';');
		if (theme.outerbgrepeat) outerCss.push('background-repeat:', theme.outerbgrepeat, ';');
		if (theme.outerbgposition) outerCss.push('background-position:', theme.outerbgposition, ';');
		if (theme.outerbgimage) outerCss.push('background-image:', 'url(', this.imageURL(themeName, theme.outerbgimage), ');');
		
		var innerCss = [];
		if (theme.innerbgcolor) innerCss.push('background-color:', theme.innerbgcolor, ';');
		if (theme.innerbgrepeat) innerCss.push('background-repeat:', theme.innerbgrepeat, ';');
		if (theme.innerbgposition) innerCss.push('background-position:', theme.innerbgposition, ';');
		if (theme.innerbgimage) innerCss.push('background-image:', 'url(', this.imageURL(themeName, theme.innerbgimage), ');');
		
		var style = [
			'<style id="theme-style">',
			'div.sopform{',
				outerCss.join(''),
			'}',
			'div.form-tab-panel{',
				innerCss.join(''),
			'}',
			'</style>'
		].join('');
		$(document.body).append(style);*/
	},

	removeTheme: function() {
		$('#theme-style').remove();
	},

	imageURL: function(themeName, imageName) {
		var base = Form.Loader.toBaseResourcePath(this.FQN);
		base = base.replace(/\w*$/, '');
		return base + themeName + '/' + imageName;
	},

	getTheme: function(name) {
		return this.THEMES[name];
	},

	getDefaultThemeName: function() {
		return 'Default';
	},

	getThemeNames: function() {
		var names = [];
		$jq.each(this.THEMES, function(name) {
			names.push(name);
		});
		return names;
	},

	FQN: FQN
}

}(jQuery));