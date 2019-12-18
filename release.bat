@echo on
@echo =============================================================
@echo $                          ELEHALL                          $
@echo =============================================================
@echo.
@echo off

@title ELEHALL DEPLOY
@color 0b

call mvn clean deploy -B -e -U -V -Possrh -DskipTests
pause > nul