function js_request(form) {

    let url = form['url'].value;
    let params = form['params'].value;
    let method = form['method'].value;
    let token = form['token'].value;

    $.ajax({
        url: `${url}${params}`,
        type: `${method}`,
        contentType: 'application/json',
        headers: {
            "Authorization": "Bearer " + token
        },
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}


function js_request_game_put(form) {

    let url = form['url'].value;
    let token = form['token'].value;
    let game_id = form['gameId'].value;
    let team1Score = form['team1Score'].value;
    let team2Score = form['team2Score'].value;
    let data = {
        "gameId": game_id,
        "team1Score": team1Score,
        "team2Score": team2Score
    };
    $.ajax({
        url: `${url}`,
        type: `PUT`,
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify(data),
        contentType: 'application/json',
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}


function js_request_season_post(form) {

    let url = form['url'].value;
    let token = form['token'].value;
    let season_name = form['season_name'].value;
    let start_time = form['start_time'].value;
    let start_ppp = form['start_ppp'].value;
    let ppp_gap = form['ppp_gap'].value;
    let data = {
        "seasonName": season_name,
        "startTime": start_time,
        "startPpp": start_ppp,
        "pppGap": ppp_gap
    };
    $.ajax({
        url: `${url}`,
        type: `POST`,
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify(data),
        contentType: 'application/json',
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}

function js_request_season_put(form) {

    let url = form['url'].value;
    let token = form['token'].value;
    let id = form['id'].value;
    let start_ppp = form['start_ppp'].value;
    let ppp_gap = form['ppp_gap'].value;
    let data = {
        "id": id,
        "startPpp": start_ppp,
        "pppGap": ppp_gap
    };
    $.ajax({
        url: `${url}`,
        type: `PUT`,
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify(data),
        contentType: 'application/json',
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}

function js_request_delete(form) {

    let token = form['token'].value;
    let id = "/" + form['id'].value;
    let url = form['url'].value + id;
    $.ajax({
        url: `${url}`,
        type: `DELETE`,
        headers: {
            "Authorization": "Bearer " + token
        },
        contentType: 'application/json',
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}

function js_request_cron_put(form) {

    let url = form['url'].value;
    let token = form['token'].value;
    let second = form['second'].value;
    let interval_time = form['interval_time'].value;
    let start_time = form['start_time'].value;
    let end_time = form['end_time'].value;
    let data = {
        "cron": second + " */" + interval_time + " " + start_time + "-" + end_time + " * * *"
    };
    $.ajax({
        url: `${url}`,
        type: `PUT`,
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify(data),
        contentType: 'application/json',
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}

function js_request_rank_cron_put(form) {

    let url = form['url'].value;
    let token = form['token'].value;
    let second = form['second'].value;
    let minute = form['minute'].value;
    let hour = form['hour'].value;
    let data = {
        "cron": second + " " + minute + " " + hour + " * * *"
    };
    $.ajax({
        url: `${url}`,
        type: `PUT`,
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify(data),
        contentType: 'application/json',
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}

function js_request_path(form) {

    let url = form['url'].value;
    let path = form['path'].value;
    let method = form['method'].value;
    let token = form['token'].value;

    $.ajax({
        url: `${url}${path}`,
        type: `${method}`,
        contentType: 'application/json',
        headers: {
            "Authorization": "Bearer " + token
        },
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}

function js_request_input(form) {
    let url = form['url'].value;
    let input_name = form['input'].name;
    let input_message = form['input'].value;
    let params = form['params'].value;
    let method = form['method'].value;
    let token = form['token'].value;

    /* 입력창 */
    let inputString = prompt(input_message, null);
    if (inputString == null)
        return;
    params = params + "&" + input_name + "=" + inputString;
    $.ajax({
        url: `${url}${params}`,
        type: `${method}`,
        contentType: 'application/json',
        headers: {
            "Authorization": "Bearer " + token
        },
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}

function js_request_current_match_create(form) {

    let url = form['url'].value;
    let token = form['token'].value;
    let userId = form['userId'].value;
    let slotId = form['slotId'].value;
    let gameId = form['gameId'].value;
    let matchImminent = form['matchImminent'].value;
    let isMatched = form['isMatched'].value;

    let data = {
        'userId': userId,
        'slotId': slotId,
        'gameId': gameId,
        'matchImminent': matchImminent,
        'isMatched': isMatched
    };
    $.ajax({
        url: `${url}`,
        type: `POST`,
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify(data),
        contentType: 'application/json',
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}

function js_request_current_match_update(form) {

    let url = form['url'].value;
    let token = form['token'].value;
    let id = form['id'].value;
    let userId = form['userId'].value;
    let slotId = form['slotId'].value;
    let gameId = form['gameId'].value;
    let matchImminent = form['matchImminent'].value;
    let isMatched = form['isMatched'].value;

    let data = {
        'currentMatchId': id,
        'userId': userId,
        'slotId': slotId,
        'gameId': gameId,
        'matchImminent': matchImminent,
        'isMatched': isMatched
    };
    $.ajax({
        url: `${url}`,
        type: `PUT`,
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify(data),
        contentType: 'application/json',
        async: false,
        error: function () {
            alert("Error!");
        },
        success: function () {
            alert("success!");
        },
        complete: function () {
            history.replaceState({}, null, location.pathname);
        }
    })
}