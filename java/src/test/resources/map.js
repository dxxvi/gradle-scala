'use strict';

var printUsage = function() {
	console.log('Syntax: phantomjs map.js 40.82 -73.94 widthUnit numberOfWidthUnit heightUnit numberOfHeightUnit keyword');
    console.log('  where widthUnit:heightUnit = 8:10 or 10:8');
};

var widthUnit, numberOfWidthUnit, heightUnit, numberOfHeightUnit;

var args = require('system').args;
if (args.length !== 8) {
	printUsage();
	phantom.exit();
}
else {
    widthUnit          = parseInt(args[3]);
    numberOfWidthUnit  = parseInt(args[4]);
    heightUnit         = parseInt(args[5]);
    numberOfHeightUnit = parseInt(args[6]);
    if (widthUnit === NaN || numberOfWidthUnit === NaN || heightUnit === NaN || numberOfHeightUnit === NaN) {
    	printUage();
    	console.log('width or height is not a number');
    	phantom.exit();
    }
}

var calculateTimeout = function(widthUnit, numberOfWidthUnit, heightUnit, numberOfHeightUnit) {
	var p = widthUnit * numberOfWidthUnit * heightUnit * numberOfHeightUnit;
	if (p > 23501120) return 12000;
	else if (p > 11750560) return 10000;
	else if (p > 5875280) return 4000;
	else if (p > 2937640) return 1904;
	else return 419;
};

var inbrowserF = function() {
	var f3 = function(cssSelector, result) {
		var t = document.querySelectorAll(cssSelector);
		if (t.length > 0) {
			t = t[0];
			t.parentElement.removeChild(t);
			return result;
		}
		else {
			return result + '\ndocument.querySelectorAll("' + cssSelector + '") is empty';
		}
	};

	var style = document.createElement('style');
	style.setAttribute('type', 'text/css');
	style.appendChild(document.createTextNode([
		'div.__mine {position: absolute; z-index: 9999; padding-top: 10px; padding-left: 10px; font: normal 27px sans-serif; color: rgba(255,0,0,0.5); }'
	].join('\n')));
	document.body.appendChild(style);

	var widthUnit =1355, numberOfWidthUnit = 4, heightUnit = 1084, numberOfHeightUnit = 4;
	for (var y = 0; y < numberOfHeightUnit; y++) {
		for (var x = 0; x < numberOfWidthUnit; x++) {
			var div = document.createElement('div');
			div.setAttribute('class', '__mine');
			div.style.top = y * heightUnit + 'px';
			div.style.left = x * widthUnit + 'px';
			div.appendChild(document.createTextNode(x + ' - ' + y));
			document.body.appendChild(div);
		}
	}

	var result = f3('.ml-inset-map-type-control.ml-visible', '');
	result = f3('.ml-gutter-bar-container', result);
	result = f3('.ml-floating-searchbox-and-cards', result);
	result = f3('.ml-onegoogle', result);
	result = f3('.gmnoprint.gm-bundled-control.gm-bundled-control-on-bottom', result);
	result = f3('.ml-lite-mode-popover-container.visible', result);
/*
	Array.prototype.slice.call(document.getElementsByTagName('DIV')).forEach(function(d) {
		if (d.style && d.style.width === '256px' && d.style.height === '256px') {
			d.style.border = '1px dashed rgba(82, 82, 82, 0.3)';
		}
	});
*/
	return result;
};

var f2 = function() {
	var result = page.evaluate(inbrowserF);
	console.log('result: ' + result);
    page.render(args[7] + '-' + args[1] + '-' + args[2] + '-' + args[3] + '-' + args[4] + '-' + args[5] + '-' + args[6] + '.png');
    phantom.exit();
};

var f1 = function(status) {
	console.log('Status: ' + status);
    if(status === 'success') {
    	setTimeout(f2, calculateTimeout());
    }
    else {
    	phantom.exit();
	}
};

var page = require('webpage').create();
page.viewportSize = { width: widthUnit*numberOfWidthUnit, height: heightUnit*numberOfHeightUnit };
page.open('https://www.google.com/maps/@' + args[1] + ',' + args[2] + ',15z?force=lite', f1);

