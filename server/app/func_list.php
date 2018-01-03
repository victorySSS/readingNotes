<?php

$host="localhost";
$user="root";
$pass="ludics";

function nameToID($username){
    $conn = mysqli_connect($host, $user, $pass);
    if(! $conn )
    {
        die('连接失败: ' . mysqli_error($conn));
    }
    // 设置编码，防止中文乱码
    mysqli_query($conn , "set names utf8");
 
    $sql = 'SELECT userID, userName
            FROM User
            WHERE userName == $username;'
 
    mysqli_select_db( $conn, 'Notes' );
    $retval = mysqli_query( $conn, $sql );
    if(! $retval )
    {
        die('无法读取数据: ' . mysqli_error($conn));
    }
    while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC))
    {
        $id = $rows['userID'];
    }
    mysqli_close($conn);

    return $id;
}


// function userExisted($username){
//     // 查询用户名是否已存在，存在则返回True，不存在返回False
// }

// function addUser($username, $password){
//     // 新建用户
// }

// function passwordRight($username, $password){
//     // 查询用户密码是否正确，返回 True, False
// }  

function addNote($userid, $note, $text = NULL, $bookname = NULL){
    // 添加: 返回noteid
    $conn = mysqli_connect($host, $user, $pass);
    if(! $conn )
    {
        die('连接失败: ' . mysqli_error($conn));
    }
    // 设置编码，防止中文乱码
    mysqli_query($conn , "set names utf8");
 
    //$ = '$note';
    //$ = '$text';
 
    $sql = "INSERT INTO Note".
            "(userID, noteAddress, textAddress, bookName) ".
            "VALUES ".
            "('$userid','$note', '$text', '$bookName')";

    mysqli_select_db( $conn, 'Notes' );
    $retval = mysqli_query( $conn, $sql );
    if(! $retval )
    {
    die('无法插入数据: ' . mysqli_error($conn));
    }

    //获取上一个插入笔记的ID
    $sql = "SELECT last_insert_id();";

    $retval = mysqli_query( $conn, $sql );
    $rows = mysql_fetch_row( $retval );
    $noteID = $rows[0];
    
    mysqli_close($conn);

    return $noteID;
}

function deleteNote($noteid){ 
    //删除给定id的笔记
    $conn = mysqli_connect($host, $user, $pass);
    if(! $conn )
    {
        die('连接失败: ' . mysqli_error($conn));
    }
    // 设置编码，防止中文乱码
    mysqli_query($conn , "set names utf8");
 
    $sql = 'DELETE FROM Note
            WHERE noteID == $noteid;'
 
    mysqli_select_db( $conn, 'Notes' );
    $retval = mysqli_query( $conn, $sql );

    mysqli_close($conn);
    
    return $retval;
} 
 
function modifyNote($noteid, $note){ 
    //将noteid对应笔记内容改为$note
    $conn = mysqli_connect($host, $user, $pass);
    if(! $conn )
    {
        die('连接失败: ' . mysqli_error($conn));
    }
    // 设置编码，防止中文乱码
    mysqli_query($conn , "set names utf8");
 
    //$newNoteAdd = 

    $sql = 'UPDATE Note
            SET noteAddress = "$note"
            WHERE noteID == $noteid;'
 
    mysqli_select_db( $conn, 'Notes' );
    $retval = mysqli_query( $conn, $sql );

    mysqli_close($conn);
    
    return $retval;
}

function getMyNotes($userid){
    // 获取 userid全部的笔记
    $conn = mysqli_connect($host, $user, $pass);
    if(! $conn )
    {
        die('连接失败: ' . mysqli_error($conn));
    }
    // 设置编码，防止中文乱码
    mysqli_query($conn , "set names utf8");
 
    $sql = 'SELECT * FROM Note
            WHERE userID == $userid;'
 
    mysqli_select_db( $conn, 'Notes' );
    $retval = mysqli_query( $conn, $sql );
    if(! $retval )
    {
        die('无法读取数据: ' . mysqli_error($conn));
    }
    $row = mysqli_fetch_array($retval, MYSQLI_ASSOC);
    mysqli_close($conn);
    
    //返回所有符合条件note的数组
    return $row;
}

function getOtherNotes($times){
    // 获取 10*$times ~ 10*($times+1)-1 项笔记内容
    $conn = mysqli_connect($host, $user, $pass);
    if(! $conn )
    {
        die('连接失败: ' . mysqli_error($conn));
    }
    // 设置编码，防止中文乱码
    mysqli_query($conn , "set names utf8");

    mysqli_select_db( $conn, 'Notes' );
    $stmt = $conn->prepare('SELECT * FROM Persons ORDER BY noteID LIMIT (?, ?)');
    $stmt->bind_param("ii", $start, $end);

    $start = (int)(10*$times);
    $end = (int)(10*($times+1)-1);
    $retval = $stmt->execute();
    if(! $retval )
    {
        die('无法读取数据: ' . mysqli_error($conn));
    }
    $row = mysqli_fetch_array($retval, MYSQLI_ASSOC))
    
    mysqli_close($conn);
    
    //返回所有符合条件note的数组
    return $row;
}

?>