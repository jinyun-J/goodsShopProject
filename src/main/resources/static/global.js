/* 유틸, 토스틀 */
toastr.options = {
    closeButton: true,
    debug: false,
    newestOnTop: true,
    progressBar: true,
    positionClass: "toast-top-right",
    preventDuplicates: false,
    onclick: null,
    showDuration: "300",
    hideDuration: "1000",
    timeOut: "5000",
    extendedTimeOut: "1000",
    showEasing: "swing",
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut"
};

function parseMsg(msg) {
    const [pureMsg, ttl] = msg.split(";ttl=");

    if ( ttl === undefined ) {
        return [pureMsg, true];
    };

    const currentJsUnixTimestamp = new Date().getTime();

    if (ttl && parseInt(ttl) < currentJsUnixTimestamp) {
        return [pureMsg, false];
    }

    return [pureMsg, true];
}

function toastMsg(isNotice, msg) {
    if (isNotice) toastNotice(msg);
    else toastWarning(msg);
}

function toastNotice(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if (needToShow) {
        toastr["success"](pureMsg, "알림");
    }
}

function toastWarning(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if (needToShow) {
        toastr["warning"](pureMsg, "경고");
    }
}

/* 유틸, URL */
function getUrlParams(urlString) {
    // 평문 쿼리 문자열을 추출합니다.
    const url = new URL(urlString);
    // URL의 쿼리 문자열 이후로 모든 키와 값을 디코딩합니다.
    const queryParams = new URLSearchParams(url.search);
    // 객체를 만들어 줍니다.
    const params = {};

    for (let [key, value] of queryParams.entries()) {
        // 객체에 키와 값을 추가합니다.
        params[key] = value;
    }

    return params;
}

$(function () {
    $('select[value]').each(function (index, el) {
        const value = $(el).attr('value');
        if (value) $(el).val(value);
    });

    $('a[method="DELETE"], a[method="POST"], a[method="PUT"]').click(function (e) {
        if ($(this).attr('onclick-after')) {
            let onclickAfter = null;

            eval("onclickAfter = function() { " + $(this).attr('onclick-after') + "}");

            if (!onclickAfter()) return false;
        }

        const action = $(this).attr('href');
        const csfTokenValue = $("meta[name='_csrf']").attr("content");

        const formHtml = `
        <form action="${action}" method="POST">
            <input type="hidden" name="_csrf" value="${csfTokenValue}">
            <input type="hidden" name="_method" value="${$(this).attr('method')}">
        </form>
        `;

        const $form = $(formHtml);
        $('body').append($form);
        $form.submit();

        return false;
    });

    $('a[method="POST"][onclick], a[method="DELETE"][onclick], a[method="PUT"][onclick]').each(function (index, el) {
        const onclick = $(el).attr('onclick');

        $(el).removeAttr('onclick');

        $(el).attr('onclick-after', onclick);
    });
});

.flex {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.justify-between {
    justify-content: space-between;
}

.items-center {
    align-items: center;
}

.p-4 {
    padding: 1rem; /* 16px */
}

.border-b {
    border-bottom: 1px solid #e2e8f0; /* Tailwind CSS의 gray-200 */
}

.border-gray-200 {
    border-color: #e2e8f0;
}

.text-lg {
    font-size: 1.125rem; /* 18px */
}

.font-medium {
    font-weight: 500;
}

.text-gray-700 {
    color: #4a5568; /* Tailwind CSS의 gray-700 */
}

.hover\:text-gray-900:hover {
    color: #1a202c; /* Tailwind CSS의 gray-900 */
}

.w-10 {
    width: 2.5rem; /* 40px */
}

.h-10 {
    height: 2.5rem; /* 40px */
}

.rounded-full {
    border-radius: 50%;
}