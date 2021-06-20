package top.jolyoulu.zookeepr_zkclient.entity;

import java.io.Serializable;

public class User implements Serializable {
        private String name;
        private String age;

       public User(String name, String age) {
           this.name = name;
           this.age = age;
       }

       @Override
       public String toString() {
           final StringBuffer sb = new StringBuffer("User{");
           sb.append("name='").append(name).append('\'');
           sb.append(", age='").append(age).append('\'');
           sb.append('}');
           return sb.toString();
       }
   }