package com.doro.core.init;

import cn.hutool.core.util.ReflectUtil;
import com.doro.common.constant.Settings;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class PropertiesInit {

    String template = "CREATE TABLE `global_setting` (\n" +
            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
            "  `k` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,\n" +
            "  `v` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
            "  `parent_id` int NOT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";


    public static void main(String[] args) throws ClassNotFoundException {

//        System.out.println(SettingTypeEnum.BOOLEAN.getFunction().apply("true"));
        Field[] fields = Settings.class.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }

    }
}
