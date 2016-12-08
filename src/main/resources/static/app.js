var app = angular.
    module('chat', [])
    .controller("MainCtrl", function ($scope) {
        var stompClient = null;
        $scope.members = [{
            username: 'all',
            messages: []
        }];

        $scope.active = 0;

        $scope.setActive = function(index) {
            $scope.active = index;
        }

        function connect() {
            var socket = new SockJS('/chat');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/chat/all', function (message) {
                    $scope.members[0].messages.push(JSON.parse(message.body));
                    $scope.$apply();
                });

                stompClient.subscribe('/app/chat.participants', function (message) {
                    $scope.members = $scope.members.concat(JSON.parse(message.body));
                    $scope.me = $scope.members.filter(m => m.me)[0].username;
                    $scope.$apply();
                });

                stompClient.subscribe('/chat/chat.login', function (message) {
                    $scope.members.push(JSON.parse(message.body));
                    $scope.$apply();
                });

                stompClient.subscribe('/chat/chat.logout', function (message) {
                    let username = JSON.parse(message.body).username;
                    $scope.members = $scope.members.filter(m => m.username !== username);
                    $scope.$apply();
                });

                stompClient.subscribe("/user/exchange/amq.direct/chat.message", function(frame) {
                    let message = JSON.parse(frame.body);
                    let member = $scope.members.filter(m => m.username == message.username)[0];
                    if (!member.messages) {
                        member.messages = [];    
                    }
                    member.messages.push(message);
                    $scope.$apply();
                });
            });
        }

        function disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
        }

        $scope.send = function () {
            let active = $scope.active;
            let url = "/app/chat." + $scope.members[active].username;
            stompClient.send(url, {}, JSON.stringify({'text': $scope.newMessage}));
            if (active !== 0) {
                let member = $scope.members[active];
                if (!member.messages) {
                    member.messages = [];
                }
                member.messages.push({ text: $scope.newMessage, username: $scope.me});
            }
            $scope.newMessage = '';
        };

        connect();
    });

app.directive('myEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.myEnter);
                });

                event.preventDefault();
            }
        });
    };
});