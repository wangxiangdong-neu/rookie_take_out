## 菜鸟外卖功能清单

### 本项目使用SpringBoot + MyBatisPlus + Maven + MySQL构建

### 后台系统

#### 员工管理:

- 登录、退出
- 新增
- 修改
- 列表
- 禁用、启用

#### 分类管理:

- 新增
- 修改
- 列表
- 删除

#### 菜品管理:

- 新增
- 修改
- 列表
- 删除
- 停售、启售

#### 套餐管理:

- 新增
- 修改
- 列表
- 删除
- 停售、启售


#### 订单明细

- 列表
- 查看明细
- 状态操作

### 移动端应用

- 手机号登录
- 个人中心
- 地址管理
- 历史订单
- 浏览菜品、套餐
- 购物车
- 添加购物车
- 清空购物车
- 提交订单
- 再来一单

数据库表在 resource\sql 目录下，部署项目先运行maven，然后创建数据库，再修改application.yml配置文件中的数据库用户名、密码和rookie:  path配置项（设置成项目目录下src\main\resources\uploadFile\img\）



后台登录页：http://localhost:8080/backend/page/login/login.html

前台移动端登录页（需用手机或浏览器的手机模式打开）：http://localhost:8080/front/page/login.html

