package com.doro.core.utils;

/**
 * @author jiage
 */
public class WilsonScoreUtil {

    public static double getScore(double u, double n) {
        // zP一般取值2即可，即95%的置信度
        return getScore(u, n, 2);
    }

    /**
     * 计算威尔逊得分
     *
     * @param u  好评数
     * @param n  评论总数
     * @param zP 正态分布的分位数
     * @return s 威尔逊得分
     */
    public static double getScore(double u, double n, double zP) {
        // 好评率
        double p = u / n;
        System.out.println("好评率为：" + p);
        // 威尔逊得分
        return (p + (Math.pow(zP, 2) / (2. * n)) - ((zP / (2. * n)) * Math.sqrt(4. * n * (1. - p) * p + Math.pow(zP, 2)))) / (1. + Math.pow(zP, 2) / n);
    }
}
