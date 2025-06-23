火车订票管理系统：
需求：通过MVC设计模式，使用Servlet控制器（表示层、控制层、模型层）和Ajax、jQuery异步交互技术
实现当日火车信息，订单信息的列表显示； 对当日火车信息进行增删改查的功能；对订单信息的增删改查功能：订票（从当日火车信息中添加到订单列表），订单查询（多条件查询），修改订单信息和状态（已改），退票功能。 

1、配置MySql或其他数据库数据库，创建数据库trainer，User表,Train表和TicketOrder表

2、建立一个JavaWeb工程，导入mysql数据库连接驱动包

3、编写实体类User，TicketOrder，Train 

4、编写dao层代码：通用数据库连接类、接口cketOrderDao，UserDao、实现类TicketOrderDaoImpl，UserDaoImpl

5、编写biz业务逻辑层代码：接口TicketOrderBiz，UserBiz TrainBiz和实现类TicketOrderBizImpl，UserBizImpl ，TrainBizImpl

6、编写servlet控制器，实现用户请求及模型调用:LoginServlet,OrderServlet，RegisterServlet，TrainServlet

7、编写ui表示层代码：通过jsp页面实现数据请求及展示功能（EL+JSTL）login.jsp：登录页面;orderList.jsp：订单管理页面，register.jsp：注册页面，结合 EL + JSTL 展示数据；train.jsp当日火车列表页面 

8、使用Ajax和jQuery异步交互技术实现数据请求和呈现:登录验证,异步加载订单数据,增、删、改，查操作的异步提交

9、测试并调试程序
