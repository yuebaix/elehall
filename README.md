```
提交一个snapshot版本：
1：修改version加一个-SNAPSHOT, 执行 mvn clean deploy
发布一个release版本:
1：修改version 不要加-SNAPSHOT,  可以手动修改，也可以执行 
2: 执行 mvn clean deploy -P release
```