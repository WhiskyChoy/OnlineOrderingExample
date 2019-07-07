## 开发环境
* SYSTEM: Windows
* IDE: Intellij IDEA
* SQLDB: MySQL
## 开发心得体会
1. 采用前后端分离，接口的稳定性很重要，随意变动会给开发带来很大困难，比如之前因为界面设计有些变动就想着修改接口来适应这个变动，后来发现很多内容要跟着改，最后还是以保证接口稳定性为先，在前端做了一些数据处理；
2. 不能过分依赖框架，特别是数据库，使用JPA的Repository来进行查询，开发效率快，查询效率会慢一些，有些应当放在一个事务内完成的内容应该自行开启session在完成后自行关闭session；
3. 充分阅读框架文档后再使用。如getOne方法获得的是代理对象，如果调用者方法没有@Transactional标识，那么这一代理对象将无法通过重载后的get方法进行数据获取，此外如果没有相关内容，getOne方法将会抛出异常而不是返回null。在阅读文档后了解到getById可以返回Optional对象，使用orElse(null)即可满足无结果返回null的要求，且通过Optional获取到的对象通过get方法获取数据不会出现前面提到的getOne的问题。
## 代码运行方式
### 先启动两个后端项目：
* cd至后端项目所在目录
* 运行mvn install
* 运行mvn spring-boot:run
### 再启动两个前端项目
* cd至前端项目所在目录
*	运行npm install
*	运行npm run serve
### 如果不希望启动前端项目，可以：
*	cd至前端项目所在目录
*	运行npm run build
*	将public文件夹中的内容拷贝到对应后端项目的client文件夹
