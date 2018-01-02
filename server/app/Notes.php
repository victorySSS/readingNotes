<!DOCTYPE html>
<html>
<body>

<?php
$dbhost = 'localhost:3306';  // mysql服务器主机地址
$dbuser = 'root';            // mysql用户名
$dbpass = '123456';          // mysql用户名密码
$conn = mysqli_connect($dbhost, $dbuser, $dbpass);
if(! $conn )
{
  die('连接错误: ' . mysqli_error($conn));
}
echo '连接成功<br />';
$sql = 'CREATE DATABASE Notes';
$retval = mysqli_query($conn, $sql);
if(! $retval )
{
    die('创建数据库失败: ' . mysqli_error($conn));
}
echo "数据库 Notes 创建成功\n";

$sql = "CREATE TABLE IF NOT EXISTS `Notes`.`NoteList` (
        `userID` INT UNSIGNED NOT NULL,
        `noteID` INT UNSIGNED NOT NULL,
        PRIMARY KEY (`userID`, `noteID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;;";
mysqli_select_db( $conn, 'Notes' );
$retval = mysqli_query( $conn, $sql );
if(! $retval )
{
    die('数据表创建失败: ' . mysqli_error($conn));
}
echo "数据表创建成功\n";

$sql = "CREATE TABLE IF NOT EXISTS `Notes`.`Note` (
        `userID` INT UNSIGNED NOT NULL,
        `noteID` INT UNSIGNED NOT NULL,
        `noteAddress` VARCHAR(255) NOT NULL,
        `createTime` DATETIME NOT NULL,
        `totalLikes` INT UNSIGNED NOT NULL,
        `totalComments` INT UNSIGNED NOT NULL,
        `NoteList_userID` INT UNSIGNED NOT NULL,
        `NoteList_noteID` INT UNSIGNED NOT NULL,
        `User_userID` INT UNSIGNED NOT NULL,
        PRIMARY KEY (`userID`, `noteID`),
        INDEX `fk_Note_NoteList1_idx` (`NoteList_userID` ASC, `NoteList_noteID` ASC),
        INDEX `fk_Note_User1_idx` (`User_userID` ASC),
        CONSTRAINT `fk_Note_NoteList1`
            FOREIGN KEY (`NoteList_userID` , `NoteList_noteID`)
            REFERENCES `Notes`.`NoteList` (`userID` , `noteID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
        CONSTRAINT `fk_Note_User1`
            FOREIGN KEY (`User_userID`)
            REFERENCES `Notes`.`User` (`userID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION);";
mysqli_select_db( $conn, 'Notes' );
$retval = mysqli_query( $conn, $sql );
if(! $retval )
{
    die('数据表创建失败: ' . mysqli_error($conn));
}
echo "数据表创建成功\n";

$sql = "CREATE TABLE IF NOT EXISTS `Notes`.`User` (
        `userID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
        `userName` VARCHAR(16) NOT NULL,
        `userPassword` VARCHAR(32) NOT NULL,
        `userNickname` VARCHAR(16) NULL,
        `userPortrait` VARCHAR(255) NULL,
        `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
        `Note_userID` INT UNSIGNED NOT NULL,
        `Note_noteID` INT UNSIGNED NOT NULL,
        PRIMARY KEY (`userID`),
        INDEX `fk_User_Note1_idx` (`Note_userID` ASC, `Note_noteID` ASC),
        CONSTRAINT `fk_User_Note1`
            FOREIGN KEY (`Note_userID` , `Note_noteID`)
            REFERENCES `Notes`.`Note` (`userID` , `noteID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION);";
mysqli_select_db( $conn, 'Notes' );
$retval = mysqli_query( $conn, $sql );
if(! $retval )
{
    die('数据表创建失败: ' . mysqli_error($conn));
}
echo "数据表创建成功\n";

$sql = "CREATE TABLE IF NOT EXISTS `Notes`.`Comment` (
        `noteID` INT UNSIGNED NOT NULL,
        `commentID` INT UNSIGNED NOT NULL,
        `comFromID` INT UNSIGNED NOT NULL,
        `comToID` INT UNSIGNED NOT NULL,
        `commentText` VARCHAR(255) NOT NULL,
        `Note_userID` INT UNSIGNED NOT NULL,
        `Note_noteID` INT UNSIGNED NOT NULL,
        PRIMARY KEY (`noteID`, `comFromID`),
        INDEX `fk_Comment_Note1_idx` (`Note_userID` ASC, `Note_noteID` ASC),
        CONSTRAINT `fk_Comment_Note1`
            FOREIGN KEY (`Note_userID` , `Note_noteID`)
            REFERENCES `Notes`.`Note` (`userID` , `noteID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION);";
mysqli_select_db( $conn, 'Notes' );
$retval = mysqli_query( $conn, $sql );
if(! $retval )
{
    die('数据表创建失败: ' . mysqli_error($conn));
}
echo "数据表创建成功\n";

mysqli_close($conn);

?>

</body>
</html>