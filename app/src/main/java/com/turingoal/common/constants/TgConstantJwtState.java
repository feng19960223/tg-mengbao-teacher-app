package com.turingoal.common.constants;

/**
 * 定义token的三种状态
 */
public interface TgConstantJwtState {
    String EXPIRED = "EXPIRED"; // 过期
    String INVALID = "INVALID"; // 无效(token不合法)
    String VALID = "VALID"; // 有效的
}
