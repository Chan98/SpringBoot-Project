var main = {
    init : function (){
        var _this = this;
        $('#btn-save').on('click', function(){
            _this.save();
        })

        $('#btn-update').on('click', function(){
            _this.update();
        })

        $('#btn-delete').on('click', function(){
            _this.delete();
        })
    },
    save : function () {
        var data = {
            title: $('#title').val(),
            author:$('#author').val(),
            content:$('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(){//글 등록이 성공하면 메인페이지(/)로 이동
            alert('글이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },
    update : function () {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(){
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },
    delete : function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function(){
            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    }
};

main.init();

//왜 굳이 var main = {...}를 통해 index라는 변수의 속성으로 function에 추가했을까?
//여러 function이 있을 때 이름이 겹치면 먼저 로딩된 js의 function이 이전 거를 덮어쓰게 될 수 있다.
//그렇다고 사람들이 다 function 이름을 확인하면서 만들 수 없기 때문에 자신만의 유효범위를 만드는 것이다.
//여기서는(index.js) var main을 만들어 해당 객체에 필요한 모든 function을 선언하여 유효범위를 만들었다.
