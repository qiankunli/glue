## 简介

Glue，以事件驱动的方式编写代码， 并简化调用过程。

将过程式代码改造为 事件驱动代码有以下好处

1. 针对业务逻辑变化，AService，BService，CService对外接口可能会经常改变。而抽象为事件后，事件变化的可能性较少。
2. 过程式代码中，对add User的处理混杂在一起，存在大量的if else等。改造为事件驱动代码后，对add User的处理分散在各个类中，每一块代码都较少， 容易理解。
3. 大多数时候，我们都在用面向对象语言编写面向过程代码（大多数时候代码也都可用）。事件驱动方式，强制开发者对事件进行抽象。

glue即为胶水的意思，意图就是将分散在各个位置的代码按配置整合起来

若一个Service 依赖的 Service 较少，则不必要使用Glue

## 说明

基本接口

假设存在UserService，执行addUser时，依赖AService，BService，CService。
则抽取UserAddListener/UserChangeListener 接口，由ABC Service 实现


原有代码：

    class UserService{
        @Autowire
        private AService aService;
        @Autowire
        private BService bService;
        @Autowire
        private CService cService;
        void addUser(User user){
            // business code
            aService.addUser(user);
            bService.addUser(user);
            cService.addUser(user);
        }
    }


简化代码：


1. 调用方


        class UserService{
            void addUser(User user){
                // business code
                // 调用所有实现UserAddListener onUserAdd 方法 并 设置 GlueListener 的实例方法执行
                Glues.call(UserAddListener.class,"onUserAdd",args)
            }
        }

2. 被调用方

        class AService implements UserAddListener{
            @GlueListener(name="123")
            void onUserAdd(User user){
                //...
            }
        }

3. spring 配置文件

        <bean class="com.ximalaya.glue.GlueBeanPostProcessor"/>




## 可以玩的花活儿

### 优先级策略制定

三个任意配置一个

1. before，指定在哪个name之前执行
2. after，指定在哪个name之后执行
3. order，指定执行顺序，从小到大

### 异常处理（未完成）

当下为中断事件处理并抛出

### 异步处理（未完成）

当下为串行执行所有事件，为提高处理速度，可以

1. 发布事件后，立即返回，异步处理所有事件。
2. 发布事件后，所有监听者并行处理，待处理完毕后，合并结果，返回

### 对外使用（未完成）

当下

     <bean class="com.ximalaya.glue.GlueBeanPostProcessor"/>

期待

     <glue enable="true"/>

[扩展Spring的几种方式](http://nobodyiam.com/2017/02/26/several-ways-to-extend-spring/)