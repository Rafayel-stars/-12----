package org.example.bookmanagesystem01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.CommandLineRunner;

@MapperScan("org.example.bookmanagesystem01.mapper")
@SpringBootApplication
@EntityScan("org.example.bookmanagesystem01.entity")       // 指定实体类所在包
@EnableJpaRepositories("org.example.bookmanagesystem01.repository")  // 指定仓库接口所在包
public class BookManageSystem01Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BookManageSystem01Application.class, args);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 执行简单查询，例如查询某张表的记录数（替换表名）
        String sql = "SELECT COUNT(*) FROM user";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        System.out.println("查询结果：" + count);
    }
}
