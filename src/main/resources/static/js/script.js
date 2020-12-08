import {content, socket, socket_send} from "./webSockect.js";
//import {content} from "./webSockect.js";
var botui = new BotUI('my-botui-app');


botui.message
    .bot('Would you like to add a reminder?')
    .then(function () {
        return botui.action.button({
            delay: 1000,
            action: [{
                text: 'Yep',
                value: 'yes'
            }, {
                text: 'Nope!',
                value: 'no'
            }]
        })
    }).then(function (res) {
    if(res.value == 'yes') {
        userInput();
    } else {
        botui.message.bot('Okay.');
    }
});

var userInput = function () {
    botui.message
        .bot({
            delay: 500,
            content: 'Write your <br> reminder below:'
        })
        .then(function () {
            return botui.action.text({
                delay: 1000,
                action: {
                    placeholder: 'Buy some milk'
                }
            })
        })
        .then(function (res) {
            search(res.value);
        })
}
function showResponse(content) {
    console.log(content);

    return botui.message
        .bot({
            delay: 500,
            content: 'reminder added: <br/>!' + content
        }).then(ifContinue);
}
function ifContinue(){
    botui.action.button({
        delay: 1000,
        action: [{
            icon: 'plus',
            text: 'add another',
            value: 'yes'
        }]
    })
    .then(userInput);
}


function search(msg){
    console.log(msg);
    socket_send(msg);
}

function back(content){
    console.log(content);
}

export { back , showResponse};