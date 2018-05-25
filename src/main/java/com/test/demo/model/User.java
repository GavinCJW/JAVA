package com.test.demo.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
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

}
