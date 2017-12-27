<?php

$host="localhost";
$user="root";
$pass="ludics";

function nameToID($username){
    // return id
    

}


function userExisted($username){
    // 查询用户名是否已存在，存在则返回True，不存在返回False
}

function addUser($username, $password){
    // 新建用户
}

function passwordRight($username, $password){
    // 查询用户密码是否正确，返回 True, False
}  

function addNote($userid, $note, $bookname = NULL, $text = NULL){
    // 添加: 返回bool
}

function getMyNotes($userid){
    // 获取 userid全部的笔记
    //  
}

function getOtherNotes($times){
    
    // 获取 10*$times ~ 10*($times+1)-1 项笔记内容
    // 

}


?>