'use strict';

/*
 * produces wget commands to download 256px x 256px tiles in lite google map. Then use TileTest.java to put these
 * tiles together.
 */

var f1 = function(status) {
    console.log('Status: ' + status);
    if(status === 'success') {
        setTimeout(f2, 10000);
    }
    else {
        phantom.exit();
    }
};

var f2 = function() {
    page.evaluate(f3).forEach(function(r) {
        console.log('wget -O ' + r.left + r.top + '.png \'https://www.google.com' + r.url + '\'');
    });
    phantom.exit();
};

var f3 = function() {
    var result = [];
    Array.prototype.slice.call(document.querySelectorAll('img')).filter(function(img) {
        var src = img.getAttribute('src');
        if (src && src.indexOf('/maps/vt?') == 0) {
            var s = img.style;
            if (s.position !== 'absolute' || s.left !== '0px' || s.top !== '0px' || s.width !== '256px' || s.height !== '256px') {
                return false;
            }
            var d = img.parentElement;
            return d && d.tagName === 'DIV';
        }
        return false;
    }).forEach(function(img) {
        var d = img.parentElement;
        result.push({
            url: img.getAttribute('src'),
            left: d.style.left,
            top: d.style.top
        });
    });

    return result;
};

var page = require('webpage').create();
page.viewportSize = { width: 10240, height: 10240 };

page.open('https://www.google.com/maps/@40.58,-74.64,15z?force=lite', f1);