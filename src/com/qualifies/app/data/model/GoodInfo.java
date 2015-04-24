package com.qualifies.app.data.model;

import java.math.BigDecimal;

import android.R.string;
import android.graphics.Bitmap;

public class GoodInfo {

	protected int goods_id;
	protected String goods_name;
	protected String goods_img;
	protected Bitmap goods_imgBitmap;
	
	protected boolean is_coll;
	protected String origin;
	
	protected BigDecimal market_price;
	protected BigDecimal shop_price;

	
	protected boolean is_best;
	protected boolean is_new;
	protected boolean is_hot;
	protected boolean is_promote;
	
	public GoodInfo() {
		// TODO Auto-generated constructor stub
	}
	

	public GoodInfo(int goods_id, String goods_name, String goods_img,
			boolean is_coll, String origin, BigDecimal market_price,
			BigDecimal shop_price, boolean is_best, boolean is_new,
			boolean is_hot, boolean is_promote) {
		super();
		this.goods_id = goods_id;
		this.goods_name = goods_name;
		this.goods_img = goods_img;
		this.is_coll = is_coll;
		this.origin = origin;
		this.market_price = market_price;
		this.shop_price = shop_price;
		this.is_best = is_best;
		this.is_new = is_new;
		this.is_hot = is_hot;
		this.is_promote = is_promote;
	}


	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	
	public Bitmap getGoods_imgBitmap() {
		return goods_imgBitmap;
	}

	public void setGoods_imgBitmap(Bitmap goods_imgBitmap) {
		this.goods_imgBitmap = goods_imgBitmap;
	}
	public boolean isIs_coll() {
		return is_coll;
	}
	public void setIs_coll(boolean is_coll) {
		this.is_coll = is_coll;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public BigDecimal getMarket_price() {
		return market_price;
	}
	public void setMarket_price(BigDecimal market_price) {
		this.market_price = market_price;
	}
	public BigDecimal getShop_price() {
		return shop_price;
	}
	public void setShop_price(BigDecimal shop_price) {
		this.shop_price = shop_price;
	}
	public boolean isIs_best() {
		return is_best;
	}
	public void setIs_best(boolean is_best) {
		this.is_best = is_best;
	}
	public boolean isIs_new() {
		return is_new;
	}
	public void setIs_new(boolean is_new) {
		this.is_new = is_new;
	}
	public boolean isIs_hot() {
		return is_hot;
	}
	public void setIs_hot(boolean is_hot) {
		this.is_hot = is_hot;
	}
	public boolean isIs_promote() {
		return is_promote;
	}
	public void setIs_promote(boolean is_promote) {
		this.is_promote = is_promote;
	}


}
