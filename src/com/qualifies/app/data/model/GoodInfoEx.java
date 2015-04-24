package com.qualifies.app.data.model;

import java.math.BigDecimal;
import java.util.List;

import android.graphics.Bitmap;

public class GoodInfoEx extends GoodInfo{

	int startion;
	int goods_number;
	
	List<String> goods_imgs;
	List<Bitmap> goods_imgBitmaps;
	
	BigDecimal promote_price;
	String promote_start_date;
	String promote_end_date;
	String goods_bried;
	boolean is_shipping;
	boolean is_on_sale;
	int directtrade;
	String number_nuit;
	int max_sale_number;
	
	public GoodInfoEx() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public List<String> getGoods_imgs() {
		return goods_imgs;
	}
	public void setGoods_imgs(List<String> goods_imgs) {
		this.goods_imgs = goods_imgs;
	}
	public List<Bitmap> getGoods_imgBitmaps() {
		return goods_imgBitmaps;
	}
	public void setGoods_imgBitmaps(List<Bitmap> goods_imgBitmaps) {
		this.goods_imgBitmaps = goods_imgBitmaps;
	}
	
	public int getStartion() {
		return startion;
	}

	public void setStartion(int startion) {
		this.startion = startion;
	}

	public int getGoods_number() {
		return goods_number;
	}

	public void setGoods_number(int goods_number) {
		this.goods_number = goods_number;
	}

	public BigDecimal getPromote_price() {
		return promote_price;
	}

	public void setPromote_price(BigDecimal promote_price) {
		this.promote_price = promote_price;
	}

	public String getPromote_start_date() {
		return promote_start_date;
	}

	public void setPromote_start_date(String promote_start_date) {
		this.promote_start_date = promote_start_date;
	}

	public String getPromote_end_date() {
		return promote_end_date;
	}

	public void setPromote_end_date(String promote_end_date) {
		this.promote_end_date = promote_end_date;
	}

	public String getGoods_bried() {
		return goods_bried;
	}

	public void setGoods_bried(String goods_bried) {
		this.goods_bried = goods_bried;
	}

	public boolean isIs_shipping() {
		return is_shipping;
	}

	public void setIs_shipping(boolean is_shipping) {
		this.is_shipping = is_shipping;
	}

	public boolean isIs_on_sale() {
		return is_on_sale;
	}

	public void setIs_on_sale(boolean is_on_sale) {
		this.is_on_sale = is_on_sale;
	}

	public int getDirecttrade() {
		return directtrade;
	}

	public void setDirecttrade(int directtrade) {
		this.directtrade = directtrade;
	}

	public String getNumber_nuit() {
		return number_nuit;
	}

	public void setNumber_nuit(String number_nuit) {
		this.number_nuit = number_nuit;
	}

	public int getMax_sale_number() {
		return max_sale_number;
	}

	public void setMax_sale_number(int max_sale_number) {
		this.max_sale_number = max_sale_number;
	}
}
