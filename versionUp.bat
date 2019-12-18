@echo on
@echo =============================================================
@echo $                          ELEHALL                          $
@echo =============================================================
@echo.
@echo off

@title ELEHALL VERSIONUP
@color 0a

rem #######################################################################################
rem 发布versionUp
rem #######################################################################################

set /p version=plz set a useful version...:
if "%version%"=="" goto versionUpfailed

rem set version=1.0.0-SNAPSHOT
rem set version=1.0.0

echo ***************************************************************************************
echo change version to %version%...
echo ***************************************************************************************
call mvn versions:set -DoldVersion=* -DnewVersion=%version% -DprocessAllModules=true -DallowSnapshots=true -Pall

rem 确认可以编译通过
echo ***************************************************************************************
echo make sure compile is working
echo ***************************************************************************************
call mvn clean install -B -e -U -V -Pall -DskipTests
if "%errorlevel%"=="1" goto versionUpfailed
goto versionUpsuccess

:versionUpsuccess
rem 检查工程依赖树
echo ***************************************************************************************
echo inspect dependency tree
echo ***************************************************************************************
call mvn dependency:tree -Pall -Dverboss

set /p input=modify version is %version%,continue?(y/n):
if "%input%"=="n" goto versionUpfailed
if "%input%" neq "y" echo WRONG INPUT & goto versionUpfailed

rem 修改版本动作
call mvn versions:commit -Pall
echo ***************************************************************************************
echo MODIFY VERSION SUCCESS
echo ***************************************************************************************
goto end

:versionUpfailed
call mvn versions:revert -Pall
echo ***************************************************************************************
echo MODIFY VERSION FAIL
echo ***************************************************************************************
goto end

:end
pause > nul