var teacher={
}
teacher.toTeacherQuery=function (pageNo){
    pageNo=pageNo?pageNo:1;
    var param={
        pageNo:pageNo,
        tname:$('#search-tname').val()
    }
    $.post('teacher/pageTemplate.html',param,function (view) {
        $('#pageTemplate').replaceWith(view)
    })
}
teacher.toCLearTeacherQuery=function () {
    $('#search-tname').val('')
    teacher.toTeacherQuery()
}
teacher.toPageTeacherQuery=function (pageNo) {
teacher.toTeacherQuery(pageNo)
}
teacher.toAdd=function (){
    $.post('teacher/formTemplate.html',{},function(view){
        $('#teacher-modal-title').html('新建教师');
        $('#teacher-modal-body').html(view);
        $('#teacher-modal-submit').click(function(){
            var param = {
                tname : $('#form-tname').val()
            }
            $.post('teacher/save',param,function(f){
                if(f == true){
                    alert('保存成功') ;

                    $('#teacher-modal').modal('hide') ;

                    teacher.toClearTeacherQuery();
                }else{
                    alert('名称重复')
                }
            });
        });

        $('#teacher-modal').modal('show') ;
    });
}
teacher.toEdit = function(id){
    $.post('teacher/formTemplate.html',{id:id},function(view){
        $('#teacher-modal-title').html('编辑教师');
        $('#teacher-modal-body').html(view);
        $('#teacher-modal-submit').click(function(){
            var param = {
                id :$('#form-id').val(),
                tname:$('#form-tname').val()
            }
            $.post('teacher/update',param,function(f){
                if(f == true){
                    alert('修改成功') ;
                    $('#teacher-modal').modal('hide') ;
                    teacher.toTeacherQuery() ;
                }else{
                    alert('名称重复')
                }
            });
        });

        $('#teacher-modal').modal('show') ;
    });

}
teacher.toCheckAll=function (){
    var checked = $('#teacherGrid thead :checkbox').prop('checked');
    $('#teacherGrid tbody :checkbox').prop('checked',checked)
}
teacher.toDeleteAll=function (){
    if($('#teacherGrid tbody :checked').length==0){
        alert('请选择要删除的记录')
        return;
    }
    if (!confirm('是否确认删除选中的记录')){
        return;
    }
    var ids='';
    $('#teacherGrid tbody :checked').each(function (i,element){
        var id = element.value;
        alert(id)
        ids+=id+","
    })
    $.post('teacher/deleteAll',{ids:ids},function (){
        alert('删除成功')
        var pageNo = $('.pagination .active').text();
        teacher.toTeacherQuery(pageNo)
    })
}
teacher.toImports=function (){
    var uploading = false ;
    $.post('teacher/importsTemplate.html', {}, function (view) {
        $('#teacher-modal-title').html('导入教师信息');
        $('#teacher-modal-body').html(view);
        $('#teacher-modal-submit').click(function () {

            var fileInfo = $('#import-excel').val();
            if(!fileInfo){
                alert('请选择要上传的excel文件');
                return ;
            }
            if(uploading){
                alert("文件正在上传中，请稍候");
                return false;
            }
            $.ajax({
                url: 'teacher/imports',
                type: 'POST',
                cache: false,
                data: new FormData($('#teacher-import-form')[0]),
                processData: false,
                contentType: false,
                beforeSend: function () {
                    uploading = true;
                },
                success: function (msg) {
                    uploading = false;
                    msg = msg.replace(/\|/g,"\r\n");
                    alert(msg) ;
                    $('#teacher-modal').modal('hide');
                    teacher.toClearTeacherQuery();
                }
            });

        });

        $('#teacher-modal').modal('show');
    });
}
teacher.changeFile=function () {
    var fileInfo = $('#import-excel').val();
    if (fileInfo.endsWith('.xls')||fileInfo.endsWith('.xlsx')){
        $('#fileMsg').html(fileInfo)
    }else {
        alert('请选择正确的excel文件')
    }
}