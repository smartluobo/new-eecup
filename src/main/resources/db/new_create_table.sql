CREATE TABLE `tb_user_coupons` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `oppen_id` varchar(128) DEFAULT NULL COMMENT '用户唯一表示',
  `coupons_id` int(11) DEFAULT NULL COMMENT '优惠券id',
  `coupons_name` varchar(32) DEFAULT NULL COMMENT '优惠券名称',
  `receive_date` int(8) DEFAULT NULL COMMENT '领取日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(2) DEFAULT NULL COMMENT '状态 0未使用 1锁定中 2已经使用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_api_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `nick_name` varchar(64) CHARACTER SET latin1 DEFAULT NULL COMMENT '微信昵称',
  `wechat_num` varchar(64) CHARACTER SET latin1 DEFAULT NULL COMMENT '微信号',
  `oppen_id` varchar(128) CHARACTER SET latin1 DEFAULT NULL COMMENT '微信唯一标识',
  `wechat_phone_num` varchar(16) CHARACTER SET latin1 DEFAULT NULL COMMENT '微信关联电话号码',
  `user_bind_phone_num` varchar(16) CHARACTER SET latin1 DEFAULT NULL COMMENT '用户绑定电话号码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_head_image` varchar(128) CHARACTER SET latin1 DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_sku_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sku_type_name` varchar(16) DEFAULT NULL COMMENT 'sku类型名称如 规格 温度 糖度',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_sku_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_type_id` int(11) DEFAULT NULL COMMENT 'sku明细所属sku_type的id',
  `sku_detail_name` varchar(16) DEFAULT NULL COMMENT '明细显示名称 如 大 中 小 常温 加冰等',
  `sku_detail_price` int(4) DEFAULT '0' COMMENT 'sku_detail的价格',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `oppen_id` varchar(128) DEFAULT NULL COMMENT '用户唯一标识',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品id',
  `show_price` double(12,2) DEFAULT NULL COMMENT '购物车显示价格，商品价格+sku_detail价格',
  `sku_detail_id` varchar(128) DEFAULT NULL COMMENT '用户选中的sku_detai_id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  CREATE TABLE `tb_api_user_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `oppen_id` varchar(128) DEFAULT NULL COMMENT '用户唯一标识',
  `address_name` varchar(128) DEFAULT NULL COMMENT '地址名称',
  `longitude` varchar(16) DEFAULT NULL COMMENT '地址经度',
  `latitude` varchar(16) DEFAULT NULL COMMENT '地址纬度',
  `phone_num` varchar(16) DEFAULT NULL COMMENT '地址联系电话',
  `bind_num` varchar(16) DEFAULT NULL COMMENT '用户绑定的电话号码',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_user_pay_record` (
  `id` varchar(64) NOT NULL,
  `oppen_id` varchar(128) DEFAULT NULL,
  `order_id` varchar(64) DEFAULT NULL,
  `order_payment` decimal(10,2) DEFAULT NULL,
  `payment` decimal(10,2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

