<?
foreach ($_POST as $k => $v){
  file_put_contents("savenczi_".date("Y-m-d-H").".log", $k."\r\n".urldecode($v)."\r\n\r\n", FILE_APPEND);
}
file_put_contents("savenczi_".date("Y-m-d-H").".log", "============================\r\n", FILE_APPEND);
echo "OK";