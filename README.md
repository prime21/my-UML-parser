# UML
又没了

## UML element
有`type`,`parentid`,`id`,`name`四个域.
### Type
#### UML_ASSOCIATION
有两个儿子
1. end1 
2. end2

#### UML_ASSOCIATION_END
ref是指向对应的关系

### UML_INTERFACE_REALIZATION
src: 类名
tar: 接口名

### UML_GENERALIZATION

设计思路

## UML Tree
原封不动的建立 UML Tree

再建立特殊的节点

UML_Class_node

UML_Interface_node

## 遍历操作
### UML 遍历操作

确定每一个点单独的效应

确定每一个点单独的操作

### CLASS遍历

是一片森林的遍历，更新需要继承下传的内容

### 接口遍历

是一张DAG的遍历，注意到UML阶段已知每个类实现的接口关系，所以一趟下传即可。