package com.test.demo.model;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "ttt")
public class User {
        @Id
        private int id;
        @Excel(name = "状态", replace = {"激活_0", "未激活_1"}, orderNum = "3")
        private int status;
        @Excel(name = "姓名", orderNum = "0")
        private String name;
        @Excel(name = "金额", orderNum = "1")
        private double price;
        @Excel(name = "生日", orderNum = "2")
        private String date;

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public int getStatus() {
                return status;
        }

        public void setStatus(int status) {
                this.status = status;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public double getPrice() {
                return price;
        }

        public void setPrice(double price) {
                this.price = price;
        }

        public String getDate() {
                return date;
        }

        public void setDate(String date) {
                this.date = date;
        }

}
