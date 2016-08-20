// ==UserScript==
// @name        safaribooksonline
// @namespace   http://thepiratebay.org
// @include     https://www.safaribooksonline.com/
// @include     https://www.safaribooksonline.com/library/view/*.html
// @include     https://www.safaribooksonline.com/library/view/*.xhtml
// @version     0.1
// @grant       GM_xmlhttpRequest
// @grant       GM_log
// ==/UserScript==

'use strict';

var hasSignIn = false;
if (location.href == "https://www.safaribooksonline.com/") {
    (function() {
        var $ = unsafeWindow.jQuery;
        if (typeof $ == "function" && typeof $.fn == "object" && $("a.login.js-login").is(":visible")) {
            hasSignIn = true;
            setTimeout(function() {
                $("a.login.js-login").trigger("click");
                setTimeout(function() {
                    var email    = document.getElementById('id_email');
                    var password = document.getElementById('id_password1');
                    if (email && password) {
                        email.value    = 'safarinexas';
                        password.value = 'Abcd1234';
                    }
                }, 1027);
            }, 419);
        }
        else {
            console.log("didn't call click: " + (typeof $) );
        }
    })();
}

/**
 * @return false if this is not a video page, otherwise an array of video urls
 */
var isVideoPage = function() {
    var videos = document.getElementsByTagName("VIDEO");
    if (videos.length > 0) {
        var result = [];
        var foundValidUrl = false;
        for (var i = 0; i < videos.length; i++) {
            if (videos[i].src && videos[i].src.indexOf(".kaltura.") > 0) {
                foundValidUrl = true;
                result.push(videos[i].src);
            }
        }
        if (foundValidUrl) { console.log('this is a video page'); return result; }
        else { console.log('not video page case 1'); return false; }
    }
    else {
        // console.log('not video page case 2');
        return false;
    }
};

// it's the title attribute of a <div data-plugin-name="titleLabel" class="... titleLabel ...">
var getVideoTitle = function() {
    var divs = document.getElementsByTagName("DIV");
    for (var i = 0; i < divs.length; i++) {
        if (divs[i].getAttribute("data-plugin-name") == "titleLabel" && (" " + divs[i].className + " ").indexOf("titleLabel") >= 0) {
            return divs[i].getAttribute("title").trim();
        }
    }
    console.log('unable to find the video title');
    return null;
};

var getPartNumber = function(href) {
    var x = href.match(/\d[\d]*\.html/);
    if (x !== null && x.length !== 0) {
        x = x[0].match(/\d[\d]*/);
        return x.length == 0 ? "~~" : x[0];
    }

    return "~~";
};

var processVideoPage = function(videoUrls) {
    var videoTitle = getVideoTitle();
    for (var i = 0; i < videoUrls.length; i++) {
        var command = "wget -O \"" + getPartNumber(window.location.href) + "-" + videoTitle + ".mp4\" '" + videoUrls[i] + "'";
        // var encodedCommand = encodeURI(command.replace(/&/g, "_____"));
        console.log(command);

        // console.log(encodedCommand);

        GM_xmlhttpRequest({
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            url: "http://localhost:8080/safaribooksonline-video",
            data: JSON.stringify({
                textarea: '',
                wgetimg: command
            })
        });
    }
};

// return true if this is a video page, false otherwise
var keepCheckingIfVideoPage = function() {
    var x = isVideoPage();
    if (x !== false) {
        processVideoPage(x);
    }
    else {
        setTimeout(keepCheckingIfVideoPage, 1027);
    }
};

if (!hasSignIn) {
    setTimeout(keepCheckingIfVideoPage, 1027);
    // console.log('gonna check if this is a video page soon');
}
else console.log('not gonna check if this is a video page because the Sign In button is found');

var f = function() {
    if (isVideoPage() === false) {
        var div = document.createElement("div");
        div.setAttribute('style', 'position: fixed; top: 60px; left: 60px; z-index: 999;');

        var _div = document.createElement('div');
        var button = document.createElement('button');
        button.appendChild(document.createTextNode('GM_xmlhttpRequest'));
        var f1 = function() {
            GM_xmlhttpRequest({
                method: 'POST',
                url: 'http://localhost:8080/safaribooksonline-text',
                headers: {
                    "Content-Type": 'application/json'
                },
                data: JSON.stringify({
                    textarea: document.getElementById('__textarea__').value,
                    wgetimg: document.getElementById('__wgetimg__').value
                }),
                onload: function(response) {
                    console.log([
                        response.status,
                        response.statusText,
                        response.readyState,
                        response.responseHeaders,
                        response.responseText,
                        response.finalUrl
                    ].join("\n"));
                }
            });
        };
        button.onclick = f1;
        _div.appendChild(button);
        div.appendChild(_div);

        var textarea = document.createElement("textarea");
        textarea.setAttribute('id', '__textarea__');
        textarea.setAttribute('style', 'font-size: small;');
        div.appendChild(textarea);

        var wgetimg = document.createElement("textarea");
        wgetimg.setAttribute('id', '__wgetimg__');
        wgetimg.setAttribute('style', 'font-size: small;');
        div.appendChild(document.createElement("br"));
        div.appendChild(document.createElement("br"));
        div.appendChild(wgetimg);

        // document.body.appendChild(div);  // TODO don't forget to turn me on later

        div = document.getElementById("sbo-rt-content");

        var invisibleElements = [];
        if (div.children.length > 0) {
            div.style.padding = '0';
            div.style.maxWidth = '69rem';
            var grandChildren = div.children[0].children;
            for (var i = 0; i < grandChildren.length; i++) {
                if (grandChildren[i].offsetParent === null) {  // check if it's invisible or not
                    invisibleElements.push(grandChildren[i]);
                }
            }
        }
        for (var i = 0; i < invisibleElements.length; i++) {
            invisibleElements[i].parentNode.removeChild(invisibleElements[i]);
        }
        textarea.value = div.innerHTML;
        textarea.select();
        textarea.focus();

        // wget img
        var imgs = document.getElementsByTagName("IMG");
        var s = "";
        for (var i = 0; i < imgs.length; i++) {
            s += "wget " + imgs[i].src + "\n";
        }
        wgetimg.value = s;

        f1();
    }
};

if (!hasSignIn) setTimeout(f, 419);