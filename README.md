```
版本发布流程:
1.执行versionUp.bat修改项目版本
2.执行release.bat发布到中央仓库,版本号后缀为SNAPSHOT则发布到snapshot仓库，若没有则发布到staging仓库
3.测试staging仓库包没问题以后，登录staging库close掉发布，该版本即可发布到中央仓库
4.github对该分支打tag，然后用此tag发布release包
```